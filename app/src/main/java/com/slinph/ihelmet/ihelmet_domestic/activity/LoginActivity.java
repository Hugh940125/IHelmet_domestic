package com.slinph.ihelmet.ihelmet_domestic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.slinph.ihelmet.ihelmet_domestic.R;
import com.slinph.ihelmet.ihelmet_domestic.internet.Url;
import com.slinph.ihelmet.ihelmet_domestic.internet.Vo.BindInfoVO;
import com.slinph.ihelmet.ihelmet_domestic.internet.Vo.QualifiedVO;
import com.slinph.ihelmet.ihelmet_domestic.internet.Vo.TreatmentProgramsVO;
import com.slinph.ihelmet.ihelmet_domestic.internet.Vo.UserVO;
import com.slinph.ihelmet.ihelmet_domestic.internet.model.Page;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.DataConverter;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.HttpUtils;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.ResultData;
import com.slinph.ihelmet.ihelmet_domestic.utils.SharePreferencesConfig;
import com.slinph.ihelmet.ihelmet_domestic.utils.SharePreferencesUtils;
import com.slinph.ihelmet.ihelmet_domestic.utils.StaticVariables;
import com.slinph.ihelmet.ihelmet_domestic.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_phone;
    private EditText et_password;
    private CheckBox cb_save_user;
    private Button btn_revise;
    private Button btn_register;
    private Button bt_login;
    private String phone_input;
    private String password_input;
    private String phone;
    private AlertDialog alertDialog;
    private TextView tv_protocol;
    private int codeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InitView();
        InitEvent();

        /*if (cb_save_user.isChecked()){
            phone_input = SharePreferencesUtils.getString(this, "SP_PHONE", "");
            password_input = SharePreferencesUtils.getString(this, "SP_PSW", "");
            if (!TextUtils.isEmpty(password_input)&&!TextUtils.isEmpty(password_input)){
                Login();
            }
        }*/
    }

    private boolean getInputContent() {
        phone_input = et_phone.getText().toString().trim();
        password_input = et_password.getText().toString().trim();
        if (!phone_input.isEmpty()&&!password_input.isEmpty()){
            return true;
        }else {
            return false;
        }
    }

    private void InitEvent() {
        btn_revise.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        bt_login.setOnClickListener(this);
        cb_save_user.setOnClickListener(this);
        tv_protocol.setOnClickListener(this);
    }

    private void InitView() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_password = (EditText) findViewById(R.id.et_password);
        cb_save_user = (CheckBox) findViewById(R.id.cb_save_user);
        btn_revise = (Button) findViewById(R.id.btn_revise);
        btn_register = (Button) findViewById(R.id.btn_register);
        bt_login = (Button) findViewById(R.id.btn_login);
        tv_protocol = (TextView) findViewById(R.id.tv_protocol);
        boolean save_or_not = SharePreferencesUtils.getBoolean(this, "save_or_not", true);
        if (save_or_not){
            cb_save_user.setChecked(true);
        }else {
            cb_save_user.setChecked(false);
        }
        //cb_save_user.isChecked();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_revise:
                startActivity(new Intent(LoginActivity.this,ChangePasswordActivity.class));
                break;
            case R.id.btn_register:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
            case R.id.btn_login:
                Login();
                break;
            case R.id.cb_save_user:
                if (cb_save_user.isChecked()){
                    SharePreferencesUtils.putBoolean(this,"save_or_not",true);
                }else{
                    SharePreferencesUtils.putBoolean(this,"save_or_not",false);
                }
                break;
            case R.id.tv_protocol:
                    startActivity(new Intent(LoginActivity.this,ProtocolActivity.class));
                break;
        }
    }

    private void Login() {
        //if (TextUtils.isEmpty(phone_input)||TextUtils.isEmpty(password_input)){
            getInputContent();
            //password_input = Md5PasswordEncrypt.encrypt(password_input);
        //}
        Log.e(phone_input,password_input);
        if (TextUtils.isEmpty(phone_input)||TextUtils.isEmpty(password_input)){
            Toast.makeText(LoginActivity.this, R.string.phone_or_psw_cant_null, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ValidateUtils.validateMobile(phone_input)) {//正则验证手机号码
            et_phone.setError(getString(R.string.phone_format_error));
            et_phone.requestFocus();
            return;
        }

        alertDialog = DataConverter.showDialog(this, "登录中···");
        HttpUtils.postAsync(this, Url.rootUrl+"/iheimi/user/login", new HttpUtils.ResultCallback<ResultData<UserVO>>(){

                    @Override
                    public void onError(int statusCode, Throwable error) {
                        alertDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "网络异常"+statusCode+"0x02", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(ResultData<UserVO> response) {
                        if (response.getSuccess() && response.getCode() == 200){
                            UserVO data = response.getData();
                            String last_phone = SharePreferencesUtils.getString(LoginActivity.this, "SP_PHONE", "");
                            if (!last_phone.isEmpty()&&last_phone.equals(phone_input)){
                                ArrayList<String> empty = new ArrayList<>();
                                SharePreferencesUtils.putStrListValue(LoginActivity.this,"mapList",empty);
                            }
                            if (data != null){
                                Integer sex = data.getSex();
                                phone = data.getPhone();
                                Long id = data.getId();
                                StaticVariables.ID = String.valueOf(id);
                                int mid = data.getMid();
                                Log.e("mid",mid+"");

                                long time = data.getCreateDtm().getTime();
                                StaticVariables.REGIS_TIME = time;
                                StaticVariables.USER_REALNAME = data.getRealName();
                                StaticVariables.USER_AGE = String.valueOf(data.getAge());
                                StaticVariables.USER_PHONE = phone;

                                BindOrNot(mid);

                                if (sex == 1){
                                    StaticVariables.USER_SEX = "男";
                                }else{
                                    StaticVariables.USER_SEX = "女";
                                }
                                Log.e("USER_SEX",StaticVariables.USER_SEX);
                                SaveInfo();
                                alertDialog.dismiss();
                            }
                        }
                        else{
                            alertDialog.dismiss();
                            Toast.makeText(LoginActivity.this,response.getMsg() , Toast.LENGTH_SHORT).show();//R.string.psw_mistake
                        }
                    }
                },new String[]{"phone",phone_input}
                ,new String[]{"password",password_input});
    }

    private void SaveInfo() {
        SharePreferencesUtils.putString(LoginActivity.this ,"SP_PHONE",phone_input);
        SharePreferencesUtils.putString(LoginActivity.this,"SP_PSW",password_input);
        SharePreferencesUtils.putString(LoginActivity.this, SharePreferencesConfig.SP_USER_PHONE,phone);
        if (cb_save_user.isChecked()){
            SharePreferencesUtils.putBoolean(this,"save_or_not",true);
        }else {
            SharePreferencesUtils.putBoolean(this,"save_or_not",false);
        }
    }

    private void gotoInfo() {
        startActivity(new Intent(this,LoseHairInfoActivity.class));
        finish();
    }

    private void BindOrNot(final int mid) {
        HttpUtils.postAsync(this, Url.rootUrl+"/iheimi/code/selectOne", new HttpUtils.ResultCallback<ResultData<List<BindInfoVO>>>(){

            @Override
            public void onError(int statusCode, Throwable error) {
                Toast.makeText(LoginActivity.this, "网络异常"+statusCode+"0x04", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ResultData<List<BindInfoVO>> response) {
                if (response.getCode() == 200){
                    List<BindInfoVO> data = response.getData();
                    if (data != null){
                        Log.e("条形码集合",data.toString());
                        codeCount = data.size();
                    }
                    if (data != null && data.size() > 0){
                        if (mid != 0){
                            StaticVariables.MID = String.valueOf(mid);
                            JudgeJump();
                        }else {
                            HashMap mapFromSp = StringUtils.getMapFromSp(LoginActivity.this);
                            if (mapFromSp.isEmpty()){
                                gotoInfo();
                            }else {
                                Intent intent = new Intent(LoginActivity.this, TakePhotoActivity.class);
                                intent.putExtra("Where","Info_Saved");
                                startActivity(intent);
                            }
                        }
                    }else {
                        gotoScan();
                    }
                }
            }
        });
    }

    private void gotoScan() {
        startActivity(new Intent(this,VerificationActivity.class));
        finish();
    }

    private void gotoHome() {
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        finish();
    }

    private void JudgeJump(){
        //获取最新的报告
        HttpUtils.postAsync(this,Url.rootUrl+"/iheimi/treatmentPrograms/selectOneNew",new HttpUtils.ResultCallback<ResultData<TreatmentProgramsVO>>(){
            @Override
            public void onError(int statusCode, Throwable error) {
                Toast.makeText(LoginActivity.this, "网络异常"+statusCode+"0x05", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ResultData<TreatmentProgramsVO> response) {
                Boolean success = response.getSuccess();
                if (success){
                    final TreatmentProgramsVO data = response.getData();
                    Log.e("获取最新的报告",data + "");
                    if (data == null){
                        StaticVariables.CHECK_TYPE = 1;
                        //如果最新报告获取的内容为空，则查询审核信息
                        StaticVariables.IS_REPORT_EXIST = false;
                        HttpUtils.postAsync(LoginActivity.this, Url.rootUrl+"/iheimi/patientQualified/selectOneByExample", new HttpUtils.ResultCallback<ResultData<QualifiedVO>>() {
                            @Override
                            public void onError(int statusCode, Throwable error) {
                            }

                            @Override
                            public void onResponse(ResultData<QualifiedVO> response) {
                                if (response.getSuccess()){
                                    QualifiedVO data = response.getData();
                                    if (data != null){
                                        Log.e("查询审核信息",data + "");
                                    }
                                    if (data != null){
                                        StaticVariables.IS_QUALIFIDE_EXIST = true;
                                        gotoHome();
                                        alertDialog.dismiss();
                                    }else {
                                        StaticVariables.IS_QUALIFIDE_EXIST = false;
                                        alertDialog.dismiss();
                                        HashMap mapFromSp = StringUtils.getMapFromSp(LoginActivity.this);
                                        if (mapFromSp == null){
                                            gotoInfo();
                                        }else {
                                            Intent intent = new Intent(LoginActivity.this, TakePhotoActivity.class);
                                            intent.putExtra("Where","Info_Saved");
                                            startActivity(intent);
                                        }
                                    }
                                }
                            }
                        }, new String[]{"patientId", StaticVariables.MID}
                         , new String[]{"type", String.valueOf(StaticVariables.CHECK_TYPE)});
                    } else{
                        StaticVariables.IS_REPORT_EXIST = true;
                        Long patientId = data.getPatientId();
                        if (!StaticVariables.MID.equals(String.valueOf(patientId))){
                            StaticVariables.CHECK_TYPE = 2;
                            StaticVariables.USER_IS_SHOW_WAIT = true;
                            gotoHome();
                        }else {
                            HttpUtils.postAsync(LoginActivity.this, Url.rootUrl+"/iheimi/treatmentPrograms/selectByExample", new HttpUtils.ResultCallback<ResultData<Page<TreatmentProgramsVO>>>() {
                                @Override
                                public void onError(int statusCode, Throwable error) {
                                }

                                @Override
                                public void onResponse(ResultData<Page<TreatmentProgramsVO>> response) {
                                    if (response.getSuccess()){
                                        Page<TreatmentProgramsVO> dat = response.getData();
                                        List<TreatmentProgramsVO> list = dat.getList();
                                        int size = list.size();
                                        Log.e("报告份数",size + "");
                                        Date createDtm = data.getCreateDtm();
                                        Log.e("查询最新报告时间",createDtm + "");
                                        long reportTime = createDtm.getTime();
                                        Log.e("报告时间的毫秒数",reportTime + "");
                                        long currentTimeMillis = System.currentTimeMillis();
                                        Log.e("现在的毫秒数",currentTimeMillis + "");
                                        long timeDistence = currentTimeMillis - reportTime;
                                        if (timeDistence >= 432000000L  && size < codeCount*5){//2592000000  3600000  432000000
                                            startActivity(new Intent(LoginActivity.this,FollowUpActivity.class));
                                            LoginActivity.this.finish();
                                        }else {
                                            gotoHome();
                                        }
                                    }
                                }
                            });
                        }
                    }
                }else{
                    Toast.makeText(LoginActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        },new String[]{"patient_id",StaticVariables.MID});
    }
}
