package com.mattwiduch.bakeit.ui.recipe_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.data.database.entries.Step;
import com.mattwiduch.bakeit.ui.recipe_detail.RecipeStepAdapter.RecipeStepViewHolder;
import com.mattwiduch.bakeit.ui.step_detail.StepDetailActivity;
import com.mattwiduch.bakeit.ui.step_detail.StepDetailFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@link RecipeStepAdapter} class.
 *
 * The adapter provides access to the items in the {@link RecipeStepViewHolder}.
 */
public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepViewHolder> {

  private final RecipeDetailActivity mParentActivity;
  private List<Step> mStepList;
  private final boolean mTwoPane;
  private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {

      if (mTwoPane) {
        Bundle arguments = new Bundle();
        arguments.putString(StepDetailFragment.ARG_ITEM_ID, (String) view.getTag());
        StepDetailFragment fragment = new StepDetailFragment();
        fragment.setArguments(arguments);
        mParentActivity.getSupportFragmentManager().beginTransaction()
            .replace(R.id.step_detail_container, fragment)
            .commit();
      } else {
        Context context = view.getContext();
        Intent intent = new Intent(context, StepDetailActivity.class);
        intent.putExtra(StepDetailFragment.ARG_ITEM_ID, (String) view.getTag());

        context.startActivity(intent);
      }
    }
  };

  RecipeStepAdapter(RecipeDetailActivity parent, boolean twoPane) {
    mStepList = new ArrayList<>();
    mParentActivity = parent;
    mTwoPane = twoPane;
  }

  @Override
  public RecipeStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_item_step, parent, false);
    return new RecipeStepViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final RecipeStepViewHolder holder, int position) {
    Step step = mStepList.get(position);

    holder.mIdView.setText(step.getShortDescription());

    holder.itemView.setTag(step.getDbId());
    holder.itemView.setOnClickListener(mOnClickListener);
  }

  @Override
  public int getItemCount() {
    return mStepList.size();
  }

  class RecipeStepViewHolder extends RecyclerView.ViewHolder {

    final TextView mIdView;
    final TextView mContentView;

    RecipeStepViewHolder(View view) {
      super(view);
      mIdView = (TextView) view.findViewById(R.id.id_text);
      mContentView = (TextView) view.findViewById(R.id.content);
    }
  }

  void updateSteps(List<Step> steps) {
    mStepList = steps;
    notifyDataSetChanged();
  }
}