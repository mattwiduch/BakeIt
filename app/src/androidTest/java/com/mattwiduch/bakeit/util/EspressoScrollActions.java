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
package com.mattwiduch.bakeit.util;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.util.HumanReadables;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

/**
 * Created by F1sherKK on 07/03/2017. Source: https://medium.com/azimolabs/guide-to-make-custom-viewaction-solving-problem-of-nestedscrollview-in-espresso-35b133850254
 */

public class EspressoScrollActions {

  public static ViewAction nestedScrollTo() {
    return new ViewAction() {

      @Override
      public Matcher<View> getConstraints() {
        return Matchers.allOf(
            isDescendantOfA(isAssignableFrom(NestedScrollView.class)),
            withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE));
      }

      @Override
      public String getDescription() {
        return "Find parent with type " + NestedScrollView.class +
            " of matched view and programmatically scroll to it.";
      }

      @Override
      public void perform(UiController uiController, View view) {
        try {
          NestedScrollView nestedScrollView = (NestedScrollView)
              findFirstParentLayoutOfClass(view, NestedScrollView.class);
          if (nestedScrollView != null) {
            CoordinatorLayout coordinatorLayout =
                (CoordinatorLayout) findFirstParentLayoutOfClass(view, CoordinatorLayout.class);
            if (coordinatorLayout != null) {
              CollapsingToolbarLayout collapsingToolbarLayout =
                  findCollapsingToolbarLayoutChildIn(coordinatorLayout);
              if (collapsingToolbarLayout != null) {
                int toolbarHeight = collapsingToolbarLayout.getHeight();
                nestedScrollView.startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                nestedScrollView.dispatchNestedPreScroll(0, toolbarHeight, null, null);
              }
            }
            nestedScrollView.scrollTo(0, view.getTop());
          } else {
            throw new Exception("Unable to find NestedScrollView parent.");
          }
        } catch (Exception e) {
          throw new PerformException.Builder()
              .withActionDescription(this.getDescription())
              .withViewDescription(HumanReadables.describe(view))
              .withCause(e)
              .build();
        }
        uiController.loopMainThreadUntilIdle();
      }
    };
  }

  private static CollapsingToolbarLayout findCollapsingToolbarLayoutChildIn(ViewGroup viewGroup) {
    for (int i = 0; i < viewGroup.getChildCount(); i++) {
      View child = viewGroup.getChildAt(i);
      if (child instanceof CollapsingToolbarLayout) {
        return (CollapsingToolbarLayout) child;
      } else if (child instanceof ViewGroup) {
        return findCollapsingToolbarLayoutChildIn((ViewGroup) child);
      }
    }
    return null;
  }

  private static View findFirstParentLayoutOfClass(View view, Class<? extends View> parentClass) {
    ViewParent parent = new FrameLayout(view.getContext());
    ViewParent incrementView = null;
    int i = 0;
    while (parent != null && !(parent.getClass() == parentClass)) {
      if (i == 0) {
        parent = findParent(view);
      } else {
        parent = findParent(incrementView);
      }
      incrementView = parent;
      i++;
    }
    return (View) parent;
  }

  private static ViewParent findParent(View view) {
    return view.getParent();
  }

  private static ViewParent findParent(ViewParent view) {
    return view.getParent();
  }
}
