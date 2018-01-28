/*
 * Copyright (C) 2018 Mateusz Widuch
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mattwiduch.bakeit.ui.recipe_list;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static com.mattwiduch.bakeit.util.TestUtils.isConnected;

import static com.mattwiduch.bakeit.util.matchers.DrawableMatcher.hasDrawable;
import static com.mattwiduch.bakeit.util.matchers.DrawableMatcher.withDrawableId;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.data.RecipeRepository;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import com.mattwiduch.bakeit.data.network.RecipeNetworkDataSource;
import com.mattwiduch.bakeit.testing.SingleFragmentActivity;
import com.mattwiduch.bakeit.ui.recipe_detail.RecipeDetailActivity;
import com.mattwiduch.bakeit.util.EspressoTestUtil;
import com.mattwiduch.bakeit.util.TaskExecutorWithIdlingResourceRule;
import com.mattwiduch.bakeit.util.TestUtils;
import com.mattwiduch.bakeit.util.ViewModelUtil;
import com.mattwiduch.bakeit.util.matchers.RecyclerViewItemCountAssertion;
import com.mattwiduch.bakeit.util.matchers.RecyclerViewMatcher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RecipeListTest {
  @Rule
  public ActivityTestRule<SingleFragmentActivity> activityTestRule =
      new ActivityTestRule<>(SingleFragmentActivity.class, true, true);

  @Rule
  public TaskExecutorWithIdlingResourceRule executorTestRule =
      new TaskExecutorWithIdlingResourceRule();

  private MutableLiveData<List<Recipe>> recipes = new MutableLiveData<>();
  private RecipeListFragment recipeListFragment;
  private RecipeListViewModel viewModel;
  private RecipeNetworkDataSource dataSource;

  @Before
  public void init() {
    EspressoTestUtil.disableProgressBarAnimations(activityTestRule);
    recipeListFragment = new RecipeListFragment();
    viewModel = mock(RecipeListViewModel.class);
    dataSource = mock(RecipeNetworkDataSource.class);
    when(viewModel.getAllRecipes()).thenReturn(recipes);

    recipeListFragment.viewModelFactory = ViewModelUtil.createFor(viewModel);
    recipeListFragment.recipeNetworkDataSource = dataSource;
    activityTestRule.getActivity().setFragment(recipeListFragment);
  }

  @Test
  public void useAppContext() throws Exception {
    // Context of the app under test.
    Context appContext = InstrumentationRegistry.getTargetContext();

    assertEquals("com.mattwiduch.bakeit", appContext.getPackageName());
  }

  @Test
  public void verifyViewModel() {
    RecipeRepository repository = mock(RecipeRepository.class);
    MutableLiveData<List<Recipe>> allRecipes = new MutableLiveData<>();
    List<Recipe> recipeList = TestUtils.createRecipeList(50);
    allRecipes.postValue(recipeList);
    when(repository.getAllRecipes()).thenReturn(allRecipes);
    RecipeListViewModel viewModel = new RecipeListViewModel(repository);
    assertEquals(viewModel.getAllRecipes().getValue(), recipeList);
  }

  @Test
  public void showNetworkError() {
    if (!isConnected(activityTestRule.getActivity())) {
      onView(allOf(withId(android.support.design.R.id.snackbar_text),
          withText(R.string.snackbar_no_network)))
          .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }
  }

  @Test
  public void showEmptyList() {
    recipes.postValue(new ArrayList<>());
    onView(withId(R.id.recipes_empty_list)).check(matches(isDisplayed()));
    onView(withId(R.id.recipes_loading_indicator)).check(matches(not(isDisplayed())));
    onView(withId(R.id.recipes_recycler_view)).check(matches(not(isDisplayed())));
  }

  @Test
  public void showLoadingList() {
    recipes.postValue(null);
    onView(withId(R.id.recipes_empty_list)).check(matches(not(isDisplayed())));
    onView(withId(R.id.recipes_loading_indicator)).check(matches(isDisplayed()));
    onView(withId(R.id.recipes_recycler_view)).check(matches(not(isDisplayed())));
  }

  @Test
  public void showPopulatedList() {
    onView(withId(R.id.recipes_empty_list)).check(matches(not(isDisplayed())));
    onView(withId(R.id.recipes_loading_indicator)).check(matches(not(isDisplayed())));
    onView(withId(R.id.recipes_recycler_view)).check(matches(isDisplayed()));
  }

  @Test
  public void checkRecipeCount() {
    recipes.postValue(TestUtils.createRecipeList(100));
    onView(withId(R.id.recipes_recycler_view)).check(new RecyclerViewItemCountAssertion(100));
  }

  @Test
  public void checkLoadedRecipe() {
    recipes.postValue(Arrays.asList(TestUtils.createRecipe(1, "Pancakes", 10,
        "http://lorempixel.com/200/200/food")));
    onView(listMatcher().atPosition(0)).check(matches(hasDescendant(withText("Pancakes"))));
    onView(listMatcher().atPosition(0)).check(matches(hasDescendant(withText("10 Servings"))));
    onView(withId(R.id.recipe_image)).check(matches(hasDrawable()));
  }

  @Test
  public void checkLastRecipe() {
    recipes.postValue(TestUtils.createRecipeList(50));
    onView(withId(R.id.recipes_recycler_view)).perform(RecyclerViewActions.scrollToPosition(49));
    onView(listMatcher().atPosition(49)).check(matches(isDisplayed()));
  }

  @Test
  public void checkRecipeWithNoImage() {
    recipes.postValue(Arrays.asList(TestUtils.createRecipe(1, "Pancakes", 10, "")));
    onView(listMatcher().atPosition(0)).check(matches(hasDescendant(withText("Pancakes"))));
    onView(listMatcher().atPosition(0)).check(matches(hasDescendant(withText("10 Servings"))));
    onView(withId(R.id.recipe_image)).check(matches(withDrawableId(R.drawable.ic_recipe)));
  }

  @Test
  public void openRecipe() {
    Intents.init();
    ActivityResult result = new ActivityResult(Activity.RESULT_OK, new Intent());
    intending(anyIntent()).respondWith(result);

    recipes.postValue(Arrays.asList(TestUtils.createRecipe(1, "Pancakes", 10, "")));
    onView(withId(R.id.recipes_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(
        0, click()));

    intended(allOf(
        hasComponent(RecipeDetailActivity.class.getName()),
        hasExtra(equalTo(RecipeDetailActivity.RECIPE_ID_EXTRA), equalTo(1))
    ));
    Intents.release();
  }

  @NonNull
  private RecyclerViewMatcher listMatcher() {
    return new RecyclerViewMatcher(R.id.recipes_recycler_view);
  }


}
