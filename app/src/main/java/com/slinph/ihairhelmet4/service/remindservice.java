package com.slinph.ihairhelmet4.service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by hs on 2016/7/10.
 */
public class remindservice extends Service {

    private int hour_set_shp;
    private int minute_set_shp;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //提高服务的优先级
        Notification notification = new Notification();
        startForeground(0,notification);
        notification.flags =Notification.FLAG_AUTO_CANCEL;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
        SharedPreferences remind_or_mot = getSharedPreferences("Remind_or_mot", MODE_PRIVATE);

        int set_hour = 0;
        int set_minute = 0;
        Calendar calendar = Calendar.getInstance();
        if (intent != null){
            set_hour =  intent.getIntExtra("set_hour",0);
            set_minute = intent.getIntExtra("set_minute", 0);
        }
        if (set_hour == 0 && set_minute == 0){
            hour_set_shp = remind_or_mot.getInt("hour_24", 21);
            minute_set_shp = remind_or_mot.getInt("minute", 30);
        }else {
            hour_set_shp = set_hour;
            minute_set_shp = set_minute;
        }
        Log.e("服务获取的设定时间",hour_set_shp + ":" + minute_set_shp);


        //calendar.setTimeInMillis(System.currentTimeMillis());
/*        int HOUR_OF_DAY = c.get(Calendar.HOUR_OF_DAY);
        int MINUTE = c.get(Calendar.MINUTE);*/

        calendar.set(Calendar.HOUR_OF_DAY, hour_set_shp);
        calendar.set(Calendar.MINUTE, minute_set_shp);

        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND , 0);
       /* 设定的时间与当前时间对比*/
        if (calendar.getTimeInMillis() < System.currentTimeMillis()){
            calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR) + 1);
        }

        Intent intent_pi = new Intent(this,AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,
                0, intent_pi, PendingIntent.FLAG_CANCEL_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), (48*1000*3600), pi);//重复设置:每48小时提醒一次
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //在Service结束后关闭AlarmManager
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.cancel(pi);
    }
}
