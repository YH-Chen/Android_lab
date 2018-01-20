package com.danboard.lab4;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

/**
 * Created by Danboard on 2017/10/30.
 */

public class DynamicReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("DYNAMIC_ACTION")) {
            Bundle bundle = intent.getExtras();

            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), bundle.getInt("icon"));

            //获取状态通知栏管理
            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            //实例化通知栏构造器
            //Notification.Builder builder = new Notification.Builder(context);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            //对builder进行配置
            /*
              设置通知栏标题
              设置通知栏显示内容
              通知首次出现在通知栏,带上升动画效果的
              设置大ICON
              设置小icon
              通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
              设置这个标志当用户单击面板就可以将通知取消
             */
            builder.setContentTitle("马上下单")
                    .setContentText(bundle.getString("name")+"已添加到购物车")
                    .setTicker("您有一条新消息")
                    .setLargeIcon(bitmap)
                    .setSmallIcon(bundle.getInt("icon"))
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true);
            //绑定intent，点击图标能够进入购物车列表（主活动）
            Intent mIntent = new Intent(context, MainActivity.class);
            mIntent.putExtra("shoppingList", "run");
            PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(mPendingIntent);
            //绑定Notification，发送通知请求
            Notification notify = builder.build();
            manager.notify(1, notify);
//            //使用时间标记通知，显示多条通知
//            manager.notify((int)System.currentTimeMillis(),notify);

            RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.m_widget);
            mIntent.addCategory(Intent.CATEGORY_DEFAULT);
            PendingIntent pi = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            updateViews.setTextViewText(R.id.appwidget_text, bundle.getString("name")+"已添加到购物车");
            updateViews.setImageViewResource(R.id.appwidget_icon, bundle.getInt("icon"));
            updateViews.setOnClickPendingIntent(R.id.widget, pi);//给RemoteView上的Button设置按钮事件

            ComponentName me = new ComponentName(context, mWidget.class);
            //获取AppWidgetManager实例
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(me, updateViews);
        }
    }
}