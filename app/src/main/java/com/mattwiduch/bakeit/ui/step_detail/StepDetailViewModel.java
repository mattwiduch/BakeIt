package com.mattwiduch.bakeit.ui.step_detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import com.mattwiduch.bakeit.data.RecipeRepository;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import com.mattwiduch.bakeit.data.database.entries.Step;
import com.mattwiduch.bakeit.utils.AbsentLiveData;
import com.mattwiduch.bakeit.utils.ConnectionDetector;
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
  StepDetailViewModel(RecipeRepository repository, Context context) {
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
      if (compositeStep != null) {
        compositeStep.setRecipe(recipe);
        mStepMediator.postValue(compositeStep);
      }
    });

    mStepMediator.addSource(mRecipeSteps, steps -> {
      CompositeStep compositeStep = mStepMediator.getValue();
      if (compositeStep != null) {
        compositeStep.setStepList(steps);
        mStepMediator.postValue(compositeStep);
      }
    });

    mStepMediator.addSource(mCurrentStep, step -> {
      CompositeStep compositeStep = mStepMediator.getValue();
      if (compositeStep != null) {
        compositeStep.setStep(step);
        mStepMediator.postValue(compositeStep);
      }
    });

    // Add network connection observer to step mediator live data
    ConnectionDetector connectionDetector = new ConnectionDetector(context);
    mStepMediator.addSource(connectionDetector, status -> {
      if (status != null) {
        CompositeStep compositeStep = mStepMediator.getValue();
        if (compositeStep != null) {
          compositeStep.setConnected(status.getIsConnected());
          mStepMediator.postValue(compositeStep);
        }
      }
    });
  }

  LiveData<CompositeStep> getCompositeStep() {
    return mStepMediator;
  }

  void setStepData(int recipeId, int stepNumber) {
    StepData stepData = new StepData(recipeId, stepNumber);
    if (Objects.equals(this.mStepData.getValue(), stepData)) {
      return;
    }
    this.mStepData.postValue(stepData);
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