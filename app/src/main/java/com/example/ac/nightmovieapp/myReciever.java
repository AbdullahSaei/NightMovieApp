package com.example.ac.nightmovieapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by AC on 11/14/2016.
 */

public class myReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(myReciever.class.getSimpleName(), "action: " + intent.getAction());
        Log.d("Test", "onReceive: Called");
        if (isConnected(context)) {
            Toast.makeText(context, "Connected.", Toast.LENGTH_LONG).show();
//TODO:
//            new MainActivityFragment().update();
        }else Toast.makeText(context, "Lost connect.", Toast.LENGTH_LONG).show();
    }

    public boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        return isConnected;
    }
}