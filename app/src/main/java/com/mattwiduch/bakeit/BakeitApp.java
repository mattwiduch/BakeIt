package com.mattwiduch.bakeit;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import com.mattwiduch.bakeit.di.injector.AppInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;
import javax.inject.Inject;

/**
 * Entry point to the App. It's main purpose is to setup Dagger 2 dependency injection.
 */
public class BakeitApp extends Application implements HasActivityInjector, HasServiceInjector {

  @Inject
  DispatchingAndroidInjector<Service> serviceInjector;
  @Inject
  DispatchingAndroidInjector<Activity> activityInjector;

  @Override
  public void onCreate() {
    super.onCreate();
    AppInjector.init(this);
  }

  @Override
  public DispatchingAndroidInjector<Activity> activityInjector() {
    return activityInjector;
  }

  @Override
  public DispatchingAndroidInjector<Service> serviceInjector() {
    return serviceInjector;
  }
}
