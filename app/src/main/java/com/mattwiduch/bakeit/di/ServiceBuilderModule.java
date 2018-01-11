package com.mattwiduch.bakeit.di;

import com.mattwiduch.bakeit.data.network.RecipeFirebaseJobService;
import com.mattwiduch.bakeit.data.network.RecipeSyncIntentService;
import com.mattwiduch.bakeit.ui.widget.IngredientsWidgetService;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceBuilderModule {
  @ContributesAndroidInjector
  abstract IngredientsWidgetService contributeIngredientsWidgetService();

  @ContributesAndroidInjector
  abstract RecipeFirebaseJobService contributeRecipeFirebaseJobService();

  @ContributesAndroidInjector
  abstract RecipeSyncIntentService contributeRecipeSyncIntentService();
}
