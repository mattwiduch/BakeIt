package com.mattwiduch.bakeit.ui.recipe_detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.ui.recipe_detail.RecipeStepAdapter.RecipeStepAdapterOnItemClickHandler;
import com.mattwiduch.bakeit.ui.step_detail.StepDetailActivity;
import com.mattwiduch.bakeit.ui.step_detail.StepDetailFragment;
import com.mattwiduch.bakeit.utils.InjectorUtils;

/**
 * An activity representing a list of Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeDetailActivity extends AppCompatActivity implements
    RecipeStepAdapterOnItemClickHandler {

  @BindView(R.id.steps_recycler_view)
  RecyclerView stepsRecyclerView;

  public static final String RECIPE_ID_EXTRA = "RECIPE_ID_EXTRA";

  /**
   * Whether or not the activity is in two-pane mode, i.e. running on a tablet
   * device.
   */
  private boolean mTwoPane;
  private RecipeDetailViewModel mViewModel;
  private RecipeStepAdapter mStepsAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_detail);
    ButterKnife.bind(this);

    // The detail container view will be present only in the large-screen layouts
    // (res/values-w900dp). If this view is present, then the activity should be in two-pane mode.
    if (findViewById(R.id.step_detail_container) != null) {
      mTwoPane = true;
    }

    // Setup recycler view
    setupRecyclerView();

    // Retrieve recipe id from intent extras
    int recipeId = getIntent().getIntExtra(RECIPE_ID_EXTRA, -1);

    // Get the ViewModel from the factory
    RecipeDetailModelFactory factory = InjectorUtils.provideRecipeDetailViewModelFactory(
        this.getApplicationContext(), recipeId);
    mViewModel = ViewModelProviders.of(this, factory).get(RecipeDetailViewModel.class);

    // Observe changes in recipe data
    mViewModel.getRecipeIngredients().observe(this, ingredients -> {
      if (ingredients != null) {
        // TODO: Update UI
      }
    });
    mViewModel.getRecipeSteps().observe(this, steps -> {
      if (steps != null) {
        mStepsAdapter.updateSteps(steps);
      }
    });
  }

  /**
   * Prepares recycler view that displays list of recipe steps.
   */
  private void setupRecyclerView() {
    mStepsAdapter = new RecipeStepAdapter(this);
    stepsRecyclerView.setAdapter(mStepsAdapter);
    stepsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    stepsRecyclerView.setHasFixedSize(true);
  }

  /**
   * Responds to item clicks on recipes in the list.
   *
   * @param stepId Id of recipe step that has been clicked
   */
  @Override
  public void onItemClick(int stepId) {
    if (mTwoPane) {
      Bundle arguments = new Bundle();
      arguments.putInt(StepDetailFragment.RECIPE_STEP_ID, stepId);
      StepDetailFragment fragment = new StepDetailFragment();
      fragment.setArguments(arguments);
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.step_detail_container, fragment)
          .commit();
    } else {
      Intent intent = new Intent(this, StepDetailActivity.class);
      intent.putExtra(StepDetailFragment.RECIPE_STEP_ID, stepId);
      startActivity(intent);
    }
  }
}
