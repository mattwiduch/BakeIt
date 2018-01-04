package com.mattwiduch.bakeit.ui.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mattwiduch.bakeit.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration activity for IngredientsWidget. Provides user with a list of recipes to choose
 * from.
 */

public class IngredientsWidgetConfigActivity extends Activity {

  int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.ingredients_widget_config);
    ButterKnife.bind(this);
    setResult(RESULT_CANCELED);

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
          AppWidgetManager.INVALID_APPWIDGET_ID);

      // TODO: Observe data

      final List<String> recipes = new ArrayList<>();
      if (true) {
      // TODO: Show this toast when live data object is empty
      } else {
        Toast.makeText(this, R.string.empty_recipe_database, Toast.LENGTH_LONG).show();
        finish();
      }
    }
  }

  @OnClick(R.id.widget_config_apply)
  public void addWidget() {
    // Make sure we pass back the original appWidgetId
    Intent resultValue = new Intent();
    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
    setResult(RESULT_OK, resultValue);
    finish();
  }

  @OnClick(R.id.widget_config_cancel)
  public void cancelWidget() {
    finish();
  }
}
