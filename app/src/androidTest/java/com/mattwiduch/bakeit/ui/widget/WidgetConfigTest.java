package com.mattwiduch.bakeit.ui.widget;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.ActivityResultMatchers.hasResultCode;
import static android.support.test.espresso.contrib.ActivityResultMatchers.hasResultData;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.mattwiduch.bakeit.util.matchers.ListViewMatcher.withCount;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ListView;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import com.mattwiduch.bakeit.testing.SingleFragmentActivity;
import com.mattwiduch.bakeit.ui.recipe_list.RecipeListViewModel;
import com.mattwiduch.bakeit.util.EspressoTestUtil;
import com.mattwiduch.bakeit.util.TaskExecutorWithIdlingResourceRule;
import com.mattwiduch.bakeit.util.TestUtils;
import com.mattwiduch.bakeit.util.ViewModelUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class WidgetConfigTest {
  @Rule
  public ActivityTestRule<SingleFragmentActivity> activityTestRule =
      new ActivityTestRule<>(SingleFragmentActivity.class, true, true);

  @Rule
  public TaskExecutorWithIdlingResourceRule executorTestRule =
      new TaskExecutorWithIdlingResourceRule();

  private MutableLiveData<List<Recipe>> recipes = new MutableLiveData<>();
  private IngredientsWidgetConfigFragment widgetConfigFragment;
  private RecipeListViewModel viewModel;

  @Before
  public void init() {
    EspressoTestUtil.disableProgressBarAnimations(activityTestRule);
    widgetConfigFragment = new IngredientsWidgetConfigFragment();
    Bundle args = new Bundle();
    args.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, 55);
    widgetConfigFragment.setArguments(args);
    viewModel = mock(RecipeListViewModel.class);
    when(viewModel.getAllRecipes()).thenReturn(recipes);

    widgetConfigFragment.viewModelFactory = ViewModelUtil.createFor(viewModel);
    activityTestRule.getActivity().setFragment(widgetConfigFragment);
  }

  @Test
  public void useAppContext() throws Exception {
    // Context of the app under test.
    Context appContext = InstrumentationRegistry.getTargetContext();

    assertEquals("com.mattwiduch.bakeit", appContext.getPackageName());
  }

  @Test
  public void showErrorWhenDatabaseIsEmpty() throws InterruptedException {
    recipes.postValue(new ArrayList<>());
    // Unable to test Toast since parent activity closes immediately
//    onView(withText(R.string.empty_recipe_database)).
//        inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView()))).
//        check(matches(isDisplayed()));
    SystemClock.sleep(500);
    assertThat(activityTestRule.getActivityResult(), hasResultCode(Activity.RESULT_CANCELED));
    assertTrue(activityTestRule.getActivity().isFinishing());
    assertTrue(activityTestRule.getActivity().isDestroyed());
  }

  @Test
  public void showConfigDialog() {
    recipes.postValue(TestUtils.createRecipeList(6));

    onView(withText(R.string.widget_config_label)).check(matches(isDisplayed()));
    onView(withId(R.id.widget_config_spinner)).check(matches(isDisplayed()));
    onView(withId(R.id.widget_config_cancel)).check(matches(isDisplayed()));
    onView(withId(R.id.widget_config_apply)).check(matches(isDisplayed()));
  }

  @Test
  public void verifySpinnerContent() {
    Recipe recipe1 = TestUtils.createRecipe(1, "Brownies", 8, "");
    Recipe recipe2 = TestUtils.createRecipe(2, "Shortbread", 10, "");
    recipes.postValue(Arrays.asList(recipe1, recipe2));

    onView(withId(R.id.widget_config_spinner)).check(matches(hasDescendant(withText("Brownies"))));
    onData(is(instanceOf(Recipe.class))).atPosition(0).check(matches(withText("Brownies")));

    onView(withId(R.id.widget_config_spinner)).perform(click());
    onData(is(instanceOf(Recipe.class))).atPosition(1).perform(click());
    onView(withId(R.id.widget_config_spinner)).check(matches(hasDescendant(withText("Shortbread"))));
  }

  @Test
  public void verifySpinnerRecipeCount() {
    recipes.postValue(TestUtils.createRecipeList(34));

    onView(withId(R.id.widget_config_spinner)).perform(click());
    onView(instanceOf(ListView.class)).check(matches(withCount(34)));
  }

  @Test
  public void scrollSpinnerRecipeList() {
    recipes.postValue(TestUtils.createRecipeList(44));

    onView(withId(R.id.widget_config_spinner)).perform(click());
    onData(is(instanceOf(Recipe.class))).atPosition(43).perform(scrollTo());
    onData(is(instanceOf(Recipe.class))).atPosition(43).check(matches(isDisplayed()));
  }

  @Test
  public void cancelWidget() {
    recipes.postValue(TestUtils.createRecipeList(12));

    onView(withId(R.id.widget_config_cancel)).check(matches(isClickable()));
    onView(withId(R.id.widget_config_cancel)).perform(click());

    assertThat(activityTestRule.getActivityResult(), hasResultCode(Activity.RESULT_CANCELED));
    assertTrue(activityTestRule.getActivity().isFinishing());
  }

  @Test
  public void confirmWidget() {
    recipes.postValue(TestUtils.createRecipeList(12));

    onView(withId(R.id.widget_config_apply)).check(matches(isClickable()));
    onView(withId(R.id.widget_config_apply)).perform(click());
    assertThat(activityTestRule.getActivityResult(), hasResultCode(Activity.RESULT_OK));
    assertThat(activityTestRule.getActivityResult(), hasResultData(
        IntentMatchers.hasExtraWithKey(AppWidgetManager.EXTRA_APPWIDGET_ID)));
    assertThat(activityTestRule.getActivityResult(), hasResultData(
        IntentMatchers.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 55)));
  }
}
