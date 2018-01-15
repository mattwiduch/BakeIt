package com.mattwiduch.bakeit.ui.recipe_detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.ui.recipe_detail.RecipeDetailFragment.OnRecipeLoadedListener;
import com.mattwiduch.bakeit.ui.recipe_detail.RecipeDetailFragment.OnStepSelectedListener;
import com.mattwiduch.bakeit.ui.step_detail.StepDetailActivity;
import com.mattwiduch.bakeit.ui.step_detail.StepDetailFragment;
import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import javax.inject.Inject;

/**
 * An activity representing a list of Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeDetailActivity extends AppCompatActivity implements HasSupportFragmentInjector,
    OnStepSelectedListener, OnRecipeLoadedListener {

  @BindView(R.id.recipe_name)
  TextView recipeNameTv;

  @Inject
  DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

  public static final String RECIPE_ID_EXTRA = "RECIPE_ID_EXTRA";
  public static final String KEY_CURRENT_STEP = "KEY_CURRENT_STEP";
  public static final String KEY_TWO_PANE_MODE = "KEY_TWO_PANE_MODE";

  /**
   * Whether or not the activity is in two-pane mode, i.e. running on a tablet
   * device.
   */
  private boolean mTwoPane;
  private int mRecipeId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_detail);
    ButterKnife.bind(this);

    // Allows to draw behind status bar
    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

    // The detail container view will be present only in the large-screen layouts
    // (res/values-w600dp). If this view is present, then the activity should be in two-pane mode.
    if (findViewById(R.id.step_detail_container) != null) {
      mTwoPane = true;
    }

    // Retrieve recipe id from intent extras
    mRecipeId = getIntent().getIntExtra(RECIPE_ID_EXTRA, -1);

    // Show first step upon launch in two pane mode
    if (savedInstanceState == null) {
      // Load fragment that displays recipe data
      loadDetailFragment(mRecipeId);

      if (mTwoPane) {
        loadStepFragment(0);
      }
    }
  }

  @Override
  public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
    return dispatchingAndroidInjector;
  }

  /**
   * Responds to item clicks on recipes in the list.
   * @param stepNumber Id of recipe step that has been clicked
   */
  @Override
  public void onStepSelected(int stepNumber) {
    if (mTwoPane) {
      loadStepFragment(stepNumber);
    } else {
      Intent intent = new Intent(this, StepDetailActivity.class);
      intent.putExtra(RECIPE_ID_EXTRA, mRecipeId);
      intent.putExtra(StepDetailFragment.RECIPE_STEP_NUMBER, stepNumber);
      startActivity(intent);
    }
  }

  /**
   * Loads fragment that displays step data in two pane mode.
   * @param stepNumber of step to display
   */
  private void loadStepFragment(int stepNumber) {
    Bundle arguments = new Bundle();
    arguments.putInt(RECIPE_ID_EXTRA, mRecipeId);
    arguments.putInt(StepDetailFragment.RECIPE_STEP_NUMBER, stepNumber);
    StepDetailFragment fragment = new StepDetailFragment();
    fragment.setArguments(arguments);
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.step_detail_container, fragment)
        .commit();
  }

  /**
   * Loads fragment that displays recipe data.
   * @param recipeId of recipe to display
   */
  private void loadDetailFragment(int recipeId) {
    Bundle arguments = new Bundle();
    arguments.putInt(RECIPE_ID_EXTRA, recipeId);
    arguments.putBoolean(KEY_TWO_PANE_MODE, mTwoPane);
    RecipeDetailFragment fragment = new RecipeDetailFragment();
    fragment.setArguments(arguments);
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.recipe_details_fragment, fragment)
        .commit();
  }

  /**
   * Sets toolbar text to recipe's name.
   * @param recipeName to display
   */
  @Override
  public void onRecipeLoaded(String recipeName) {
    recipeNameTv.setText(recipeName);
  }
}
