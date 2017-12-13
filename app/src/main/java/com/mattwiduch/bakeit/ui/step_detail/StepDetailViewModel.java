package com.mattwiduch.bakeit.ui.step_detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.mattwiduch.bakeit.data.RecipeRepository;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import com.mattwiduch.bakeit.data.database.entries.Step;
import java.util.List;

/**
 * {@link ViewModel} for {@link StepDetailActivity}
 */
public class StepDetailViewModel extends ViewModel {
  // Current recipes
  private final LiveData<Recipe> mRecipe;
  // Current recipe step
  private final LiveData<Step> mCurrentStep;
  // List of all steps
  private final LiveData<List<Step>> mRecipeSteps;

  public StepDetailViewModel (RecipeRepository repository, int recipeId, int stepNumber) {
    mRecipe = repository.getRecipe(recipeId);
    mCurrentStep = repository.getStep(recipeId, stepNumber);
    mRecipeSteps = repository.getStepsForRecipe(recipeId);
  }

  LiveData<Recipe> getRecipe() {return mRecipe;}
  LiveData<Step> getCurrentStep() {return mCurrentStep;}
  LiveData<List<Step>> getRecipeSteps() {return mRecipeSteps;}
}
