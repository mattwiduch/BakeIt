package com.mattwiduch.bakeit.data.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import com.mattwiduch.bakeit.utils.InjectorUtils;

/**
 * An {@link IntentService} subclass for immediately scheduling a sync with the server off of the
 * main thread. This is necessary because {@link com.firebase.jobdispatcher.FirebaseJobDispatcher}
 * will not trigger a job immediately. This should only be called when the application is on the
 * screen.
 */
public class RecipeSyncIntentService extends IntentService {

  private static final String LOG_TAG = RecipeSyncIntentService.class.getSimpleName();

  public RecipeSyncIntentService() {
    super("RecipeSyncIntentService");
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    Log.d(LOG_TAG, "Recipe IntentService started");
    RecipeNetworkDataSource networkDataSource = InjectorUtils.provideNetworkDataSource(
        this.getApplicationContext());
    networkDataSource.fetchRecipes();
  }
}
