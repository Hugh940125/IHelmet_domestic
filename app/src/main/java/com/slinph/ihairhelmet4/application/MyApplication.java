package com.slinph.ihairhelmet4.application;

import android.app.Activity;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.slinph.ihairhelmet4.activity.MainActivity;
import com.slinph.ihairhelmet4.bluetooth.BlueToothManager;
import com.uuzuche.lib_zxing.ZApplication;

import java.util.List;


/**
 * Created by hugh on 2016/6/4.
 *
 */
public class MyApplication extends ZApplication {
    private static MyApplication instance;
    ///private static CacheManager mCacheManager;//缓存管理
    private String actionName;
    private String mTelephonyService;//手机设备号
   /// private LocationManagerProxy mLocationManagerProxy;
    ///private LocationListener locationListener;
    private List<Activity> mActivitys;

    //成功登录后,需要保存的用户id和户名
    public static String userId;
    public static String userPhone;//手机号
    public static MainActivity mainActivity;

    /**
     * bluetooth管理类
     */
    public static BlueToothManager mBlueToothManager;

    /**
     *      hugh注释掉
     */
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context=getApplicationContext();
        mBlueToothManager = new BlueToothManager(context);
        //初始化Fresco
        Fresco.initialize(this);
    }

    /**
     * 获取Application实例
     * @return
     */
    public static MyApplication getInstance() {
        return instance;
    }

    /**
     * 添加activity到集合
     *
     * @param activity
     */
   /* public void addActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        mActivitys.add(activity);
    }*/

    /**
     * finish所有activity
     */
    public void finishAll() {
        if (mActivitys == null) {
            return;
        }
        for (Activity activity : mActivitys) {
            activity.finish();
        }
    }

    public static Context getContext() {
        return context;
    }

}
