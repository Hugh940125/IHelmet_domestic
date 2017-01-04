package com.slinph.ihelmet.ihelmet_domestic.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.slinph.ihelmet.ihelmet_domestic.R;
import com.slinph.ihelmet.ihelmet_domestic.internet.Url;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.HttpUtils;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.ResultData;

import me.drakeet.materialdialog.MaterialDialog;


/**
 * Created by Administrator on 2015/12/4.
 *
 */
public class ChangePasswordActivity extends BaseActivity {

    private EditText et_phone, et_captcha, et_new_password, et_sure_password;
    private Button btn_get_captcha;
    private long time = 0;
    private String phone;
    private String captcha;
    private String newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar(R.string.changePassword);
    }

    @Override
    protected int addLayoutId() {
        return R.layout.activity_change_password;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.equip, menu);
        return true;
    }

    @Override
    protected void findViews() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_captcha = (EditText) findViewById(R.id.et_captcha);
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        et_sure_password = (EditText) findViewById(R.id.et_sure_password);
        btn_get_captcha = (Button) findViewById(R.id.btn_get_captcha);
        btn_get_captcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCaptcha();//获取验证码
            }
        });
        Button btn_sure = (Button) findViewById(R.id.btn_sure);
        if (btn_sure != null){
            btn_sure .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changePassword();
                }
            });
        }
    }

    /**
     * 验证密码
     */
    private void changePassword() {
        phone = et_phone.getText().toString().trim();
        captcha = et_captcha.getText().toString().trim();
        newPassword = et_new_password.getText().toString().trim();
        String surePassword = et_sure_password.getText().toString().trim();
        if (!ValidateUtils.validateMobile(phone)) {//正则验证手机号码
            et_phone.setError("手机号码不正确");
            et_phone.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(captcha) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(surePassword)) {
            showToast("填写内容不能为空");
            return;
        }
        if (!newPassword.equals(surePassword)) {
            showToast("两次输入密码不相同");
            return;
        }
/*        if (!captcha.equals(mCaptcha)) {
            showToast("验证码错误");
            return;
        }*/
        requestD();
    }

    /**
     * 获取验证码
     */
    private void getCaptcha() {
        String phone = et_phone.getText().toString();
        if (!ValidateUtils.validateMobile(phone)) {//正则验证手机号码
            et_phone.setError("手机号码不正确");
            et_phone.requestFocus();//获取焦点
            return;
        }

        HttpUtils.postAsync(this, Url.rootUrl+"/iheimi/user/code4findpsw", new HttpUtils.ResultCallback<ResultData>() {
            @Override
            public void onError(int statusCode, Throwable error) {
                Toast.makeText(ChangePasswordActivity.this, error.getMessage() , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ResultData response) {
                Boolean success = response.getSuccess();
                if (success){
                    btn_get_captcha.setClickable(false);
                    if (System.currentTimeMillis() - time > 120000) {
                        new CountDownTimer(120000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("剩余： ");
                                stringBuilder.append(millisUntilFinished/1000);
                                stringBuilder.append(" 秒");
                                btn_get_captcha.setText(stringBuilder.toString());//"剩余:" + millisUntilFinished / 1000 + "秒"
                            }

                            public void onFinish() {
                                btn_get_captcha.setText(R.string.again_validate);
                                time = 0;
                                btn_get_captcha.setClickable(true);
                            }
                        }.start();
                    }
                    time = System.currentTimeMillis();
                }
                else{
                    Toast.makeText(ChangePasswordActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        },new String[]{"phone",phone});
    }

    /**
     * 找回密码请求
     */
    private final void requestD() {
        //newPassword = Md5PasswordEncrypt.encrypt(newPassword);
        HttpUtils.postAsync(ChangePasswordActivity.this,Url.rootUrl+"/iheimi/user/updatePassword",new HttpUtils.ResultCallback<ResultData>() {

            @Override
            public void onError(int statusCode, Throwable error) {
                Toast.makeText(ChangePasswordActivity.this, error.getMessage() , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ResultData response) {
              if (response.getSuccess()){
                  final MaterialDialog dialog = new MaterialDialog(ChangePasswordActivity.this);
                  dialog.setPositiveButton(R.string.sure, new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          dialog.dismiss();//点击空白地方对话框不会消失
                          finish();//返回登录页面
                      }
                  });
                  dialog.setTitle(R.string.tip);
                  dialog.setMessage("密码修改成功！");
                  dialog.show();
              } else {
                  Toast.makeText(ChangePasswordActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
              }
            }
        },new String[]{"phone",phone}
        ,new String[]{"code",captcha}
        ,new String[]{"password",newPassword});
    }
}
