package com.juantorres.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.juantorres.bakingapp.data.Recipe;
import com.juantorres.bakingapp.data.json.RequestInterface;

import org.parceler.Parcels;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    public final static String EXTRA_RECIPE = "EXTRA_RECIPE";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        View recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

    }

    private void loadJSON(final RecyclerView recyclerView){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.recipe_api_domain))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final RequestInterface request = retrofit.create(RequestInterface.class);
        Call<List<Recipe>> call = request.getJSON();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> recipes=  response.body();
//                data = new ArrayList<>(Arrays.asList(recipes));
                recyclerView.setAdapter(new RecipeRecyclerViewAdapter(recipes));
                Log.d("Info", "Recipes downloaded successfully.");
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
//        recyclerView.setAdapter(new RecipeRecyclerViewAdapter(DummyContent.ITEMS));
        //TODO: check if internet connection
        if (true) {
            loadJSON(recyclerView);
        }
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
                        //.centerCrop() TODO find out why this doesn't work
                        .into(holder.mIvRecipeImage);
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, RecipeDetailActivity.class);
                    intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, holder.mItem.getIdAsString());
                    intent.putExtra(EXTRA_RECIPE, Parcels.wrap(holder.mItem));
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
                mTvRecipeName = (TextView) view.findViewById(R.id.tv_recipe_name);
                mTvServingsCount = (TextView) view.findViewById(R.id.tv_recipe_servings_amount);
                mIvRecipeImage = (ImageView) view.findViewById(R.id.iv_recipe_picture);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTvRecipeName.getText() + "'";
            }
        }
    }
}
