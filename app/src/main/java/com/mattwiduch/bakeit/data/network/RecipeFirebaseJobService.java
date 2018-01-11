package com.mattwiduch.bakeit.data.network;

import android.util.Log;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;
import dagger.android.AndroidInjection;
import javax.inject.Inject;

/**
 * Schedules recipe database update job.
 */

public class RecipeFirebaseJobService extends JobService {

  @Inject
  RecipeNetworkDataSource mRecipeNetworkDataSource;

  private static final String LOG_TAG = RecipeFirebaseJobService.class.getSimpleName();

  @Override
  public void onCreate() {
    AndroidInjection.inject(this);
    super.onCreate();
  }

  /**
   * The entry point to your Job. Implementations should offload work to another thread of
   * execution as soon as possible.
   * <p>
   * This is called by the Job Dispatcher to tell us we should start our job. Keep in mind this
   * method is run on the application's main thread, so we need to offload work to a background
   * thread.
   *
   * @return whether there is more work remaining.
   */
  @Override
  public boolean onStartJob(JobParameters job) {
    Log.d(LOG_TAG, "Started recipe job service");

    mRecipeNetworkDataSource.fetchRecipes();

    jobFinished(job, false);

    return true;
  }

  /**
   * Called when the scheduling engine has decided to interrupt the execution of a running job,
   * most likely because the runtime constraints associated with the job are no longer satisfied.
   *
   * @return whether the job should be retried
   * @see Job.Builder#setRetryStrategy(RetryStrategy)
   * @see RetryStrategy
   */
  @Override
  public boolean onStopJob(JobParameters job) {
    return true;
  }
}
