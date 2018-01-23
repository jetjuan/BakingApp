package com.juantorres.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.juantorres.bakingapp.R;
import com.juantorres.bakingapp.RecipeDetailActivity;
import com.juantorres.bakingapp.RecipeListActivity;
import com.juantorres.bakingapp.data.Recipe;
import com.juantorres.bakingapp.utils.DownloadUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by juantorres on 1/16/18.
 */

public class RecipeAppWidgetProvider extends AppWidgetProvider implements Callback<List<Recipe>> {
    private List<Recipe> mRecipes;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //TODO Downloading JSON here
        DownloadUtils downloadUtils = new DownloadUtils();
        downloadUtils.downloadRecipesJSON(context, this);


        final int N = appWidgetIds.length;

        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe_list);
//            views.setOnClickPendingIntent(R.layout.widget_recipe_list, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }


    }


    @Override
    public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
        mRecipes=  response.body();
//                data = new ArrayList<>(Arrays.asList(recipes));
        Log.d("Info", "Recipes downloaded successfully.");
    }

    @Override
    public void onFailure(Call<List<Recipe>> call, Throwable t) {
        //TODO display an error message and allow a retry method
    }
}
