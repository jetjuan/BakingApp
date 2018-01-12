package com.juantorres.bakingapp;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.os.Parcel;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.juantorres.bakingapp.data.Step;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity implements StepFragment.OnStepFragmentListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (savedInstanceState == null) {
            createStepFragment();
        }



    }

    private void createStepFragment(){
        Bundle arguments = new Bundle();
        arguments.putParcelable(RecipeDetailFragment.ARG_STEPS, getIntent().getParcelableExtra(RecipeDetailFragment.ARG_STEPS));
        arguments.putInt(RecipeDetailFragment.ARG_STEP_INDEX, getIntent().getIntExtra(RecipeDetailFragment.ARG_STEP_INDEX, 0));
        arguments.putBoolean(RecipeDetailFragment.ARG_IS_TABLET_VIEW, getIntent().getBooleanExtra(RecipeDetailFragment.ARG_IS_TABLET_VIEW, true));
        StepFragment fragment = new StepFragment();
        fragment.setArguments(arguments);

        FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.step_container, fragment)
                        .commit();

    }

    private void replaceStepFragment(List<Step> steps, int stepIndex){
        Bundle arguments = new Bundle();
        arguments.putParcelable(RecipeDetailFragment.ARG_STEPS, Parcels.wrap(steps));
        arguments.putInt(RecipeDetailFragment.ARG_STEP_INDEX, stepIndex);
        arguments.putBoolean(RecipeDetailFragment.ARG_IS_TABLET_VIEW, getIntent().getBooleanExtra(RecipeDetailFragment.ARG_IS_TABLET_VIEW, true));

        StepFragment fragment = new StepFragment();
        fragment.setArguments(arguments);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.step_container, fragment)
                .commit();
    }


    @Override
    public void onArrowClicked(List<Step> steps, int stepIndex) {
        replaceStepFragment(steps, stepIndex);
    }
}
