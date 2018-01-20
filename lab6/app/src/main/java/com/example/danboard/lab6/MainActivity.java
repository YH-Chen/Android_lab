package com.example.danboard.lab6;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {

    private IBinder mBinder;
    private ServiceConnection sc;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private static boolean hasPermission = false;
    private boolean rotate = false;
    private int degree = 0;
    private SimpleDateFormat mFormat = new SimpleDateFormat("mm:ss");
    private ObjectAnimator animator;
    private int position;
    /**
     *动态文件读取权限申请
     */
    public static void verifyStoragePermissions(Activity activity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有读取权限，申请权限弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            } else {
                hasPermission = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);

        final ImageView mImage = (ImageView)findViewById(R.id.image);
        final TextView mState = (TextView)findViewById(R.id.state);
        final TextView mCurrentTime = (TextView)findViewById(R.id.current_time);
        final SeekBar mSeekBar = (SeekBar)findViewById(R.id.seekBar);
        final TextView mTotalTime = (TextView)findViewById(R.id.total_time);
        final Button mPlay = (Button)findViewById(R.id.play);
        final Button mStop = (Button)findViewById(R.id.stop);
        final Button mQuit = (Button)findViewById(R.id.quit);

//        float[] degree = {0.0F, 359.9F};
//        animator = ObjectAnimator.ofFloat(mImage, "rotation", degree);
//        LinearInterpolator lin = new LinearInterpolator();  //匀速
//        animator.setInterpolator(lin);
//        animator.setRepeatCount(-1);
//        animator.setDuration(20000);
//        animator.setRepeatMode((int)1);

        // ServiceConnection实例化
        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d("Hello", "onServiceConnected");
                mBinder = service;
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d("Hello", "onServiceDisconnected");
                sc = null;
            }
        };
        final Intent intent = new Intent(this,MusicService.class);
        startService(intent);  //开启服务
        //绑定activity和服务
        bindService(intent, sc, Context.BIND_AUTO_CREATE);

//        final Parcel data = Parcel.obtain();
//        final Parcel reply = Parcel.obtain();

        // 处理信息
        @SuppressLint("HandlerLeak")
        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 123:
                        try {
                            int code = 104;
                            int flag;
                            Parcel data = Parcel.obtain();
                            Parcel reply = Parcel.obtain();
                            if(mBinder != null) {
                                mBinder.transact(code, data, reply, 0);
                                Bundle bundle = reply.readBundle(getClass().getClassLoader());
                                mCurrentTime.setText(mFormat.format(bundle.getInt("current")));
                                mTotalTime.setText(mFormat.format(bundle.getInt("total")));
                                mSeekBar.setProgress(bundle.getInt("current"));
                                mSeekBar.setMax(bundle.getInt("total"));
                                flag = bundle.getInt("state");
                                if (flag == 0) {
                                    mState.setText("Paused");
                                    mPlay.setText("PLAY");
                                    rotate = false;
//                                animator.pause();
                                } else if (flag == 1) {
                                    mState.setText("Playing");
                                    mPlay.setText("PAUSED");
                                    rotate = true;
//                                animator.start();
                                } else if (flag == -1) {
                                    mState.setText("Stopped");
                                    mPlay.setText("PLAY");
                                    mImage.setRotation(0);
                                    rotate = false;
//                                animator.resume();
//                                animator.pause();
                                } else {
                                    mState.setText("");
                                }
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                        if (rotate) {
                            degree += 1;
                            mImage.setRotation(degree);
                        }
                        break;
                }
            }
        };
        // 新建线程（负责耗时工作）
        new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (sc != null && hasPermission) {
                        // 构造消息并发送
                        mHandler.obtainMessage(123).sendToTarget();
                    }
                } while (true);
            }
        }).start();

        //播放
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int code = 101;
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    mBinder.transact(code, data, reply, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if(mState.getText() == "Playing") {
                    mPlay.setText("PLAY");
                    rotate = false;
//                    animator.pause();
                } else {
                    mPlay.setText("PAUSED");
                    rotate = true;
//                    animator.start();
                }
            }
        });
        //停止
        mStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int code = 102;
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    mBinder.transact(code, data, reply, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                mSeekBar.setProgress(0);
                degree = 0;
                mImage.setRotation(degree);
                rotate = false;
//                animator.resume();
//                animator.pause();
            }
        });
        //退出
        mQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(sc);
                sc = null;
                try {
                    MainActivity.this.finish();
                    int code = 103;
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    mBinder.transact(code, data, reply, 0);
                    System.exit(0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        //拖动进度条
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                try {
                    if(fromUser) {
                        int code = 105;
                        Parcel data = Parcel.obtain();
                        Parcel reply = Parcel.obtain();
                        data.writeInt(seekBar.getProgress());
                        mBinder.transact(code, data, reply, 0);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unbindService(sc);
            MainActivity.this.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // 同意权限
//            try {
//                int code = 106;
//                Parcel data = Parcel.obtain();
//                Parcel reply = Parcel.obtain();
//                mBinder.transact(code, data, reply, 0);
//                hasPermission = true;
//            } catch (RemoteException e) {
//                    e.printStackTrace();
//            }
            Log.d("Hello", "onRequestPermissionsResult");
        } else {
            // 拒绝权限
            System.exit(0);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
