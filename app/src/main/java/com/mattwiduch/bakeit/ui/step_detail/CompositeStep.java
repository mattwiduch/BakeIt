package com.mattwiduch.bakeit.ui.step_detail;

import com.mattwiduch.bakeit.data.database.entries.Recipe;
import com.mattwiduch.bakeit.data.database.entries.Step;
import java.util.List;

/**
 * A helper class that connects all LiveData objects providing step data.
 */
public class CompositeStep {

  private boolean isConnected;
  private Recipe recipe;
  private Step step;
  private List<Step> stepList;

  public CompositeStep() {
    isConnected = true;
  }

  public boolean isConnected() {
    return isConnected;
  }

  public void setConnected(boolean connected) {
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

  public List<Step> getStepList() {
    return stepList;
  }

  public void setStepList(List<Step> stepList) {
    this.stepList = stepList;
  }
}
