package com.mattwiduch.bakeit.data.network;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;
import com.mattwiduch.bakeit.AppExecutors;
import com.mattwiduch.bakeit.data.database.entries.Recipe;

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

  //  public void insertAllRecipes() {
//    // Initialise recipe service
//    RecipeService recipeService = RetrofitClient.createService(RecipeService.class);
//
//    recipeService.getRecipes().enqueue(new Callback<List<Recipe>>() {
//      @Override
//      public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
//        Log.v(LOG_TAG, "Status code: " + response.code());
//        if (response.isSuccessful()) {
//          Recipe[] recipes = new Recipe[response.body().size()];
//          recipes = response.body().toArray(recipes);
//          new insertAllRecipesAsyncTask(mRecipeDao).execute(recipes);
//          Log.d(LOG_TAG, "Recipes loaded from web");
//        } else {
//          int statusCode = response.code();
//          // handle request errors depending on status code
//          Log.e(LOG_TAG, "Recipes not loaded: " + statusCode);
//        }
//      }
//
//      @Override
//      public void onFailure(Call<List<Recipe>> call, Throwable t) {
//        Log.e(LOG_TAG, "Network exception occurred: " + t.getMessage());
//      }
//    });
//    new insertAllRecipesAsyncTask(mRecipeDao).execute();
//  }
//
//  private static class insertAllRecipesAsyncTask extends AsyncTask<Recipe, Void, Void> {
//    private RecipeDao mAsyncTaskDao;
//
//    insertAllRecipesAsyncTask(RecipeDao dao) {
//      mAsyncTaskDao = dao;
//    }
//
//    @Override
//    protected Void doInBackground(final Recipe... recipes) {
//      mAsyncTaskDao.bulkInsert(recipes);
//      Log.d(LOG_TAG, "Recipes inserted to database");
//      return null;
//    }
//  }
}
