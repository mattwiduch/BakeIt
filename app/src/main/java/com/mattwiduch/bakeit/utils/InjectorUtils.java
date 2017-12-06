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

package com.mattwiduch.bakeit.utils;

import android.content.Context;
import com.mattwiduch.bakeit.AppExecutors;
import com.mattwiduch.bakeit.data.RecipeRepository;
import com.mattwiduch.bakeit.data.database.RecipeDatabase;

import com.mattwiduch.bakeit.data.network.RecipeNetworkDataSource;
import com.mattwiduch.bakeit.ui.recipe_list.RecipeListModelFactory;

/**
 * Provides static methods to inject the various classes needed for Bake it
 */

public class InjectorUtils {

  private InjectorUtils() {
    // Utility class. Not meant to be instantiated.
  }

  public static RecipeListModelFactory provideRecipeListViewModelFactory(Context context) {
    RecipeRepository repository = provideRepository(context.getApplicationContext());
    return new RecipeListModelFactory(repository);
  }

  private static RecipeRepository provideRepository(Context context) {
    RecipeDatabase database = RecipeDatabase.getInstance(context.getApplicationContext());
    AppExecutors executors = AppExecutors.getInstance();
    RecipeNetworkDataSource networkDataSource = RecipeNetworkDataSource.getInstance(
        context.getApplicationContext(), executors);
    return RecipeRepository.getInstance(database.recipeDao(), networkDataSource, executors);
  }

  public static RecipeNetworkDataSource provideNetworkDataSource(Context context) {
    AppExecutors executors = AppExecutors.getInstance();
    return RecipeNetworkDataSource.getInstance(context.getApplicationContext(), executors);
  }
}