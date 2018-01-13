/*
 * Copyright (C) 2017 The Android Open Source Project
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
package com.mattwiduch.bakeit.di;

import android.app.Application;
import com.mattwiduch.bakeit.BakeitApp;
import com.mattwiduch.bakeit.di.modules.ActivityBuilderModule;
import com.mattwiduch.bakeit.di.modules.ServiceBuilderModule;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import javax.inject.Singleton;

/**
 * Root for Dagger Graph. Responsible for providing application scope instances (Database, Retrofit,
 * SharedPrefs, etc.).
 */
@Singleton
@Component(modules = {
    AndroidSupportInjectionModule.class,
    AppModule.class,
    ActivityBuilderModule.class,
    ServiceBuilderModule.class
})
public interface AppComponent {
  // Adds method to the builder that binds application instance to Dagger graph
  @Component.Builder
  interface Builder {
    @BindsInstance
    Builder application(Application application);
    AppComponent build();
  }

  void inject(BakeitApp bakeitApp);
}