package com.mattwiduch.bakeit.ui.recipe_list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.mattwiduch.bakeit.data.RecipeRepository;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import java.util.List;

/**
 * {@link ViewModel} for {@link RecipeListActivity}
 */

public class RecipeListViewModel extends ViewModel {

  // List of recipes shown to the user
  private final LiveData<List<Recipe>> mAllRecipes;

  public RecipeListViewModel (RecipeRepository repository) {
    mAllRecipes = repository.getAllRecipes();
  }

  LiveData<List<Recipe>> getAllRecipes() {
    return mAllRecipes;
  }
}
