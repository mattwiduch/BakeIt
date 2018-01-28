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

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.ImageView;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class DrawableMatcher extends TypeSafeMatcher<View> {

  private final int expectedId;
  private String resourceName;

  private DrawableMatcher(@DrawableRes int expectedId) {
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

    if (expectedDrawable == null) {
      return false;
    }

    // Applies tint of image view to expected drawable
    if (imageView.getImageTintList() != null) {
      expectedDrawable
          .setColorFilter(imageView.getImageTintList().getDefaultColor(), Mode.SRC_ATOP);
    }

    Bitmap bitmap = getBitmap(imageView.getDrawable());
    Bitmap otherBitmap = getBitmap(expectedDrawable);
    return bitmap.sameAs(otherBitmap);
  }

  private Bitmap getBitmap(Drawable drawable) {
    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    drawable.draw(canvas);
    return bitmap;
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

  public static Matcher<View> withDrawableId(final int resourceId) {
    return new DrawableMatcher(resourceId);
  }

  public static BoundedMatcher<View, ImageView> hasDrawable() {
    return new BoundedMatcher<View, ImageView>(ImageView.class) {
      @Override
      public void describeTo(Description description) {
        description.appendText("has drawable");
      }

      @Override
      public boolean matchesSafely(ImageView imageView) {
        return imageView.getDrawable() != null;
      }
    };
  }
}