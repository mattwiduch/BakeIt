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
package com.mattwiduch.bakeit.ui.step_detail;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.mattwiduch.bakeit.util.TestUtils.TEST_CURRENT_STEP;
import static com.mattwiduch.bakeit.util.TestUtils.TEST_FIRST_STEP;
import static com.mattwiduch.bakeit.util.TestUtils.TEST_LAST_STEP;
import static com.mattwiduch.bakeit.util.TestUtils.TEST_RECIPE_ID;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.testing.SingleFragmentActivity;
import com.mattwiduch.bakeit.ui.recipe_detail.RecipeDetailActivity;
import com.mattwiduch.bakeit.util.EspressoTestUtil;
import com.mattwiduch.bakeit.util.TestUtils;
import com.mattwiduch.bakeit.util.ViewModelUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
public class StepDetailNavigationTest {

  @Rule
  public ActivityTestRule<SingleFragmentActivity> activityTestRule =
      new ActivityTestRule<>(SingleFragmentActivity.class, true, true);

  private MutableLiveData<CompositeStep> compositeStep = new MutableLiveData<>();
  private StepDetailFragment firstStepFragment;
  private StepDetailFragment lastStepFragment;
  private StepDetailViewModel viewModel;

  @Before
  public void init() {
    EspressoTestUtil.disableProgressBarAnimations(activityTestRule);
    viewModel = mock(StepDetailViewModel.class);
    when(viewModel.getCompositeStep()).thenReturn(compositeStep);

    firstStepFragment = createStepDetailSpy(TEST_FIRST_STEP);
    firstStepFragment.viewModelFactory = ViewModelUtil.createFor(viewModel);

    lastStepFragment = createStepDetailSpy(TEST_LAST_STEP);
    lastStepFragment.viewModelFactory = ViewModelUtil.createFor(viewModel);
  }

  @Test
  public void hidePrevButton() {
    activityTestRule.getActivity().setFragment(firstStepFragment);
    compositeStep.postValue(TestUtils.createCompositeStep(TEST_RECIPE_ID, TEST_CURRENT_STEP,
        false, false, true));
    onView(withId(R.id.step_previous_btn)).check(matches(not(isDisplayed())));
  }

  @Test
  public void showNextButton() {
    activityTestRule.getActivity().setFragment(firstStepFragment);
    compositeStep.postValue(TestUtils.createCompositeStep(TEST_RECIPE_ID, TEST_CURRENT_STEP,
        false, false, true));
    onView(withId(R.id.step_next_btn)).check(matches(isDisplayed()));
  }

  @Test
  public void clickNextButton() {
    activityTestRule.getActivity().setFragment(firstStepFragment);
    compositeStep.postValue(TestUtils.createCompositeStep(TEST_RECIPE_ID, TEST_CURRENT_STEP,
        false, false, true));
    onView(withId(R.id.step_next_btn)).check(matches(isClickable()));
    onView(withId(R.id.step_next_btn)).perform(click());
    verify(firstStepFragment).loadStepFragment(TEST_FIRST_STEP + 1);
  }

  @Test
  public void hideNextButton() {
    activityTestRule.getActivity().setFragment(lastStepFragment);
    compositeStep.postValue(TestUtils.createCompositeStep(TEST_RECIPE_ID, TEST_CURRENT_STEP,
        false, false, true));
    onView(withId(R.id.step_next_btn)).check(matches(not(isDisplayed())));
  }

  @Test
  public void showPrevButton() {
    activityTestRule.getActivity().setFragment(lastStepFragment);
    compositeStep.postValue(TestUtils.createCompositeStep(TEST_RECIPE_ID, TEST_CURRENT_STEP,
        false, false, true));
    onView(withId(R.id.step_previous_btn)).check(matches(isDisplayed()));
  }

  @Test
  public void clickPrevButton() {
    activityTestRule.getActivity().setFragment(lastStepFragment);
    compositeStep.postValue(TestUtils.createCompositeStep(TEST_RECIPE_ID, TEST_CURRENT_STEP,
        false, false, true));
    onView(withId(R.id.step_previous_btn)).check(matches(isClickable()));
    onView(withId(R.id.step_previous_btn)).perform(click());
    verify(lastStepFragment).loadStepFragment(TEST_LAST_STEP - 1);
  }

  /**
   * Mockito spy that mocks loadStepFragment method within StepDetailFragment.
   */
  private StepDetailFragment createStepDetailSpy(int stepNumber) {
    StepDetailFragment fragment = Mockito.spy(new StepDetailFragment());
    int nextStep = stepNumber == TEST_FIRST_STEP ? stepNumber + 1 : stepNumber - 1;
    Mockito.doNothing().when(fragment).loadStepFragment(nextStep);

    Bundle arguments = new Bundle();
    arguments.putInt(StepDetailFragment.RECIPE_STEP_NUMBER, stepNumber);
    arguments.putInt(RecipeDetailActivity.RECIPE_ID_EXTRA, TEST_RECIPE_ID);
    fragment.setArguments(arguments);

    return fragment;
  }
}

