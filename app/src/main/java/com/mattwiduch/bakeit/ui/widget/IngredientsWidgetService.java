package com.mattwiduch.bakeit.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.RemoteViewsService.RemoteViewsFactory;
import com.mattwiduch.bakeit.R;
import com.mattwiduch.bakeit.data.RecipeRepository;
import com.mattwiduch.bakeit.data.database.entries.Ingredient;
import com.mattwiduch.bakeit.ui.recipe_detail.RecipeDetailActivity;
import com.mattwiduch.bakeit.utils.StringUtils;
import dagger.android.AndroidInjection;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;

/**
 * An interface for an adapter between ListView and the ingredients data for that view.
 */
public class IngredientsWidgetService extends RemoteViewsService {

  @Inject
  RecipeRepository repository;

  @Override
  public void onCreate() {
    AndroidInjection.inject(this);
    super.onCreate();
  }

  @Override
  public RemoteViewsFactory onGetViewFactory(Intent intent) {
    return new IngredientsListRemoteViewsFactory(this.getApplicationContext(), repository,
        intent.getIntExtra(RecipeDetailActivity.RECIPE_ID_EXTRA, 0));
  }
}

class IngredientsListRemoteViewsFactory implements RemoteViewsFactory {

  private final RecipeRepository mRepository;
  private Context mContext;
  private List<Ingredient> mIngredients;
  private int mRecipeId;

  IngredientsListRemoteViewsFactory(Context context, RecipeRepository repository, int id) {
    mContext = context;
    mRepository = repository;
    mRecipeId = id;
  }

  @Override
  public void onCreate() {}

  /**
   * Called on start and when notifyAppWidgetViewDataChanged is called.
   */
  @Override
  public void onDataSetChanged() {
    mIngredients = mRepository.getIngredientsData(mRecipeId);
  }

  @Override
  public void onDestroy() {}

  @Override
  public int getCount() {
    if (mIngredients == null) return 0;
    return mIngredients.size();
  }

  @Override
  public RemoteViews getViewAt(int i) {
    if (mIngredients == null) return null;

    Ingredient ingredient = mIngredients.get(i);
    String quantity = StringUtils.formatQuantity(ingredient.getQuantity().toString());
    String measure = StringUtils.formatMeasure(ingredient.getMeasure());
    String name = ingredient.getName();

    RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.list_item_widget);

    views.setTextViewText(R.id.ingredient_quantity, String.format(Locale.getDefault(),
        "%s%s ", quantity, measure));
    views.setTextViewText(R.id.ingredient_name, name);

    return views;
  }

  @Override
  public RemoteViews getLoadingView() {
    return null;
  }

  @Override
  public int getViewTypeCount() {
    return 1;
  }

  @Override
  public long getItemId(int i) {
    return 0;
  }

  @Override
  public boolean hasStableIds() {
    return false;
  }
}
