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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.slinph.ihelmet.ihelmet_domestic.R;
import com.slinph.ihelmet.ihelmet_domestic.internet.Url;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.HttpUtils;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.ResultData;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class ScanActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 111;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 5;
    private TextView tv_scan_tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Button scan = (Button) findViewById(R.id.scan);
        tv_scan_tip = (TextView) findViewById(R.id.tv_scan_tip);
        ImageButton ib_scan_back = (ImageButton) findViewById(R.id.ib_scan_back);
        if (ib_scan_back != null){
            ib_scan_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        if (scan != null){
            scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(ScanActivity.this);
                    builder2.setTitle("提示：")
                            .setMessage("二维码扫描后即被绑定且无法取消，请确认后继续")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                                        if(ActivityCompat.shouldShowRequestPermissionRationale(ScanActivity.this,Manifest.permission.READ_CONTACTS)){

                                        }else {
                                            ActivityCompat.requestPermissions(ScanActivity.this,new String[]{Manifest.permission.CAMERA},MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                                        }
                                    }else {
                                        Intent intent = new Intent(ScanActivity.this, CaptureActivity.class);
                                        startActivityForResult(intent, REQUEST_CODE);
                                    }
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }) .show();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults){
        switch(requestCode){
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:{
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(ScanActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }else{
                    Toast.makeText(ScanActivity.this, "拍照权限被拒绝", Toast.LENGTH_SHORT).show();
                }
            }

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
                    //tv_scan_tip.setText(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    //Toast.makeText(ScanActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                    //tv_scan_tip.setText("解析二维码失败");
                }
            }
        }
    }

    private void Anthentication(String QRcode){
        HttpUtils.postAsync(this, Url.rootUrl+"/iheimi/code/checkByExample", new HttpUtils.ResultCallback<ResultData>() {
                    @Override
                    public void onError(int statusCode, Throwable error) {
                        Toast.makeText(ScanActivity.this, error.getMessage() , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(ResultData response) {
                        Boolean success = response.getSuccess();
                        if (success){
                            startActivity(new Intent(ScanActivity.this,ScanSuccessActivity.class));
                            finish();
                        }else{
                            int code = response.getCode();
                            if (code == 200){
                                startActivity(new Intent(ScanActivity.this,BindSuccessActivity.class));
                            }else if (code == 400){
                                AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
                                builder.setTitle("提示：")
                                        .setMessage("二维码已被绑定")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }) .show();
                            }else if (code == 404){
                                AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
                                builder.setTitle("提示：")
                                        .setMessage("二维码不存在")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }) .show();
                            }else {
                                Toast.makeText(ScanActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },new String[]{"code", QRcode}
                ,new String[]{"status","1"});
    }
}
