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
package com.mattwiduch.bakeit.di.modules;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import com.mattwiduch.bakeit.ui.BakeitViewModelFactory;
import com.mattwiduch.bakeit.ui.recipe_detail.RecipeDetailViewModel;
import com.mattwiduch.bakeit.ui.recipe_list.RecipeListViewModel;
import com.mattwiduch.bakeit.ui.step_detail.StepDetailViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
  @Binds
  @IntoMap
  @ViewModelKey(RecipeListViewModel.class)
  abstract ViewModel bindRecipeListViewModel(RecipeListViewModel recipeListViewModel);

  @Binds
  @IntoMap
  @ViewModelKey(RecipeDetailViewModel.class)
  abstract ViewModel bindRecipeDetailViewModel(RecipeDetailViewModel recipeDetailViewModel);

  @Binds
  @IntoMap
  @ViewModelKey(StepDetailViewModel.class)
  abstract ViewModel bindStepDetailViewModel(StepDetailViewModel stepDetailViewModel);

  @Binds
  abstract ViewModelProvider.Factory bindViewModelFactory(BakeitViewModelFactory factory);
}
