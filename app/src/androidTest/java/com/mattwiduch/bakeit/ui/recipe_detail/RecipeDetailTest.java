package com.mattwiduch.bakeit.ui.recipe_detail;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasTextColor;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.mattwiduch.bakeit.ui.recipe_detail.RecipeDetailActivity.RECIPE_ID_EXTRA;
import static com.mattwiduch.bakeit.util.TestUtils.TEST_RECIPE_ID;
import static com.mattwiduch.bakeit.util.TestUtils.TEST_STEP_NAME;
import static com.mattwiduch.bakeit.util.matchers.DrawableMatcher.hasDrawable;
import static com.mattwiduch.bakeit.util.matchers.DrawableMatcher.withDrawableId;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
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
import com.mattwiduch.bakeit.util.EspressoScrollActions;
import com.mattwiduch.bakeit.util.EspressoTestUtil;
import com.mattwiduch.bakeit.util.TestUtils;
import com.mattwiduch.bakeit.util.ViewModelUtil;
import com.mattwiduch.bakeit.util.matchers.RecyclerViewItemCountAssertion;
import com.mattwiduch.bakeit.util.matchers.RecyclerViewMatcher;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailTest {
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
    recipeDetailFragment.setArguments(args);

    viewModel = mock(RecipeDetailViewModel.class);
    when(viewModel.getRecipe()).thenReturn(recipe);
    when(viewModel.getRecipeIngredients()).thenReturn(ingredients);
    when(viewModel.getRecipeSteps()).thenReturn(steps);

    recipeDetailFragment.viewModelFactory = ViewModelUtil.createFor(viewModel);
    activityTestRule.getActivity().setFragment(recipeDetailFragment);
  }

  @Test
  public void useAppContext() throws Exception {
    // Context of the app under test.
    Context appContext = InstrumentationRegistry.getTargetContext();

    assertEquals("com.mattwiduch.bakeit", appContext.getPackageName());
  }

  @Test
  public void verifyIngredientsButton() {
    recipe.postValue(TestUtils.createRecipe(TEST_RECIPE_ID, "Marshmallows", 12, ""));

    onView(withId(R.id.ingredients_btn)).check(matches(isDisplayed()));
    onView(withId(R.id.ingredients_btn)).check(matches(isClickable()));
    onView(withId(R.id.recipe_ingredients_label)).check(matches(withText(R.string.recipe_ingredients_label)));
    onView(withId(R.id.recipe_ingredients_for)).check(matches(withText(R.string.recipe_ingredients_for)));
    onView(withId(R.id.recipe_servings)).check(matches(withText("12 Servings")));
    onView(withId(R.id.recipe_ingredients_arrow)).check(matches(hasDrawable()));
    onView(withId(R.id.recipe_ingredients_arrow)).check(matches(
        withDrawableId(R.drawable.ic_arrow_drop_down_black_36dp)));
    onView(withId(R.id.ingredients_container)).check(matches(not(isDisplayed())));
    onView(withId(R.id.ingredients_recycler_view)).check(matches(not(isDisplayed())));
  }

  @Test
  public void expandIngredientsList() {
    recipe.postValue(TestUtils.createRecipe(TEST_RECIPE_ID, "Marshmallows", 12, ""));
    ingredients.postValue(TestUtils.createIngredientsList(10));

    // For some reason, clicking on this view alone does not work
    onView(withId(R.id.ingredients_btn)).perform(click());
    onView(withId(R.id.recipe_ingredients_label)).perform(click());
    onView(withId(R.id.ingredients_container)).check(matches(isDisplayed()));
    onView(withId(R.id.ingredients_recycler_view)).check(matches(isDisplayed()));
    // Unable to test icon due to custom animation
  }

  @Test
  public void collapseIngredientsList() {
    recipe.postValue(TestUtils.createRecipe(TEST_RECIPE_ID, "Marshmallows", 12, ""));
    ingredients.postValue(TestUtils.createIngredientsList(10));

    // For some reason, clicking on this view alone does not work
    onView(withId(R.id.ingredients_btn)).perform(click());
    onView(withId(R.id.recipe_ingredients_label)).perform(click());
    onView(withId(R.id.recipe_ingredients_label)).perform(click());
    onView(withId(R.id.ingredients_container)).check(matches(not(isDisplayed())));
    onView(withId(R.id.ingredients_recycler_view)).check(matches(not(isDisplayed())));
    // Unable to test icon due to custom animation
  }

  @Test
  public void checkIngredientsCount() {
    recipe.postValue(TestUtils.createRecipe(TEST_RECIPE_ID, "Marshmallows", 12, ""));
    ingredients.postValue(TestUtils.createIngredientsList(24));
    onView(withId(R.id.ingredients_recycler_view)).check(new RecyclerViewItemCountAssertion(24));
  }

  @Test
  public void verifyIngredientListItem() {
    recipe.postValue(TestUtils.createRecipe(TEST_RECIPE_ID, "Marshmallows", 12, ""));
    ingredients.postValue(TestUtils.createIngredientsList(8));

    // For some reason, clicking on this view alone does not work
    onView(withId(R.id.ingredients_btn)).perform(click());
    onView(withId(R.id.recipe_ingredients_label)).perform(click());
    onView(ingredientsMatcher().atPosition(3)).check(matches(hasDescendant(withText("3 tsp "))));
    onView(ingredientsMatcher().atPosition(3)).check(matches(hasDescendant(withText("Fancy ingredient"))));
  }

  @Test
  public void scrollIngredientsList() {
    recipe.postValue(TestUtils.createRecipe(TEST_RECIPE_ID, "Marshmallows", 12, ""));
    ingredients.postValue(TestUtils.createIngredientsList(78));

    // For some reason, clicking on this view alone does not work
    onView(withId(R.id.ingredients_btn)).perform(click());
    onView(withId(R.id.recipe_ingredients_label)).perform(click());
    onView(withId(R.id.steps_recycler_view)).perform(EspressoScrollActions.nestedScrollTo());
    onView(ingredientsMatcher().atPosition(77)).check(matches(isDisplayed()));
  }

  @Test
  public void showStepList() {
    recipe.postValue(TestUtils.createRecipe(TEST_RECIPE_ID, "Marshmallows", 12, ""));
    ingredients.postValue(TestUtils.createIngredientsList(78));
    steps.postValue(TestUtils.createStepList(TEST_RECIPE_ID, 15));

    onView(withId(R.id.steps_recycler_view)).check(matches(isDisplayed()));
    onView(withId(R.id.recipe_steps_label)).check(matches(isDisplayed()));
  }

  @Test
  public void checkStepsCount() {
    recipe.postValue(TestUtils.createRecipe(TEST_RECIPE_ID, "Marshmallows", 12, ""));
    ingredients.postValue(TestUtils.createIngredientsList(78));
    steps.postValue(TestUtils.createStepList(TEST_RECIPE_ID, 15));

    onView(withId(R.id.steps_recycler_view)).check(new RecyclerViewItemCountAssertion(15));
  }

  @Test
  public void verifyStepListItem() {
    recipe.postValue(TestUtils.createRecipe(TEST_RECIPE_ID, "Marshmallows", 12, ""));
    ingredients.postValue(TestUtils.createIngredientsList(8));
    steps.postValue(TestUtils.createStepList(TEST_RECIPE_ID, 15));

    onView(stepsMatcher().atPositionOnView(0, R.id.recipe_step_number))
        .check(matches(withText("1")));
    onView(stepsMatcher().atPositionOnView(0, R.id.recipe_step_description))
        .check(matches(withText(TEST_STEP_NAME)));
  }

  @Test
  public void scrollStepList() {
    recipe.postValue(TestUtils.createRecipe(TEST_RECIPE_ID, "Marshmallows", 12, ""));
    ingredients.postValue(TestUtils.createIngredientsList(8));
    steps.postValue(TestUtils.createStepList(TEST_RECIPE_ID, 34));

    onView(stepsMatcher().atPosition(33)).perform(EspressoScrollActions.nestedScrollTo());
    onView(stepsMatcher().atPosition(33)).check(matches(isDisplayed()));
  }

  @Test
  public void scrollStepListWithExpandedIngredients() {
    recipe.postValue(TestUtils.createRecipe(TEST_RECIPE_ID, "Marshmallows", 12, ""));
    ingredients.postValue(TestUtils.createIngredientsList(8));
    steps.postValue(TestUtils.createStepList(TEST_RECIPE_ID, 34));

    // For some reason, clicking on this view alone does not work
    onView(withId(R.id.ingredients_btn)).perform(click());
    onView(withId(R.id.recipe_ingredients_label)).perform(click());
    onView(stepsMatcher().atPosition(33)).perform(EspressoScrollActions.nestedScrollTo());
    onView(stepsMatcher().atPosition(33)).check(matches(isDisplayed()));
  }

  @Test
  public void verifyClickedStepIsNotHighlighted() {
    recipe.postValue(TestUtils.createRecipe(TEST_RECIPE_ID, "Marshmallows", 12, ""));
    ingredients.postValue(TestUtils.createIngredientsList(8));
    steps.postValue(TestUtils.createStepList(TEST_RECIPE_ID, 34));

    onView(withId(R.id.steps_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(
        0, click()));
    onView(stepsMatcher().atPositionOnView(0, R.id.recipe_step_number))
        .check(matches(hasTextColor(R.color.colorAccent)));
  }

  @NonNull
  private RecyclerViewMatcher ingredientsMatcher() {
    return new RecyclerViewMatcher(R.id.ingredients_recycler_view);
  }

  @NonNull
  private RecyclerViewMatcher stepsMatcher() {
    return new RecyclerViewMatcher(R.id.steps_recycler_view);
  }
}
