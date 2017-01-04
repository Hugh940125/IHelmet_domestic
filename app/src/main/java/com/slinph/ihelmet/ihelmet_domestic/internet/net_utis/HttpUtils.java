package com.slinph.ihelmet.ihelmet_domestic.internet.net_utis;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.JavaType;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import cz.msebera.android.httpclient.Header;


/**
 * Created by hugh on 2016/4/17.
 */
public class HttpUtils {

    private static String sessionId = null;
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static PersistentCookieStore cookieStore;

    private final static int JSON_EXCEPTION = -1;

    static {
        //设置网络超时时间
        client.setTimeout(30 * 1000);
        client.addHeader("token", "123");
    }

    private static TextHttpResponseHandler getHandler(final Context context, final ResultCallback callback) {
        return new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(statusCode + " ----- " + throwable.toString(),"");
                if (callback != null) {
                    callback.onError(statusCode, throwable);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.e(statusCode + " ----- " + responseString,"");
                if (callback != null) {
                    Object obj = JacksonUtils.fromJson(responseString, callback.javaType);
                    if (obj == null) {
                        Log.e("json转化结果为null","");
                        callback.onError(JSON_EXCEPTION, null);
                        return;
                    }
                    try {
                        ResultData rd = (ResultData) obj;
                        /*if (rd.getCode() == 444) {
                            boolean result = UserInfoCache.reLogin();
                            if(!result) {
                                context.startActivity(new Intent(context, LoginRegisterActivity.class));
                            }
                        }*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    callback.onResponse(obj);
                }
            }
        };
    }

    public static void getAsync(Context context, String url, final ResultCallback callback, final String[]... params) {
        if (!NetUtils.isConnectedAndToast(context)) {
            callback.onError(0, null);
            return;
        }
        RequestParams requestParams = new RequestParams();
        for (String[] param : params) {
            requestParams.put(param[0], param[1]);
        }
        client.get(context, url, requestParams, getHandler(context,callback));
    }

    public static void postAsync(Context context, String url, final ResultCallback callback, final String[]... params) {
        if (!NetUtils.isConnectedAndToast(context)) {
            callback.onError(0, null);
            return;
        }
        RequestParams requestParams = new RequestParams();
        for (String[] param : params) {
            requestParams.put(param[0], param[1]);
        }
        client.post(context, url, requestParams, getHandler(context,callback));
    }

    public static void getAsyncByObj(Context context, String url, final ResultCallback callback, final Object obj) {
        if (!NetUtils.isConnectedAndToast(context)) {
            callback.onError(0, null);
            return;
        }
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = ObjToMap.getValueMap(obj);
        for (String key : map.keySet()) {
            requestParams.put(key, map.get(key));
        }
        client.get(context, url, requestParams, getHandler(context,callback));
    }

    public static void postAsyncByObj(Context context, String url, final ResultCallback callback, final Object obj) {
        if (!NetUtils.isConnectedAndToast(context)) {
            callback.onError(0, null);
            return;
        }
        RequestParams requestParams = new RequestParams();
        Map<String, String> map = ObjToMap.getValueMap(obj);
        for (String key : map.keySet()) {
            requestParams.put(key, map.get(key));
        }
        client.post(context, url, requestParams, getHandler(context,callback));
    }

    public static void upload(Context context, String url, String filePath, final ResultCallback callback) {
        File myFile = new File(filePath);
        RequestParams params = new RequestParams();
        try {
            params.put("files", myFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        client.post(context, url, params, getHandler(context,callback));
    }

    public static void upload(Context context, String url, File[] files, final ResultCallback callback, final String[]... params) {
        RequestParams requestParams = new RequestParams();
        try {
            for (String[] param : params){
                requestParams.put(param[0], param[1]);
            }
            requestParams.put("files", files);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        client.post(context, url, requestParams, getHandler(context,callback));
    }

    public static void upload(Context context, String url, File file, final ResultCallback callback, final String[]... params) {
        RequestParams requestParams = new RequestParams();
        try {
            for (String[] param : params){
                requestParams.put(param[0], param[1]);
            }
            requestParams.put("files", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        client.post(context, url, requestParams, getHandler(context,callback));
    }


    public static void cancelAll() {
        client.cancelAllRequests(true);
    }

    public static AsyncHttpClient getClient() {
        return client;
    }

    public static String getSessionId() {
        return sessionId;
    }

    public static void setSessionId(String sessionId) {
        HttpUtils.sessionId = sessionId;
    }

    public static PersistentCookieStore getCookieStore() {
        return cookieStore;
    }

    public static void setCookieStore(PersistentCookieStore cookieStore) {
        HttpUtils.cookieStore = cookieStore;
        client.setCookieStore(cookieStore);
    }

    public static abstract class ResultCallback<T> {
        JavaType javaType = JacksonUtils.getInstance().constructType(getSuperclassTypeParameter(getClass()));

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterizedType = (ParameterizedType) superclass;
            return parameterizedType.getActualTypeArguments()[0];
        }

        public abstract void onError(int statusCode, Throwable error);

        public abstract void onResponse(T response);
    }

}
