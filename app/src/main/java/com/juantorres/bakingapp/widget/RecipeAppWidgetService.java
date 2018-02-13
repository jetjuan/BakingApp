package com.juantorres.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.juantorres.bakingapp.R;
import com.juantorres.bakingapp.data.Recipe;
import com.juantorres.bakingapp.utils.DownloadUtils;


import org.parceler.Parcels;

import java.util.List;


import static com.juantorres.bakingapp.RecipeListActivity.EXTRA_RECIPE;

/**
 * Created by juantorres on 1/23/18.
 */

public class RecipeAppWidgetService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoveViewFactory(this.getApplicationContext(), intent);
    }

    public class RecipeRemoveViewFactory implements RemoteViewsFactory{
        private Context mContext;
        private List<Recipe> mRecipes;

        RecipeRemoveViewFactory(Context context, Intent intent) {
            mContext = context;
        }

        @Override
        public void onCreate() {

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
//            final Callback<List<Recipe>> callback = this;
//            Thread download = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    DownloadUtils downloadUtils = new DownloadUtils();
//                    mRecipes = downloadUtils.downloadRecipesJSON(mContext);
//                }
//            });
//            download.run();
            DownloadUtils downloadUtils = new DownloadUtils();
            mRecipes = downloadUtils.downloadRecipesJSON(mContext);
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
            fillInIntent.putExtra(EXTRA_RECIPE, Parcels.wrap(recipe));

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
    }


}
