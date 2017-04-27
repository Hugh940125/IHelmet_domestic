package com.slinph.ihairhelmet4.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.slinph.ihairhelmet4.R;
import com.slinph.ihairhelmet4.internet.Url;
import com.slinph.ihairhelmet4.internet.Vo.BindInfoVO;
import com.slinph.ihairhelmet4.internet.Vo.QualifiedVO;
import com.slinph.ihairhelmet4.internet.Vo.TreatmentProgramsVO;
import com.slinph.ihairhelmet4.internet.Vo.UserVO;
import com.slinph.ihairhelmet4.internet.model.Page;
import com.slinph.ihairhelmet4.internet.net_utis.HttpUtils;
import com.slinph.ihairhelmet4.internet.net_utis.ResultData;
import com.slinph.ihairhelmet4.utils.SharePreferencesUtils;
import com.slinph.ihairhelmet4.utils.StaticVariables;
import com.slinph.ihairhelmet4.utils.StringUtils;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/3/28.
 * usage :welcome & load data
 */
public class WelcomeActivity extends Activity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 6;
    private Intent it;
    private String phone_input;
    private String password_input;
    private String phone;
    private Handler handler;
    private int codeCount;
    private ProgressBar pb_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Context context = getApplicationContext();
        XGPushManager.registerPush(context);
        // 2.36（不包括）之前的版本需要调用以下2行代码
        Intent service = new Intent(context, XGPushService.class);
        context.startService(service);

        pb_login = (ProgressBar) findViewById(R.id.pb_login);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)){

            }else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA},MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }else {
            it = new Intent(WelcomeActivity.this, LoginActivity.class);
            //做完所有操作后再进行延时
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (isNetworkConnected(WelcomeActivity.this)){
                        StaticVariables.IS_NET_CONNECTED = true;
                        Message message = handler.obtainMessage();
                        message.what = 888;
                        handler.sendMessage(message);
                    }else {
                        StaticVariables.IS_NET_CONNECTED = false;
                        startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                        finish();
                    }
                }
            };
            timer.schedule(task, 5000); // 5秒后
        }

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 888:
                        Log.e("get","888");
                        autoLogin();
                        break;
                }
            }
        };
    }

    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // 获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空
            if (networkInfo != null)
                return networkInfo.isAvailable();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults){
        switch(requestCode){
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:{
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    it = new Intent(WelcomeActivity.this, LoginActivity.class);
                    //做完所有操作后再进行延时
                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            if (isNetworkConnected(WelcomeActivity.this)){
                                StaticVariables.IS_NET_CONNECTED = true;
                                Message message = handler.obtainMessage();
                                message.what = 888;
                                handler.sendMessage(message);
                            }else {
                                StaticVariables.IS_NET_CONNECTED = false;
                                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                                finish();
                            }
                        }
                    };
                    timer.schedule(task, 5000); // 5秒后
                }else{
                    Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        }
    }

    public void autoLogin(){
        boolean save_or_not = SharePreferencesUtils.getBoolean(this, "save_or_not", false);
        if (save_or_not){
            phone_input = SharePreferencesUtils.getString(this, "SP_PHONE", "");
            password_input = SharePreferencesUtils.getString(this, "SP_PSW", "");
            Log.e(phone_input+"__",password_input+"__");
            if (!TextUtils.isEmpty(password_input)&&!TextUtils.isEmpty(password_input)){
                if (pb_login != null){
                    pb_login.setVisibility(View.VISIBLE);
                }
                Login();
            }else {
                startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                finish();
            }
        }else {
            startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
            finish();
        }
    }


    private void Login() {
        /*if (TextUtils.isEmpty(phone_input)||TextUtils.isEmpty(password_input)){
            getInputContent();
            //password_input = Md5PasswordEncrypt.encrypt(password_input);
        }*/
        Log.e(phone_input, password_input);
        if (TextUtils.isEmpty(phone_input)||TextUtils.isEmpty(password_input)){
            //Toast.makeText(WelcomeActivity.this, R.string.phone_or_psw_cant_null, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
            WelcomeActivity.this.finish();
            return;
        }
        /*if (!ValidateUtils.validateMobile(phone_input)) {//正则验证手机号码
            et_phone.setError(getString(R.string.phone_format_error));
            et_phone.requestFocus();
            return;
        }*/

        //final AlertDialog alertDialog = DataConverter.showDialog(WelcomeActivity.this, "登录中···");
        HttpUtils.postAsync(this, Url.rootUrl+"/iheimi/user/login", new HttpUtils.ResultCallback<ResultData<UserVO>>(){

                    @Override
                    public void onError(int statusCode, Throwable error) {
                        Toast.makeText(WelcomeActivity.this, R.string.net_error, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                        finish();
                    }

                    @Override
                    public void onResponse(ResultData<UserVO> response) {
                        if (response.getSuccess() && response.getCode() == 200){
                            UserVO data = response.getData();
                            String last_phone = SharePreferencesUtils.getString(WelcomeActivity.this, "SP_PHONE", "");
                            if (!last_phone.isEmpty()&&!last_phone.equals(phone_input)){
                                ArrayList<String> empty = new ArrayList<>();
                                SharePreferencesUtils.putStrListValue(WelcomeActivity.this,"mapList",empty);
                            }
                            //alertDialog.dismiss();
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

                                boolean helmet_version = SharePreferencesUtils.getBoolean(WelcomeActivity.this, "helmet_version");
                                Log.e("是不是88版本",helmet_version+"");
                                if (helmet_version){
                                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    StaticVariables.HELMET_VERSION = 0;
                                    finish();
                                }else {
                                    BindOrNot(mid);
                                }

                                if (sex == 1){
                                    StaticVariables.USER_SEX = "男";
                                }else{
                                    StaticVariables.USER_SEX = "女";
                                }
                                SharePreferencesUtils.putString(WelcomeActivity.this,"user_sex",StaticVariables.USER_SEX);
                                Log.e("USER_SEX",StaticVariables.USER_SEX);
                            }
                        }
                        else{
                            Toast.makeText(WelcomeActivity.this,response.getMsg() , Toast.LENGTH_SHORT).show();//R.string.psw_mistake
                            startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                            finish();
                        }
                    }
                },new String[]{"phone",phone_input}
                ,new String[]{"password",password_input});
    }

    private void BindOrNot(final int mid) {
        HttpUtils.postAsync(this, Url.rootUrl+"/iheimi/code/selectOne", new HttpUtils.ResultCallback<ResultData<List<BindInfoVO>>>(){

            @Override
            public void onError(int statusCode, Throwable error) {
                Toast.makeText(WelcomeActivity.this, "网络异常"+statusCode+"0x04", Toast.LENGTH_SHORT).show();
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
                            HashMap mapFromSp = StringUtils.getMapFromSp(WelcomeActivity.this);
                            if (mapFromSp.isEmpty()){
                                Log.e("信息的map是空的","啊啊啊啊啊啊啊");
                                gotoInfo();
                            }else {
                                Log.e("信息的map不是空的",mapFromSp.toString());
                                Intent intent = new Intent(WelcomeActivity.this, TakePhotoActivity.class);
                                intent.putExtra("Where","Info_Saved");
                                startActivity(intent);
                                finish();
                            }
                        }
                    }else {
                        gotoScan();
                    }
                }else {
                    Toast.makeText(WelcomeActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                    finish();
                }
            }
        });

        HttpUtils.postAsync(WelcomeActivity.this, Url.rootUrl+"/iheimi/treatmentPrograms/selectByExample", new HttpUtils.ResultCallback<ResultData<Page<TreatmentProgramsVO>>>() {
            private int size = 0;
            @Override
            public void onError(int statusCode, Throwable error) {
            }

            @Override
            public void onResponse(ResultData<Page<TreatmentProgramsVO>> response) {
                if (response.getSuccess()){
                    Page<TreatmentProgramsVO> dat1 = response.getData();
                    if (dat1 != null){
                        List<TreatmentProgramsVO> list =  dat1.getList();
                        size = list.size();
                    }
                    Log.e("报告份数",size + "");
                    StaticVariables.LEFT_TIMES = codeCount*5 - size;
                }
            }
        });
    }

    private void gotoInfo() {
        startActivity(new Intent(this,LoseHairInfoActivity.class));
        finish();
    }

    private void gotoScan() {
        startActivity(new Intent(this,VerificationActivity.class));
        finish();
    }

    private void JudgeJump(){
        //获取最新的报告
        HttpUtils.postAsync(this,Url.rootUrl+"/iheimi/treatmentPrograms/selectOneNew",new HttpUtils.ResultCallback<ResultData<TreatmentProgramsVO>>(){
            @Override
            public void onError(int statusCode, Throwable error) {
                // Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
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
                        HttpUtils.postAsync(WelcomeActivity.this, Url.rootUrl+"/iheimi/patientQualified/selectOneByExample", new HttpUtils.ResultCallback<ResultData<QualifiedVO>>() {
                            @Override
                            public void onError(int statusCode, Throwable error) {
                            }

                            @Override
                            public void onResponse(ResultData<QualifiedVO> response) {
                                if (response.getSuccess()){
                                    QualifiedVO data = response.getData();
                                    Log.e("查询审核信息",data + "");
                                    if (data != null){
                                        StaticVariables.IS_QUALIFIDE_EXIST = true;
                                        gotoHome();
                                    }else {
                                        StaticVariables.IS_QUALIFIDE_EXIST = false;
                                        HashMap mapFromSp = StringUtils.getMapFromSp(WelcomeActivity.this);
                                        if (mapFromSp == null){
                                            gotoInfo();
                                        }else {
                                            Intent intent = new Intent(WelcomeActivity.this, TakePhotoActivity.class);
                                            intent.putExtra("Where","Info_Saved");
                                            startActivity(intent);
                                            WelcomeActivity.this.finish();
                                        }
                                    }
                                }else {
                                    Toast.makeText(WelcomeActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                                    finish();
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
                        } else {
                            HttpUtils.postAsync(WelcomeActivity.this, Url.rootUrl+"/iheimi/treatmentPrograms/selectByExample", new HttpUtils.ResultCallback<ResultData<Page<TreatmentProgramsVO>>>() {
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
                                        if (size <codeCount*5){
                                            StaticVariables.IS_CAN_FOLLOWUP = true;
                                        }
                                        if (timeDistence >= 23328000000L){
                                            StaticVariables.TIME_DISTANCE_OK = true;
                                        }
                                        StaticVariables.LEFT_TIMES = codeCount*5 - size;
                                        if (timeDistence >= 23328000000L && timeDistence <= 23932800000L && size < codeCount*5){//2592000000  3600000
                                            startActivity(new Intent(WelcomeActivity.this,FollowUpActivity.class));
                                            WelcomeActivity.this.finish();
                                        }else {
                                            gotoHome();
                                        }
                                    }
                                }
                            });
                        }
                    }
                }else{
                    Toast.makeText(WelcomeActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                    finish();
                }
            }
        },new String[]{"patient_id",StaticVariables.MID});
    }

    private void gotoHome() {
        startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
        finish();
    }

   @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
