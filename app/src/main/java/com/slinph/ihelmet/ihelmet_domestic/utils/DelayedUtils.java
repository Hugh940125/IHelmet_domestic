package com.slinph.ihelmet.ihelmet_domestic.utils;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;

/**
 * Created by waitinghc on 2015/7/6.
 * 延时工具类，解决有些页面业务处理完后瞬间跳转，dialog显示后瞬间关闭造成的体验不好问题
 */
public class DelayedUtils {


    private static CountDownTimer time;
    private static int COUNT_DOWN_INTERVAL = 1000;//开始倒计时 默认时间间隔
    private static int DEFAULT_ACTIVITY_FINISH_TIME = 1000;//activity 默认延时finish时间
    private static int DEFAULT_DIALOG_DISMISS_TIME = 600;//dialog 默认延时关闭时间


    /**
     * 延时finish Activity 默认延时1s
     *
     * @param context
     */
    public static void activityDelayedFinish(final Context context) {
        time = new CountDownTimer(DEFAULT_ACTIVITY_FINISH_TIME, COUNT_DOWN_INTERVAL) {

            /**
             * Callback fired on regular interval.
             *
             * @param millisUntilFinished The amount of time until finished.
             */
            @Override
            public void onTick(long millisUntilFinished) {

            }

            /**
             * Callback fired when the time is up.
             */
            @Override
            public void onFinish() {
                ((Activity) context).finish();
                time.cancel();
            }
        };
        time.start();
    }

    /**
     * dialog 延时dismiss 默认延时1s
     *
     * @param dialog
     */
//    public static void dialogDelayedDismiss(final Dialog dialog) {
//        time = new CountDownTimer(DEFAULT_DIALOG_DISMISS_TIME, COUNT_DOWN_INTERVAL) {
//            /**
//             * Callback fired on regular interval.
//             *
//             * @param millisUntilFinished The amount of time until finished.
//             */
//            @Override
//            public void onTick(long millisUntilFinished) {
//
//            }
//
//            /**
//             * Callback fired when the time is up.
//             */
//            @Override
//            public void onFinish() {
//                    dialog.dismiss();
//            }
//        };
//        time.start();
//    }
}
