package com.sensun.baseutil.tipview.ui;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ExpandableUnClickLayout extends LinearLayout {

	private Context mContext;
	private LinearLayout mContentView;
	int mContentHeight = 0;
	private boolean isExpand;
	private boolean isClick=true;

	public ExpandableUnClickLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public boolean isExpand() {
		return isExpand;
	}



	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		this.mContentView.measure(widthMeasureSpec, 0);
		this.mContentHeight = this.mContentView.getMeasuredHeight();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		this.mContentView =  (LinearLayout) this.findViewWithTag("ExpandValue");
		mContentView.setVisibility(View.GONE);
	}


	public void ExpandClose(){
		if (isExpand) {
			isExpand = false;
			mContentView.setVisibility(GONE);

			if(mOnExpandListener!=null)mOnExpandListener.OnExpandListener(this,isExpand);
		}
	}

	public void ExpandOpen(){
		if (!isExpand) {
			isExpand = true;
			mContentView.setVisibility(VISIBLE);

			if(mOnExpandListener!=null)mOnExpandListener.OnExpandListener(this,isExpand);
		}
	}


	public interface OnExpandListener{
		void OnExpandListener(ViewGroup root, boolean isExpand);
	}

	OnExpandListener mOnExpandListener=null;
	public void setOnExpandListener(OnExpandListener e){
		mOnExpandListener=e;
	}
}
