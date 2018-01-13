package com.mattwiduch.bakeit.ui.step_detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import com.mattwiduch.bakeit.data.RecipeRepository;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import com.mattwiduch.bakeit.data.database.entries.Step;
import com.mattwiduch.bakeit.utils.AbsentLiveData;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;

/**
 * {@link ViewModel} for {@link StepDetailActivity}
 */
public class StepDetailViewModel extends ViewModel {
  final MutableLiveData<StepData> mStepData = new MutableLiveData<>();

  // Current recipe
  private final LiveData<Recipe> mRecipe;
  // Current recipe step
  private final LiveData<Step> mCurrentStep;
  // List of all steps
  private final LiveData<List<Step>> mRecipeSteps;

  @Inject
  public StepDetailViewModel (RecipeRepository repository) {
    mRecipe = Transformations.switchMap(mStepData, stepData -> {
      if (stepData == null) {
        return AbsentLiveData.create();
      } else {
       return repository.getRecipe(stepData.getRecipeId());
      }
    });

    mRecipeSteps = Transformations.switchMap(mStepData, stepData -> {
      if (stepData == null) {
        return AbsentLiveData.create();
      } else {
        return repository.getStepsForRecipe(stepData.getRecipeId());
      }
    });

    mCurrentStep = Transformations.switchMap(mStepData, stepData -> {
      if (stepData == null) {
        return AbsentLiveData.create();
      } else {
        return repository.getStep(stepData.getRecipeId(), stepData.getStepNumber());
      }
    });
  }

  LiveData<Recipe> getRecipe() {return mRecipe;}
  LiveData<Step> getCurrentStep() {return mCurrentStep;}
  LiveData<List<Step>> getRecipeSteps() {return mRecipeSteps;}

  public void setStepData(int recipeId, int stepNumber) {
    StepData stepData = new StepData(recipeId, stepNumber);
    if (Objects.equals(this.mStepData.getValue(), stepData)) {
      return;
    }
    this.mStepData.setValue(stepData);
  }
}

/**
 * Helper class to observe step data properties.
 */
class StepData {
  private int mRecipeId;
  private int mStepNumber;

  StepData(int recipeId, int stepNumber) {
    mRecipeId = recipeId;
    mStepNumber = stepNumber;
  }

  public int getRecipeId() {
    return mRecipeId;
  }

  public int getStepNumber() {
    return mStepNumber;
  }
}