package com.juantorres.bakingapp;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TODo setup Step
        Step mCurrentStep = Parcels.unwrap(getIntent().getExtras().getParcelable(RecipeDetailFragment.ARG_STEP));


        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(RecipeDetailFragment.ARG_STEP, getIntent().getParcelableExtra(RecipeDetailFragment.ARG_STEP));
            StepFragment fragment = new StepFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_container, fragment)
                    .commit();
        }

    }


}
