package com.sensun.baseutil.tipview.core;


import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import com.sensun.baseutil.TipViewManager;
import com.sensun.baseutil.tipview.ui.TipView;

import java.util.Iterator;
import java.util.Set;


public class AppLifecycleCallbacks extends BroadcastReceiver implements Application.ActivityLifecycleCallbacks, IManagerBroadcast {
    private static final String TAG = "AppLifecycleCallbacks";
    Activity mActivity;
    TipViewManager mTipViewManager;
    public AppLifecycleCallbacks(Activity mActivity,TipViewManager tipViewManager) {
        mTipViewManager=tipViewManager;
        this.mActivity = mActivity;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (activity == mActivity) {
            activity.registerReceiver(this, mIntentFilter);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activity == mActivity) {
            activity.unregisterReceiver(this);
            activity.getApplication().unregisterActivityLifecycleCallbacks(this);
//            mTipViewManager.destroy();
        }
    }

    @Override
    public void addIntentFilter(String actionName, TipView view) {
        views.put(actionName, view);
        mIntentFilter.addAction(actionName);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String actionName = intent.getAction();
        TipView view = getTiView(actionName);
        if (view != null) {
            view.onStatus();
        }
    }

    public TipView getTiView(String actionName) {
        Set<String> set = views.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (key.equals(actionName)) {
                return views.get(key);
            }
        }
        return null;
    }
}
