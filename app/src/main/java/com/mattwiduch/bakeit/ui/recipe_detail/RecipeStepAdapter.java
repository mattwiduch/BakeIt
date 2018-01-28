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

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.data.database.entries.Step;
import com.mattwiduch.bakeit.ui.recipe_detail.RecipeStepAdapter.RecipeStepViewHolder;
import de.hdodenhof.circleimageview.CircleImageView;
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
  // Context used to load resources
  private Context mContext;
  //
  private boolean mTwoPane;
  // List of recipe steps to display in Recycler View
  private List<Step> mStepList;
  // Currently selected item's position
  private int mSelectedPosition = RecyclerView.NO_POSITION;

  /**
   * Default constructor for {@link RecipeStepViewHolder} adapter.
   *
   * @param clickHandler The on-click handler for this adapter
   */
  RecipeStepAdapter(RecipeStepAdapterOnItemClickHandler clickHandler, Context context,
      boolean twoPane) {
    mClickHandler = clickHandler;
    mStepList = new ArrayList<>();
    mContext = context;
    mTwoPane = twoPane;
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
   * Replaces the content in the views that make up the recipe item view. This method is invoked
   * by the layout manager.
   */
  @Override
  public void onBindViewHolder(final RecipeStepViewHolder holder, int position) {
    Step step = mStepList.get(position);
    holder.stepNumberTv.setText(String.format(Locale.getDefault(), "%d",
        step.getStepNumber() + 1));
    holder.stepDescriptionTv.setText(step.getShortDescription());

    // Highlight currently selected item (in two pane mode only)
    if (mTwoPane) {
      if (position == mSelectedPosition) {
        holder.stepNumberFrame.setImageResource(R.color.colorAccent);
        holder.stepNumberFrame
            .setBorderColor(mContext.getResources().getColor(R.color.colorAccent));
        holder.stepNumberTv.setTextColor(
            mContext.getResources().getColor(R.color.colorCardBackground));
        holder.stepNumberTv.setTypeface(holder.stepNumberTv.getTypeface(), Typeface.BOLD);
        holder.stepDescriptionTv
            .setTypeface(ResourcesCompat.getFont(mContext, R.font.poppins_medium));
        holder.stepArrowIv.setColorFilter(mContext.getResources().getColor(R.color.colorAccent));
      } else {
        holder.stepNumberFrame.setImageResource(R.color.colorCardBackground);
        holder.stepNumberFrame
            .setBorderColor(mContext.getResources().getColor(R.color.colorSecondary));
        holder.stepNumberTv.setTextColor(
            mContext.getResources().getColor(R.color.colorAccent));
        holder.stepNumberTv.setTypeface(holder.stepNumberTv.getTypeface(), Typeface.NORMAL);
        holder.stepDescriptionTv
            .setTypeface(ResourcesCompat.getFont(mContext, R.font.poppins_light));
        holder.stepArrowIv.setColorFilter(mContext.getResources().getColor(R.color.colorSecondary));
      }
    }
  }

  /**
   * Returns the size of the dataset.
   */
  @Override
  public int getItemCount() {
    return mStepList.size();
  }

  /**
   * Update list of recipe steps to display.
   *
   * @param steps List of steps
   */
  void updateSteps(List<Step> steps) {
    mStepList = steps;
    notifyDataSetChanged();
  }

  /**
   * Sets currently selected item's position.
   *
   * @param position of currently selected item
   */
  void setSelectedItem(int position) {
    mSelectedPosition = position;
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

    @BindView(R.id.recipe_step_number_frame)
    CircleImageView stepNumberFrame;
    @BindView(R.id.recipe_step_number)
    TextView stepNumberTv;
    @BindView(R.id.recipe_step_description)
    TextView stepDescriptionTv;
    @BindView(R.id.recipe_step_arrow)
    ImageView stepArrowIv;

    RecipeStepViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    /**
     * This gets called by the child views during a click. It passes recipe id to onItemClickHandler
     * registered with this adapter.
     *
     * @param v the View that was clicked
     */
    @Override
    public void onClick(View v) {
      int stepNumber = mStepList.get(getAdapterPosition()).getStepNumber();

      // Update items to highlight currently selected position
      notifyItemChanged(mSelectedPosition);
      mSelectedPosition = getAdapterPosition();
      notifyItemChanged(mSelectedPosition);

      mClickHandler.onItemClick(stepNumber);
    }
  }
}