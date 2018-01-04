package com.mattwiduch.bakeit.ui.widget;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.mattwiduch.bakeit.R;

/**
 * Created by mateusz on 04/01/18.
 */

public class IngredientsWidgetConfigActivity extends Activity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.ingredients_widget_config);
    setResult(RESULT_CANCELED);
  }
}
