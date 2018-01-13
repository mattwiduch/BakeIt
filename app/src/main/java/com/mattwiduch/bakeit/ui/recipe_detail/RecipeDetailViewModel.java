package com.mattwiduch.bakeit.ui.recipe_detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import com.mattwiduch.bakeit.data.RecipeRepository;
import com.mattwiduch.bakeit.data.database.entries.Ingredient;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import com.mattwiduch.bakeit.data.database.entries.Step;
import com.mattwiduch.bakeit.utils.AbsentLiveData;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;

/**
 * {@link ViewModel} for {@link RecipeDetailActivity}
 */
public class RecipeDetailViewModel extends ViewModel {

  final MutableLiveData<Integer> recipeId = new MutableLiveData<>();
  private final LiveData<Recipe> mRecipe;
  private final LiveData<List<Ingredient>> mIngredients;
  private final LiveData<List<Step>> mSteps;

  @Inject
  RecipeDetailViewModel(RecipeRepository repository) {
    // Recipe
    mRecipe = Transformations.switchMap(recipeId, recipeId -> {
      if (recipeId == null) {
        return AbsentLiveData.create();
      } else {
        return repository.getRecipe(recipeId);
      }
    });

    // Ingredients
    mIngredients = Transformations.switchMap(recipeId, recipeId -> {
      if (recipeId == null) {
        return AbsentLiveData.create();
      } else {
        return repository.getIngredientsForRecipe(recipeId);
      }
    });

    // Steps
    mSteps = Transformations.switchMap(recipeId, recipeId -> {
      if (recipeId == null) {
        return AbsentLiveData.create();
      } else {
        return repository.getStepsForRecipe(recipeId);
      }
    });
  }

  LiveData<Recipe> getRecipe() {
    return mRecipe;
  }

  LiveData<List<Ingredient>> getRecipeIngredients() {
    return mIngredients;
  }

  LiveData<List<Step>> getRecipeSteps() {
    return mSteps;
  }

  public void setRecipeId(int recipeId) {
    if (Objects.equals(this.recipeId.getValue(), recipeId)) {
      return;
    }
    this.recipeId.setValue(recipeId);
  }
}
