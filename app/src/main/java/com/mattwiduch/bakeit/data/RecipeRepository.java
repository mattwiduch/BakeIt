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
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Handles data operations in Bake It. Acts as a mediator between {@link RecipeService}
 * and {@link RecipeDao}
 */
@Singleton
public class RecipeRepository {

  private static final String LOG_TAG = RecipeRepository.class.getSimpleName();

  private final RecipeDao mRecipeDao;
  private final RecipeNetworkDataSource mRecipeNetworkDataSource;
  private final AppExecutors mExecutors;
  private boolean mInitialized = false;

  @Inject
  RecipeRepository(RecipeDao recipeDao, RecipeNetworkDataSource recipeNetworkDataSource,
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
  public LiveData<Step> getStep(int recipeId, int stepNumber) {
    initialiseData();
    return mRecipeDao.getStep(recipeId, stepNumber);
  }
  public List<Ingredient> getIngredientsData(int recipeId) {
    initialiseData();
    return mRecipeDao.getIngredientsData(recipeId);
  }

  /**
   * Network related operation.
   */
  private void startFetchRecipesService() {
    mRecipeNetworkDataSource.startFetchRecipesService();
  }
}
