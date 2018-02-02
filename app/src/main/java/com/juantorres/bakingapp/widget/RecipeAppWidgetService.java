package com.juantorres.bakingapp.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.juantorres.bakingapp.R;
import com.juantorres.bakingapp.RecipeDetailActivity;
import com.juantorres.bakingapp.RecipeDetailFragment;
import com.juantorres.bakingapp.RecipeListActivity;
import com.juantorres.bakingapp.data.Recipe;
import com.juantorres.bakingapp.utils.DownloadUtils;


import org.parceler.Parcels;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.juantorres.bakingapp.RecipeListActivity.EXTRA_RECIPE;

/**
 * Created by juantorres on 1/23/18.
 */

public class RecipeAppWidgetService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoveViewFactory(this.getApplicationContext(), intent);
    }

    public class RecipeRemoveViewFactory implements RemoteViewsFactory, Callback<List<Recipe>> {
        private Context mContext;
        private List<Recipe> mRecipes;

        RecipeRemoveViewFactory(Context context, Intent intent) {
            mContext = context;
//            mRecipes = Parcels.unwrap(intent.getParcelableExtra(RecipeAppWidgetProvider.RECIPES_KEY));
//            mRecipes = Parcels.unwrap(mRecipes);
        }

        @Override
        public void onCreate() {
            //TODO Implement this ASAP
//            mRecipes = new ArrayList<>();
//            mRecipes.add( new Recipe());
        }

        @Override
        public int getCount() {
            return mRecipes == null ? 0 : mRecipes.size();
        }

        @Override
        public void onDataSetChanged() {
            downloadRecipes();
        }

        private void downloadRecipes(){
            DownloadUtils downloadUtils = new DownloadUtils();
            downloadUtils.downloadRecipesJSON(mContext, this);
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (position == AdapterView.INVALID_POSITION || mRecipes == null
                    || mRecipes.get(position) == null) {
                return null;
            }
            Recipe recipe = mRecipes.get(position);
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_recipe_item);
            rv.setTextViewText(R.id.tv_widget_recipe_name, recipe.getName());

            Intent fillInIntent = new Intent();
//            fillInIntent.putExtra(EXTRA_RECIPE, Parcels.wrap(recipe));
            fillInIntent.putExtra(EXTRA_RECIPE, "Something");

            rv.setOnClickFillInIntent(R.id.widget_recipe_item, fillInIntent);
            return rv;
        }

        @Override
        public long getItemId(int position) {
            Recipe recipe = mRecipes.get(position);
            return recipe == null ? position : recipe.getId();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
            mRecipes =  response.body();
        }

        @Override
        public void onFailure(Call<List<Recipe>> call, Throwable t) {
            //TODO display an error message and allow a retry method
        }
    }


}
