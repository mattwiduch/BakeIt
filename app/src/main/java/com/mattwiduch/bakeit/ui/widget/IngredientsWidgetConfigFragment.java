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
package com.mattwiduch.bakeit.ui.widget;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.mattwiduch.bakeit.ui.widget.IngredientsWidget.PREFS_NAME;
import static com.mattwiduch.bakeit.ui.widget.IngredientsWidget.PREF_ID_KEY;
import static com.mattwiduch.bakeit.ui.widget.IngredientsWidget.PREF_NAME_KEY;
import static com.mattwiduch.bakeit.ui.widget.IngredientsWidget.PREF_SERVINGS_KEY;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.mattwiduch.bakeit.di.injector.Injectable;
import com.mattwiduch.bakeit.ui.recipe_list.RecipeListViewModel;
import java.util.List;
import javax.inject.Inject;

/**
 * Provides user with a list of recipes to choose from.
 */
public class IngredientsWidgetConfigFragment extends Fragment implements Injectable,
    OnItemSelectedListener {

  @BindView(R.id.widget_config_spinner)
  Spinner recipesSpinner;

  @Inject
  ViewModelProvider.Factory viewModelFactory;

  int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
  int mSpinnerPosition;

  public IngredientsWidgetConfigFragment() {
    // Mandatory
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments() != null) {
      if (getArguments().containsKey(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
        mAppWidgetId = getArguments().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
      }
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_widget_config, container, false);
    ButterKnife.bind(this, rootView);
    return rootView;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    RecipeListViewModel mViewModel = ViewModelProviders.of(this, viewModelFactory)
        .get(RecipeListViewModel.class);

    mViewModel.getAllRecipes().observe(this, recipes -> {
      if (recipes != null && !recipes.isEmpty()) {
        ArrayAdapter<Recipe> spinnerAdapter =
            new RecipesAdapter(getActivity(), R.layout.spinner_item, recipes);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        recipesSpinner.setAdapter(spinnerAdapter);
        recipesSpinner.setOnItemSelectedListener(this);
      } else {
        Toast.makeText(getActivity(), R.string.empty_recipe_database, Toast.LENGTH_LONG).show();
        getActivity().setResult(RESULT_CANCELED);
        getActivity().finish();
      }
    });
  }

  @OnClick(R.id.widget_config_apply)
  public void addWidget() {
    // Save id, name and servings quantity of recipe to shared preferences
    SharedPreferences.Editor prefs = getActivity().getSharedPreferences(PREFS_NAME, 0).edit();
    prefs.putInt(PREF_ID_KEY + mAppWidgetId,
        ((Recipe) recipesSpinner.getAdapter().getItem(mSpinnerPosition)).getId());
    prefs.putString(PREF_NAME_KEY + mAppWidgetId,
        ((Recipe) recipesSpinner.getAdapter().getItem(mSpinnerPosition)).getName());
    prefs.putInt(PREF_SERVINGS_KEY + mAppWidgetId,
        ((Recipe) recipesSpinner.getAdapter().getItem(mSpinnerPosition)).getServings());
    prefs.commit();
    // Push widget update
    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity());
    IngredientsWidget.updateAppWidget(getActivity(), appWidgetManager, mAppWidgetId);
    // Make sure we pass back the original appWidgetId
    Intent resultValue = new Intent();
    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
    getActivity().setResult(RESULT_OK, resultValue);
    getActivity().finish();
  }

  @OnClick(R.id.widget_config_cancel)
  public void cancelWidget() {
    getActivity().setResult(RESULT_CANCELED);
    getActivity().finish();
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
