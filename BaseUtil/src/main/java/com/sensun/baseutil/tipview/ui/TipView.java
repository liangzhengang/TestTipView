package com.sensun.baseutil.tipview.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sensun.baseutil.R;


public class TipView extends LinearLayout {
    ExpandableUnClickLayout expandableUnClickLayout;
    TextView tvContent, tvBtn;
    OnTipViewListener mOnTipViewListener;

    public TipView(Context context) {
        super(context);
        init();
    }

    void init() {
       LayoutInflater.from(getContext()).inflate(R.layout.custom_view, this, true);
        expandableUnClickLayout = (ExpandableUnClickLayout) findViewById(R.id.layout);
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvBtn = (TextView) findViewById(R.id.tv_btn);
    }

    public TipView(Context context, String tip, final OnTipViewListener listener) {
        super(context);
        init();
        mOnTipViewListener = listener;
        tvBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(TipView.this);
                }
            }
        });
        setTip(tip);
    }

    public void setTip(String tip) {
        tvContent.setText(tip);
    }

    public TipView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void onStatus() {
        if (mOnTipViewListener != null)
            mOnTipViewListener.onTipStatus(this);
    }

    public void show() {
        expandableUnClickLayout.ExpandOpen();
        setVisibility(VISIBLE);
    }

    public void dismiss() {
        expandableUnClickLayout.ExpandClose();
       setVisibility(GONE);
    }

    public interface OnTipViewListener {
        void onClick(TipView tipView);
        void onTipStatus(TipView tipView);
    }

}
