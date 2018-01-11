package com.mattwiduch.bakeit.data.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import dagger.android.AndroidInjection;
import javax.inject.Inject;

/**
 * An {@link IntentService} subclass for immediately scheduling a sync with the server off of the
 * main thread. This is necessary because {@link com.firebase.jobdispatcher.FirebaseJobDispatcher}
 * will not trigger a job immediately. This should only be called when the application is on the
 * screen.
 */
public class RecipeSyncIntentService extends IntentService {

  @Inject
  RecipeNetworkDataSource mRecipeNetworkDataSource;

  private static final String LOG_TAG = RecipeSyncIntentService.class.getSimpleName();

  public RecipeSyncIntentService() {
    super("RecipeSyncIntentService");
  }

  @Override
  public void onCreate() {
    AndroidInjection.inject(this);
    super.onCreate();
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    Log.d(LOG_TAG, "Recipe IntentService started");
    mRecipeNetworkDataSource.fetchRecipes();
  }
}
