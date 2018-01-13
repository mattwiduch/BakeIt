package com.mattwiduch.bakeit.di;

import com.mattwiduch.bakeit.data.network.RecipeFirebaseJobService;
import com.mattwiduch.bakeit.data.network.RecipeSyncIntentService;
import com.mattwiduch.bakeit.ui.widget.IngredientsWidgetService;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceBuilderModule {
  @ActivityScope
  @ContributesAndroidInjector
  abstract IngredientsWidgetService contributeIngredientsWidgetService();

  @ActivityScope
  @ContributesAndroidInjector
  abstract RecipeFirebaseJobService contributeRecipeFirebaseJobService();

  @ActivityScope
  @ContributesAndroidInjector
  abstract RecipeSyncIntentService contributeRecipeSyncIntentService();
}
