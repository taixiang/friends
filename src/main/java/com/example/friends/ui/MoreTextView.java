package com.example.friends.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
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

    boolean mCollapsed = true;
    int mTextHeightWithMaxLines;
    int mMarginBetweenTxtAndBottom;
    int mCollapsedHeight;
    boolean mRelayout;
    SparseBooleanArray mCollapsedStatus;
    int mPosition;
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
        initFlag();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        contentTextView.setMaxLines(Integer.MAX_VALUE);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(contentTextView.getLineCount() < 6){
            return;
        }
        if(contentTextView.getLineCount() > 15){
            contentTextView.setSingleLine();
            contentTextView.setEllipsize(TextUtils.TruncateAt.END);
            return;
        }
        mTextHeightWithMaxLines = getRealTextViewHeight(contentTextView);
        if(mCollapsed){
            contentTextView.setMaxLines(6);
        }else {
            tipTextView.setText("收起");
        }
        tipTextView.setVisibility(VISIBLE);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if (mCollapsed) {
//            // Gets the margin between the TextView's bottom and the ViewGroup's bottom
//            contentTextView.post(new Runnable() {
//                @Override
//                public void run() {
//                    mMarginBetweenTxtAndBottom = getHeight() - contentTextView.getHeight();
//                }
//            });
//            // Saves the collapsed height of this ViewGroup
//            mCollapsedHeight = getMeasuredHeight();
//        }
    }

    private int getRealTextViewHeight(TextView textView){
        int textHeight = textView.getLayout().getLineTop(textView.getLineCount());
        int padding = textView.getCompoundPaddingTop() + textView.getCompoundPaddingBottom();
        return textHeight + padding;
    }


    private void tipTextViewOnClick(){

        mCollapsed = !mCollapsed;
        if (mCollapsedStatus != null) {
            mCollapsedStatus.put(mPosition, mCollapsed);
        }
        if(mCollapsed){
            tipTextView.setText("全文");
            contentTextView.setMaxLines(contentTextView.getLineCount());
        }else {
            tipTextView.setText("收起");
            contentTextView.setMaxLines(initLines);
        }
    }

    private void initFlag(){
        String content = tipTextView.getText().toString();
        if(!("全文".equals(content))){
            tipTextView.setText("全文");
            flag = SEE_ALL;
        }
    }


    public void setContentText(String content,SparseBooleanArray collapsedStatus, int position){
        mCollapsedStatus = collapsedStatus;
        mPosition = position;
        boolean isCollapsed = collapsedStatus.get(position, true);
        clearAnimation();
        mCollapsed = isCollapsed;
        contentTextView.setText(content);


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
