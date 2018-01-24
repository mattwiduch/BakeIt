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

import com.mattwiduch.bakeit.di.scopes.FragmentScope;
import com.mattwiduch.bakeit.ui.recipe_detail.RecipeDetailFragment;
import com.mattwiduch.bakeit.ui.recipe_list.RecipeListFragment;
import com.mattwiduch.bakeit.ui.step_detail.StepDetailFragment;
import com.mattwiduch.bakeit.ui.widget.IngredientsWidgetConfigFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuilderModule {
  @FragmentScope
  @ContributesAndroidInjector
  abstract RecipeListFragment contributeRecipeListFragment();

  @FragmentScope
  @ContributesAndroidInjector
  abstract RecipeDetailFragment contributeRecipeDetailFragment();

  @FragmentScope
  @ContributesAndroidInjector
  abstract StepDetailFragment contributeStepDetailFragment();

  @FragmentScope
  @ContributesAndroidInjector
  abstract IngredientsWidgetConfigFragment contributeIngredientsWidgetConfigFragment();
}
