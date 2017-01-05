package com.slinph.ihelmet.ihelmet_domestic.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.slinph.ihelmet.ihelmet_domestic.R;
import com.slinph.ihelmet.ihelmet_domestic.internet.Url;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.HttpUtils;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.ResultData;

public class ChangeUserInfoActivity extends AppCompatActivity {

    private EditText et_change_realname;
    private EditText et_change_username;
    private EditText et_change_gender;
    private EditText et_change_age;
    private EditText et_change_nation;
    private Button btn_confirmChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);
        InitView();
        InitEvent();
    }

    private void InitEvent() {
        btn_confirmChange.setOnClickListener(new View.OnClickListener() {

            private String gender;

            @Override
            public void onClick(View v) {
                String change_realname = et_change_realname.getText().toString().trim();
                String change_username = et_change_username.getText().toString().trim();
                String change_gender = et_change_gender.getText().toString().trim();
                String change_age = et_change_age.getText().toString().trim();
                String change_nation = et_change_nation.getText().toString().trim();
                if (TextUtils.isEmpty(change_age) || TextUtils.isEmpty(change_nation)||
                TextUtils.isEmpty(change_realname)||TextUtils.isEmpty(change_username) || TextUtils.isEmpty(change_gender)){
                    Toast.makeText(ChangeUserInfoActivity.this, "填写内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    if ("男".equals(change_gender)){
                        gender = "1";
                    }else if ("女".equals(change_gender)){
                        gender = "2";
                    }else{
                        Toast.makeText(ChangeUserInfoActivity.this, "性别请填男或女", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    HttpUtils.postAsync(ChangeUserInfoActivity.this, Url.rootUrl+"/iheimi/user/update", new HttpUtils.ResultCallback<ResultData>() {
                                @Override
                                public void onError(int statusCode, Throwable error) {
                                    Toast.makeText(ChangeUserInfoActivity.this, "网络异常"+statusCode+"0x03", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(ResultData response) {
                                    if (response.getSuccess()){
                                        Toast.makeText(ChangeUserInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            },new String[]{"realName",change_realname}
                            ,new String[]{"userName",change_username}
                            ,new String[]{"age",change_age}
                            ,new String[]{"sex",gender}
                            ,new String[]{"nation",change_nation});
                }
            }
        });
    }

    private void InitView() {
        et_change_realname = (EditText) findViewById(R.id.et_change_realname);
        et_change_username = (EditText) findViewById(R.id.et_change_username);
        et_change_gender = (EditText) findViewById(R.id.et_change_gender);
        et_change_age = (EditText) findViewById(R.id.et_change_age);
        et_change_nation = (EditText) findViewById(R.id.et_change_nation);
        btn_confirmChange = (Button) findViewById(R.id.btn_confirmChange);
    }
}
