package com.mattwiduch.bakeit.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.ui.recipe_detail.RecipeDetailActivity;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {

  static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
      int appWidgetId) {

    CharSequence widgetText = context.getString(R.string.appwidget_text);
    // Construct the RemoteViews object
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
    views.setTextViewText(R.id.appwidget_text, widgetText);

    // Create an Intent to launch RecipeDetailActivity for given recipe when clicked
    Intent intent = new Intent(context, RecipeDetailActivity.class);
    // TODO: Change to actual id
    intent.putExtra(RecipeDetailActivity.RECIPE_ID_EXTRA, 1);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
    views.setOnClickPendingIntent(R.id.ingredients_widget_container, pendingIntent);

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // There may be multiple widgets active, so update all of them
    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId);
    }
  }

  @Override
  public void onEnabled(Context context) {
    // Enter relevant functionality for when the first widget is created
  }

  @Override
  public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
  }
}

