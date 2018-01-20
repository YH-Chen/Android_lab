package com.example.danboard.lab3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.NotificationCompat;

/**
 * Created by Danboard on 2017/10/30.
 */

public class StaticReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("STATIC_ACTION")) {
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
            builder.setContentTitle("新商品热卖")
                    .setContentText(bundle.getString("name")+"仅售"+bundle.getString("price"))
                    .setTicker("您有一条新消息")
                    .setLargeIcon(bitmap)
                    .setSmallIcon(bundle.getInt("icon"))
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true);
            //绑定intent，点击图标能够进入商品详情活动
            Intent mInent = new Intent(context, GoodsInformation.class);
            mInent.putExtra("name", bundle.getString("name"));
            PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, mInent, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(mPendingIntent);
            //绑定Notification，发送通知请求
            Notification notify = builder.build();
            manager.notify(0, notify);
        }
    }
}
