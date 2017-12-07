package com.mattwiduch.bakeit.ui.recipe_detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import com.mattwiduch.bakeit.data.RecipeRepository;

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 * {@link RecipeRepository} and recipe id.
 */

public class RecipeDetailModelFactory extends ViewModelProvider.NewInstanceFactory {

  private final RecipeRepository mRepository;
  private final int mRecipeId;

  public RecipeDetailModelFactory(RecipeRepository repository, int recipeId) {
    mRepository = repository;
    mRecipeId = recipeId;
  }

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    //noinspection unchecked
    return (T) new RecipeDetailViewModel(mRepository, mRecipeId);
  }
}
