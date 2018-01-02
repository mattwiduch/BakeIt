package com.mattwiduch.bakeit.ui.recipe_detail;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;
import com.mattwiduch.bakeit.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Draws lines connecting step items' step number containers.
 */

public class StepItemDecoration extends RecyclerView.ItemDecoration {

  private Drawable mDecorationLine;

  StepItemDecoration(Context context) {
    mDecorationLine = context.getResources().getDrawable(R.drawable.step_item_decoration);
  }

  @Override
  public void onDrawOver(Canvas c, RecyclerView parent, State state) {
    int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      View child = parent.getChildAt(i);
      CircleImageView stepNumberFrame = child.findViewById(R.id.recipe_step_number_frame);

      // Center of the circle containing step number
      int left = ((stepNumberFrame.getLeft() + stepNumberFrame.getRight()) / 2) - 1;
      int right = left + 2;

      // Draw line above the circle except for first item
      if (parent.getChildAdapterPosition(child) > 0) {
        int top = child.getTop();
        int bottom = child.getTop() + stepNumberFrame.getTop();
        mDecorationLine.setBounds(left, top, right, bottom);
        mDecorationLine.draw(c);
      }
      // Draw line below the circle except for last item
      if (parent.getChildAdapterPosition(child) < parent.getAdapter().getItemCount() - 1) {
        int top = child.getTop() + stepNumberFrame.getBottom();
        int bottom = child.getBottom();
        mDecorationLine.setBounds(left, top, right, bottom);
        mDecorationLine.draw(c);
      }
    }
  }
}
