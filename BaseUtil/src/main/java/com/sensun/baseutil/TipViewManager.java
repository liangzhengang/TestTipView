package com.sensun.baseutil;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.senssun.permission.PermissionConstants;
import com.senssun.permission.PermissionUtils;
import com.senssun.permission.Utils;
import com.sensun.baseutil.tipview.core.AppLifecycleCallbacks;
import com.sensun.baseutil.tipview.ui.TipView;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class TipViewManager {
    private static Application myApp;
    private AppLifecycleCallbacks mAppLifecycleCallbacks;
    private Activity mActivity;
    private LinearLayout rootView;
    private ViewGroup parentView;
    private TipViewCallback tipViewCallback;

    public TipViewManager setTipViewCallback(TipViewCallback tipViewCallback) {
        this.tipViewCallback = tipViewCallback;
        return this;
    }

    private TipViewManager(Activity mActivity) {
        this.mActivity = mActivity;
        mAppLifecycleCallbacks = new AppLifecycleCallbacks(mActivity, this);
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

    public LinearLayout getTipViews() {
        return rootView;
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

    public TipViewManager addBlue(String str, String btn) {
        if (TextUtils.isEmpty(str)) {
            str = "BLUETOOTH  IS DISABLED";
        }
        this.buildTip(BluetoothAdapter.ACTION_STATE_CHANGED, str, btn, new TipView.OnTipViewListener() {
            @Override
            public void onClick(TipView tipView) {
                Intent intentOpen = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                mActivity.startActivityForResult(intentOpen, 10);
            }

            @Override
            public void onTipStatus(TipView tipView) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (bluetoothAdapter.isEnabled()) {
                    tipView.dismiss();
                } else {
                    tipView.show();
                }
                if (tipViewCallback != null) {
                    tipViewCallback.onBlue(bluetoothAdapter.isEnabled());
                }
            }
        });
        return this;
    }

    public TipViewManager buildTip(String actionName, String tip, TipView.OnTipViewListener clickEvent) {
        TipView tipView = addTip(tip, clickEvent);
        rootView.addView(tipView);
        mAppLifecycleCallbacks.addIntentFilter(actionName, tipView);
        return this;
    }

    public TipViewManager buildTip(String actionName, String tip, String btn, TipView.OnTipViewListener clickEvent) {
        TipView tipView = addTip(tip, clickEvent);
        tipView.setBtn(btn);
        rootView.addView(tipView);
        mAppLifecycleCallbacks.addIntentFilter(actionName, tipView);
        return this;
    }

    private TipView addTip(String tip, TipView.OnTipViewListener clickEvent) {
        return new TipView(mActivity, tip, clickEvent);
    }

    public TipViewManager addLocation(String str, String btn) {
        if (TextUtils.isEmpty(str)) {
            str = "LOCATION IS DISABLED";
        }
        buildTip(LocationManager.PROVIDERS_CHANGED_ACTION, str, btn, new TipView.OnTipViewListener() {
            @Override
            public void onClick(TipView tipView) {
                //跳转GPS设置界面
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mActivity.startActivityForResult(intent, 20);
            }

            @Override
            public void onTipStatus(TipView tipView) {
                if (checkGPSIsOpen()) {
                    tipView.dismiss();
                } else {
                    tipView.show();
                }
                if (tipViewCallback != null) {
                    tipViewCallback.onLocation(checkGPSIsOpen());
                }
            }
        });
        return this;
    }

    public boolean getPermissionView() {

        TipView tipView = getTipView(Manifest.permission.ACCESS_COARSE_LOCATION);
        boolean isGranted = PermissionUtils.hasPermission(Utils.getApp(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if (isGranted) {
            tipView.dismiss();
        } else {
            tipView.show();
        }
        if (tipViewCallback != null) {
            tipViewCallback.onPermission(isGranted);
        }
        return isGranted;
    }

    private static final String TAG = "TipViewManager";

    public TipViewManager addPermission(String str, String btn) {
        if (TextUtils.isEmpty(str)) {
            str = "PERMISSION IS DISABLED";
        }
        buildTip(Manifest.permission.ACCESS_COARSE_LOCATION, str, btn, new TipView.OnTipViewListener() {
            @Override
            public void onClick(TipView tipView) {
                PermissionUtils.permission(PermissionConstants.LOCATION).callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        //权限授权成功
                        Log.d(TAG, "onGranted: " + permissionsGranted.get(0));
                        Intent intent = new Intent(Manifest.permission.ACCESS_COARSE_LOCATION);
                        mActivity.sendBroadcast(intent);
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                        if (permissionsDeniedForever.size() > 0) {
                            Toast.makeText(Utils.getApp(), "The location permissions are not allowed. Please grant permission to the app information interface", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                            Uri uri = Uri.fromParts("package", Utils.getApp().getPackageName(), null);
                            intent.setData(uri);
                            Utils.getApp().startActivity(intent);
                        }
                    }
                }).request();
            }

            @Override
            public void onTipStatus(TipView tipView) {
                boolean isGranted = PermissionUtils.hasPermission(Utils.getApp(), Manifest.permission.ACCESS_COARSE_LOCATION);
                if (isGranted) {
                    tipView.dismiss();
                } else {
                    tipView.show();
                }
            }
        });
        return this;
    }


    private boolean checkGPSIsOpen() {
        boolean gps, network;
        LocationManager locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        gps = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        network = locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER);
        return gps || network;
    }

    public boolean getBlueView() {
        TipView blueView = getTipView(BluetoothAdapter.ACTION_STATE_CHANGED);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            blueView.show();
        } else {
            blueView.dismiss();
        }
        return bluetoothAdapter.isEnabled();
    }

    public TipView getTipView(String actionName) {
        return mAppLifecycleCallbacks.getTiView(actionName);
    }

    public boolean getLocationView() {
        if (Build.VERSION.SDK_INT >= 23) {
            TipView gpsView = getTipView(LocationManager.PROVIDERS_CHANGED_ACTION);
            if (!checkGPSIsOpen()) {
                gpsView.show();
            } else {
                gpsView.dismiss();
            }
        }
        return checkGPSIsOpen();
    }

    public interface TipViewCallback {
        void onBlue(boolean show);

        void onPermission(boolean show);

        void onLocation(boolean show);
    }
}
