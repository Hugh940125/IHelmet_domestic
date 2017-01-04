package cn.aigestudio.datepicker.utils;

/**
 * Created by Administrator on 2016/12/16.
 *
 */
public class DateStaticVeriable {
    public static  boolean IS_TODAY_RECORD = false;

    public static boolean isTodayRecord() {
        return IS_TODAY_RECORD;
    }

    public static void setIsTodayRecord(boolean isTodayRecord) {
        IS_TODAY_RECORD = isTodayRecord;
    }
}
