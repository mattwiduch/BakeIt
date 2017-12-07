package com.mattwiduch.bakeit.ui.recipe_detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.mattwiduch.bakeit.data.RecipeRepository;
import com.mattwiduch.bakeit.data.database.entries.Ingredient;
import com.mattwiduch.bakeit.data.database.entries.Step;
import java.util.List;

/**
 * {@link ViewModel} for {@link RecipeDetailActivity}
 */
public class RecipeDetailViewModel extends ViewModel {
  // List of all ingredients
  private final LiveData<List<Ingredient>> mRecipeIngredients;
  // List of all steps
  private final LiveData<List<Step>> mRecipeSteps;

  public RecipeDetailViewModel (RecipeRepository repository, int recipeId) {
    mRecipeIngredients = repository.getIngredientsForRecipe(recipeId);
    mRecipeSteps = repository.getStepsForRecipe(recipeId);
  }

  LiveData<List<Ingredient>> getRecipeIngredients() {return mRecipeIngredients;}
  LiveData<List<Step>> getRecipeSteps() {return mRecipeSteps;}
}
