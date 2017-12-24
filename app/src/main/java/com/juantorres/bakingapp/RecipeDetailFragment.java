package com.juantorres.bakingapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juantorres.bakingapp.data.Recipe;
import com.juantorres.bakingapp.data.Step;
import com.juantorres.bakingapp.utils.IngredientsUtil;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.juantorres.bakingapp.RecipeListActivity.EXTRA_RECIPE;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
//    public static final String ARG_STEP = "ARG_STEP";
    public static final String ARG_STEPS = "ARG_STEPS";
    public static final String ARG_STEP_INDEX = "ARG_STEP_INDEX";
    public static final String ARG_IS_TABLET_VIEW = "ARG_IS_TABLET_VIEW";
    public static final String ARG_SELECTED_STEP_POSITION = "ARG_SELECTED_STEP_POSITION";


    /**
     * The dummy content this fragment is presenting.
     */
    private Recipe mItem;
    private boolean mIsTabletView;
    private View mSelectedStep;
    @BindView(R.id.ingredients) public TextView mIngredientsText;
    @BindView(R.id.rv_steps)    public RecyclerView mStepsRecyclerView;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = Parcels.unwrap(getArguments().getParcelable(EXTRA_RECIPE));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getName());
            }
        }

        if (savedInstanceState == null){
            mIsTabletView = !(getActivity().findViewById(R.id.two_pane_separator) == null);

        }else {
            mIsTabletView = savedInstanceState.getBoolean(ARG_IS_TABLET_VIEW);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        ButterKnife.bind(this, rootView);


        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            //Todo Complete this to retrieve
            String ingredientString = IngredientsUtil.getIngredientsStrings(mItem.getIngredients());
            mIngredientsText.setText(
                    Html.fromHtml(ingredientString));
            mStepsRecyclerView.setAdapter( new StepsRecyclerViewAdapter(mItem.getSteps()));
//            ((TextView) rootView.findViewById(R.id.ingredients)).setText(
//                    Html.fromHtml(ingredientString));
        }


        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARG_IS_TABLET_VIEW, mIsTabletView );
//        outState.putInt(ARG_SELECTED_STEP_POSITION, mStepsRecyclerView.getChildLayoutPosition(mSelectedStep));
    }

    public class StepsRecyclerViewAdapter
            extends RecyclerView.Adapter<StepsRecyclerViewAdapter.StepsViewHolder> implements View.OnClickListener {

        private final List<Step> mSteps;

        public StepsRecyclerViewAdapter(List<Step> items) {
            mSteps = items;
        }

        @Override
        public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_list_content, parent, false);
            return new StepsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final StepsViewHolder holder, final int position) {

            holder.mStep = mSteps.get(position);
            String shortDescription = (position == 0) ? holder.mStep.getShortDescription() : (position+" - " + holder.mStep.getShortDescription());
            holder.mTvShortDescription.setText(shortDescription );


            holder.mView.setOnClickListener(this);
            holder.mView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return mSteps.size();
        }

        @Override
        public void onClick(View v) {
            int position = (int)v.getTag();

            if(mIsTabletView){
                //Todo add code in case app is running on Tablet
                selectStep(position);

                ((RecipeDetailActivity) getActivity()).displayStepFragment( mItem.getSteps(), position);
            }else{
                Intent intent = new Intent(getContext(), StepActivity.class);
//                        intent.putExtra(ARG_STEP, Parcels.wrap(holder.mStep) );
                intent.putExtra(ARG_STEPS, Parcels.wrap(mItem.getSteps()));
                intent.putExtra(ARG_STEP_INDEX, position);
                startActivity(intent);
            }
        }

        public class StepsViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mTvShortDescription;
            public Step mStep;

            public StepsViewHolder(View view) {
                super(view);
                mView = view;
                mTvShortDescription = view.findViewById(R.id.tv_step_short_description);

            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTvShortDescription.getText() + "'";
            }
        }
    }

    private void selectStep(int position){
        View newSelectedStep = mStepsRecyclerView.getLayoutManager().getChildAt(position);

        if (mSelectedStep != null){
            mSelectedStep.setBackgroundColor( newSelectedStep.getSolidColor());
        }


        mSelectedStep = newSelectedStep;
        mSelectedStep.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }


}
