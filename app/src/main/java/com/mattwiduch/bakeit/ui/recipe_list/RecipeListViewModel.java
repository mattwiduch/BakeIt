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
package com.mattwiduch.bakeit.ui.recipe_list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.mattwiduch.bakeit.data.RecipeRepository;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import java.util.List;
import javax.inject.Inject;

/**
 * {@link ViewModel} for {@link RecipeListFragment}
 */

public class RecipeListViewModel extends ViewModel {

  // List of recipes shown to the user
  private final LiveData<List<Recipe>> mAllRecipes;

  @Inject
  RecipeListViewModel (RecipeRepository repository) {
    mAllRecipes = repository.getAllRecipes();
  }

  public LiveData<List<Recipe>> getAllRecipes() {
    return mAllRecipes;
  }
}
