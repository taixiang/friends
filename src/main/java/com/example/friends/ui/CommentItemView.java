package com.example.friends.ui;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.friends.CommentItem;
import com.example.friends.R;

/**
 * Created by taixiang on 2015/12/30.
 */
public class CommentItemView extends LinearLayout {
    TextView nameText;
    CommentItem commentItem;
    public CommentItemView(Context context) {
        super(context);
        initView(context);
    }

    public CommentItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context){
        View.inflate(context, R.layout.view_comment_item,this);
        nameText = (TextView) findViewById(R.id.nameText);
    }

    public void setData(CommentItem commentItem){
        this.commentItem = commentItem;
        StringBuilder sb = new StringBuilder();
        sb.append(commentItem.getName());
        int nameLenght = commentItem.getName().length();

        int toNameLength = 0;
        if(commentItem.getToName() != null && commentItem.getToName().length() > 0){
            sb.append("回复");
            sb.append(commentItem.getToName());
            toNameLength = commentItem.getToName().length() + 2;
        }
        sb.append(commentItem.getComment());

        SpannableStringBuilder ssb = new SpannableStringBuilder(sb.toString());

        ForegroundColorSpan nameSpan = new ForegroundColorSpan(getResources().getColor(R.color.textBlue));
        ForegroundColorSpan toNameSpan = new ForegroundColorSpan(getResources().getColor(R.color.textBlue));
        ssb.setSpan(nameSpan, 0, nameLenght, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        if(toNameLength > 2){
            ssb.setSpan(toNameSpan,nameLenght+2,nameLenght+toNameLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        nameText.setText(ssb);
    }


}
