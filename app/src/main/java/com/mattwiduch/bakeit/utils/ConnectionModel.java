package com.mattwiduch.bakeit.utils;

/**
 * Connection model that contains connection state as well as its type.
 */
public class ConnectionModel {
  private int mConnectionType;
  private boolean mIsConnected;

  ConnectionModel(int type, boolean isConnected) {
    mConnectionType = type;
    mIsConnected = isConnected;
  }

  public int getType() {
    return mConnectionType;
  }

  public boolean getIsConnected() {
    return mIsConnected;
  }
}
