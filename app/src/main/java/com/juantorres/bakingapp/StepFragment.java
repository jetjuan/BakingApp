package com.juantorres.bakingapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
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

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StepFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StepFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepFragment extends Fragment {
    private Step mCurrentStep;
    private List<Step> mSteps;
    private int mStepIndex;
    private SimpleExoPlayer mExoPlayer;
    @BindView(R.id.tv_short_description)
    public TextView mTvShortDescription;
    @BindView(R.id.tv_step_long_description)
    public TextView mTvLongDescription;
    @BindView(R.id.left_arrow)
    public View mLeftArrow;
    @BindView(R.id.right_arrow)
    public View mRightArrow;
    @BindView(R.id.video_container)
    public SimpleExoPlayerView mExoPlayerView;

    private OnFragmentInteractionListener mListener;

    public StepFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param step Parameter Step.
     * @return A new instance of fragment StepFragment.
     */
    public static StepFragment newInstance(Step step) {
        StepFragment fragment = new StepFragment();
        Bundle args = new Bundle();
        args.putParcelable(RecipeDetailFragment.ARG_STEPS, Parcels.wrap(step));
        args.putParcelable(RecipeDetailFragment.ARG_STEP_INDEX, Parcels.wrap(step));


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSteps = Parcels.unwrap( getArguments().getParcelable(RecipeDetailFragment.ARG_STEPS));
            mStepIndex = getArguments().getInt(RecipeDetailFragment.ARG_STEP_INDEX);
            mCurrentStep = mSteps.get(mStepIndex);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);

        //TODO check this
        ButterKnife.bind(this, rootView);
        displayStepData();

        //TODO DELETE ME
        mTvLongDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createExoPlayer();
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //TODO implements this
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer !=null && mExoPlayer.getPlayWhenReady()) mExoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (mExoPlayer !=null && !mExoPlayer.getPlayWhenReady()) mExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer.release();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public interface OnStepFragmentListener{
        void onArrowClicked(List<Step> steps, int stepIndex);
    }

    private void displayStepData(){
        mTvLongDescription.setText(mCurrentStep.getDescription());
        mTvShortDescription.setText(mCurrentStep.getShortDescription());

        if(mStepIndex > 0){
            mLeftArrow.setVisibility(View.VISIBLE);
            mLeftArrow.setOnClickListener(arrowClickListener);

        }

        if(mStepIndex < mSteps.size()-1){
            mRightArrow.setVisibility(View.VISIBLE);
            mRightArrow.setOnClickListener(arrowClickListener);
        }

        if ((mCurrentStep != null) && !mCurrentStep.getVideoURL().isEmpty()) {
            createExoPlayer();
        }else {
            mExoPlayerView.setVisibility(View.GONE);
        }

        //TODO implement video thumb
//        String thumbnailURL  = mCurrentStep.getThumbnailURL();
//        if(!thumbnailURL.equals("") && thumbnailURL !=null){
//            RequestOptions requestOptions = new RequestOptions();
//            requestOptions.placeholder(R.drawable.ic_recipe);
//            requestOptions.error(R.drawable.ic_recipe);
//
//            Glide
//                    .with(getContext())
//                    .setDefaultRequestOptions(requestOptions)
//                    .load(thumbnailURL)
//                    //.centerCrop() TODO find out why this doesn't work
//                    .into(mExoPlayerView);
//        }


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
    // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(Uri.parse("https://www.w3schools.com/html/mov_bbb.mp4"));
                .createMediaSource(Uri.parse(mCurrentStep.getVideoURL()));

        // Prepare the player with the source.
        mExoPlayer.prepare(videoSource);
    }
}
