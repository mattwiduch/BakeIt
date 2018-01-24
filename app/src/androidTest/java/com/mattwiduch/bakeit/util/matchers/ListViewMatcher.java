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
