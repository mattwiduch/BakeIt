package com.mattwiduch.bakeit.data;

import android.arch.lifecycle.LiveData;
import android.util.Log;
import com.mattwiduch.bakeit.AppExecutors;
import com.mattwiduch.bakeit.data.database.RecipeDao;
import com.mattwiduch.bakeit.data.database.entries.Ingredient;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import com.mattwiduch.bakeit.data.database.entries.Step;
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

    // Observe the network Live Data
    LiveData<Recipe[]> recipeData = mRecipeNetworkDataSource.getAllRecipes();
    recipeData.observeForever(newRecipesFromNetwork -> {
      mExecutors.diskIO().execute(() -> {
        // Insert downloaded data into the database
        mRecipeDao.insertRecipes(newRecipesFromNetwork);
        Log.d(LOG_TAG, "New recipes inserted");
      });
    });
    LiveData<List<Ingredient>> ingredientsData = mRecipeNetworkDataSource.getAllIngredients();
    ingredientsData.observeForever(newIngredientsFromNetwork -> {
      mExecutors.diskIO().execute(() -> {
        mRecipeDao.insertIngredients(newIngredientsFromNetwork);
        Log.d(LOG_TAG, "New ingredients inserted");
      });
    });
    LiveData<List<Step>> stepsData = mRecipeNetworkDataSource.getAllSteps();
    stepsData.observeForever(newStepsFromNetwork -> {
      mExecutors.diskIO().execute(() -> {
        mRecipeDao.insertSteps(newStepsFromNetwork);
        Log.d(LOG_TAG, "New steps inserted");
      });
    });
  }

  /**
   * Creates periodic sync tasks and checks to see if an immediate sync is required. If an
   * immediate sync is required, this method will take care of making sure that sync occurs.
   */
  private synchronized void initialiseData() {

    // Only perform initialization once per app lifetime. If initialization has already been
    // performed, we have nothing to do in this method.
    if (mInitialized) return;
    mInitialized = true;

    // This method call triggers Sunshine to create its task to synchronize weather data
    // periodically.
    mRecipeNetworkDataSource.scheduleRecurringFetchRecipesSync();

    mExecutors.diskIO().execute(this::startFetchRecipesService);
  }

  /**
   * Database related operations.
   **/
  public LiveData<List<Recipe>> getAllRecipes() {
    initialiseData();
    return mRecipeDao.getAllRecipes();
  }
  public LiveData<Recipe> getRecipe(int recipeId) {
    initialiseData();
    return mRecipeDao.getRecipe(recipeId);
  }
  public LiveData<List<Ingredient>> getIngredientsForRecipe(int recipeId) {
    initialiseData();
    return mRecipeDao.getIngredientsForRecipe(recipeId);
  }
  public LiveData<List<Step>> getStepsForRecipe(int recipeId) {
    initialiseData();
    return mRecipeDao.getStepsForRecipe(recipeId);
  }
  public LiveData<Step> getStep(int stepId) {
    initialiseData();
    return mRecipeDao.getStep(stepId);
  }

  /**
   * Network related operation.
   */
  private void startFetchRecipesService() {
    mRecipeNetworkDataSource.startFetchRecipesService();
  }
}
