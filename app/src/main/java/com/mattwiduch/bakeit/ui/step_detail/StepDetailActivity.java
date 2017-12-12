package com.mattwiduch.bakeit.ui.step_detail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.ui.recipe_detail.RecipeDetailActivity;
import com.mattwiduch.bakeit.utils.InjectorUtils;

/**
 * An activity representing a single Step detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeDetailActivity}.
 */
public class StepDetailActivity extends AppCompatActivity {

  @BindView(R.id.recipe_name)
  TextView recipeNameTv;

  private StepDetailViewModel mViewModel;
  private int mRecipeId;
  private int mStepId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_step_detail);
    ButterKnife.bind(this);

    // Allows to draw behind status bar
    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

    // Get extras passed on to this activity
    mRecipeId = getIntent().getIntExtra(RecipeDetailActivity.RECIPE_ID_EXTRA, -1);
    mStepId = getIntent().getIntExtra(StepDetailFragment.RECIPE_STEP_ID, -1);

    // Get the ViewModel from the factory
    StepDetailModelFactory factory = InjectorUtils.provideStepDetailViewModelFactory(
        this, mRecipeId, mStepId);
    mViewModel = ViewModelProviders.of(this, factory).get(StepDetailViewModel.class);

    // Observe changes in recipe data
    mViewModel.getRecipe().observe(this, recipe -> {
      if (recipe != null) {
        recipeNameTv.setText(recipe.getName());
      }
    });

    // savedInstanceState is non-null when there is fragment state
    // saved from previous configurations of this activity
    // (e.g. when rotating the screen from portrait to landscape).
    // In this case, the fragment will automatically be re-added
    // to its container so we don't need to manually add it.
    // For more information, see the Fragments API guide at:
    //
    // http://developer.android.com/guide/components/fragments.html
    //
    if (savedInstanceState == null) {
      // Create the detail fragment and add it to the activity
      // using a fragment transaction.
      Bundle arguments = new Bundle();
      arguments.putInt(StepDetailFragment.RECIPE_STEP_ID, mStepId);
      arguments.putInt(RecipeDetailActivity.RECIPE_ID_EXTRA, mRecipeId);
      StepDetailFragment fragment = new StepDetailFragment();
      fragment.setArguments(arguments);
      getSupportFragmentManager().beginTransaction()
          .add(R.id.step_detail_container, fragment)
          .commit();
    }
  }
}
