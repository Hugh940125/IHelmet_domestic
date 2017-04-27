package com.slinph.ihairhelmet4.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import com.slinph.ihairhelmet4.R;
import com.slinph.ihairhelmet4.activity.MainActivity;
import com.slinph.ihairhelmet4.activity.WelcomeActivity;
import com.slinph.ihairhelmet4.utils.StaticVariables;

/**
 * Created by hs on 2016/7/10.
 *
 */
public class AlarmReceiver extends BroadcastReceiver {

    private Intent notify_intent;

    @Override
    public void onReceive(Context context, Intent intent) {
        //设置通知内容并在onReceive()这个函数执行时开启
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (StaticVariables.IS_MAINACTIVITY_LUNCHED == false){
            notify_intent = new Intent(context, WelcomeActivity.class);
        }
        else{
            notify_intent = new Intent(context, MainActivity.class);
        }
        notify_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//关键的一步，设置启动模式 | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notify_intent, 0);
        Notification notification=new Notification.Builder(context)
                .setContentTitle("Time reached")
                .setContentText("Treatment time has been reached")
                .setSmallIcon(R.mipmap.remind_logo)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.remind_logo))
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults = Notification.DEFAULT_ALL;
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(1, notification);
    }
}
