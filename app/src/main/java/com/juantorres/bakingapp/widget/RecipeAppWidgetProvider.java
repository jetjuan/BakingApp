package com.juantorres.bakingapp.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
import android.widget.RemoteViews;

import com.juantorres.bakingapp.R;
import com.juantorres.bakingapp.RecipeDetailActivity;
import com.juantorres.bakingapp.data.Recipe;

import org.parceler.Parcels;

import java.util.ArrayList;


/**
 * Created by juantorres on 1/16/18.
 */

public class RecipeAppWidgetProvider extends AppWidgetProvider{

    public final static String ACTION_APPWIDGET_UPDATE = "ACTION_APPWIDGET_UPDATE";
    public final static String WIDGET_RECIPE_EXTRA = "WIDGET_RECIPE_EXTRA";
    public final static String WIDGET_RECIPE_ID_EXTRA = "WIDGET_RECIPE_ID_EXTRA";



    Recipe lastVisitedRecipe;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipes_layout);

            Intent remoteAdapterIntent = new Intent(context, RecipeAppWidgetService.class);
            if (lastVisitedRecipe != null){
                remoteAdapterIntent.putExtra(WIDGET_RECIPE_ID_EXTRA, lastVisitedRecipe.getId());
                remoteAdapterIntent.setData(Uri.parse(remoteAdapterIntent.toUri(Intent.URI_INTENT_SCHEME)));
            }

            views.setRemoteAdapter(R.id.widget_recipes_list_view, remoteAdapterIntent);
            views.setEmptyView(R.id.widget_recipes_list_view, R.id.widget_empty_view);

//            Intent clickIntentTemplate = new Intent(context, RecipeDetailActivity.class);
//            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
//                    .addNextIntentWithParentStack(clickIntentTemplate)
//                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//            views.setPendingIntentTemplate(R.id.widget_recipes_list_view, clickPendingIntentTemplate);

//            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.widget_recipes_list_view);
            appWidgetManager.updateAppWidget(appWidgetId, views);



        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        lastVisitedRecipe = Parcels.unwrap(
                intent.getParcelableExtra(WIDGET_RECIPE_EXTRA)
        );
        int[] appWidgetIDs = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        boolean shouldUpdateWidget = intent.getAction().equals(ACTION_APPWIDGET_UPDATE)
                && (appWidgetIDs != null);
        if (shouldUpdateWidget){
            this.onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIDs);
        }
    }
}
