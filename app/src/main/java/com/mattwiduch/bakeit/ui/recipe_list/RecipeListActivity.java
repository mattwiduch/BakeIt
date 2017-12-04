package com.mattwiduch.bakeit.ui.recipe_list;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import java.util.ArrayList;

public class RecipeListActivity extends AppCompatActivity {

  @BindView(R.id.recipes_recycler_view)
  RecyclerView recipesRecyclerView;

  private static final String LOG_TAG = RecipeListActivity.class.getSimpleName();

  private RecipeAdapter mRecipeAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_list);
    ButterKnife.bind(this);
    mRecipeAdapter = new RecipeAdapter(new ArrayList<Recipe>(0));
    recipesRecyclerView.setAdapter(mRecipeAdapter);
    recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    //    if (isConnected()) {
    //    } else {
    //      Snackbar.make(findViewById(R.id.layout_recipe_list), R.string.error_not_connected,
    //          Snackbar.LENGTH_LONG).show();
    //    }
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
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
  }
}
