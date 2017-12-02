package com.mattwiduch.bakeit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.mattwiduch.bakeit.model.Recipe;
import com.mattwiduch.bakeit.rest.RecipeService;
import com.mattwiduch.bakeit.rest.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends AppCompatActivity {

  private static final String LOG_TAG = RecipeListActivity.class.getSimpleName();
  private RecipeService mRecipeService;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_list);
    mRecipeService = RetrofitClient.createService(RecipeService.class);

    mRecipeService.getRecipes().enqueue(new Callback<Recipe>() {
      @Override
      public void onResponse(Call<Recipe> call, Response<Recipe> response) {
        if (response.isSuccessful()) {
          Log.d(LOG_TAG, "Recipes loaded from web");
        } else {
          int statusCode = response.code();
          // handle request errors depending on status code
          Log.e(LOG_TAG, "Recipes not loaded: " + statusCode);
        }
      }

      @Override
      public void onFailure(Call<Recipe> call, Throwable t) {
        Log.e(LOG_TAG, "Network exception occurred while communicating with the server :(");
      }
    });
  }
}
