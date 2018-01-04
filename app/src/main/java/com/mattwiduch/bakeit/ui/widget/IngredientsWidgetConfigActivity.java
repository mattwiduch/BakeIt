package com.mattwiduch.bakeit.ui.widget;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.ui.recipe_list.RecipeListModelFactory;
import com.mattwiduch.bakeit.ui.recipe_list.RecipeListViewModel;
import com.mattwiduch.bakeit.utils.InjectorUtils;

/**
 * Configuration activity for IngredientsWidget. Provides user with a list of recipes to choose
 * from.
 */

public class IngredientsWidgetConfigActivity extends AppCompatActivity {

  int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
  private RecipeListViewModel mViewModel;

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
      RecipeListModelFactory factory = InjectorUtils.provideRecipeListViewModelFactory(
          this.getApplicationContext());
      mViewModel = ViewModelProviders.of(this, factory).get(RecipeListViewModel.class);

      mViewModel.getAllRecipes().observe(this, recipes -> {
        if (recipes != null && !recipes.isEmpty()) {
          // TODO: Populate spinner with recipes
        } else {
          Toast.makeText(this, R.string.empty_recipe_database, Toast.LENGTH_LONG).show();
          finish();
        }
      });
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
