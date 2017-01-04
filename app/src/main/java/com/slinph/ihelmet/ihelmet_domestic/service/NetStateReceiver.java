package com.slinph.ihelmet.ihelmet_domestic.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/9/13.
 */
/*public class NetStateReceiver {
}*/
/**
 *     网络连接状态改变的广播
 */
public class NetStateReceiver extends BroadcastReceiver {
    public static final String ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (ACTION.equals(intent.getAction())) {
            //获取手机的连接服务管理器，这里是连接管理器类
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    Toast.makeText(context, "连接到wifi网络", Toast.LENGTH_SHORT).show();
                    //LoadData.getPhotos(context);
                    //LoadData.isReportBack(context,"1");
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    Toast.makeText(context, "连接到运营商网络", Toast.LENGTH_SHORT).show();
                    //LoadData.getPhotos(context);
                    //LoadData.isReportBack(context,"1");
                }
            } else {
                // not connected to the internet
                Toast.makeText(context, "无网络连接", Toast.LENGTH_SHORT).show();
            }
        }
    }
}