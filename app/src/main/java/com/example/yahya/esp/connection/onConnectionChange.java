package com.example.yahya.esp.connection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


public class onConnectionChange extends BroadcastReceiver {
    int state = -1;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("app","Network connectivity change");
        if(intent.getExtras()!=null) {
            NetworkInfo ni=(NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
            if(ni!=null && ni.getState()==NetworkInfo.State.CONNECTED) {
                state =1;
            }
        }
        if(intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE)) {
            state = 2;
        }

        Intent local = new Intent();
        local.setAction("service.to.activity.transfer");
        local.putExtra("state", state);
        context.sendBroadcast(local);
    }


}