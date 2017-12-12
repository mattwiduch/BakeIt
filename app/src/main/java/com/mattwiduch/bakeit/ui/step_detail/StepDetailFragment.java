package com.mattwiduch.bakeit.ui.step_detail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.ui.recipe_detail.RecipeDetailActivity;
import com.mattwiduch.bakeit.utils.InjectorUtils;
import com.mattwiduch.bakeit.utils.StringUtils;

/**
 * A fragment representing a single Step detail screen.
 * This fragment is either contained in a {@link RecipeDetailActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment {

  @BindView(R.id.step_number)
  TextView stepNumberTv;
  @BindView(R.id.step_description)
  TextView stepDescriptionTv;

  /**
   * The fragment argument representing the step ID that this fragment
   * represents.
   */
  public static final String RECIPE_STEP_ID = "recipe_step_id";

  private StepDetailViewModel mViewModel;
  private int mRecipeId;
  private int mStepId;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public StepDetailFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments().containsKey(RecipeDetailActivity.RECIPE_ID_EXTRA)) {
      mRecipeId = getArguments().getInt(RecipeDetailActivity.RECIPE_ID_EXTRA);
    }
    if (getArguments().containsKey(RECIPE_STEP_ID)) {
      mStepId = getArguments().getInt(RECIPE_STEP_ID);
    }

    // Get the ViewModel from the factory
    StepDetailModelFactory factory = InjectorUtils.provideStepDetailViewModelFactory(
        getContext(), mRecipeId, mStepId);
    mViewModel = ViewModelProviders.of(this, factory).get(StepDetailViewModel.class);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
    ButterKnife.bind(this, rootView);

    // Observe changes in step data
    mViewModel.getCurrentStep().observe(this, step -> {
      if (step != null) {
        stepNumberTv.setText(getString(R.string.step_number, step.getStepNumber() + 1));
        stepDescriptionTv.setText(StringUtils.removeStepNumber(step.getDescription()));
      }
    });

    return rootView;
  }
}
