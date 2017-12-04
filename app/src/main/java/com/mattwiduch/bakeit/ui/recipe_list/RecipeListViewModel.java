package com.mattwiduch.bakeit.ui.recipe_list;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import com.mattwiduch.bakeit.data.RecipeRepository;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import java.util.List;

/**
 * {@link ViewModel} for {@link RecipeListActivity}
 */

public class RecipeListViewModel extends AndroidViewModel {
  private final RecipeRepository mRepository;
  private final LiveData<List<Recipe>> mAllRecipes;

  public RecipeListViewModel (Application application) {
    super(application);
    mRepository = new RecipeRepository(application);
    mAllRecipes = mRepository.getAllRecipes();
  }

  LiveData<List<Recipe>> getAllRecipes() {
    return mAllRecipes;
  }
}
