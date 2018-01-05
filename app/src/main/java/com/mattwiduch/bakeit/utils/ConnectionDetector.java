package com.mattwiduch.bakeit.utils;

import android.arch.lifecycle.LiveData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Connection detection utility based on LivaData object.
 */
public class ConnectionDetector extends LiveData<ConnectionModel> {
  private Context mContext;

  public ConnectionDetector(Context context) {
    mContext = context;
  }

  @Override
  protected void onActive() {
    super.onActive();
    IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    mContext.registerReceiver(networkReceiver, filter);
  }

  @Override
  protected void onInactive() {
    super.onInactive();
    mContext.unregisterReceiver(networkReceiver);
  }

  private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      if(intent.getExtras()!=null) {
        ConnectivityManager connectivityManager =
            (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork =
            connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(isConnected) {
          postValue(new ConnectionModel(activeNetwork.getType(), true));
        } else {
          postValue(new ConnectionModel(0,false));
        }
      }
    }
  };
}