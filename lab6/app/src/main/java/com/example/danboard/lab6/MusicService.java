package com.example.danboard.lab6;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.util.Log;

public class MusicService extends Service {

    private final IBinder binder = new MyBinder();
    private MediaPlayer mp = new MediaPlayer();;
    private int state = 3;

    public MusicService() {
        try {
            //获取音频文件
            mp.setDataSource(Environment.getExternalStorageDirectory() + "/melt.mp3");
            mp.prepare();
            mp.setLooping(true);
            super.onCreate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Hello", "MusicService");
    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("Hello", "onBind");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("Hello", "onUnbind");
        return super.onUnbind(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("Hello", "onCreate");
        String MusicName = "Melt";
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.image);
        Notification.Builder builder =  new Notification.Builder(this);
        builder.setContentTitle(MusicName)
                .setLargeIcon(bitmap)
                .setWhen(getCurrentTime())
                .setAutoCancel(true);
        Intent mIntent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        Notification notify = builder.build();
        startForeground(1, notify);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Hello", "onstart");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mp != null) {
            mp.stop();
            mp.reset();
            mp.release();
        }
        stopSelf();
        super.onDestroy();
    }

    //播放or暂停
    public void play() {
        if (mp.isPlaying()) {
            mp.pause();
            state = 0;
        } else {
            mp.start();
            state = 1;
        }
    }
    //停止
    public void Stop() {
        if (mp != null) {
            mp.stop();
            state = -1;
            try {
                mp.prepare();
                mp.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public int getCurrentTime() {
        return mp.getCurrentPosition();
    }
    public int getDuration() {
        return mp.getDuration();
    }
    public int getState() {
        return state;
    }
    /**
     * 绑定Activity和service
     */
    public class MyBinder extends Binder {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 101:
                    // 播放
                    play();
                    break;
                case 102:
                    // 停止
                    MusicService.this.Stop();
                    break;
                case 103:
                    // 退出
                    onDestroy();
                    break;
                case 104:
                    // 刷新界面
                    Bundle bundle = new Bundle();
                    bundle.putInt("current", getCurrentTime());
                    bundle.putInt("total", MusicService.this.getDuration());
//                    Log.d("CurrentTime", String.valueOf(getCurrentTime()));
                    bundle.putInt("state", getState());
//                    Log.d("State", String.valueOf(getState()));
                    reply.writeBundle(bundle);
                    break;
                case 105:
                    // 拖动进度条
                    int i = data.readInt();
                    Log.d("jumpTime", String.valueOf(i));
                    mp.seekTo(i);
                    break;
//                case 106:
//                    // 获取权限
//                    try {
//                        //获取音频文件
//                        mp.setDataSource(Environment.getExternalStorageDirectory() + "/melt.mp3");
//                        mp.prepare();
//                        mp.setLooping(true);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    break;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }
}
