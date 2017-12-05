package com.mattwiduch.bakeit.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.mattwiduch.bakeit.AppExecutors;
import com.mattwiduch.bakeit.data.database.entries.Ingredient;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import com.mattwiduch.bakeit.data.database.entries.Step;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Provides an API for doing all operations with the server data.
 */
public class RecipeNetworkDataSource {

  private static final String LOG_TAG = RecipeNetworkDataSource.class.getSimpleName();
  // Interval at which to sync with the weather. Use TimeUnit for convenience, rather than
  // writing out a bunch of multiplication ourselves and risk making a silly mistake.
  private static final int SYNC_INTERVAL_HOURS = 3;
  private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
  private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;
  private static final String BAKEIT_SYNC_TAG = "bakeit-sync";

  // For Singleton instantiation
  private static final Object LOCK = new Object();
  private static RecipeNetworkDataSource sInstance;
  private final Context mContext;

  // LiveData storing the latest downloaded recipes
  private final MutableLiveData<Recipe[]> mDownloadedRecipes;
  private final MutableLiveData<List<Ingredient>> mDownloadedIngredients;
  private final MutableLiveData<List<Step>> mDownloadedSteps;

  private final AppExecutors mExecutors;

  /**
   * Get the singleton for this class.
   */
  public static RecipeNetworkDataSource getInstance(Context context, AppExecutors executors) {
    Log.d(LOG_TAG, "Getting the network data source");
    if (sInstance == null) {
      synchronized (LOCK) {
        sInstance = new RecipeNetworkDataSource(context.getApplicationContext(), executors);
        Log.d(LOG_TAG, "Made new network data source");
      }
    }
    return sInstance;
  }

  private RecipeNetworkDataSource(Context context, AppExecutors executors) {
    mContext = context;
    mExecutors = executors;
    mDownloadedRecipes = new MutableLiveData<>();
    mDownloadedIngredients = new MutableLiveData<>();
    mDownloadedSteps = new MutableLiveData<>();
  }

  /**
   * Starts an intent service to fetch the weather.
   */
  public void startFetchRecipesService() {
    Intent intentToFetch = new Intent(mContext, RecipeSyncIntentService.class);
    mContext.startService(intentToFetch);
    Log.d(LOG_TAG, "Service created");
  }

  /**
   * Schedules a repeating job service which fetches recipes.
   */
  public void scheduleRecurringFetchRecipesSync() {
    // Create a new dispatcher using the Google Play driver.
    FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(mContext));

    // Create the Job to periodically sync Sunshine
    Job syncRecipesJob = dispatcher.newJobBuilder()
        .setService(RecipeFirebaseJobService.class)
        .setTag(BAKEIT_SYNC_TAG)
        .setConstraints(Constraint.ON_ANY_NETWORK)
        .setLifetime(Lifetime.FOREVER)
        .setRecurring(true)
        .setTrigger(Trigger.executionWindow(SYNC_INTERVAL_SECONDS,
            SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
        .setReplaceCurrent(true)
        .build();

    // Schedule the job
    dispatcher.schedule(syncRecipesJob);
    Log.d(LOG_TAG, "Recipe sync job scheduled");
  }

  /**
   * Gets latest recipes.
   */
  public void fetchRecipes() {
    Log.d(LOG_TAG, "Fetching recipes");
    mExecutors.networkIO().execute(() -> {
      // Initialise recipe service
      RecipeService recipeService = RetrofitClient.createService(RecipeService.class);

      recipeService.getRecipes().enqueue(new Callback<List<Recipe>>() {
        @Override
        public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
          Log.v(LOG_TAG, "Status code: " + response.code());
          if (response.isSuccessful()) {
            Recipe[] recipes = new Recipe[response.body().size()];
            recipes = response.body().toArray(recipes);

            List<Ingredient> allIngredients = new ArrayList<>();
            List<Step> allSteps = new ArrayList<>();
            for (Recipe recipe : recipes) {
              // Get all ingredients for recipe
              List<Ingredient> ingredients = recipe.getIngredients();
              for (Ingredient ingredient : ingredients) {
                ingredient.setRecipeId(recipe.getId());
              }
              allIngredients.addAll(ingredients);

              // Get all steps for recipe
              List<Step> steps = recipe.getSteps();
              for (Step step : steps) {
                step.setRecipeId(recipe.getId());
              }
              allSteps.addAll(steps);
            }

            mDownloadedRecipes.postValue(recipes);
            mDownloadedIngredients.postValue(allIngredients);
            mDownloadedSteps.postValue(allSteps);
            Log.d(LOG_TAG, "Recipes loaded from web");
          } else {
            int statusCode = response.code();
            // handle request errors depending on status code
            Log.e(LOG_TAG, "Recipes not loaded: " + statusCode);
          }
        }

        @Override
        public void onFailure(Call<List<Recipe>> call, Throwable t) {
          Log.e(LOG_TAG, "Network exception occurred: " + t.getMessage());
        }
      });
    });
  }

  /**
   * Returns LiveData object containing the most recently downloaded data from the network.
   *
   * @return List of recipes
   */
  public LiveData<Recipe[]> getAllRecipes() {
    return mDownloadedRecipes;
  }

  /**
   * Returns LiveData object containing the most recently downloaded data from the network.
   *
   * @return List of ingredients
   */
  public LiveData<List<Ingredient>> getAllIngredients() {
    return mDownloadedIngredients;
  }

  /**
   * Returns LiveData object containing the most recently downloaded data from the network.
   *
   * @return List of steps
   */
  public LiveData<List<Step>> getAllSteps() {
    return mDownloadedSteps;
  }
}
