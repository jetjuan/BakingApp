package com.juantorres.bakingapp.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.juantorres.bakingapp.R;
import com.juantorres.bakingapp.RecipeDetailActivity;
import com.juantorres.bakingapp.RecipeListActivity;
import com.juantorres.bakingapp.data.Recipe;

import java.util.List;

/**
 * Created by juantorres on 1/16/18.
 */

public class RecipeAppWidgetProvider extends AppWidgetProvider{
    private List<Recipe> mRecipes;
    public static final String RECIPES_KEY = "RECIPES_KEY";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipes_layout);

            Intent remoteAdapterIntent = new Intent(context, RecipeAppWidgetService.class);
            views.setRemoteAdapter(R.id.widget_recipes_list_view, remoteAdapterIntent);
//            views.setEmptyView(R.id.widget_recipes_container, R.id.widget_empty_view);

            Intent clickIntentTemplate = new Intent(context, RecipeListActivity.class);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//            views.setPendingIntentTemplate(R.id.widget_recipes_list_view, clickPendingIntentTemplate);

            //Todo deleteme
            PendingIntent test = PendingIntent.getActivity(context, 0, new Intent(context, RecipeListActivity.class), 0);
            views.setOnClickPendingIntent(R.id.widget_title, test);

//            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.widget_recipes_list_view);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }


}
