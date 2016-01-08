package com.example.friends.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.friends.Main2Activity;
import com.example.friends.R;


/**
 * Created by taixiang on 2015/12/16.
 * 内容大于一定行数，提供收起，全文的功能
 */
public class MoreTextView extends LinearLayout {
    TextView contentTextView;
    int initLines = 6;
    int maxLines = 15;
    boolean PICK_UP = true;
    boolean SEE_ALL = false;
    TextView tipTextView;
    String contentText;
    Context context;
    int lineNum;
    boolean flag = SEE_ALL;
    OnClickListener contentTextViewOnClickListener;
    OnClickListener tipTextViewOnClickListener;

    public MoreTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.my_text_view,this);
        contentTextView = (TextView) view.findViewById(R.id.contentTextView);
        tipTextView = (TextView) view.findViewById(R.id.tipTextView);
//        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.my_text_view);
//        contentText = typedArray.getString(R.styleable.my_text_view_contentText);
        tipTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tipTextViewOnClick();
            }
        });


    }

    private void tipTextViewOnClick(){
        if(lineNum <= initLines){
            return;
        }
        if(SEE_ALL == flag){
            tipTextView.setText("收起");
            flag = PICK_UP;
            contentTextView.setMaxLines(lineNum);
        }else if(PICK_UP = flag){
            tipTextView.setText("全文");
            flag = SEE_ALL;
            contentTextView.setMaxLines(initLines);
        }
    }

    private void initMyTextView(){
        Log.i("》》》》 ","  linenum="+lineNum);
        if(lineNum > maxLines){
            tipTextView.setVisibility(GONE);
            Log.i("》》》》   ", " 1111111 ");
            contentTextView.setSingleLine(true);
            contentTextView.setEllipsize(TextUtils.TruncateAt.END);
            contentTextView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), Main2Activity.class);
                    intent.putExtra("content", contentTextView.getText().toString());
                    getContext().startActivity(intent);
                }
            });
        }else if(lineNum > initLines && lineNum < maxLines){
            Log.i("》》》》   "," 22222222 ");
            contentTextView.setSingleLine(false);
            if(tipTextView.getVisibility() != VISIBLE){
                tipTextView.setVisibility(VISIBLE);
            }
            contentTextView.setMaxLines(initLines);
        }else if(lineNum <= initLines){
            Log.i("》》》》   "," 333333333 ");
            contentTextView.setSingleLine(false);
            if(tipTextView.getVisibility() != GONE){
                tipTextView.setVisibility(GONE);
            }
        }
    }

    private void initFlag(){
        String content = tipTextView.getText().toString();
        if(!("全文".equals(content))){
            tipTextView.setText("全文");
            flag = SEE_ALL;
        }
    }

    private void getLineNum(){
        contentTextView.post(new Runnable() {
            @Override
            public void run() {
                lineNum = contentTextView.getLineCount();
                initMyTextView();
            }
        });
    }

    public void setContentText(String content){
        contentTextView.setText(content);
        getLineNum();
        initFlag();
        contentTextView.setVisibility(VISIBLE);
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = measureTextViewHeight(contentTextView.getText().toString(),(int)contentTextView.getTextSize(),width);
        Log.i("》》》》  ","  height"+ height);

    }

    private int measureTextViewHeight(String text, int textSize, int deviceWidth) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(deviceWidth, MeasureSpec.AT_MOST);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();
    }


    public TextView getContentTextView() {
        return contentTextView;
    }

    public TextView getTipTextView() {
        return tipTextView;
    }

    public void setContentTextViewOnClickListener(OnClickListener contentTextViewOnClickListener) {
        this.contentTextViewOnClickListener = contentTextViewOnClickListener;
    }
}
