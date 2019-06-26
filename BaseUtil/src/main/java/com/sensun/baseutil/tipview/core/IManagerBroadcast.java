package com.sensun.baseutil.tipview.core;
import android.content.IntentFilter;

import com.sensun.baseutil.tipview.ui.TipView;

import java.util.HashMap;
import java.util.Map;


public interface IManagerBroadcast {

    void addIntentFilter(String actionName, TipView view);
}
