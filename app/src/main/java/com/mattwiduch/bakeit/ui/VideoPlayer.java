/*
 * Copyright (C) 2018 Mateusz Widuch
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mattwiduch.bakeit.ui;

import android.content.Context;
import android.net.Uri;
import android.view.SurfaceView;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

/**
 * Creates singleton instance of ExoPlayer.
 */
public class VideoPlayer {

  // bandwidth meter to measure and estimate bandwidth
  private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
  // Singleton instance
  private static VideoPlayer sInstance;
  // ExoPlayer
  private SimpleExoPlayer mVideoPlayer;
  private long mPlaybackPosition;
  private boolean mPlayWhenReady;
  private Uri mVideoUri;

  private VideoPlayer() {
  }

  /**
   * Returns singleton instance of ExoPlayer handler.
   *
   * @return ExoPlayer handler
   */
  public static VideoPlayer getInstance() {
    if (sInstance == null) {
      sInstance = new VideoPlayer();
    }
    return sInstance;
  }

  /**
   * Creates a player instance and a media source needed for streaming recipe step media.
   *
   * @param context App context
   * @param videoUrl link to video
   * @param videoPlayerView ExoPlayerView
   */
  public void initialiseExoPlayer(Context context, Uri videoUrl,
      SimpleExoPlayerView videoPlayerView) {
    if (context != null && videoUrl != null && videoPlayerView != null) {
      if (mVideoPlayer == null || !videoUrl.equals(mVideoUri)) {
        // a factory to create an AdaptiveVideoTrackSelection
        TrackSelection.Factory adaptiveTrackSelectionFactory =
            new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);

        mVideoPlayer = ExoPlayerFactory.newSimpleInstance(
            new DefaultRenderersFactory(context),
            new DefaultTrackSelector(adaptiveTrackSelectionFactory),
            new DefaultLoadControl());

        mVideoUri = videoUrl;
        prepareVideo(mVideoUri, true);

        videoPlayerView.setPlayer(mVideoPlayer);
        videoPlayerView.hideController();
      }

      mVideoPlayer.clearVideoSurface();
      mVideoPlayer.setVideoSurfaceView((SurfaceView) videoPlayerView.getVideoSurfaceView());
      videoPlayerView.setPlayer(mVideoPlayer);
    }
  }

  /**
   * Prepares recipe step video.
   *
   * @param videoUrl of video to play
   * @param resetPosition true to reset
   */
  public void prepareVideo(Uri videoUrl, boolean resetPosition) {
    MediaSource mediaSource = buildMediaSource(videoUrl);
    mVideoPlayer.prepare(mediaSource, resetPosition, false);
  }

  /**
   * Constructs and returns a ExtractorMediaSource for the given uri.
   *
   * @param uri pointing to a media to play
   * @return media source
   */
  private MediaSource buildMediaSource(Uri uri) {
    return new ExtractorMediaSource(uri,
        new DefaultHttpDataSourceFactory("ua", BANDWIDTH_METER),
        new DefaultExtractorsFactory(), null, null);
  }

  /**
   * Suspends video player until restored.
   */
  public void suspend() {
    if (mVideoPlayer != null) {
      mPlayWhenReady = mVideoPlayer.getPlayWhenReady();
      mPlaybackPosition = mVideoPlayer.getCurrentPosition();
      mVideoPlayer.setPlayWhenReady(false);
    }
  }

  /**
   * Restores video player state.
   */
  public void resume() {
    if (mVideoPlayer != null) {
      mVideoPlayer.seekTo(mPlaybackPosition);
      mVideoPlayer.setPlayWhenReady(mPlayWhenReady);
    }
  }

  /**
   * Releases exoPlayer resources.
   */
  public void release() {
    if (mVideoPlayer != null) {
      mVideoPlayer.release();
      mVideoPlayer = null;
    }
  }

  /**
   * Starts video playback.
   */
  public void play() {
    if (mVideoPlayer != null) {
      mVideoPlayer.setPlayWhenReady(true);
    }
  }
}
