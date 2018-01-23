package com.mattwiduch.bakeit.ui.recipe_detail;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasTextColor;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.mattwiduch.bakeit.ui.recipe_detail.RecipeDetailActivity.KEY_TWO_PANE_MODE;
import static com.mattwiduch.bakeit.ui.recipe_detail.RecipeDetailActivity.RECIPE_ID_EXTRA;
import static com.mattwiduch.bakeit.util.TestUtils.TEST_RECIPE_ID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.data.database.entries.Ingredient;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import com.mattwiduch.bakeit.data.database.entries.Step;
import com.mattwiduch.bakeit.testing.SingleFragmentActivity;
import com.mattwiduch.bakeit.util.EspressoTestUtil;
import com.mattwiduch.bakeit.util.TestUtils;
import com.mattwiduch.bakeit.util.ViewModelUtil;
import com.mattwiduch.bakeit.util.matchers.RecyclerViewMatcher;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailTabletTest {
  @Rule
  public ActivityTestRule<SingleFragmentActivity> activityTestRule =
      new ActivityTestRule<>(SingleFragmentActivity.class, true, true);

  private MutableLiveData<Recipe> recipe = new MutableLiveData<>();
  private MutableLiveData<List<Ingredient>> ingredients = new MutableLiveData<>();
  private MutableLiveData<List<Step>> steps = new MutableLiveData<>();
  private RecipeDetailFragment recipeDetailFragment;
  private RecipeDetailViewModel viewModel;

  @Before
  public void init() {
    EspressoTestUtil.disableProgressBarAnimations(activityTestRule);
    recipeDetailFragment = new RecipeDetailFragment();
    Bundle args = new Bundle();
    args.putInt(RECIPE_ID_EXTRA, TEST_RECIPE_ID);
    args.putBoolean(KEY_TWO_PANE_MODE, true);
    recipeDetailFragment.setArguments(args);

    viewModel = mock(RecipeDetailViewModel.class);
    when(viewModel.getRecipe()).thenReturn(recipe);
    when(viewModel.getRecipeIngredients()).thenReturn(ingredients);
    when(viewModel.getRecipeSteps()).thenReturn(steps);

    recipeDetailFragment.viewModelFactory = ViewModelUtil.createFor(viewModel);
    activityTestRule.getActivity().setFragment(recipeDetailFragment);
  }

  @Test
  public void verifyFirstStepIsSelectedUponLaunch() {
    recipe.postValue(TestUtils.createRecipe(TEST_RECIPE_ID, "Marshmallows", 12, ""));
    ingredients.postValue(TestUtils.createIngredientsList(8));
    steps.postValue(TestUtils.createStepList(TEST_RECIPE_ID, 4));

    onView(stepsMatcher().atPositionOnView(0, R.id.recipe_step_number))
        .check(matches(hasTextColor(R.color.colorCardBackground)));
  }

  @Test
  public void highlightStepWhenSelected() {
    recipe.postValue(TestUtils.createRecipe(TEST_RECIPE_ID, "Marshmallows", 12, ""));
    ingredients.postValue(TestUtils.createIngredientsList(8));
    steps.postValue(TestUtils.createStepList(TEST_RECIPE_ID, 4));

    onView(withId(R.id.steps_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(
        2, click()));

    // Verify step item is not highlighted after clicking
    onView(stepsMatcher().atPositionOnView(0, R.id.recipe_step_number))
        .check(matches(hasTextColor(R.color.colorAccent)));
    onView(stepsMatcher().atPositionOnView(2, R.id.recipe_step_number))
        .check(matches(hasTextColor(R.color.colorCardBackground)));
  }

  @Test
  public void verifyStepIsStillSelectedAfterRotation() {
    recipe.postValue(TestUtils.createRecipe(TEST_RECIPE_ID, "Marshmallows", 12, ""));
    ingredients.postValue(TestUtils.createIngredientsList(8));
    steps.postValue(TestUtils.createStepList(TEST_RECIPE_ID, 4));

    onView(withId(R.id.steps_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(
        2, click()));
    rotateScreen();

    // Verify step item is not highlighted after clicking
    onView(stepsMatcher().atPositionOnView(0, R.id.recipe_step_number))
        .check(matches(hasTextColor(R.color.colorAccent)));
    onView(stepsMatcher().atPositionOnView(2, R.id.recipe_step_number))
        .check(matches(hasTextColor(R.color.colorCardBackground)));
  }

  /**
   * Helps testing restoring fragment state.
   */
  private void rotateScreen() {
    Context context = InstrumentationRegistry.getTargetContext();
    int orientation = context.getResources().getConfiguration().orientation;
    Activity activity = activityTestRule.getActivity();
    activity.setRequestedOrientation((orientation == Configuration.ORIENTATION_PORTRAIT) ?
        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
  }

  @NonNull
  private RecyclerViewMatcher stepsMatcher() {
    return new RecyclerViewMatcher(R.id.steps_recycler_view);
  }
}
