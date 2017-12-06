package com.mattwiduch.bakeit.ui.recipe_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import com.mattwiduch.bakeit.ui.recipe_list.RecipeAdapter.RecipeViewHolder;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@link RecipeAdapter} class.
 *
 * The adapter provides access to the items in the {@link RecipeViewHolder}.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
  // Interface to handle clicks on items within this Adapter
  private final RecipeAdapterOnItemClickHandler mClickHandler;
  // List of recipes to display in Recycler View
  private List<Recipe> mRecipeList;

  /**
   * Default constructor for {@link RecipeViewHolder} adapter.
   *
   * @param clickHandler The on-click handler for this adapter
   */
  RecipeAdapter(RecipeAdapterOnItemClickHandler clickHandler) {
    setHasStableIds(true);
    mClickHandler = clickHandler;
    mRecipeList = new ArrayList<>();
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


  void updateRecipes(List<Recipe> recipes) {
    mRecipeList = recipes;
    notifyDataSetChanged();
  }

  /**
   * The interface that receives onItemClick messages.
   */
  public interface RecipeAdapterOnItemClickHandler {
    void onItemClick(int recipeId);
  }

  /**
   * The {@link RecipeViewHolder} class. Provides a reference to each view in the recipe item view.
   */
  class RecipeViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
    @BindView(R.id.list_item_recipe_name)
    TextView recipeName;

    RecipeViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    /**
     * This gets called by the child views during a click. It passes recipe id to onItemClickHandler
     * registered with this adapter.
     * @param v the View that was clicked
     */
    @Override
    public void onClick(View v) {
      int recipeId = mRecipeList.get(getAdapterPosition()).getId();
      mClickHandler.onItemClick(recipeId);
    }
  }
}
