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
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.slinph.ihairhelmet4.R;
import com.slinph.ihairhelmet4.internet.Url;
import com.slinph.ihairhelmet4.internet.net_utis.HttpUtils;
import com.slinph.ihairhelmet4.internet.net_utis.ResultData;
import com.slinph.ihairhelmet4.utils.MIUIUtils;
import com.slinph.ihairhelmet4.utils.StaticVariables;
import com.slinph.ihairhelmet4.view.SelectCodePopupWindow;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class ScanActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 111;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 5;
    private static int REQUEST_CODE_PICK = 4;
    private SelectCodePopupWindow selectPicPopupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Button scan = (Button) findViewById(R.id.scan);
        TextView tv_left_time = (TextView) findViewById(R.id.tv_left_time);
        if (tv_left_time != null){
            if (StaticVariables.LEFT_TIMES == -1){
                StaticVariables.LEFT_TIMES = 0;
            }
            tv_left_time.setText(Html.fromHtml("当前剩余的专家服务次数是：<font color='#ff0000'><big><big>"+ StaticVariables.LEFT_TIMES +"</big></big></font> 次"));
        }
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

                    selectPicPopupWindow = new SelectCodePopupWindow(ScanActivity.this, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()){
                                case R.id.takeCodeBtn:
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
                                    break;
                            }

                        }
                    });
                    View check_layout = findViewById(R.id.scan_code);
                    selectPicPopupWindow.showAtLocation(check_layout, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
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
                    Anthentication(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                }
            }
        }else if (requestCode == REQUEST_CODE_PICK) {
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
                            //Toast.makeText(ScanActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                            Anthentication(result);
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
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
                                public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                                    //Toast.makeText(ScanActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                                    Anthentication(result);
                                    selectPicPopupWindow.dismiss();
                                }

                                @Override
                                public void onAnalyzeFailed() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
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
            }
            /*if (data != null) {
                Uri uri = data.getData();
                ContentResolver cr = getContentResolver();
                try {
                    Bitmap mBitmap = MediaStore.Images.Media.getBitmap(cr, uri);//显得到bitmap图片

                    CodeUtils.analyzeBitmap(mBitmap, new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            Toast.makeText(ScanActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(ScanActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                        }
                    });

                    if (mBitmap != null) {
                        mBitmap.recycle();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
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
