package com.slinph.ihairhelmet4.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 *
 */
public class StringUtils {
    //将string转为s,t,r,i,n,g的形式
    public static String splice(String str){
        StringBuilder stringBuider = null;
        char[] charArray = str.toCharArray();
        stringBuider = new StringBuilder();
        for(int j=0;j<charArray.length;j++){
            if(j==charArray.length-1){
                stringBuider.append(charArray[j]);
            }else{
                stringBuider.append(charArray[j]+",");
            }
        }
        System.out.println(stringBuider.toString());
        return stringBuider.toString();
    }

    //把map转成list
    public static List<String[]> MapToStringArrayList(HashMap info){
        ArrayList<String[]> arrayList = new ArrayList<>();
        for(Object key:info.keySet()){
            String keyString = (String)key;
            String [] vlaue = new String[]{keyString,(String) info.get(keyString)};
            arrayList.add(vlaue);
        }
        return arrayList;
    }

    //将Map保存到sp
    public static void saveMapToSp(Context context,HashMap<String, String> map) {
        ArrayList<String> strings = new ArrayList<>();
        strings.clear();
        for (String key:map.keySet()){
            strings.add(key);
            strings.add(map.get(key));
        }
        SharePreferencesUtils.putStrListValue(context,"mapList",strings);
    }

    //从sp取出map
    public static HashMap getMapFromSp(Context context){
        List<String> listFromSp = SharePreferencesUtils.getStrListValue(context, "mapList");
        HashMap<String, String> HashMap = new HashMap<>();
        if (listFromSp != null){
            for (int i=0;i<listFromSp.size();i=i+2){
                String s = listFromSp.get(i);
                String s1 = listFromSp.get(i + 1);
                HashMap.put(s,s1);
            }
        }
        return HashMap;
    }
}
