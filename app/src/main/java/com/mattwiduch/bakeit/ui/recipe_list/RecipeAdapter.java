package com.mattwiduch.bakeit.ui.recipe_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.data.database.Recipe;
import com.mattwiduch.bakeit.ui.recipe_list.RecipeAdapter.RecipeViewHolder;
import java.util.List;

/**
 * The {@link RecipeAdapter} class.
 *
 * The adapter provides access to the items in the {@link RecipeViewHolder}.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
  // List of recipes to display in Recycler View
  private List<Recipe> mRecipeList;

  /**
   * Default constructor for {@link RecipeViewHolder} adapter.
   *
   * @param myDataset List of recycler view items
   */
  RecipeAdapter(List<Recipe> myDataset) {
    setHasStableIds(true);
    mRecipeList = myDataset;
  }

  /**
   * Creates a new view for a recipe item view. This method is invoked by the layout manager.
   */
  @Override
  public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate(R.layout.list_item_recipe, parent, false);
    return new RecipeViewHolder(view);
  }

  /**
   *  Replaces the content in the views that make up the recipe item view. This method is invoked
   *  by the layout manager.
   */
  @Override
  public void onBindViewHolder(RecipeViewHolder holder, int position) {
    Recipe recipe = mRecipeList.get(position);
    holder.recipeName.setText(recipe.getName());
  }

  /**
   *  Returns the size of the dataset.
   */
  @Override
  public int getItemCount() {
    return mRecipeList.size();
  }

  /**
   * The {@link RecipeViewHolder} class.
   * Provides a reference to each view in the recipe item view.
   */
  class RecipeViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.list_item_recipe_name)
    TextView recipeName;

    RecipeViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  void updateRecipes(List<Recipe> recipes) {
    mRecipeList = recipes;
    notifyDataSetChanged();
  }
}
