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
package com.mattwiduch.bakeit.ui.recipe_detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.data.database.entries.Ingredient;
import com.mattwiduch.bakeit.ui.recipe_detail.RecipeIngredientAdapter.RecipeIngredientViewHolder;
import com.mattwiduch.bakeit.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The {@link RecipeIngredientAdapter} class.
 *
 * The adapter provides access to the items in the {@link RecipeIngredientViewHolder}.
 */
public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientViewHolder> {
  // List of recipe ingredients to display in Recycler View
  private List<Ingredient> mIngredientList;

  /**
   * Default constructor for {@link RecipeIngredientViewHolder} adapter.
   */
  RecipeIngredientAdapter() {
    setHasStableIds(true);
    mIngredientList = new ArrayList<>();
  }

  /**
   * Creates a new view for a recipe step item view. This method is invoked by the layout manager.
   */
  @Override
  public RecipeIngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_ingredient, parent, false);
    return new RecipeIngredientViewHolder(view);
  }

  /**
   *  Replaces the content in the views that make up the recipe item view. This method is invoked
   *  by the layout manager.
   */
  @Override
  public void onBindViewHolder(RecipeIngredientViewHolder holder, int position) {
    Ingredient ingredient = mIngredientList.get(position);

    holder.ingredientQuantityTv.setText(String.format(Locale.getDefault(), "%s%s ",
        StringUtils.formatQuantity(ingredient.getQuantity().toString()),
        StringUtils.formatMeasure(ingredient.getMeasure())));
    holder.ingredientNameTv.setText(ingredient.getName());
  }

  /**
   *  Returns the size of the dataset.
   */
  @Override
  public int getItemCount() {
    return mIngredientList.size();
  }

  /**
   * Update list of recipe ingredients to display.
   * @param ingredients List of ingredients
   */
  void updateIngredients(List<Ingredient> ingredients) {
    mIngredientList = ingredients;
    notifyDataSetChanged();
  }

  /**
   * The {@link RecipeIngredientAdapter.RecipeIngredientViewHolder} class.
   * Provides a reference to each view in the recipe item view.
   */
  class RecipeIngredientViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ingredient_name)
    TextView ingredientNameTv;
    @BindView(R.id.ingredient_quantity)
    TextView ingredientQuantityTv;

    RecipeIngredientViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, itemView);
    }
  }
}
