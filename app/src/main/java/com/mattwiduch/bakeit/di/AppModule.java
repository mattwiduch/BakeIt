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

import static com.mattwiduch.bakeit.data.database.RecipeDatabase.DATABASE_NAME;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import com.mattwiduch.bakeit.data.database.RecipeDao;
import com.mattwiduch.bakeit.data.database.RecipeDatabase;
import com.mattwiduch.bakeit.di.modules.ViewModelModule;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Defines classes and methods that provide dependencies in Application scope (Database, Retrofit,
 * SharedPrefs, etc.).
 */
@Module(includes = ViewModelModule.class)
class AppModule {

  @Provides
  @Singleton
  Context provideContext(Application application) {
    return application;
  }

  @Provides
  @Singleton
  RecipeDatabase provideDatabase(Application app) {
    return Room.databaseBuilder(app, RecipeDatabase.class, DATABASE_NAME).build();
  }

  @Provides
  @Singleton
  RecipeDao provideRecipeDao(RecipeDatabase db) {
    return db.recipeDao();
  }
}
