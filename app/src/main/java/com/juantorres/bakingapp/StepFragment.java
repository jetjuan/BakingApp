package com.juantorres.bakingapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.juantorres.bakingapp.data.Step;
import com.juantorres.bakingapp.utils.ImageUtils;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepFragment extends Fragment {
    private final static String RESUME_WINDOW_KEY  = "RESUME_WINDOW_KEY";
    private final static String RESUME_POSITION_KEY = "RESUME_POSITION_KEY";
    private Step mCurrentStep;
    private List<Step> mSteps;
    private int mStepIndex;
    private SimpleExoPlayer mExoPlayer;
    private int resumeWindow;
    private long resumePosition;
    private boolean shouldDisplayVideoThumb;
    private boolean isTabletView;
    @Nullable
    @BindView(R.id.tv_short_description)
    public TextView mTvShortDescription;
    @Nullable
    @BindView(R.id.tv_step_long_description)
    public TextView mTvLongDescription;
    @Nullable
    @BindView(R.id.left_arrow)
    public View mLeftArrow;
    @Nullable
    @BindView(R.id.right_arrow)
    public View mRightArrow;
    @Nullable
    @BindView(R.id.video_player_view)
    public SimpleExoPlayerView mExoPlayerView;
    @Nullable
    @BindView(R.id.video_thumb)
    public ImageView mVideoThumb;

    public StepFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSteps = Parcels.unwrap( getArguments().getParcelable(RecipeDetailFragment.ARG_STEPS));
            mStepIndex = getArguments().getInt(RecipeDetailFragment.ARG_STEP_INDEX);
            mCurrentStep = mSteps.get(mStepIndex);

            String thumbnailURL = mCurrentStep.getThumbnailURL();
            isTabletView = getArguments().getBoolean(RecipeDetailFragment.ARG_IS_TABLET_VIEW, true);
            shouldDisplayVideoThumb = TextUtils.isEmpty(thumbnailURL) && ImageUtils.isImage(thumbnailURL);
        }

        if(savedInstanceState != null){
            resumePosition = savedInstanceState.getLong(RESUME_POSITION_KEY);
            resumeWindow = savedInstanceState.getInt(RESUME_WINDOW_KEY);
            isTabletView = savedInstanceState.getBoolean(RecipeDetailFragment.ARG_IS_TABLET_VIEW, true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        boolean displayVideoFullscreen =  !TextUtils.isEmpty(mCurrentStep.getVideoURL())
                && !isTabletView
                && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        View rootView =  displayVideoFullscreen ?
                inflater.inflate(R.layout.fragment_fullscreen_step_video, container, false)
                 : inflater.inflate(R.layout.fragment_step, container, false);

        ButterKnife.bind(this, rootView);
        if(displayVideoFullscreen){
            displayVideoThumb(mCurrentStep.getThumbnailURL());
        }else {
            displayStepData();
        }


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer !=null ) releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mExoPlayer == null && stepHasVideo()) {
            createExoPlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(RESUME_POSITION_KEY, resumePosition);
        outState.putInt(RESUME_WINDOW_KEY, resumeWindow);
        outState.putBoolean(RecipeDetailFragment.ARG_IS_TABLET_VIEW, isTabletView);


    }


    public interface OnStepFragmentListener{
        void onArrowClicked(List<Step> steps, int stepIndex);
    }

    private void displayStepData(){
        final String stepDescription = mCurrentStep.getDescription();
        final String stepShortDescription = mCurrentStep.getShortDescription();
        final String thumbnailURL = mCurrentStep.getThumbnailURL();
        final int firstStepIndex = 0;
        final int lastStepIndex = mSteps.size()-1;

        mTvLongDescription.setText(stepDescription);
        mTvShortDescription.setText(stepShortDescription);

        if(mStepIndex > firstStepIndex){
            mLeftArrow.setVisibility(View.VISIBLE);
            mLeftArrow.setOnClickListener(arrowClickListener);

        }

        if(mStepIndex < lastStepIndex){
            mRightArrow.setVisibility(View.VISIBLE);
            mRightArrow.setOnClickListener(arrowClickListener);
        }

        if(shouldDisplayVideoThumb) displayVideoThumb(thumbnailURL);


    }

    private View.OnClickListener arrowClickListener= new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newStepIndex = 0;

                switch (view.getId()){
                    case R.id.right_arrow:
                        newStepIndex = ++mStepIndex;
                        break;
                    case R.id.left_arrow:
                        newStepIndex = --mStepIndex;
                        break;
                }

                Activity act = getActivity();
                if(act instanceof OnStepFragmentListener){
                    ((OnStepFragmentListener) act).onArrowClicked(mSteps, newStepIndex);
                }

                if(act instanceof RecipeDetailActivity) {
                    RecipeDetailActivity recipeDetailActivity = (RecipeDetailActivity) act;
                    RecipeDetailFragment fragment = (RecipeDetailFragment) recipeDetailActivity.getSupportFragmentManager().findFragmentById(R.id.recipe_detail_container);
//                    if (fragment != null)
                        fragment.selectStep(newStepIndex);
                }
            }
        };


    private void createExoPlayer(){
        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        mExoPlayer =
                ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);


        //3. Attaching the player
        mExoPlayerView.setPlayer(mExoPlayer);
        mExoPlayerView.setVisibility(View.VISIBLE);

        //4. Prepare the player
        prepareVideoPlayer();


        //5. Play
//        mExoPlayer.setPlayWhenReady(true);

    }

    private void prepareVideoPlayer(){
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
    // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), "yourApplicationName"), bandwidthMeter);


        boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;
        if (haveResumePosition) {
            mExoPlayer.seekTo(resumeWindow, resumePosition);
        }

    // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(mCurrentStep.getVideoURL()));

        // Prepare the player with the source.
        mExoPlayer.prepare(videoSource);
    }

    private void displayVideoThumb(String thumbUrl){
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_recipe);
        requestOptions.error(R.drawable.ic_recipe);

        Glide
                .with(getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(thumbUrl)
                .into(mVideoThumb);

        mVideoThumb.setVisibility(View.VISIBLE);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            updateResumePosition();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void updateResumePosition() {
        resumeWindow = mExoPlayer.getCurrentWindowIndex();
        resumePosition = mExoPlayer.isCurrentWindowSeekable() ? Math.max(0, mExoPlayer.getCurrentPosition())
                : C.TIME_UNSET;
    }

    private boolean stepHasVideo(){
        String videoURL = mCurrentStep.getVideoURL();
        return (videoURL!= null) && !videoURL.isEmpty();
    }

}
