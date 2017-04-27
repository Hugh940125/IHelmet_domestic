package com.slinph.ihairhelmet4.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//采用MD5单次对密码进行不可逆加密
public class Md5PasswordEncrypt {
	public static String encrypt(String msg){
		//获取加密算法
		MessageDigest digest=null;
		try {
			digest= MessageDigest.getInstance("md5");
			byte[] bytes = digest.digest(msg.getBytes());
			StringBuilder builder = new StringBuilder();
			// byte数组转换成字符串
			for(byte b:bytes){
				int v = b & 255;
				String str = Integer.toHexString(v);
				if (str.length() == 1)
					builder.append("0");
				builder.append(str);
			}
			return builder.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		//返回NULL加密失败
	}

}
