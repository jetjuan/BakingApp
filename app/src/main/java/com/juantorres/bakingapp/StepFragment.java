package com.juantorres.bakingapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    @BindView(R.id.tv_short_description)
    public TextView mTvShortDescription;
    @BindView(R.id.tv_step_long_description)
    public TextView mTvLongDescription;
    @BindView(R.id.left_arrow)
    public View mLeftArrow;
    @BindView(R.id.right_arrow)
    public View mRightArrow;

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
}
