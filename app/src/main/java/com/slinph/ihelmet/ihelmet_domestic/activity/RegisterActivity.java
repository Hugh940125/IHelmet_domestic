package com.slinph.ihelmet.ihelmet_domestic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.slinph.ihelmet.ihelmet_domestic.R;
import com.slinph.ihelmet.ihelmet_domestic.internet.Url;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.HttpUtils;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.ResultData;

import me.drakeet.materialdialog.MaterialDialog;


/**
 * Created by Waiting on 2016/2/25.
 * h
 */
public class RegisterActivity extends Activity implements View.OnClickListener {

    private EditText et_phone, et_captcha, et_password, et_password2;
    private CheckBox cb_is_agree;
    private Button btn_register, btn_getCaptcha;
    private String mCaptcha;
    private long time = 0;
    private EditText et_realname;
    private EditText et_gender;
    private EditText et_age;
    private ImageButton ib_regis_back;
    private RadioButton rb_m;
    private RadioButton rb_f;
    private RadioGroup rg_gender;
    private String gendercode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
    }

    protected void findViews() {
        et_realname = (EditText) findViewById(R.id.et_realname);
        rg_gender = (RadioGroup) findViewById(R.id.rg_gender);
        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_m:
                        gendercode = "1";
                        break;
                    case R.id.rb_f:
                        gendercode = "2";
                        break;
                }
            }
        });

        TextView tv_agree_protocol = (TextView) findViewById(R.id.tv_agree_protocol);
        tv_agree_protocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,ProtocolActivity.class));
            }
        });
        et_age = (EditText) findViewById(R.id.et_age);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_captcha = (EditText) findViewById(R.id.et_captcha);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password2 = (EditText) findViewById(R.id.et_password2);
        cb_is_agree = (CheckBox) findViewById(R.id.cb_is_agree);
        btn_getCaptcha = (Button) findViewById(R.id.btn_getCaptcha);
        btn_register = (Button) findViewById(R.id.btn_register);

        ib_regis_back = (ImageButton) findViewById(R.id.ib_regis_back);
        ib_regis_back.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_getCaptcha.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_getCaptcha://获取验证码
                getCaptcha();
                break;
            case R.id.btn_register://注册
                register();
                break;
            case R.id.ib_regis_back://注册
                finish();
                break;
        }
    }

    private void register() {
        String realname = et_realname.getText().toString().trim();
        String age = et_age.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String captcha = et_captcha.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String password2 = et_password2.getText().toString().trim();
        boolean checked = cb_is_agree.isChecked();

        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(captcha) || TextUtils.isEmpty(password2)) {
            Toast.makeText(RegisterActivity.this, "填写内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ValidateUtils.validateMobile(phone)) {//正则验证手机号码
            et_phone.setError("手机号码不正确");
            et_phone.requestFocus();
            return;
        }
        if (!password.equals(password2)) {
            Toast.makeText(RegisterActivity.this, "两次输入密码不相同", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!checked) {
            Toast.makeText(RegisterActivity.this, "请选择\"我已阅读同意《使用协议》\"", Toast.LENGTH_SHORT).show();

            return;
        }
      /*  if (!captcha.equals(mCaptcha)) {
            showToast("验证码错误");
            return;
        }*/

        //注册请求
        //password = Md5PasswordEncrypt.encrypt(password);
        HttpUtils.postAsync(this, Url.rootUrl+"/iheimi/user/insert", new HttpUtils.ResultCallback<ResultData>() {
            @Override
            public void onError(int statusCode, Throwable error) {
                Toast.makeText(RegisterActivity.this, error.getMessage() , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ResultData response) {
                Boolean success = response.getSuccess();
                if (success){
                    final MaterialDialog dialog = new MaterialDialog(RegisterActivity.this);
                    dialog.setPositiveButton(R.string.sure, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            RegisterActivity.this.finish();
                        }
                    });
                    dialog.setTitle(R.string.tip);
                    dialog.setMessage("注册成功！");
                    dialog.show();
                }else{
                    Toast.makeText(RegisterActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        },new String[]{"realName",realname}
         ,new String[]{"sex", gendercode}
         ,new String[]{"age",age}
         ,new String[]{"phone",phone}
         ,new String[]{"password",password}
         ,new String[]{"code",captcha});
    }

    public void getCaptcha() {
        String phone = et_phone.getText().toString().trim();
        if (!ValidateUtils.validateMobile(phone)) {//正则验证手机号码
            et_phone.setError("手机号码不正确");
            et_phone.requestFocus();
            return;
        }
        //请求验证码
        HttpUtils.postAsync(this, Url.rootUrl+"/iheimi/user/code4register", new HttpUtils.ResultCallback<ResultData>() {
            @Override
            public void onError(int statusCode, Throwable error) {
                Toast.makeText(RegisterActivity.this, error.getMessage() , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ResultData response) {
                Boolean success = response.getSuccess();
                if (success){
                        btn_getCaptcha.setClickable(false);
                        if (System.currentTimeMillis() - time > 120000) {
                            new CountDownTimer(120000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                    btn_getCaptcha.setText("剩余:" + millisUntilFinished / 1000 + "秒");
                                }
                                public void onFinish() {
                                    btn_getCaptcha.setText(R.string.again_validate);
                                    time = 0;
                                    btn_getCaptcha.setClickable(true);
                                }
                            }.start();
                        }
                        time = System.currentTimeMillis();
                }else{
                    Toast.makeText(RegisterActivity.this, response.getMsg() , Toast.LENGTH_SHORT).show();
                }
            }
        },new String[]{"phone",phone});
    }
}
