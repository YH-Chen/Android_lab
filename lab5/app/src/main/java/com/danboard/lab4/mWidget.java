package com.danboard.lab4;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class mWidget extends AppWidgetProvider {

    private static final int RESULT_OK = 1;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews updateViews = new RemoteViews(context.getPackageName(),R.layout.m_widget);//实例化RemoteView,其对应相应的Widget布局

        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        updateViews.setOnClickPendingIntent(R.id.widget, pi);//给RemoteView上的Button设置按钮事件

        ComponentName me = new ComponentName(context, mWidget.class);
        appWidgetManager.updateAppWidget(me, updateViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if(intent.getAction().equals("STATIC_ACTION")) {
            Bundle bundle = intent.getExtras();

            Intent i = new Intent(context, GoodsInformation.class);
//            i.addCategory(Intent.CATEGORY_DEFAULT);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            i.putExtras(intent.getExtras());

            RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.m_widget);
            PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            updateViews.setOnClickPendingIntent(R.id.widget, pi);//给RemoteView上的Button设置按钮事件
            updateViews.setTextViewText(R.id.appwidget_text, bundle.getString("name")+"仅售"+bundle.getString("price")+"!");
            updateViews.setImageViewResource(R.id.appwidget_icon, bundle.getInt("icon"));

            ComponentName me = new ComponentName(context, mWidget.class);

            //获取AppWidgetManager实例
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(me, updateViews);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
