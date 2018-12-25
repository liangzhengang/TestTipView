package com.sensun.baseutil.tipview.core;
import android.content.IntentFilter;

import com.sensun.baseutil.tipview.ui.TipView;

import java.util.HashMap;
import java.util.Map;


public interface IManagerBroadcast {
    IntentFilter mIntentFilter = new IntentFilter();
    Map<String, TipView> views = new HashMap<>();

    void addIntentFilter(String actionName, TipView view);
}
