package com.slinph.ihelmet.ihelmet_domestic.application;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.slinph.ihelmet.ihelmet_domestic.activity.MainActivity;
import com.slinph.ihelmet.ihelmet_domestic.bluetooth.BlueToothManager;
import com.uuzuche.lib_zxing.ZApplication;

import java.util.List;


/**
 * Created by hugh on 2016/6/4.
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
    private static SharedPreferences.Editor editor;
    private static SharedPreferences datas;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context=getApplicationContext();
        mBlueToothManager = new BlueToothManager(context);
        //初始化Fresco
        Fresco.initialize(this);

        datas = getSharedPreferences("Datas", MODE_PRIVATE);
        editor = datas.edit();
    }

    public static void saveStringData(String key,String value){
        editor.putString(key,value);
        editor.commit();
    }

    public static void saveBooleanData(String key,Boolean value){
        editor.putBoolean(key,value);
        editor.commit();
    }

    public static void saveIntData(String key,int value){
        editor.putInt(key,value);
        editor.commit();
    }

    public static String getStringData(String key,String defValue){
        String stringData = null;
        if (datas != null){
            stringData =  datas.getString(key, defValue);
        }
        return stringData;
    }

    public static Boolean getBooleanData(String key,Boolean defValue){
        Boolean booleanData = null;
        if (datas != null) {
            booleanData = datas.getBoolean(key, defValue);
        }
        return booleanData;
    }

    public static int getIntData(String key,int defValue){
        int intData = -1;
        if (datas != null){
            intData = datas.getInt(key, defValue);
        }
        return intData;
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
