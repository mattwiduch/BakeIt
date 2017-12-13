package com.mattwiduch.bakeit.ui.step_detail;

import static com.mattwiduch.bakeit.ui.recipe_detail.RecipeDetailActivity.RECIPE_ID_EXTRA;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.transition.Explode;
import android.support.transition.Fade;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
  @BindView(R.id.step_previous_btn)
  Button previousStepBtn;
  @BindView(R.id.step_next_btn)
  Button nextStepButton;

  /**
   * The fragment argument representing the step ID that this fragment
   * represents.
   */
  public static final String RECIPE_STEP_NUMBER = "recipe_step_id";

  private StepDetailViewModel mViewModel;
  private int mRecipeId;
  private int mStepNumber;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public StepDetailFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments().containsKey(RECIPE_ID_EXTRA)) {
      mRecipeId = getArguments().getInt(RECIPE_ID_EXTRA);
    }
    if (getArguments().containsKey(RECIPE_STEP_NUMBER)) {
      mStepNumber = getArguments().getInt(RECIPE_STEP_NUMBER);
    }

    // Get the ViewModel from the factory
    StepDetailModelFactory factory = InjectorUtils.provideStepDetailViewModelFactory(
        getContext(), mRecipeId, mStepNumber);
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
        if (mStepNumber == 0) {
          previousStepBtn.setVisibility(View.INVISIBLE);
        }
        stepNumberTv.setText(getString(R.string.step_number, step.getStepNumber() + 1));
        stepDescriptionTv.setText(StringUtils.removeStepNumber(step.getDescription()));
      }
    });

    mViewModel.getRecipeSteps().observe(this, steps -> {
      if (steps != null) {
        if (mStepNumber == steps.size() - 1) {
          nextStepButton.setVisibility(View.INVISIBLE);
        }
      }
    });

    return rootView;
  }

  /**
   * Reloads step data for given step number.
   * @param stepNumber number of step to load
   */
  private void loadStepFragment(int stepNumber) {
    Bundle arguments = new Bundle();
    arguments.putInt(RECIPE_ID_EXTRA, mRecipeId);
    arguments.putInt(StepDetailFragment.RECIPE_STEP_NUMBER, stepNumber);
    StepDetailFragment fragment = new StepDetailFragment();
    fragment.setArguments(arguments);
    fragment.setExitTransition(new Explode());
    fragment.setEnterTransition(new Fade());
    FragmentManager fragmentManager = getFragmentManager();
    if (fragmentManager != null) {
          fragmentManager.beginTransaction()
          .replace(R.id.step_detail_container, fragment)
          .commit();
    }
  }

  /**
   * Loads previous step when PREV button is clicked.
   */
  @OnClick(R.id.step_previous_btn)
  public void loadPreviousStep() {
    loadStepFragment(mStepNumber - 1);
  }

  /**
   * Loads next step when NEXT button is clicked.
   */
  @OnClick(R.id.step_next_btn)
  public void loadNextStep() {
    loadStepFragment(mStepNumber + 1);
  }
}
