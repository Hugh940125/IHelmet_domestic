package com.slinph.ihelmet.ihelmet_domestic.internet.model;


import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.ResultData;

public class Constant {
	
	public static <T> ResultData<T> noLogin(ResultData<T> resultData) {
		resultData.setCode(444);
		resultData.setSuccess(false);
		resultData.setMsg("未登录");
		return resultData;
	}
	
}
