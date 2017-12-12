package com.mattwiduch.bakeit.ui.step_detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.data.database.entries.Step;
import com.mattwiduch.bakeit.ui.recipe_detail.RecipeDetailActivity;

/**
 * A fragment representing a single Step detail screen.
 * This fragment is either contained in a {@link RecipeDetailActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment {

  /**
   * The fragment argument representing the step ID that this fragment
   * represents.
   */
  public static final String RECIPE_STEP_ID = "recipe_step_id";

  /**
   * The dummy content this fragment is presenting.
   */
  private Step mItem;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public StepDetailFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments().containsKey(RECIPE_STEP_ID)) {
      // Load the dummy content specified by the fragment
      // arguments. In a real-world scenario, use a Loader
      // to load content from a content provider.
      // mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);

    // Show the dummy content as text in a TextView.
    if (mItem != null) {
      ((TextView) rootView.findViewById(R.id.step_detail)).setText(mItem.getDescription());
    }

    return rootView;
  }
}
