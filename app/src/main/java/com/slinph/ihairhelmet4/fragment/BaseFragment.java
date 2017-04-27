package com.slinph.ihairhelmet4.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONObject;

///import com.slinph.ihairhelmet.http.CommonRequest;
///import com.slinph.ihairhelmet.http.ResponeHandler;
///import com.slinph.ihairhelmet.utils.Lg;
///import org.codehaus.jackson.map.DeserializationConfig;
///import org.codehaus.jackson.map.ObjectMapper;

@SuppressLint("ValidFragment")
public class BaseFragment extends Fragment {

    private boolean mShowDialogFlag = true;//默认显示加载dialog
    ///protected ObjectMapper mObjectMapper;//解析json

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///mObjectMapper = new ObjectMapper();
        ///mObjectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public void setShowLoadingDialog(boolean showDialogFlag) {
        mShowDialogFlag = showDialogFlag;
    }

    /**
     * 默认加载提示文字
     *
     * @param url
     * @param params
     */
    /**
     *      hugh注释掉
     */

    /*protected void request(Context context, String url, Map<String, String> params) {
        request(context, url, params, 0);
    }*/

    /**
     * 自定义加载提示文字
     *
     * @param url
     * @param params
     * @param resId
     */
    /**
     *      hugh注释掉的
     */
   /* protected void request(Context context, String url, Map<String, String> params, int resId) {
        Lg.e("**url", url + "");
        Lg.e("**params", params + "");
        CommonRequest.request(context, url, params, resId, mShowDialogFlag, new ResponeHandler() {
            @Override
            public void onSuccess(JSONObject responseData) {
                Lg.e("**responseData", responseData + "");
                refreshData(responseData);
            }

            @Override
            public void onFailure() {
                loadFailure();
            }

            @Override
            public void onFinish() {
                loadFinish();
            }
        });
    }*/

    /**
     * 请求成功
     */
    protected void refreshData(JSONObject responseData) {

    }

    /**
     * 加载失败，网络或服务器出现异常
     */
    protected void loadFailure() {
    }

    /**
     * 加载完成
     */
    protected void loadFinish() {

    }

    /**
     * 跳转activity
     * @param clazz
     */
    protected void toAty(Class<? extends Activity> clazz) {
        getActivity().startActivity(new Intent(getActivity(), clazz));
    }

    /**
     * toast提示
     *
     * @param resId
     */
    protected void showToast(int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }
}
