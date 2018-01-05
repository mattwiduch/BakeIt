package com.mattwiduch.bakeit.ui.recipe_list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.ui.recipe_detail.RecipeDetailActivity;
import com.mattwiduch.bakeit.ui.recipe_list.RecipeAdapter.RecipeAdapterOnItemClickHandler;
import com.mattwiduch.bakeit.utils.ConnectionDetector;
import com.mattwiduch.bakeit.utils.InjectorUtils;

/**
 * Displays a list of baking recipes.
 */
public class RecipeListActivity extends AppCompatActivity implements
    RecipeAdapterOnItemClickHandler {

  @BindView(R.id.recipes_coordinator)
  CoordinatorLayout recipesCoordinator;
  @BindView(R.id.recipes_recycler_view)
  RecyclerView recipesRecyclerView;
  @BindView(R.id.recipes_loading_indicator)
  ProgressBar recipesLoadingIndicator;
  @BindView(R.id.recipes_empty_list)
  LinearLayout recipesEmptyList;

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
    recipesRecyclerView.setLayoutManager(new GridLayoutManager(this,
        getResources().getInteger(R.integer.recipe_list_columns)));
    recipesRecyclerView.setHasFixedSize(true);
    mRecipeAdapter = new RecipeAdapter(this, this);
    recipesRecyclerView.setAdapter(mRecipeAdapter);

    RecipeListModelFactory factory = InjectorUtils.provideRecipeListViewModelFactory(
        this.getApplicationContext());
    mViewModel = ViewModelProviders.of(this, factory).get(RecipeListViewModel.class);

    // Create snack bar that shows connection error messages
    final Snackbar snackbar = Snackbar.make(recipesCoordinator, R.string.connection_error,
        Snackbar.LENGTH_INDEFINITE);
    View snackbarView = snackbar.getView();
    snackbarView.setBackgroundColor(getResources().getColor(R.color.colorAccent));

    // Observe device connectivity
    ConnectionDetector connectionDetector = new ConnectionDetector(getApplicationContext());
    connectionDetector.observe(this, status -> {
      if (status != null && status.getIsConnected()) {
        // Request new data only if recipe list is empty
        if (mRecipeAdapter.getItemCount() < 1) {
          InjectorUtils.provideNetworkDataSource(this).startFetchRecipesService();
        }
        snackbar.dismiss();
      } else {
        snackbar.show();
      }
    });

    mViewModel.getAllRecipes().observe(this, recipes -> {
      mRecipeAdapter.updateRecipes(recipes);

      if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
      recipesRecyclerView.smoothScrollToPosition(mPosition);

      if (recipes != null) {
        if (!recipes.isEmpty()) {
          showRecipes();
        } else {
         showEmpty();
        }
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
    recipesEmptyList.setVisibility(View.GONE);
    recipesLoadingIndicator.setVisibility(View.GONE);
    recipesRecyclerView.setVisibility(View.VISIBLE);
  }

  /**
   * Hides recipes list and shows loading indicator.
   */
  private void showLoading() {
    recipesEmptyList.setVisibility(View.GONE);
    recipesRecyclerView.setVisibility(View.GONE);
    recipesLoadingIndicator.setVisibility(View.VISIBLE);
  }

  /**
   * Hides empty list view and shows recipe list.
   */
  private void showEmpty() {
    recipesLoadingIndicator.setVisibility(View.GONE);
    recipesRecyclerView.setVisibility(View.GONE);
    recipesEmptyList.setVisibility(View.VISIBLE);
  }
}
