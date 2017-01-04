package com.slinph.ihelmet.ihelmet_domestic.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.slinph.ihelmet.ihelmet_domestic.R;
import com.slinph.ihelmet.ihelmet_domestic.utils.SystemBarTintManager;
import com.slinph.ihelmet.ihelmet_domestic.utils.UIUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.Map;


public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar tb_toolbar;//toolbar类
    private ImageView iv_menu;//menu
    private TextView tv_toolbar_title;//boolbar_title
    private FrameLayout container;//需要填充的父控件
    private boolean mShowDialogFlag = true;//加载网络等待dialoy

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//所有页面限制为竖屏显示
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base);
        ////MyApplication.getInstance().addActivity(this);

        tb_toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        tv_toolbar_title = (TextView)findViewById(R.id.tv_toolbar_title);
        iv_menu = (ImageView) findViewById(R.id.iv_menu);
        initSystemBar(this);//沉浸式状态栏，改变状态栏的颜色使之与APP风格一体化

        View child = LayoutInflater.from(this).inflate(addLayoutId(), null);//获取需要填充的view
        container = (FrameLayout) findViewById(R.id.ll_container);
        container.addView(child);
        findViews();
    }

    /**
     * 沉浸式状态栏，改变状态栏的颜色使之与APP风格一体化
     *
     * @param activity
     */
    private void initSystemBar(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            //api<19,布局变换
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dp2px(42));
            tb_toolbar.setLayoutParams(layoutParams);//重新设置toolbar的高度,防止右侧图片靠下
        }else{
            setTranslucentStatus(activity, true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setNavigationBarTintEnabled(true);//这句要放在前面,状态栏设置颜色才有效果
        tintManager.setStatusBarTintEnabled(true);
        ////tintManager.setTintColor(R.color.colorPrimary);//设置标题栏颜色
        ////tintManager.setStatusBarTintResource(R.color.colorPrimary);// 设置状态栏颜色
    }

    @TargetApi(19)
    private void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * toolbar设置,默认左边为返回按钮
     *
     * @param resId 标题,资源id
     */
    public void setToolbar(int resId) {
        setToolbar(resId, null, R.mipmap.btn_back_wirth);
    }

    /**
     * toolbar设置,默认左边为返回按钮
     *
     * @param title 标题,string
     */
    public void setToolbar(String title) {
        setToolbar(0, title, R.mipmap.btn_back_wirth);
    }

    /**
     * toolbar设置
     *
     * @param resId  标题,资源id
     * @param title  标题,string
     * @param iconId 左边图标,资源id
     */
    public void setToolbar(int resId, String title, final int iconId) {
        setSupportActionBar(tb_toolbar);
        //setNavigationIcon的设定要在setSupoortActionBar之后才有作用,否则会出现backbuttom

        if (iconId == 0) {
            tb_toolbar.setNavigationIcon(null);
        } else {
            tb_toolbar.setNavigationIcon(iconId);
        }

        if (resId == 0) {
            tv_toolbar_title.setText(title);
        } else {
            tv_toolbar_title.setText(resId);
        }
        tb_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iconId == R.mipmap.btn_back_wirth)//返回按钮
                    finish();
                else {
                    clickToolbarIcon(iconId);//分别处理点击事件
                }
            }
        });
    }

    /**
     * Toolbar左边按钮:点击事件处理
     *
     * @param iconId 左边图标,资源id
     */
    protected void clickToolbarIcon(int iconId) {

    }

    /**
     * 设置Toolbar右边按钮
     * @param imgRes
     */
    protected void setToolbarMenu(final int imgRes){
        if(imgRes==0){
            iv_menu.setImageResource(R.color.transparent);
            iv_menu.setOnClickListener(null);
        }else {
            iv_menu.setImageResource(imgRes);
            iv_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickToolbarMenu(imgRes);//分别处理点击事件
                }
            });
        }
    }

    /**
     * Toolbar右边按钮:点击事件处理
     *
     * @param imgRes 左边图标,资源id
     */
    protected void clickToolbarMenu(int imgRes) {

    }

    /**
     * 加载toolbar下面的容器中的viewId
     *
     * @return layoutId
     */
    protected abstract int addLayoutId();

    /**
     * findViewById都写在这里
     */
    protected abstract void findViews();

    /**
     * toast提示
     *
     * @param resId
     */
    protected void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }


    /**
     * 默认加载提示文字(加载中...)
     *
     * @param url
     * @param params
     */
    protected void request(String url, Map<String, String> params) {
        request(url, params, 0);
    }

    /**
     * resId:自定义加载提示文字
     *
     * @param url    接口地址
     * @param params 参数
     * @param resId  在加载过程中显示的文字,0:为默认(加载中...)
     */
    protected void request(String url, Map<String, String> params, int resId) {
/*
        CommonRequest.request(this, url, params, resId, mShowDialogFlag, new ResponeHandler() {
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
        });*/
    }


    protected void upload(String url, Map<String, String> params, String[] filesKey, File[] files) {
        upload(url, params, filesKey, files, 0);
    }


    /**
     * @param url
     * @param params
     * @param filesKey
     * @param files
     * @param resId    加载过程中的提示文字,0为默认文字:上传中...
     */
    protected void upload(String url, Map<String, String> params, String[] filesKey, File[] files, int resId) {
       /* Lg.e("**url", url);
        Lg.e("**params", params.toString());
        CommonRequest.upload(this, url, params, filesKey, files, resId, new FileResponseHandle() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(JSONObject responseData) {
                Lg.e("**responseData", responseData + "");
                refreshData(responseData);
            }

            @Override
            public void onFailure(String errorStr) {
                Lg.e("**responseData", "errorStr");
                loadFailure();
            }

            @Override
            public void onFinish() {
                loadFinish();
            }
        });*/
    }


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

    protected void setShowLoadingDialog(boolean showDialogFlag) {
        mShowDialogFlag = showDialogFlag;
    }
    
    /**
     * 跳转activity
     *
     * @param clazz
     */
    protected void toAty(Class<? extends Activity> clazz) {
        startActivity(new Intent(this, clazz));
    }

}
