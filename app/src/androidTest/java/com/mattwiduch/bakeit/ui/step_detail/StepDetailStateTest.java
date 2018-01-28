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
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.mattwiduch.bakeit.util.TestUtils.TEST_RECIPE_ID;
import static com.mattwiduch.bakeit.util.TestUtils.TEST_CURRENT_STEP;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
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

@RunWith(AndroidJUnit4.class)
public class StepDetailStateTest {
  @Rule
  public ActivityTestRule<SingleFragmentActivity> activityTestRule =
      new ActivityTestRule<>(SingleFragmentActivity.class, true, true);

  private MutableLiveData<CompositeStep> compositeStep = new MutableLiveData<>();
  private StepDetailFragment stepDetailFragment;
  private StepDetailViewModel viewModel;

  @Before
  public void init() {
    EspressoTestUtil.disableProgressBarAnimations(activityTestRule);
    Bundle arguments = new Bundle();
    arguments.putInt(StepDetailFragment.RECIPE_STEP_NUMBER, TEST_CURRENT_STEP);
    arguments.putInt(RecipeDetailActivity.RECIPE_ID_EXTRA, TEST_RECIPE_ID);
    stepDetailFragment = new StepDetailFragment();
    stepDetailFragment.setArguments(arguments);
    viewModel = mock(StepDetailViewModel.class);
    when(viewModel.getCompositeStep()).thenReturn(compositeStep);

    stepDetailFragment.viewModelFactory = ViewModelUtil.createFor(viewModel);
    activityTestRule.getActivity().setFragment(stepDetailFragment);
  }

  @Test
  public void verifyStepDataIsShownAfterRotation() {
    compositeStep.postValue(TestUtils.createCompositeStep(TEST_RECIPE_ID, TEST_CURRENT_STEP,
        true, true, true));

    onView(withId(R.id.step_image)).check(matches(not(isDisplayed())));
    onView(withId(R.id.step_video_container)).check(matches(isDisplayed()));
    onView(allOf(instanceOf(SimpleExoPlayerView.class), withId(R.id.step_video_player)))
        .check(matches(isDisplayed()));
    onView(withId(R.id.play_video_btn)).check(matches(isDisplayed()));
    onView(withId(R.id.step_number)).check(matches(withText("Step 5")));
    onView(withId(R.id.step_description)).check(matches(withText("Step description")));

    rotateScreen();

    onView(withId(R.id.step_image)).check(matches(not(isDisplayed())));
    onView(withId(R.id.step_video_container)).check(matches(isDisplayed()));
    onView(allOf(instanceOf(SimpleExoPlayerView.class), withId(R.id.step_video_player)))
        .check(matches(isDisplayed()));
    onView(withId(R.id.play_video_btn)).check(matches(isDisplayed()));
    onView(withId(R.id.step_number)).check(matches(withText("Step 5")));
    onView(withId(R.id.step_description)).check(matches(withText("Step description")));
  }

  @Test
  public void verifyVideoKeepsPlayingAfterRotation() {
    compositeStep.postValue(TestUtils.createCompositeStep(TEST_RECIPE_ID, TEST_CURRENT_STEP,
        true, true, true));

    onView(withId(R.id.play_video_btn)).perform(click());
    rotateScreen();

    onView(withId(R.id.step_image)).check(matches(not(isDisplayed())));
    onView(withId(R.id.step_video_container)).check(matches(isDisplayed()));
    onView(allOf(instanceOf(SimpleExoPlayerView.class), withId(R.id.step_video_player)))
        .check(matches(isDisplayed()));
    onView(withId(R.id.play_video_btn)).check(matches(not(isDisplayed())));
    onView(withId(R.id.playback_controller)).check(matches(isDisplayed()));
  }

  @Test
  public void verifyVideoStaysFullscreenAfterRotation() {
    compositeStep.postValue(TestUtils.createCompositeStep(TEST_RECIPE_ID, TEST_CURRENT_STEP,
        true, true, true));

    onView(withId(R.id.play_video_btn)).perform(click());
    onView(withId(R.id.exo_fullscreen)).perform(click());
    rotateScreen();

    onView(allOf(instanceOf(SimpleExoPlayerView.class), withId(R.id.step_video_player)))
        .inRoot(isDialog())
        .check(matches(isDisplayed()));
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
}
