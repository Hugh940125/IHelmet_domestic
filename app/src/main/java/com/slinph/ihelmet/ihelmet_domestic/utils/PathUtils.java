package com.slinph.ihelmet.ihelmet_domestic.utils;

/**
 * Created by Administrator on 2016/8/10.
 */
public class PathUtils {
    public static String getDateFromPath(String path){
        String[] split = path.split("/");
        String filename = split[split.length - 1];
        String date = filename.substring(0, 6);
        String year = date.substring(0, 2);
        String month = date.substring(2, 4);
        String day = date.substring(4, 6);
        return year+"-"+month+"-"+day;
    }
}
