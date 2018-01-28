/*
 * Copyright (C) 2018 Mateusz Widuch
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
