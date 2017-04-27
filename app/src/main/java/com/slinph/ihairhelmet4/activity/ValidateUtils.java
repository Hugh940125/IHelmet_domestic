package com.slinph.ihairhelmet4.activity;

import android.text.TextUtils;

/**
 * Created by Waiting on 2015/8/22.
 * 验证工具类
 */
public class ValidateUtils {

    /**
     * 验证手机号
     *
     * @param tel
     * @return
     */
    public static boolean validateMobile(String tel) {
        String telRegex = "[1][3758]\\d{9}";//;

        if (TextUtils.isEmpty(tel))
            return false;
        else
            return tel.matches(telRegex);
    }
    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean validataEmail(String email){
        String emailRegex="[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
        if (TextUtils.isEmpty(email))
            return false;
        else
            return email.matches(emailRegex);
    }
}
