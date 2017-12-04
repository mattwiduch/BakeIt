package com.mattwiduch.bakeit.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;
import com.mattwiduch.bakeit.data.database.RecipeDao;
import com.mattwiduch.bakeit.data.database.RecipeDatabase;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import com.mattwiduch.bakeit.data.network.RecipeService;
import com.mattwiduch.bakeit.data.network.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles data operations in Bake It. Acts as a mediator between {@link RecipeService}
 * and {@link RecipeDao}
 */
public class RecipeRepository {

  private static final String LOG_TAG = RecipeRepository.class.getSimpleName();
  private RecipeDao mRecipeDao;
  private LiveData<List<Recipe>> mAllRecipes;

  public RecipeRepository(Application application) {
    RecipeDatabase db = RecipeDatabase.getDatabase(application);
    mRecipeDao = db.recipeDao();
    mAllRecipes = mRecipeDao.getAllRecipes();
    insertAllRecipes();
  }

  public LiveData<List<Recipe>> getAllRecipes() {
    return mAllRecipes;
  }

  public void insertAllRecipes() {
    // Initialise recipe service
    RecipeService recipeService = RetrofitClient.createService(RecipeService.class);

    recipeService.getRecipes().enqueue(new Callback<List<Recipe>>() {
      @Override
      public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
        Log.v(LOG_TAG, "Status code: " + response.code());
        if (response.isSuccessful()) {
          Recipe[] recipes = new Recipe[response.body().size()];
          recipes = response.body().toArray(recipes);
          new insertAllRecipesAsyncTask(mRecipeDao).execute(recipes);
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
    new insertAllRecipesAsyncTask(mRecipeDao).execute();
  }

  private static class insertAllRecipesAsyncTask extends AsyncTask<Recipe, Void, Void> {
    private RecipeDao mAsyncTaskDao;

    insertAllRecipesAsyncTask(RecipeDao dao) {
      mAsyncTaskDao = dao;
    }

    @Override
    protected Void doInBackground(final Recipe... recipes) {
      mAsyncTaskDao.bulkInsert(recipes);
      Log.d(LOG_TAG, "Recipes inserted to database");
      return null;
    }
  }
}
