package com.mattwiduch.bakeit.data;

import android.arch.lifecycle.LiveData;
import android.util.Log;
import com.mattwiduch.bakeit.AppExecutors;
import com.mattwiduch.bakeit.data.database.RecipeDao;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import com.mattwiduch.bakeit.data.network.RecipeNetworkDataSource;
import com.mattwiduch.bakeit.data.network.RecipeService;
import java.util.List;

/**
 * Handles data operations in Bake It. Acts as a mediator between {@link RecipeService}
 * and {@link RecipeDao}
 */
public class RecipeRepository {

  private static final String LOG_TAG = RecipeRepository.class.getSimpleName();

  // Singleton instantiation
  private static final Object LOCK = new Object();
  private static RecipeRepository sInstance;
  private final RecipeDao mRecipeDao;
  private final RecipeNetworkDataSource mRecipeNetworkDataSource;
  private final AppExecutors mExecutors;
  private boolean mInitialized = false;

  public static synchronized RecipeRepository getInstance(RecipeDao recipeDao,
      RecipeNetworkDataSource recipeNetworkDataSource, AppExecutors executors) {
    Log.d(LOG_TAG, "Getting the repository");
    if (sInstance == null) {
      synchronized (LOCK) {
        sInstance = new RecipeRepository(recipeDao, recipeNetworkDataSource, executors);
        Log.d(LOG_TAG, "Created new repository");
      }
    }
    return sInstance;
  }

  private RecipeRepository(RecipeDao recipeDao, RecipeNetworkDataSource recipeNetworkDataSource,
      AppExecutors executors) {
    mRecipeDao = recipeDao;
    mRecipeNetworkDataSource = recipeNetworkDataSource;
    mExecutors = executors;

    // TODO: observe the network Live Data

  }

  /**
   * Database related operations
   **/
  public LiveData<List<Recipe>> getAllRecipes() {
    // TODO: initialise data
    return mRecipeDao.getAllRecipes();
  }
}
