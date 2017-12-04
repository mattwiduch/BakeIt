package com.mattwiduch.bakeit.ui.recipe_list;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import com.mattwiduch.bakeit.data.network.RecipeService;
import com.mattwiduch.bakeit.data.network.RetrofitClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends AppCompatActivity {
  @BindView(R.id.recipes_recycler_view)
  RecyclerView recipesRecyclerView;

  private static final String LOG_TAG = RecipeListActivity.class.getSimpleName();

  private RecipeService mRecipeService;
  private RecipeAdapter mRecipeAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_list);
    ButterKnife.bind(this);
    mRecipeAdapter = new RecipeAdapter(new ArrayList<Recipe>(0));
    recipesRecyclerView.setAdapter(mRecipeAdapter);
    recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    // Initialise recipe service
    mRecipeService = RetrofitClient.createService(RecipeService.class);

    if (isConnected()) {
      mRecipeService.getRecipes().enqueue(new Callback<List<Recipe>>() {
        @Override
        public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
          Log.v(LOG_TAG, "Status code: " + response.code());
          if (response.isSuccessful()) {
            mRecipeAdapter.updateRecipes(response.body());
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
    } else {
      Snackbar.make(findViewById(R.id.layout_recipe_list), R.string.error_not_connected,
          Snackbar.LENGTH_LONG).show();
    }
  }

  /**
   * Checks if device is connected to the internet.
   *
   * @return true if connected
   */
  private boolean isConnected() {
    ConnectivityManager cm =
        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    return  activeNetwork != null && activeNetwork.isConnectedOrConnecting();
  }
}
