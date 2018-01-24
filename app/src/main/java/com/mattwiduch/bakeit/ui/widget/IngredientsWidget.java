package com.mattwiduch.bakeit.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.ui.recipe_detail.RecipeDetailActivity;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {
  static final String PREFS_NAME = "com.mattwiduch.bakeit.ui.widget.IngredientsWidget";
  static final String PREF_ID_KEY = "recipeId_";
  static final String PREF_NAME_KEY = "recipeName_";
  static final String PREF_SERVINGS_KEY = "recipeServings_";

  static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
      int appWidgetId) {
    // Load recipe details from shared preferences
    SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
    int recipeId = prefs.getInt(PREF_ID_KEY + appWidgetId, 1);
    CharSequence recipeName = prefs.getString(PREF_NAME_KEY + appWidgetId,
        context.getString(R.string.app_name));
    CharSequence recipeServings = context.getString(R.string.recipe_servings,
        prefs.getInt(PREF_SERVINGS_KEY + appWidgetId, 0));

    // Construct the RemoteViews object
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
    views.setTextViewText(R.id.widget_recipe_name, recipeName);
    views.setTextViewText(R.id.widget_recipe_servings, recipeServings);

    // Populate ListView
    Intent listIntent = new Intent(context, IngredientsWidgetService.class);
    listIntent.putExtra(RecipeDetailActivity.RECIPE_ID_EXTRA, recipeId);
    views.setRemoteAdapter(R.id.widget_ingredients_list, listIntent);

    // Create an Intent to launch RecipeDetailActivity for given recipe when clicked
    Intent intent = new Intent(context, RecipeDetailActivity.class);
    intent.putExtra(RecipeDetailActivity.RECIPE_ID_EXTRA, recipeId);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
        intent, PendingIntent.FLAG_CANCEL_CURRENT);
    views.setOnClickPendingIntent(R.id.widget_bar, pendingIntent);

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

