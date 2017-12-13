package com.mattwiduch.bakeit.ui.recipe_detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.data.database.entries.Step;
import com.mattwiduch.bakeit.ui.recipe_detail.RecipeStepAdapter.RecipeStepViewHolder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The {@link RecipeStepAdapter} class.
 *
 * The adapter provides access to the items in the {@link RecipeStepViewHolder}.
 */
public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepViewHolder> {

  // Interface to handle clicks on items within this Adapter
  private final RecipeStepAdapterOnItemClickHandler mClickHandler;
  // List of recipe steps to display in Recycler View
  private List<Step> mStepList;

  /**
   * Default constructor for {@link RecipeStepViewHolder} adapter.
   *
   * @param clickHandler The on-click handler for this adapter
   */
  RecipeStepAdapter(RecipeStepAdapterOnItemClickHandler clickHandler) {
    setHasStableIds(true);
    mClickHandler = clickHandler;
    mStepList = new ArrayList<>();
  }

  /**
   * Creates a new view for a recipe step item view. This method is invoked by the layout manager.
   */
  @Override
  public RecipeStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_step, parent, false);
    return new RecipeStepViewHolder(view);
  }

  /**
   *  Replaces the content in the views that make up the recipe item view. This method is invoked
   *  by the layout manager.
   */
  @Override
  public void onBindViewHolder(final RecipeStepViewHolder holder, int position) {
    Step step = mStepList.get(position);
    holder.stepNumberTv.setText(String.format(Locale.getDefault(),"%d",
        step.getStepNumber() + 1));
    holder.stepDescriptionTv.setText(step.getShortDescription());

    if (position == mStepList.size() - 1) {
      holder.stepDecor.setVisibility(View.INVISIBLE);
    }
  }

  /**
   *  Returns the size of the dataset.
   */
  @Override
  public int getItemCount() {
    return mStepList.size();
  }

  /**
   * Update list of recipe steps to display.
   * @param steps List of steps
   */
  void updateSteps(List<Step> steps) {
    mStepList = steps;
    notifyDataSetChanged();
  }

  /**
   * The interface that receives onItemClick messages.
   */
  public interface RecipeStepAdapterOnItemClickHandler {
    void onItemClick(int stepId);
  }

  /**
   * The {@link RecipeStepViewHolder} class. Provides a reference to each view in the recipe item
   * view.
   */
  class RecipeStepViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

    @BindView(R.id.recipe_step_number)
    TextView stepNumberTv;
    @BindView(R.id.recipe_step_description)
    TextView stepDescriptionTv;
    @BindView(R.id.recipe_step_decor)
    View stepDecor;

    RecipeStepViewHolder(View view) {
      super(view);
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
      int stepNumber = mStepList.get(getAdapterPosition()).getStepNumber();
      mClickHandler.onItemClick(stepNumber);
    }
  }
}