package com.mattwiduch.bakeit.ui.step_detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import com.mattwiduch.bakeit.data.RecipeRepository;

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 * {@link RecipeRepository} and recipe id.
 */
public class StepDetailModelFactory extends ViewModelProvider.NewInstanceFactory {

  private final RecipeRepository mRepository;
  private final int mRecipeId;
  private final int mStepId;

  public StepDetailModelFactory(RecipeRepository repository, int recipeId, int stepId) {
    mRepository = repository;
    mRecipeId = recipeId;
    mStepId = stepId;
  }

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    //noinspection unchecked
    return (T) new StepDetailViewModel(mRepository, mRecipeId, mStepId);
  }
}