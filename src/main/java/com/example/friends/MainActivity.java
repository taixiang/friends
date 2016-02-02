package com.example.friends;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.friends.ui.ItemView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends Activity {

    private ListView listView;
    private InputMethodManager imm;
    private EditText edit;
    private RelativeLayout comment_view;
    private Button submit;
    int curPosition;
    int commentPos;
    int commentNum;
    /**
     * map 用于存放，edittext上输入的内容，评论时保留上次未发送的内容
     */
    HashMap<Integer,String> map = new HashMap<>();
    /**
     * positionMap,numMap 用于存放，edittext上输入的内容，回复评论时保留上次未发送的内容
     */
    HashMap<Integer,Map<String,String>> positionMap = new HashMap<>();
    Map<String,String> numMap = new HashMap<>();
    boolean isComment;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
        edit = (EditText) findViewById(R.id.edit);
        comment_view = (RelativeLayout) findViewById(R.id.comment_view);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        submit = (Button) findViewById(R.id.submit);
        listView.addHeaderView(getHeadView());
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edit.getText() != null && edit.getText().length()>0){
                    submit.setEnabled(true);
                }else {
                    submit.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        initData();
    }

    private void initData(){
        List<Item> list = new ArrayList<>();
        Item item1 = new Item();
        item1.setPortraitId(R.drawable.default_qq_avatar);
        item1.setNickName("test");
        item1.setCreatedAt("昨天");
        item1.setContent(getString(R.string.long_content));
        list.add(item1);

        Item item2 = new Item();
        item2.setPortraitId(R.drawable.default_qq_avatar);
        item2.setNickName("test");
        item2.setCreatedAt("昨天");
        List<UserImage> images1 = new ArrayList<>();
        UserImage image1 = new UserImage();
        image1.setImage(R.drawable.ic_launcher);
        images1.add(image1);
        images1.add(image1);
        item2.setImages(images1);
        list.add(item2);

        Item item3 = new Item();
        item3.setPortraitId(R.drawable.default_qq_avatar);
        item3.setNickName("test");
        item3.setCreatedAt("昨天");
        item3.setContent(getString(R.string.middle_content));
        list.add(item3);

        for(int i=0;i<5;i++){
            Item item = new Item();
            item.setPortraitId(R.drawable.default_qq_avatar);
            item.setNickName("test");
            item.setCreatedAt("昨天");
            item.setContent("床前明月光，疑是地上霜，举头望明月，低头思故乡，床前明月光，疑是地上霜，举头望明月，低头思故乡");
            List<UserImage> images = new ArrayList<>();
            UserImage image = new UserImage();
            image.setImage(R.drawable.ic_launcher);
            images.add(image);
            images.add(image);
            item.setImages(images);
            List<CommentItem> comments = new ArrayList<>();
            comments.add(new CommentItem("蓝猫", ":那个怪蜀黍在哪"));
            comments.add(new CommentItem("小鱼人", ":见你一次打你两次"));
            item.setComments(comments);
            List<String> likes = new ArrayList<>();
            likes.add("斧王");
            likes.add("维萨吉");
            item.setLikes(likes);
            list.add(item);
        }
        listView.setAdapter(new MyAdapter(list));
    }

    private View getHeadView(){
        View view = LayoutInflater.from(this).inflate(R.layout.friends_circle_head,null);
        return view;
    }

    private class MyAdapter extends BaseAdapter implements ItemView.ICommentListener,ItemView.IItemContentListener,ItemView.IShowLikesListener{
        private List<Item> lists;
        private SparseBooleanArray sparseBooleanArray;
        public MyAdapter(List<Item> lists) {
            this.lists = lists;
            sparseBooleanArray = new SparseBooleanArray();
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemView itemView = new ItemView(getBaseContext());
            itemView.setmData(lists.get(position),sparseBooleanArray);
            itemView.setPosition(position);
            itemView.setCommentListener(this);
            itemView.setItemContentListener(this);
            itemView.setiShowLikes(this);
            return itemView;
        }

        @Override
        public void comment(final int position){
            isComment = true;
            edit.setHint("");
            curPosition = position+1;
            if(map.get(curPosition)!= null && !map.get(curPosition).equals("") ){
                edit.setText(map.get(curPosition));
            }else {
                edit.setText("");
            }
            comment_view.setVisibility(View.VISIBLE);
            edit.requestFocus();
            imm.showSoftInput(edit, InputMethodManager.RESULT_SHOWN);
            imm.toggleSoftInput(InputMethodManager.RESULT_SHOWN, InputMethodManager.HIDE_IMPLICIT_ONLY);

            submit.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    final String s = edit.getText().toString();
                    if(s.length() > 0 && !s.equals("")){
                        if (imm != null){
                            imm.hideSoftInputFromWindow(comment_view.getWindowToken(), 0);
                        }
                        lists.get(position).getComments().add(new CommentItem("火女", ":" + s));
                        notifyDataSetChanged();
                        edit.setText("");
                        map.put(curPosition,"");
                        comment_view.setVisibility(View.GONE);
                    }else {
                        submit.setEnabled(false);
                    }
                }
            });
        }

        @Override
        public void addContent(final int position,final int num) {
            isComment = false;
            edit.requestFocus();
            commentPos = position +1;
            commentNum = num;
            if(positionMap.get(commentPos) != null && positionMap.get(commentPos).get(commentPos+"&"+commentNum) != null && !positionMap.get(commentPos).get(commentPos+"&"+commentNum).equals("")){
                edit.setText(positionMap.get(commentPos).get(commentPos+"&"+commentNum));
            }else {
                edit.setText("");
                edit.setHint("回复:" + lists.get(position).getComments().get(num).getName());
            }

            comment_view.setVisibility(View.VISIBLE);
            imm.showSoftInput(edit, InputMethodManager.RESULT_SHOWN);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String s = edit.getText().toString();
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(comment_view.getWindowToken(), 0);
                    }
                    List<CommentItem> comments = lists.get(position).getComments();
                    comments.add(new CommentItem("敌法师", comments.get(num).getName(), ":" + s));
                    notifyDataSetChanged();
                    edit.setText("");
                    edit.setHint("");
                    numMap.put(commentPos + "&" + commentNum, "");
                    positionMap.put(commentPos,numMap);
                    comment_view.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void showlikes(int position, boolean show) {
            List<String> likes = lists.get(position).getLikes();
            lists.get(position).setShow(show);
            if(show && !likes.contains("船长")){
                likes.add("船长");
            }else {
                likes.remove("船长");
            }
            notifyDataSetChanged();
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){

        if(ev.getAction() == MotionEvent.ACTION_DOWN){

            if(isShouldHideInput(comment_view, ev)){
                if(imm!= null){
                    if(isComment && curPosition != 0 && !edit.getText().toString().equals("")){
                        map.put(curPosition, edit.getText().toString());
                    }else {
                        numMap.put(commentPos+"&"+commentNum, edit.getText().toString());
                        positionMap.put(commentPos, numMap);
                    }
                    imm.hideSoftInputFromWindow(comment_view.getWindowToken(),0);
                    comment_view.setVisibility(View.GONE);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        if(getWindow().superDispatchTouchEvent(ev)){
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v,MotionEvent event){
        if(v != null && v instanceof RelativeLayout){
            int[] leftTop = {0,0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top +v.getHeight();
            int right = left + v.getWidth();

            if(event.getX() > left && event.getX() < right && event.getY()>top &&event.getY()<bottom){
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            }else {
                return true;
            }
        }
        return false;
    }

}
