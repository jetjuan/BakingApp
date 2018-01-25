package com.juantorres.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.juantorres.bakingapp.R;
import com.juantorres.bakingapp.data.Recipe;

import java.util.ArrayList;
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
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe_list);

            Intent intent = new Intent(context, RecipeAppWidgetService.class);
            views.setRemoteAdapter(R.id.widget_recipes_container, intent);
//            views.setEmptyView(R.id.widget_recipes_container, R.id.widget_empty_view);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }


}
