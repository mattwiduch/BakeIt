package com.mattwiduch.bakeit.ui.recipe_list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.ui.recipe_detail.RecipeDetailActivity;
import com.mattwiduch.bakeit.ui.recipe_list.RecipeAdapter.RecipeAdapterOnItemClickHandler;
import com.mattwiduch.bakeit.utils.InjectorUtils;

/**
 * Displays a list of baking recipes.
 */
public class RecipeListActivity extends AppCompatActivity implements
    RecipeAdapterOnItemClickHandler {

  @BindView(R.id.recipes_recycler_view)
  RecyclerView recipesRecyclerView;
  @BindView(R.id.recipes_loading_indicator)
  ProgressBar recipesLoadingIndicator;

  private static final String LOG_TAG = RecipeListActivity.class.getSimpleName();

  private RecipeListViewModel mViewModel;
  private RecipeAdapter mRecipeAdapter;
  private int mPosition = RecyclerView.NO_POSITION;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_list);
    ButterKnife.bind(this);

    // Recipe list RecyclerView setup
    recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    recipesRecyclerView.setHasFixedSize(true);
    mRecipeAdapter = new RecipeAdapter(this);
    recipesRecyclerView.setAdapter(mRecipeAdapter);

    RecipeListModelFactory factory = InjectorUtils.provideRecipeListViewModelFactory(
        this.getApplicationContext());
    mViewModel = ViewModelProviders.of(this, factory).get(RecipeListViewModel.class);

    mViewModel.getAllRecipes().observe(this, recipes -> {
      mRecipeAdapter.updateRecipes(recipes);

      if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
      recipesRecyclerView.smoothScrollToPosition(mPosition);

      if (recipes != null && !recipes.isEmpty()) {
        showRecipes();
      } else {
        showLoading();
      }
    });
  }

  /**
   * Responds to item clicks on recipes in the list.
   *
   * @param recipeId Id of recipe that has been clicked
   */
  @Override
  public void onItemClick(int recipeId) {
    Intent recipeDetailIntent = new Intent(RecipeListActivity.this,
        RecipeDetailActivity.class);
    recipeDetailIntent.putExtra(RecipeDetailActivity.RECIPE_ID_EXTRA, recipeId);
    startActivity(recipeDetailIntent);
  }

  /**
   * Hides loading indicator and makes recipe list visible.
   */
  private void showRecipes() {
    recipesLoadingIndicator.setVisibility(View.INVISIBLE);
    recipesRecyclerView.setVisibility(View.VISIBLE);
  }

  /**
   * Hides recipes list and shows loading indicator.
   */
  private void showLoading() {
    recipesRecyclerView.setVisibility(View.INVISIBLE);
    recipesLoadingIndicator.setVisibility(View.VISIBLE);
  }
}
