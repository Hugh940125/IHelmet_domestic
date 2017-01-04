package com.slinph.ihelmet.ihelmet_domestic.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.slinph.ihelmet.ihelmet_domestic.R;
import com.slinph.ihelmet.ihelmet_domestic.internet.Url;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.HttpUtils;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.ResultData;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class VerificationActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_CODE = 111;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 5;
    private Button scan_qr;
    private Button skip;
    private int from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        Intent intent = getIntent();
        from = intent.getIntExtra("from", -1);
        initViews();
    }

    public void initViews(){
        scan_qr = (Button) findViewById(R.id.scan_QR);
        skip = (Button) findViewById(R.id.skip);
        if (scan_qr != null){
            scan_qr.setOnClickListener(this);
        }
        if (skip != null){
            skip.setOnClickListener(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults){
        switch(requestCode){
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:{
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(VerificationActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }else{
                    Toast.makeText(VerificationActivity.this, "拍照权限被拒绝", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scan_QR:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("提示：")
                        .setMessage("二维码扫描后即被绑定且无法取消，请确认后继续")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (ContextCompat.checkSelfPermission(VerificationActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                                    if(ActivityCompat.shouldShowRequestPermissionRationale(VerificationActivity.this,Manifest.permission.READ_CONTACTS)){

                                    }else {
                                        ActivityCompat.requestPermissions(VerificationActivity.this,new String[]{Manifest.permission.CAMERA},MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                                    }
                                }else {
                                    Intent intent = new Intent(VerificationActivity.this, CaptureActivity.class);
                                    startActivityForResult(intent, REQUEST_CODE);
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }) .show();
                break;
            case R.id.skip:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                    builder.setTitle("提示：")
                                           .setMessage("跳过该步骤只能使用体验模式，是否继续？")
                                           .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialog, int which) {
                                                   if (from ==555){
                                                       finish();
                                                   }else {
                                                       startActivity(new Intent(VerificationActivity.this,MainActivity.class));
                                                       finish();
                                                   }
                                               }
                                           })
                                           .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialog, int which) {

                                               }
                                           }) .show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    //Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                    Anthentication(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    //Toast.makeText(ScanActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("提示：")
                           .setMessage("解析二维码失败，请重试")
                           .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {

                               }
                           }) .show();
                }
            }
        }
    }

    private void Anthentication(String QRcode){
        HttpUtils.postAsync(this, Url.rootUrl+"/iheimi/code/checkByExample", new HttpUtils.ResultCallback<ResultData>() {
                    @Override
                    public void onError(int statusCode, Throwable error) {
                        Toast.makeText(VerificationActivity.this, error.getMessage() , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(ResultData response) {
                        int code = response.getCode();
                        if (code == 200){
                            startActivity(new Intent(VerificationActivity.this,BindSuccessActivity.class));
                            finish();
                        }else if (code == 400){
                            AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
                            builder.setTitle("提示：")
                                    .setMessage("二维码已被绑定")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }) .show();
                        }else if (code == 404){
                            AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
                            builder.setTitle("提示：")
                                    .setMessage("二维码不存在")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }) .show();
                        }else {
                            Toast.makeText(VerificationActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },new String[]{"code", QRcode}
                ,new String[]{"status","1"});
    }

}
