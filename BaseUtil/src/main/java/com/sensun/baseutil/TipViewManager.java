package com.sensun.baseutil;

import android.app.Activity;
import android.app.Application;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import com.sensun.baseutil.tipview.core.AppLifecycleCallbacks;
import com.sensun.baseutil.tipview.ui.TipView;


public class TipViewManager {
    private static Application myApp;
    private AppLifecycleCallbacks mAppLifecycleCallbacks;
    private Activity mActivity;
    private LinearLayout rootView;
    private ViewGroup parentView;

    private TipViewManager(Activity mActivity) {
        this.mActivity = mActivity;
        mAppLifecycleCallbacks = new AppLifecycleCallbacks(mActivity,this);
        myApp.registerActivityLifecycleCallbacks(mAppLifecycleCallbacks);
        rootView = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.custom_layout, null).findViewById(R.id.ll_layout);

    }

    public static TipViewManager newInstance(Activity activity) {
        if (activity == null) {
            throw new NullPointerException("activity is null");
        }
        if (myApp == null) {
            myApp = activity.getApplication();
        }
        return new TipViewManager(activity);
    }

    public TipViewManager buildTip(String actionName, String tip, TipView.OnTipViewListener clickEvent) {
        TipView tipView = addTip(tip, clickEvent);
        rootView.addView(tipView);
        mAppLifecycleCallbacks.addIntentFilter(actionName, tipView);
        return this;
    }

    private TipView addTip(String tip, TipView.OnTipViewListener clickEvent) {
        return new TipView(mActivity, tip, clickEvent);
    }

    public LinearLayout getTipViews() {
        return rootView;
    }


    public TipView getTipView(String actionName) {
        return mAppLifecycleCallbacks.getTiView(actionName);
    }

    public void attachToLayoutHead(int resId) {
        if (mActivity != null) {
            parentView = (ViewGroup) mActivity.findViewById(resId);
            if (parentView instanceof LinearLayout) {
                parentView.addView(rootView, 0);
            } else if (parentView instanceof FrameLayout) {
                parentView.addView(rootView);
            } else {
                parentView.addView(rootView);
            }
        }
    }

    public void destroy() {
        parentView.removeView(rootView);
    }
}
