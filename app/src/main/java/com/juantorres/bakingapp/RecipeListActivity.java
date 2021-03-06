package com.juantorres.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.juantorres.bakingapp.data.Recipe;
import com.juantorres.bakingapp.utils.DownloadUtils;
import com.juantorres.bakingapp.widget.RecipeAppWidgetProvider;

import org.parceler.Parcels;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity implements Callback<List<Recipe>>{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    public final static String EXTRA_RECIPE = "EXTRA_RECIPE";
    private RecyclerView recyclerView;
    private LinearLayout mErrorMessage;
    private ProgressBar mLoadingIndicator;
    private Button mRetryButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        mErrorMessage = findViewById(R.id.network_error_message);
        mLoadingIndicator = findViewById(R.id.loading_indicator);
        mRetryButton = findViewById(R.id.network_retry_button);

        recyclerView = findViewById(R.id.recipes);
        assert recyclerView != null;
        setupRecyclerView();

    }

    @Override
    public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
        List<Recipe> recipes=  response.body();
//                data = new ArrayList<>(Arrays.asList(recipes));
        recyclerView.setAdapter(new RecipeListActivity.RecipeRecyclerViewAdapter(recipes));
        showRecyclerView();
        Log.d("Info", "Recipes downloaded successfully.");
    }

    @Override
    public void onFailure(Call<List<Recipe>> call, Throwable t) {
        showErrorMessage();
    }


    private void setupRecyclerView() {
        showLoadingIndicator();
        DownloadUtils downloader = new DownloadUtils();
        downloader.downloadRecipesJSON(this, this);
    }

    private void showLoadingIndicator(){
        mLoadingIndicator.setVisibility(View.VISIBLE);

        mErrorMessage.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

    }

    private void showErrorMessage(){
        mErrorMessage.setVisibility(View.VISIBLE);

        recyclerView.setVisibility(View.GONE);
        mLoadingIndicator.setVisibility(View.GONE);

        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupRecyclerView();
            }
        });

    }

    private void showRecyclerView(){
        recyclerView.setVisibility(View.VISIBLE);

        mLoadingIndicator.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.GONE);
    }


    public void displayRecipeOnWidget(Parcelable recipe){
        Intent intent = new Intent(this, RecipeAppWidgetProvider.class);
        intent.setAction(RecipeAppWidgetProvider.ACTION_APPWIDGET_UPDATE);
// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
// since it seems the onUpdate() is only fired on that:
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), RecipeAppWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        intent.putExtra(RecipeAppWidgetProvider.WIDGET_RECIPE_EXTRA, recipe);
        sendBroadcast(intent);
    }

    public class RecipeRecyclerViewAdapter
            extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.RecipeViewHolder> {

        private final List<Recipe> mRecipes;

        public RecipeRecyclerViewAdapter(List<Recipe> items) {
            mRecipes = items;
        }

        @Override
        public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_list_content, parent, false);
            return new RecipeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecipeViewHolder holder, int position) {
            holder.mItem = mRecipes.get(position);
            holder.mTvRecipeName.setText(mRecipes.get(position).getName());
            holder.mTvServingsCount.setText( String.valueOf(holder.mItem.getServings()));
            String picUrl = holder.mItem.getImage();
            if(!picUrl.equals("") && picUrl !=null){
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.ic_recipe);
                requestOptions.error(R.drawable.ic_recipe);

                Glide
                        .with(getApplicationContext())
                        .setDefaultRequestOptions(requestOptions)
                        .load(picUrl)
                        .into(holder.mIvRecipeImage);
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Parcelable recipeExtra = Parcels.wrap(holder.mItem);
                    displayRecipeOnWidget(recipeExtra);
                    Context context = v.getContext();
                    Intent intent = new Intent(context, RecipeDetailActivity.class);
                    intent.putExtra(EXTRA_RECIPE, recipeExtra);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mRecipes.size();
        }

        public class RecipeViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mTvRecipeName;
            public final TextView mTvServingsCount;
            public final ImageView mIvRecipeImage;
            public Recipe mItem;

            public RecipeViewHolder(View view) {
                super(view);
                mView = view;
                mTvRecipeName = view.findViewById(R.id.tv_recipe_name);
                mTvServingsCount =  view.findViewById(R.id.tv_recipe_servings_amount);
                mIvRecipeImage = view.findViewById(R.id.iv_recipe_picture);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTvRecipeName.getText() + "'";
            }
        }
    }
}
