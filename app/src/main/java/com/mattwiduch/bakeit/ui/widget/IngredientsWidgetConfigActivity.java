package com.mattwiduch.bakeit.ui.widget;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import com.mattwiduch.bakeit.ui.recipe_list.RecipeListModelFactory;
import com.mattwiduch.bakeit.ui.recipe_list.RecipeListViewModel;
import com.mattwiduch.bakeit.utils.InjectorUtils;
import java.util.List;

/**
 * Configuration activity for IngredientsWidget. Provides user with a list of recipes to choose
 * from.
 */

public class IngredientsWidgetConfigActivity extends AppCompatActivity implements
    OnItemSelectedListener {

  @BindView(R.id.widget_config_spinner)
  Spinner recipesSpinner;

  int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
  int mSpinnerPosition;

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

      RecipeListModelFactory factory = InjectorUtils.provideRecipeListViewModelFactory(
          this.getApplicationContext());
      RecipeListViewModel mViewModel = ViewModelProviders.of(this, factory)
          .get(RecipeListViewModel.class);

      mViewModel.getAllRecipes().observe(this, recipes -> {
        if (recipes != null && !recipes.isEmpty()) {
          ArrayAdapter<Recipe> spinnerAdapter =
              new RecipesAdapter(this, R.layout.spinner_item, recipes);
          spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
          recipesSpinner.setAdapter(spinnerAdapter);
          recipesSpinner.setOnItemSelectedListener(this);
        } else {
          Toast.makeText(this, R.string.empty_recipe_database, Toast.LENGTH_LONG).show();
          finish();
        }
      });
    }
  }

  @OnClick(R.id.widget_config_apply)
  public void addWidget() {
    // TODO: Save id, name and servings to shared preferences
    // Push widget update
    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
    IngredientsWidget.updateAppWidget(this, appWidgetManager, mAppWidgetId);
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

  @Override
  public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    mSpinnerPosition = i;
  }

  @Override
  public void onNothingSelected(AdapterView<?> adapterView) {
    mSpinnerPosition = 0;
  }
}

class RecipesAdapter extends ArrayAdapter<Recipe> {

  private RecipeViewHolder mViewHolder;

  RecipesAdapter(@NonNull Context context,
      int resource, @NonNull List objects) {
    super(context, resource, objects);
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    if (convertView == null) {
      convertView = LayoutInflater.from(this.getContext())
          .inflate(R.layout.spinner_item, parent, false);

      mViewHolder = new RecipeViewHolder();
      mViewHolder.recipeName = convertView.findViewById(android.R.id.text1);

      convertView.setTag(mViewHolder);
    } else {
      mViewHolder = (RecipeViewHolder) convertView.getTag();
    }

    Recipe recipe = getItem(position);
    if (recipe != null) {
      mViewHolder.recipeName.setText(recipe.getName());
    }

    return convertView;
  }

  @Override
  public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    if (convertView == null) {
      convertView = LayoutInflater.from(this.getContext())
          .inflate(R.layout.spinner_dropdown_item, parent, false);

      mViewHolder = new RecipeViewHolder();
      mViewHolder.recipeName = convertView.findViewById(android.R.id.text1);

      convertView.setTag(mViewHolder);
    } else {
      mViewHolder = (RecipeViewHolder) convertView.getTag();
    }

    Recipe recipe = getItem(position);
    if (recipe != null) {
      mViewHolder.recipeName.setText(recipe.getName());
    }

    return convertView;
  }

  private static class RecipeViewHolder {
    private TextView recipeName;
  }

}
