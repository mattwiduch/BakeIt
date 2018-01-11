package com.mattwiduch.bakeit.ui.recipe_list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.mattwiduch.bakeit.data.RecipeRepository;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import java.util.List;
import javax.inject.Inject;

/**
 * {@link ViewModel} for {@link RecipeListFragment}
 */

public class RecipeListViewModel extends ViewModel {

  // List of recipes shown to the user
  private final LiveData<List<Recipe>> mAllRecipes;

  @Inject
  RecipeListViewModel (RecipeRepository repository) {
    mAllRecipes = repository.getAllRecipes();
  }

  public LiveData<List<Recipe>> getAllRecipes() {
    return mAllRecipes;
  }
}
