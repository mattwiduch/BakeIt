package com.mattwiduch.bakeit.ui.step_detail;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.mattwiduch.bakeit.ui.recipe_detail.RecipeDetailActivity.RECIPE_ID_EXTRA;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Explode;
import android.support.transition.Fade;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import com.mattwiduch.bakeit.data.database.entries.Step;
import com.mattwiduch.bakeit.ui.VideoPlayer;
import com.mattwiduch.bakeit.ui.recipe_detail.RecipeDetailActivity;
import com.mattwiduch.bakeit.utils.ConnectionDetector;
import com.mattwiduch.bakeit.utils.StringUtils;
import dagger.android.support.AndroidSupportInjection;
import java.util.List;
import javax.inject.Inject;

/**
 * A fragment representing a single Step detail screen.
 * This fragment is either contained in a {@link RecipeDetailActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment {

  /**
   * The fragment argument representing the step number of given recipe this fragment
   * represents.
   */
  public static final String RECIPE_STEP_NUMBER = "recipe_step_number";
  private static final String KEY_VIDEO_PLAYING = "KEY_VIDEO_PLAYING";
  private static final String KEY_VIDEO_FULLSCREEN = "KEY_VIDEO_FULLSCREEN";

  @BindView(R.id.step_image)
  ImageView stepImageIv;
  @BindView(R.id.step_video_player)
  SimpleExoPlayerView videoPlayerView;
  @BindView(R.id.step_number)
  TextView stepNumberTv;
  @BindView(R.id.step_description)
  TextView stepDescriptionTv;
  @BindView(R.id.step_previous_btn)
  Button previousStepBtn;
  @BindView(R.id.step_next_btn)
  Button nextStepBtn;
  @BindView(R.id.playback_controller)
  CardView playbackController;
  @BindView(R.id.play_video_btn)
  ImageView playButton;
  @BindView(R.id.retry_video_btn)
  ImageView retryButton;
  @BindView(R.id.exo_fullscreen)
  ImageButton fullscreenButton;
  @BindView(R.id.step_video_container)
  FrameLayout videoPlayerContainer;

  @Inject
  ViewModelProvider.Factory viewModelFactory;

  private StepDetailViewModel mViewModel;
  private OnStepLoadedListener mStepLoadedCallback;
  private int mRecipeId;
  private int mStepNumber;
  private boolean mPlaying;

  // Used to play video in full screen
  private Dialog mVideoDialog;
  private boolean mVideoFullscreen;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public StepDetailFragment() {
    // Mandatory
  }

  @Override
  public void onAttach(Context context) {
    AndroidSupportInjection.inject(this);
    super.onAttach(context);

    // This makes sure that the container activity has implemented
    // the callback interface. If not, it throws an exception
    try {
      mStepLoadedCallback = (OnStepLoadedListener) context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString()
          + " must implement OnStepLoadedListener.");
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (savedInstanceState != null) {
      mPlaying = savedInstanceState.getBoolean(KEY_VIDEO_PLAYING);
      mVideoFullscreen = savedInstanceState.getBoolean(KEY_VIDEO_FULLSCREEN);
    }

    if (getArguments() != null) {
      if (getArguments().containsKey(RECIPE_ID_EXTRA)) {
        mRecipeId = getArguments().getInt(RECIPE_ID_EXTRA);
      }
      if (getArguments().containsKey(RECIPE_STEP_NUMBER)) {
        mStepNumber = getArguments().getInt(RECIPE_STEP_NUMBER);
      }
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
    ButterKnife.bind(this, rootView);
    return rootView;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    // Setup ViewModel
    mViewModel = ViewModelProviders.of(this, viewModelFactory).get(StepDetailViewModel.class);
    mViewModel.setStepData(mRecipeId, mStepNumber);

    // Add network connection observer to step mediator live data
    mViewModel.getStepMediator().addSource(new ConnectionDetector(getContext()), status -> {
      if (status != null) {
        CompositeStep compositeStep = mViewModel.getStepMediator().getValue();
        if (compositeStep != null) {
          compositeStep.setConnected(status.getIsConnected());
          mViewModel.getStepMediator().postValue(compositeStep);
        }
      }
    });

    mViewModel.getStepMediator().observe(this, compositeStep -> {
      if (compositeStep != null) {
        Recipe recipe = compositeStep.getRecipe();
        if (recipe != null) {
          mStepLoadedCallback.onStepLoaded(recipe.getName());
        }

        List<Step> steps = compositeStep.getStepList();
        if (steps != null && mStepNumber == steps.size() - 1) {
          nextStepBtn.setVisibility(View.INVISIBLE);
        }

        Step step = compositeStep.getStep();
        if (step != null) {
          if (mStepNumber == 0) {
            previousStepBtn.setVisibility(View.INVISIBLE);
          }
          stepNumberTv.setText(getString(R.string.step_number, step.getStepNumber() + 1));
          stepDescriptionTv.setText(StringUtils.removeStepNumber(step.getDescription()));

          String imageUrl = step.getThumbnailURL();
          String videoUrl = step.getVideoURL();

          if (StringUtils.checkUrl(videoUrl)) {
            if (compositeStep.isConnected()) {
              mVideoDialog = initialiseVideoDialog(getActivity());
              if (mVideoFullscreen) {
                showFullscreenVideo();
              }

              videoPlayerContainer.setVisibility(View.VISIBLE);
              if (!mPlaying) {
                playButton.setVisibility(View.VISIBLE);
              } else {
                playbackController.setVisibility(View.VISIBLE);
              }

              VideoPlayer.getInstance().initialiseExoPlayer(getActivity(), Uri.parse(videoUrl),
                  videoPlayerView);
            } else {
              // TODO: retryButton.setVisibility(View.VISIBLE);
            }
          } else if (StringUtils.checkUrl(imageUrl)) {
            // If image is available, load it using Glide
            stepImageIv.setVisibility(View.VISIBLE);
            RequestOptions glideOptions = new RequestOptions().centerCrop()
                .error(R.drawable.error_image);
            Glide.with(this)
                .load(imageUrl)
                .apply(glideOptions)
                .transition(withCrossFade())
                .into(stepImageIv);
          } else {
            // If both video and image are not available, remove image view
            stepImageIv.setVisibility(View.GONE);
          }
        }
      }
    });
  }

  @Override
  public void onResume() {
    super.onResume();
    VideoPlayer.getInstance().resume();
  }

  @Override
  public void onPause() {
    super.onPause();
    VideoPlayer.getInstance().suspend();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    VideoPlayer.getInstance().release();
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBoolean(KEY_VIDEO_PLAYING, mPlaying);
    outState.putBoolean(KEY_VIDEO_FULLSCREEN, mVideoFullscreen);
  }

  /**
   * Reloads step data for given step number.
   *
   * @param stepNumber number of step to load
   */
  private void loadStepFragment(int stepNumber) {
    Bundle arguments = new Bundle();
    arguments.putInt(RECIPE_ID_EXTRA, mRecipeId);
    arguments.putInt(StepDetailFragment.RECIPE_STEP_NUMBER, stepNumber);
    StepDetailFragment fragment = new StepDetailFragment();
    fragment.setArguments(arguments);
    fragment.setExitTransition(new Fade());
    fragment.setEnterTransition(new Explode());
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

  /**
   * Starts video playback.
   */
  @OnClick(R.id.play_video_btn)
  public void playVideo() {
    playButton.setVisibility(View.GONE);
    playbackController.setVisibility(View.VISIBLE);
    VideoPlayer.getInstance().play();
    mPlaying = true;
  }

  /**
   * Initialises Dialog that shows ExoPlayer in fullscreen mode.
   *
   * @return Dialog
   */
  private Dialog initialiseVideoDialog(Context context) {
    return new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
      public void onBackPressed() {
        if (mVideoFullscreen) {
          closeFullscreenVideo();
        }
        super.onBackPressed();
      }
    };
  }

  /**
   * Moves ExoPlayer view to full screen dialog.
   */
  private void showFullscreenVideo() {
    ((ViewGroup) videoPlayerView.getParent()).removeView(videoPlayerView);
    mVideoDialog.addContentView(videoPlayerView, new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    fullscreenButton.setImageDrawable(getActivity().getDrawable(R.drawable.ic_fullscreen_exit));
    mVideoFullscreen = true;
    mVideoDialog.show();
  }

  /**
   * Brings back ExoPlayer view to fragment layout.
   */
  private void closeFullscreenVideo() {
    ((ViewGroup) videoPlayerView.getParent()).removeView(videoPlayerView);
    videoPlayerContainer.addView(videoPlayerView);
    mVideoFullscreen = false;
    mVideoDialog.dismiss();
    fullscreenButton.setImageDrawable(getActivity().getDrawable(R.drawable.ic_fullscreen));
  }

  /**
   * Responds to clicks on fullscreen button.
   */
  @OnClick(R.id.exo_fullscreen)
  public void playVideoFullscreen() {
    if (!mVideoFullscreen) {
      showFullscreenVideo();
    } else {
      closeFullscreenVideo();
    }
  }

  /**
   * Container Activity must implement these interfaces.
   */
  public interface OnStepLoadedListener {

    void onStepLoaded(String recipeName);
  }
}
