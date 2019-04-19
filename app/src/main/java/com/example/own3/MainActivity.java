package com.example.own3;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hdl.logcatdialog.LogcatDialog;

import java.util.ArrayList;


public class MainActivity extends Activity {
    Button bind, unbind, getServiceStatus;
    Boolean Binding = false;
    // 保持所启动的Service的IBinder对象
    BindService.MyBinder binder;
    // 定义一个ServiceConnection对象
    private ServiceConnection conn = new ServiceConnection(){
        // 当该Activity与Service连接成功时回调该方法
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            System.out.println("~~~~~Connected~~~~~");
            Log.d("services","Connected");
            //获取Service的onBind方法返回的MyBinder对象
            binder = (BindService.MyBinder) iBinder;
        }
        // 当该Activity与Service断开连接时回调该方法
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("services", "Disconnected");
        }

    };
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new LogcatDialog(this).show();
        // 获取程序界面中的start、stop、getServiceStatus按钮
        bind = (Button) findViewById(R.id.btn_1);
        unbind = (Button) findViewById(R.id.btn_2);
        getServiceStatus = (Button) findViewById(R.id.btn_3);
        //创建启动Service的Intent
        final Intent intent = new Intent(MainActivity.this, BindService.class);
        //为Intent设置Action属性

        /*ActivityManager myManager = (ActivityManager) MainActivity.this.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(ServiceName)) {
                return true;
            }
        }*/

        bind.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View source){
                //绑定指定Serivce
                bindService(intent, conn, BIND_AUTO_CREATE);
                Binding = true;
            }
        });
        unbind.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View source){
                //解除绑定Serivce
                if (Binding) {
                    unbindService(conn);
                    Binding = false;
                }
            }
        });
        getServiceStatus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View source){
                // 获取、并显示Service的count值
                Toast.makeText(MainActivity.this
                        , "Serivce的count值为：" + binder.getCount()
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }
}

