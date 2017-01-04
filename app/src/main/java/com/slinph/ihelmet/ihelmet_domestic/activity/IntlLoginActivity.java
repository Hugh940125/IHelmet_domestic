package com.slinph.ihelmet.ihelmet_domestic.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.slinph.ihelmet.ihelmet_domestic.R;

import java.util.HashMap;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.google.GooglePlus;
import cn.sharesdk.linkedin.LinkedIn;

public class IntlLoginActivity extends AppCompatActivity implements  PlatformActionListener,View.OnClickListener{

    private ImageButton ib_facebook;
    private ImageButton ib_google_plus;
    private ImageButton ib_linked_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_intnl);

        ShareSDK.initSDK(this);

        FindViews();
        InitEvent();
    }

    private void InitEvent() {
        ib_facebook.setOnClickListener(this);
        ib_google_plus.setOnClickListener(this);
        ib_linked_in.setOnClickListener(this);
    }

    private void FindViews() {
        ib_facebook = (ImageButton) findViewById(R.id.ib_facebook);
        ib_google_plus = (ImageButton) findViewById(R.id.ib_google_plus);
        ib_linked_in = (ImageButton) findViewById(R.id.ib_linked_in);
    }

    //平台回调的监听
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        switch (platform.getId()){
            case 8:
                Toast.makeText(IntlLoginActivity.this, "ib_facebook_onComplete", Toast.LENGTH_SHORT).show();
                break;
            case 21:
                Toast.makeText(IntlLoginActivity.this, "ib_google_plus_onComplete", Toast.LENGTH_SHORT).show();
                break;
            case 20:
                Toast.makeText(IntlLoginActivity.this, "ib_linked_in_onComplete", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        switch (platform.getId()){
            case 8:
                Toast.makeText(IntlLoginActivity.this, "ib_facebook_onError", Toast.LENGTH_SHORT).show();
                break;
            case 21:
                Toast.makeText(IntlLoginActivity.this, "ib_google_plus_onError", Toast.LENGTH_SHORT).show();
                break;
            case 20:
                Toast.makeText(IntlLoginActivity.this, "ib_linked_in_onError", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onCancel(Platform platform, int i) {
        switch (platform.getId()){
            case 8:
                Toast.makeText(IntlLoginActivity.this, "ib_facebook_onCancel", Toast.LENGTH_SHORT).show();
                break;
            case 21:
                Toast.makeText(IntlLoginActivity.this, "ib_google_plus_onCancel", Toast.LENGTH_SHORT).show();
                break;
            case 20:
                Toast.makeText(IntlLoginActivity.this, "ib_linked_in_onCancel", Toast.LENGTH_SHORT).show();
                break;
        }    }


    //各平台图标点击事件的监听
    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.ib_facebook:
               Platform facebook = ShareSDK.getPlatform(Facebook.NAME);
               facebook.SSOSetting(false);
               facebook.setPlatformActionListener(this);
               facebook.authorize();//单独授权
               facebook.showUser(null);//授权并获取用户信息
               break;
           case R.id.ib_google_plus:
               Toast.makeText(IntlLoginActivity.this, "ib_google_plus", Toast.LENGTH_SHORT).show();
               Platform Google_Plus = ShareSDK.getPlatform(GooglePlus.NAME);
               Google_Plus.SSOSetting(false);  //设置false表示使用SSO授权方式
               Google_Plus.setPlatformActionListener(this);
               Google_Plus.authorize();//单独授权
               Google_Plus.showUser(null);//授权并获取用户信息
               break;
           case R.id.ib_linked_in:
               Platform Linked_In = ShareSDK.getPlatform(LinkedIn.NAME);
               Linked_In.SSOSetting(false);  //设置false表示使用SSO授权方式
               Linked_In.setPlatformActionListener(this);
               Linked_In.authorize();//单独授权
               Linked_In.showUser(null);//授权并获取用户信息
               break;
       }
    }
}
