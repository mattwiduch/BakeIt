package com.mattwiduch.bakeit.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.mattwiduch.bakeit.AppExecutors;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Provides an API for doing all operations with the server data.
 */
public class RecipeNetworkDataSource {

  private static final String LOG_TAG = RecipeNetworkDataSource.class.getSimpleName();

  // For Singleton instantiation
  private static final Object LOCK = new Object();
  private static RecipeNetworkDataSource sInstance;
  private final Context mContext;

  // LiveData storing the latest downloaded recipes
  private final MutableLiveData<Recipe[]> mDownloadedRecipes;

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
    mDownloadedRecipes = new MutableLiveData<Recipe[]>();
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
            mDownloadedRecipes.postValue(recipes);
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

}
