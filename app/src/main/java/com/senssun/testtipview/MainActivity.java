package com.senssun.testtipview;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.senssun.permission.PermissionConstants;
import com.senssun.permission.PermissionUtils;
import com.senssun.permission.Utils;
import com.sensun.baseutil.TipViewManager;
import com.sensun.baseutil.tipview.ui.TipView;

import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TipViewManager tipViewManager = TipViewManager.newInstance(this);
        tipViewManager.setTipViewCallback(new TipViewManager.TipViewCallback() {

            @Override
            public void onBlue(boolean show) {
                Toast.makeText(MainActivity.this,"blue",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPermission(boolean show) {
                Toast.makeText(MainActivity.this,"onPermission",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLocation(boolean show) {
                Toast.makeText(MainActivity.this,"onLocation",Toast.LENGTH_LONG).show();
            }
        });
        tipViewManager.addBlue().addLocation().addPermission();


        tipViewManager.attachToLayoutHead(R.id.ll_layout);
        tipViewManager.getPermissionView();
        boolean isGranted = PermissionUtils.hasPermission(Utils.getApp(), Manifest.permission.ACCESS_FINE_LOCATION);

    }
}
