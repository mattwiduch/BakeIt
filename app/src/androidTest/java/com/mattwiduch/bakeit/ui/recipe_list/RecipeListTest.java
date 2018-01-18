package com.mattwiduch.bakeit.ui.recipe_list;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import com.mattwiduch.bakeit.data.network.RecipeNetworkDataSource;
import com.mattwiduch.bakeit.testing.SingleFragmentActivity;
import com.mattwiduch.bakeit.util.EspressoTestUtil;
import com.mattwiduch.bakeit.util.TaskExecutorWithIdlingResourceRule;
import com.mattwiduch.bakeit.util.ViewModelUtil;
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
}
