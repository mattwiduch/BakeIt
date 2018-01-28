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
package com.mattwiduch.bakeit.util.matchers;

import android.view.View;
import android.widget.ListView;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ListViewMatcher {

  public static Matcher<View> withCount(final int count) {
    return new TypeSafeMatcher<View>() {
      @Override
      public boolean matchesSafely(final View view) {
        return ((ListView) view).getAdapter().getCount() == count;
      }

      @Override
      public void describeTo(final Description description) {
        description.appendText("The list should have count of " + count);
      }
    };
  }
}
