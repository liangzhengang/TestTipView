package com.senssun.testtipview;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sensun.baseutil.TipViewManager;
import com.sensun.baseutil.tipview.ui.TipView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TipViewManager tipViewManager = TipViewManager.newInstance(this).buildTip(BluetoothAdapter.ACTION_STATE_CHANGED, "blue is enable", new TipView.OnTipViewListener() {
            @Override
            public void onClick(TipView tipView) {
                tipView.dismiss();
            }

            @Override
            public void onTipStatus(TipView tipView) {
                    BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
                    if (bluetoothAdapter.isEnabled())
                    {
                        tipView.dismiss();
                    }else
                    {
                        tipView.show();
                    }
            }
        });
        tipViewManager.attachToLayoutHead(R.id.ll_layout);
    }
}
