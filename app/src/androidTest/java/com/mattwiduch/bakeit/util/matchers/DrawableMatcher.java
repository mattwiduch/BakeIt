package com.mattwiduch.bakeit.util.matchers;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Source: https://stackoverflow.com/a/44343520
 * Credit: drakeet
 */
public class DrawableMatcher extends TypeSafeMatcher<View> {

  private final int expectedId;
  private String resourceName;

  public DrawableMatcher(@DrawableRes int expectedId) {
    super(View.class);
    this.expectedId = expectedId;
  }

  @Override
  protected boolean matchesSafely(View target) {
    if (!(target instanceof ImageView)) {
      return false;
    }
    ImageView imageView = (ImageView) target;
    if (expectedId < 0) {
      return imageView.getDrawable() == null;
    }
    Resources resources = target.getContext().getResources();
    Drawable expectedDrawable = resources.getDrawable(expectedId);
    resourceName = resources.getResourceEntryName(expectedId);
    if (expectedDrawable != null && expectedDrawable.getConstantState() != null) {
      return expectedDrawable.getConstantState().equals(
          imageView.getDrawable().getConstantState()
      );
    } else {
      return false;
    }
  }


  @Override
  public void describeTo(Description description) {
    description.appendText("with drawable from resource id: ");
    description.appendValue(expectedId);
    if (resourceName != null) {
      description.appendText("[");
      description.appendText(resourceName);
      description.appendText("]");
    }
  }

  public static Matcher<View> withDrawableId(@DrawableRes final int id) {
    return new DrawableMatcher(id);
  }
}
