package com.mattwiduch.bakeit.ui.step_detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
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
  private final MutableLiveData<StepData> mStepData = new MutableLiveData<>();
  private MediatorLiveData<CompositeStep> mStepMediator;

  // Current recipe
  private final LiveData<Recipe> mRecipe;
  // Current recipe step
  private final LiveData<Step> mCurrentStep;
  // List of all steps
  private final LiveData<List<Step>> mRecipeSteps;

  @Inject
  StepDetailViewModel (RecipeRepository repository) {
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

    mStepMediator = new MediatorLiveData<>();
    mStepMediator.postValue(new CompositeStep());

    mStepMediator.addSource(mRecipe, recipe -> {
      CompositeStep compositeStep = mStepMediator.getValue();
      compositeStep.setRecipe(recipe);
      mStepMediator.postValue(compositeStep);
    });

    mStepMediator.addSource(mRecipeSteps, steps -> {
      CompositeStep compositeStep = mStepMediator.getValue();
      compositeStep.setStepList(steps);
      mStepMediator.postValue(compositeStep);
    });

    mStepMediator.addSource(mCurrentStep, step -> {
      CompositeStep compositeStep = mStepMediator.getValue();
      compositeStep.setStep(step);
      mStepMediator.postValue(compositeStep);
    });
  }

  MediatorLiveData<CompositeStep> getStepMediator() {return mStepMediator;}

  void setStepData(int recipeId, int stepNumber) {
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

/**
 * A helper class that connects all LiveData objects providing step data.
 */
class CompositeStep {
  private boolean isConnected;
  private Recipe recipe;
  private Step step;
  private List<Step> stepList;

  CompositeStep() {
    isConnected = true;
  }

  boolean isConnected() {
    return isConnected;
  }

  void setConnected(boolean connected) {
    isConnected = connected;
  }

  public Recipe getRecipe() {
    return recipe;
  }

  public void setRecipe(Recipe recipe) {
    this.recipe = recipe;
  }

  public Step getStep() {
    return step;
  }

  public void setStep(Step step) {
    this.step = step;
  }

  List<Step> getStepList() {
    return stepList;
  }

  void setStepList(List<Step> stepList) {
    this.stepList = stepList;
  }
}