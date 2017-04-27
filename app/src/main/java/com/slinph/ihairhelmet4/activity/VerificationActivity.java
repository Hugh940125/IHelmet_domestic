package com.slinph.ihairhelmet4.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.slinph.ihairhelmet4.R;
import com.slinph.ihairhelmet4.internet.Url;
import com.slinph.ihairhelmet4.internet.net_utis.HttpUtils;
import com.slinph.ihairhelmet4.internet.net_utis.ResultData;
import com.slinph.ihairhelmet4.utils.MIUIUtils;
import com.slinph.ihairhelmet4.utils.SharePreferencesUtils;
import com.slinph.ihairhelmet4.utils.StaticVariables;
import com.slinph.ihairhelmet4.view.SelectCodePopupWindow;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class VerificationActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_CODE = 111;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 5;
    private static final int REQUEST_CODE_PICK = 5;
    private Button scan_qr;
    private Button skip;
    private int from;
    private SelectCodePopupWindow selectPicPopupWindow;
    private TextView tv_scan_tip;
    private TextView tv_get_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        initViews();

        Intent intent = getIntent();
        from = intent.getIntExtra("from", -1);
        if (from == 555){
            tv_scan_tip.setText("扫描头盔内部或购买的二维码以获取专家服务");
            tv_get_service.setText("获取专家服务");
        }
    }

    public void initViews(){
        scan_qr = (Button) findViewById(R.id.scan_QR);
        skip = (Button) findViewById(R.id.skip);
        tv_scan_tip = (TextView) findViewById(R.id.tv_scan_tip);
        tv_get_service = (TextView) findViewById(R.id.tv_get_service);
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
                selectPicPopupWindow = new SelectCodePopupWindow(VerificationActivity.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()){
                            case R.id.takeCodeBtn:
                                /*AlertDialog.Builder builder2 = new AlertDialog.Builder(VerificationActivity.this);
                                builder2.setTitle("提示：")
                                        .setMessage("二维码扫描后即被绑定且无法取消，请确认后继续")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {*/
                                                if (ContextCompat.checkSelfPermission(VerificationActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                                                    if(ActivityCompat.shouldShowRequestPermissionRationale(VerificationActivity.this,Manifest.permission.READ_CONTACTS)){

                                                    }else {
                                                        ActivityCompat.requestPermissions(VerificationActivity.this,new String[]{Manifest.permission.CAMERA},MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                                                    }
                                                }else {
                                                    Intent intent = new Intent(VerificationActivity.this, CaptureActivity.class);
                                                    startActivityForResult(intent, REQUEST_CODE);
                                                }
                                        /*    }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }) .show();*/
                                break;
                            case R.id.pickCodeBtn:
                                boolean miui = MIUIUtils.isMIUI();
                                Intent selectIntent;
                                if (miui){
                                    selectIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                }else {
                                    selectIntent = new Intent(Intent.ACTION_PICK);
                                }
                                selectIntent.setType("image/*");
                                startActivityForResult(selectIntent, REQUEST_CODE_PICK);
                                break;
                            case R.id.cancelCodeBtn:
                                selectPicPopupWindow.dismiss();
                                //Toast.makeText(VerificationActivity.this, "cancelBtn", Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                });
                View check_layout = findViewById(R.id.code);
                selectPicPopupWindow.showAtLocation(check_layout, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                /*AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
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
                        }) .show();*/
                break;
            case R.id.skip:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                    builder.setTitle("提示：")
                                           .setMessage("跳过该步骤只能使用体验模式，是否继续？")
                                           .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialog, int which) {
                                                   if (from == 555){
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
                    final String result = bundle.getString(CodeUtils.RESULT_STRING);
                    //Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                    if (result != null){
                        if (from == 555){
                            AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
                            builder.setTitle("提示：")
                                    .setMessage("请选择是否绑定专家服务 \n注意：确认绑定后无法取消")
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Anthentication(result);
                                        }
                                    }) .show();
                        }else {
                            if (result.startsWith("H01CN") || result.startsWith("H02CN") || result.startsWith("HACCN")){
                                AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
                                builder.setTitle("提示：")
                                        .setMessage("您的头盔型号是LTD200S，请选择是否绑定专家服务 \n注意：确认绑定后无法取消")
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Anthentication(result);
                                            }
                                        }) .show();
                            }else if (result.startsWith("H11") || result.startsWith("H12")){
                                boolean helmet_version = SharePreferencesUtils.getBoolean(VerificationActivity.this, "helmet_version");
                                if (helmet_version){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
                                    builder.setTitle("提示：")
                                            .setMessage("您的头盔型号是LTD88LITE，请前往微信咨询购买服务")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    SharePreferencesUtils.putBoolean(VerificationActivity.this,"helmet_version",true);
                                                    Intent intent = new Intent(VerificationActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }) .show();
                                }else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
                                    builder.setTitle("提示：")
                                            .setMessage("您的头盔型号是LTD88LITE，点击确认进入主页")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    SharePreferencesUtils.putBoolean(VerificationActivity.this,"helmet_version",true);
                                                    Intent intent = new Intent(VerificationActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    StaticVariables.HELMET_VERSION = 0;
                                                    finish();
                                                }
                                            })
                                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            }).show();
                                }
                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
                                builder.setTitle("提示：")
                                        .setMessage("二维码信息错误，请使用正确的二维码")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }) .show();
                                selectPicPopupWindow.dismiss();
                            }
                        }
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("提示：")
                                .setMessage("解析二维码失败，请重试")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }) .show();
                        selectPicPopupWindow.dismiss();
                    }

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("提示：")
                           .setMessage("解析二维码失败，请重试")
                           .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {

                               }
                           }) .show();
                    selectPicPopupWindow.dismiss();
                }
            }
        }else if (requestCode == REQUEST_CODE_PICK){
            if (data != null) {
                if (MIUIUtils.isMIUI()) {
                    Uri localUri = data.getData();
                    String scheme = localUri.getScheme();
                    String imagePath = "";
                    if ("content".equals(scheme)) {
                        String[] filePathColumns = {MediaStore.Images.Media.DATA};
                        Cursor c = getContentResolver().query(localUri, filePathColumns, null, null, null);
                        if (c != null) {
                            c.moveToFirst();
                            int columnIndex = c.getColumnIndex(filePathColumns[0]);
                            imagePath = c.getString(columnIndex);
                            c.close();
                        }
                    } else if ("file".equals(scheme)) {//小米4选择云相册中的图片是根据此方法获得路径
                        imagePath = localUri.getPath();
                    }
                    Log.e("picturePath", imagePath);
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

                    CodeUtils.analyzeBitmap(bitmap, new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, final String result) {
                            if (result != null){
                                if (result.startsWith("H01CN") || result.startsWith("H02CN") || result.startsWith("HACCN")){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
                                    builder.setTitle("提示：")
                                            .setMessage("您的头盔型号是LTD200S，点击确定绑定专家服务")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Anthentication(result);
                                                }
                                            }) .show();
                                }else if (result.startsWith("H11") || result.startsWith("H12")){
                                    boolean helmet_version = SharePreferencesUtils.getBoolean(VerificationActivity.this, "helmet_version");
                                    if (helmet_version){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
                                        builder.setTitle("提示：")
                                                .setMessage("您的头盔型号是LTD88LITE，请前往微信咨询购买服务")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        SharePreferencesUtils.putBoolean(VerificationActivity.this,"helmet_version",true);
                                                        Intent intent = new Intent(VerificationActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }) .show();
                                    }else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
                                        builder.setTitle("提示：")
                                                .setMessage("您的头盔型号是LTD88LITE，点击确认进入主页")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        SharePreferencesUtils.putBoolean(VerificationActivity.this,"helmet_version",true);
                                                        Intent intent = new Intent(VerificationActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                        StaticVariables.HELMET_VERSION = 0;
                                                        finish();
                                                    }
                                                }) .show();
                                    }
                                }else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
                                    builder.setTitle("提示：")
                                            .setMessage("二维码信息错误，请使用正确的二维码")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            }) .show();
                                }
                            }
                            selectPicPopupWindow.dismiss();
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
                            builder.setTitle("提示：")
                                    .setMessage("解析二维码失败，请重试")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }) .show();
                            selectPicPopupWindow.dismiss();
                        }
                    });

                    if (bitmap != null){
                        bitmap.recycle();
                    }
                }else {
                    String picturePath = "";
                    try {
                        Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                        String[] filePathColumn = { MediaStore.Images.Media.DATA };
                        Cursor cursor =getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        if (cursor != null){
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            //获取照片路径
                            picturePath = cursor.getString(columnIndex);
                            Log.e("picturePath",picturePath);
                            cursor.close();
                        }
                        if (!picturePath.isEmpty()){
                            Bitmap bitmap= BitmapFactory.decodeFile(picturePath);
                            CodeUtils.analyzeBitmap(bitmap, new CodeUtils.AnalyzeCallback() {
                                @Override
                                public void onAnalyzeSuccess(Bitmap mBitmap, final String result) {
                                    //Toast.makeText(VerificationActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                                    if (result != null){
                                        if (from == 555){
                                            AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
                                            builder.setTitle("提示：")
                                                    .setMessage("请选择是否绑定专家服务 \n注意：确认绑定后无法取消")
                                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                        }
                                                    })
                                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Anthentication(result);
                                                        }
                                                    }) .show();
                                        }else {
                                            if (result.startsWith("H01CN") || result.startsWith("H02CN") || result.startsWith("HACCN")){
                                                AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
                                                builder.setTitle("提示：")
                                                        .setMessage("您的头盔型号是LTD200S，请选择是否绑定专家服务 \n注意：确认绑定后无法取消")
                                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                            }
                                                        })
                                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Anthentication(result);
                                                            }
                                                        }) .show();
                                            }else if (result.startsWith("H11") || result.startsWith("H12")){
                                                boolean helmet_version = SharePreferencesUtils.getBoolean(VerificationActivity.this, "helmet_version");
                                                if (helmet_version){
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
                                                    builder.setTitle("提示：")
                                                            .setMessage("您的头盔型号是LTD88LITE，请前往微信咨询购买服务")
                                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    SharePreferencesUtils.putBoolean(VerificationActivity.this,"helmet_version",true);
                                                                    Intent intent = new Intent(VerificationActivity.this, MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            }) .show();
                                                }else {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
                                                    builder.setTitle("提示：")
                                                            .setMessage("您的头盔型号是LTD88LITE，点击确认进入主页")
                                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    SharePreferencesUtils.putBoolean(VerificationActivity.this,"helmet_version",true);
                                                                    Intent intent = new Intent(VerificationActivity.this, MainActivity.class);
                                                                    startActivity(intent);
                                                                    StaticVariables.HELMET_VERSION = 0;
                                                                    finish();
                                                                }
                                                            })
                                                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {

                                                                }
                                                            }).show();
                                                }
                                            }else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
                                                builder.setTitle("提示：")
                                                        .setMessage("二维码信息错误，请使用正确的二维码")
                                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                            }
                                                        }) .show();
                                                selectPicPopupWindow.dismiss();
                                            }
                                        }
                                    }else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
                                        builder.setTitle("提示：")
                                                .setMessage("解析二维码失败，请重试")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                }) .show();
                                        selectPicPopupWindow.dismiss();
                                    }
                                    selectPicPopupWindow.dismiss();
                                }

                                @Override
                                public void onAnalyzeFailed() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(VerificationActivity.this);
                                    builder.setTitle("提示：")
                                            .setMessage("解析二维码失败，请重试")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            }) .show();
                                    selectPicPopupWindow.dismiss();
                                }
                            });

                            if (bitmap != null){
                                bitmap.recycle();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else{

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
