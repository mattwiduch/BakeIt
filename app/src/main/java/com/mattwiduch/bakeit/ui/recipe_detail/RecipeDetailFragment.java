package com.mattwiduch.bakeit.ui.recipe_detail;

import static com.mattwiduch.bakeit.ui.recipe_detail.RecipeDetailActivity.KEY_CURRENT_STEP;
import static com.mattwiduch.bakeit.ui.recipe_detail.RecipeDetailActivity.KEY_TWO_PANE_MODE;
import static com.mattwiduch.bakeit.ui.recipe_detail.RecipeDetailActivity.RECIPE_ID_EXTRA;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.ui.recipe_detail.RecipeStepAdapter.RecipeStepAdapterOnItemClickHandler;
import dagger.android.support.AndroidSupportInjection;
import javax.inject.Inject;
import net.cachapa.expandablelayout.ExpandableLayout;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is contained in a {@link RecipeDetailActivity}.
 */
public class RecipeDetailFragment extends Fragment implements RecipeStepAdapterOnItemClickHandler {

  @BindView(R.id.steps_recycler_view)
  RecyclerView stepsRecyclerView;
  @BindView(R.id.recipe_ingredients_arrow)
  ImageView expandIngredientsBtn;
  @BindView(R.id.ingredients_container)
  ExpandableLayout ingredientsContainer;
  @BindView(R.id.ingredients_recycler_view)
  RecyclerView ingredientsRecyclerView;
  @BindView(R.id.recipe_servings)
  TextView recipeServingsTv;

  @Inject
  ViewModelProvider.Factory viewModelFactory;

  private boolean mTwoPane;
  private int mRecipeId;
  private RecipeDetailViewModel mViewModel;
  private RecipeStepAdapter mStepsAdapter;
  private RecipeIngredientAdapter mIngredientsAdapter;
  private OnRecipeLoadedListener mRecipeLoadedCallback;
  private OnStepSelectedListener mStepSelectedCallback;
  private int mCurrentStep;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public RecipeDetailFragment() {
    // Mandatory
  }

  @Override
  public void onAttach(Context context) {
    AndroidSupportInjection.inject(this);
    super.onAttach(context);

    // This makes sure that the container activity has implemented
    // the callback interface. If not, it throws an exception
    try {
      mRecipeLoadedCallback = (OnRecipeLoadedListener) context;
      mStepSelectedCallback = (OnStepSelectedListener) context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString()
          + " must implement both OnRecipeLoadedListener and OnStepSelectedListener.");
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Get recipe id
    if (getArguments() != null) {
      if (getArguments().containsKey(RECIPE_ID_EXTRA)) {
        mRecipeId = getArguments().getInt(RECIPE_ID_EXTRA);
      }
      if (getArguments().containsKey(KEY_TWO_PANE_MODE)) {
        mTwoPane = getArguments().getBoolean(KEY_TWO_PANE_MODE);
      }
    }

    if (savedInstanceState == null) {
      mCurrentStep = 0;
    } else {
      mCurrentStep = savedInstanceState.getInt(KEY_CURRENT_STEP);
    }
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
    ButterKnife.bind(this, rootView);
    return rootView;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    // Setup recycler view
    setupRecyclerViews();

    // Setup ViewModel
    mViewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeDetailViewModel.class);
    mViewModel.setRecipeId(mRecipeId);

    // Observe changes in recipe data
    mViewModel.getRecipe().observe(this, recipe -> {
      if (recipe != null) {
        mRecipeLoadedCallback.onRecipeLoaded(recipe.getName());
        recipeServingsTv.setText(getString(R.string.recipe_servings, recipe.getServings()));
      }
    });

    mViewModel.getRecipeIngredients().observe(this, ingredients -> {
      if (ingredients != null) {
        mIngredientsAdapter.updateIngredients(ingredients);
      }
    });
    mViewModel.getRecipeSteps().observe(this, steps -> {
      if (steps != null) {
        mStepsAdapter.updateSteps(steps);
      }
    });
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    if (mTwoPane) {
      outState.putInt(KEY_CURRENT_STEP, mCurrentStep);
    }
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onItemClick(int stepNumber) {
    mCurrentStep = stepNumber;
    mStepSelectedCallback.onStepSelected(stepNumber);
  }

  /**
   * Container Activity must implement these interfaces.
   */
  public interface OnRecipeLoadedListener {
    void onRecipeLoaded(String recipeName);
  }

  public interface OnStepSelectedListener {
    void onStepSelected(int stepNumber);
  }

  /**
   * Prepares recycler views that display list of recipe ingredients and recipe steps.
   */
  private void setupRecyclerViews() {
    // Ingredients recycler view
    mIngredientsAdapter = new RecipeIngredientAdapter();
    ingredientsRecyclerView.setAdapter(mIngredientsAdapter);
    ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    ingredientsRecyclerView.setHasFixedSize(true);
    // Steps recycler view
    stepsRecyclerView.addItemDecoration(new StepItemDecoration(getContext()));
    mStepsAdapter = new RecipeStepAdapter(this, getContext(), mTwoPane);
    mStepsAdapter.setSelectedItem(mCurrentStep);
    stepsRecyclerView.setAdapter(mStepsAdapter);
    stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    stepsRecyclerView.setHasFixedSize(true);
  }

  /**
   * Expands and collapses list of ingredients.
   */
  @OnClick(R.id.ingredients_btn)
  public void toggleIngredients() {
    ingredientsContainer.toggle();
    Animation rotate = AnimationUtils.loadAnimation(getContext(), R.anim.ingredients_button);
    if (ingredientsContainer.isExpanded()) {
      expandIngredientsBtn.startAnimation(rotate);
      expandIngredientsBtn.setImageResource(R.drawable.ic_arrow_drop_down_black_36dp);
    } else {
      expandIngredientsBtn.startAnimation(rotate);
      expandIngredientsBtn.setImageResource(R.drawable.ic_arrow_drop_up_black_36dp);
    }
  }
}
