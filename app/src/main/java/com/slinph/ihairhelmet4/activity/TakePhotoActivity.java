package com.slinph.ihairhelmet4.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.slinph.ihairhelmet4.R;
import com.slinph.ihairhelmet4.internet.Url;
import com.slinph.ihairhelmet4.internet.Vo.UploadBackVO;
import com.slinph.ihairhelmet4.internet.net_utis.DataConverter;
import com.slinph.ihairhelmet4.internet.net_utis.HttpUtils;
import com.slinph.ihairhelmet4.internet.net_utis.ResultData;
import com.slinph.ihairhelmet4.utils.MIUIUtils;
import com.slinph.ihairhelmet4.utils.SharePreferencesUtils;
import com.slinph.ihairhelmet4.utils.StaticVariables;
import com.slinph.ihairhelmet4.utils.StringUtils;
import com.slinph.ihairhelmet4.utils.TimeUtils;
import com.slinph.ihairhelmet4.utils.UIUtils;
import com.slinph.ihairhelmet4.view.SelectPicPopupWindow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 拍照上传
 */
public class TakePhotoActivity extends Activity implements View.OnClickListener{
    private ImageButton imgTop,imgHairLine,imgRear,imgSide;
    private static Uri topUri = null,hairLineUri = null,rearUri = null,sideUri = null;
    private static File[] files = new File[4];
    private Button btn_next;
    private boolean isFirst=false;
    private int width_image;
    private int height_image;
    private ArrayList<String> pathList4Photo;
    private String fileName_top;
    private String fileName_hairline;
    private String fileName_rear;
    private String fileName_side;
    private ArrayList<Bitmap> bitmaps;
    private ImageButton iv_back;
    private String where;
    private ImageButton ib_takephoto_back;
    private File file1;
    private File file2;
    private File file3;
    private ArrayList<String> keys;
    private ArrayList<String> values;
    private boolean isFirstInquiry;
    private List<String[]> followUpArrayList;
    private List<String[]> firstInquiryArrayList;
    private File file4;
    SelectPicPopupWindow selectPicPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        findViews();

        keys = new ArrayList<>();
        values = new ArrayList<>();

        Intent intent = getIntent();
        where = intent.getStringExtra("Where");
        if (where != null){
            if ("FollowUp".equals(where)){
                isFirstInquiry = false;
                Bundle extras = intent.getExtras();
                HashMap fu_info = (HashMap) extras.get("FU_Info");
                followUpArrayList = StringUtils.MapToStringArrayList(fu_info);
                //Log.e("followUpArrayList",fu_info.toString());

            }else if("FirstInquiry".equals(where)){
                isFirstInquiry = true;
                Bundle extras = intent.getExtras();
                HashMap first_info = (HashMap) extras.get("First_Info");
                //Log.e("first_info",first_info.toString());
                firstInquiryArrayList = StringUtils.MapToStringArrayList(first_info);
            }else if ("Info_Saved".equals(where)){
                isFirstInquiry = true;
                HashMap mapFromSp = StringUtils.getMapFromSp(this);
                firstInquiryArrayList = StringUtils.MapToStringArrayList(mapFromSp);
            }
        }

        bitmaps = new ArrayList<>();
        //获取保存的图片路径的集合
        pathList4Photo =  (ArrayList<String>) SharePreferencesUtils.getStrListValue(this, "Path4Photo");
        if (pathList4Photo == null){
            pathList4Photo = new ArrayList<>();
        }

        //创建保存图片的文件夹
        File file = new File(Environment.getExternalStorageDirectory()+"/ihelmet/");
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * 判断SD卡是否存在
     */
     /*public static boolean hasSdcard() {
             String status = Environment.getExternalStorageState();
             if (status.equals(Environment.MEDIA_MOUNTED)) {
                     return true;
                 } else {
                     return false;
                 }
      }*/

    protected void findViews() {
        ib_takephoto_back = (ImageButton) findViewById(R.id.ib_takephoto_back);
        imgTop= (ImageButton) findViewById(R.id.img_top);
        imgHairLine= (ImageButton) findViewById(R.id.img_hair_line);
        imgRear= (ImageButton) findViewById(R.id.img_rear);
        imgSide= (ImageButton) findViewById(R.id.img_side);
        btn_next=(Button)findViewById(R.id.btn_next);
        width_image=imgTop.getHeight();
        height_image=imgTop.getWidth();
        imgTop.setOnClickListener(this);
        imgHairLine.setOnClickListener(this);
        imgRear.setOnClickListener(this);
        imgSide.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        ib_takephoto_back.setOnClickListener(this);
    }

    public void showPicturePopupWindow(final View bt){
        selectPicPopupWindow = new SelectPicPopupWindow(TakePhotoActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 隐藏弹出窗口
                selectPicPopupWindow.dismiss();
                switch (v.getId()) {
                    case R.id.takePhotoBtn:// 拍照
                        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        switch (bt.getId()){
                            case R.id.img_top:
                                //imgTop.setClickable(false);
                                //创建img_top文件
                                fileName_top = Environment.getExternalStorageDirectory()+"/ihelmet/"+ TimeUtils.getDateToStringYYMMddHHmmss(System.currentTimeMillis())+"top.jpg";
                                file1 = new File(fileName_top);
                                pathList4Photo.add(fileName_top);
                                Log.e("创建img_top文件", fileName_top);
                                if(!file1.exists())
                                    try {
                                        file1.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file1));
                                startActivityForResult(takeIntent, 1);
                                break;
                            case R.id.img_hair_line:
                                //imgTop.setClickable(false);
                                fileName_hairline = Environment.getExternalStorageDirectory()+"/ihelmet/"+ TimeUtils.getDateToStringYYMMddHHmmss(System.currentTimeMillis())+"hair.jpg";
                                file2 = new File(fileName_hairline);

                                pathList4Photo.add(fileName_hairline);
                                Log.e("创建img_top文件", fileName_hairline);

                                if(!file2.exists())
                                    try {
                                        file2.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file2));
                                startActivityForResult(takeIntent, 2);
                                break;
                            case R.id.img_rear:
                                //imgTop.setClickable(false);
                                fileName_rear = Environment.getExternalStorageDirectory()+"/ihelmet/"+ TimeUtils.getDateToStringYYMMddHHmmss(System.currentTimeMillis())+"rear.jpg";
                                file3 = new File(fileName_rear);

                                pathList4Photo.add(fileName_rear);
                                Log.e("创建img_top文件", fileName_rear);

                                if(!file3.exists())
                                    try {
                                        file3.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file3));
                                startActivityForResult(takeIntent, 3);
                                break;
                            case R.id.img_side:
                                //imgTop.setClickable(false);
                                fileName_side = Environment.getExternalStorageDirectory()+"/ihelmet/"+ TimeUtils.getDateToStringYYMMddHHmmss(System.currentTimeMillis())+"side.jpg";
                                file4 = new File(fileName_side);

                                pathList4Photo.add(fileName_side);
                                Log.e("创建img_top文件", fileName_side);

                                if(!file4.exists())
                                    try {
                                        file4.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file4));
                                startActivityForResult(takeIntent, 4);
                                break;
                        }
                        break;
                    case R.id.pickPhotoBtn:// 相册选择图片
                        boolean miui = MIUIUtils.isMIUI();
                        Intent selectIntent;
                        if (miui){
                            selectIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        }else {
                            selectIntent = new Intent(Intent.ACTION_PICK);
                        }
                        switch (bt.getId()){
                            case R.id.img_top:
                                selectIntent.setType("image/*");
                                startActivityForResult(selectIntent, 5);
                                break;
                            case R.id.img_hair_line:
                                selectIntent.setType("image/*");
                                startActivityForResult(selectIntent, 6);
                                break;
                            case R.id.img_rear:
                                selectIntent.setType("image/*");
                                startActivityForResult(selectIntent, 7);
                                break;
                            case R.id.img_side:
                                selectIntent.setType("image/*");
                                startActivityForResult(selectIntent, 8);
                                break;
                        }
                        break;
                    case R.id.cancelBtn:// 取消
                        break;
                    default:
                        break;
                }
            }
        });
        View check_layout = findViewById(R.id.takephoto_layout);
        selectPicPopupWindow.showAtLocation(check_layout, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        switch (v.getId()){
            case R.id.img_top:
                showPicturePopupWindow(v);
                break;
            case R.id.img_hair_line:
                showPicturePopupWindow(v);
                break;
            case R.id.img_rear:
                showPicturePopupWindow(v);
                break;
            case R.id.img_side:
                showPicturePopupWindow(v);
                break;
            case R.id.ib_takephoto_back:
                AlertDialog.Builder builder = new AlertDialog.Builder(TakePhotoActivity.this);
                builder.setTitle(R.string.tips)
                        .setMessage("跳过拍照上传不能使用完整功能，是否跳过？")
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                startActivity(new Intent(TakePhotoActivity.this,MainActivity.class));
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;
            case R.id.btn_next:
                //v.setClickable(false);//禁止button再次点击
                if (files.length < 4){
                    Toast.makeText(TakePhotoActivity.this, R.string.take_all_photo, Toast.LENGTH_SHORT).show();
                    return;
                }
                //String s = pathList4Photo.toString();
                //Log.e("保存的图片文件路径",s);
                //SharePreferencesUtils.putStrListValue(this,"Path4Photo",pathList4Photo);

                for (File file:files){
                    if (file != null){
                        Log.e("file.AbsolutePath()--",file.getAbsolutePath());
                    }
                }
                //上传图片与信息
               if (isFirstInquiry){
                   UpLoadFirstInquiry();
               }else {
                   UploadFollowUp();
               }
                //将照片保存到文件系统
                /*new Thread(new Runnable() {
                    private FileOutputStream outputStream;
                    @Override
                    public void run() {
                        for (int i = 0;i < files.length;i ++){
                            try {if (i>=4){}
                                else {
                                outputStream = new FileOutputStream(files[i]);
                                bitmaps.get(i).compress(Bitmap.CompressFormat.JPEG,100, outputStream);
                                outputStream.flush();
                            }
                            }
                            catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).run();*/
                break;
        }
    }

    //上传随诊的报告
    private void UploadFollowUp() {
        final AlertDialog alertDialog = DataConverter.showDialog(this, "正在上传···");
        HttpUtils.upload(TakePhotoActivity.this, Url.rootUrl+"/iheimi/followUp/insert", files, new HttpUtils.ResultCallback<ResultData<UploadBackVO>>() {
            @Override
            public void onError(int statusCode, Throwable error) {
                alertDialog.dismiss();
                Toast.makeText(TakePhotoActivity.this, "网络异常"+statusCode+"0x06", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ResultData<UploadBackVO> response) {
                if (response.getSuccess()){
                    UploadBackVO data = response.getData();
                    if (data != null){
                        String mid = String.valueOf(data.getId());
                        HttpUtils.postAsync(TakePhotoActivity.this, Url.rootUrl+"/iheimi/user/update", new HttpUtils.ResultCallback<ResultData>() {
                                    @Override
                                    public void onError(int statusCode, Throwable error) {
                                        alertDialog.dismiss();
                                        Toast.makeText(TakePhotoActivity.this, "网络异常"+statusCode+"0x07", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onResponse(ResultData response) {
                                        if (response.getSuccess()){
                                            Log.e("mid","上传成功");
                                            alertDialog.dismiss();
                                            Toast.makeText(TakePhotoActivity.this, R.string.upload_success, Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(TakePhotoActivity.this,MainActivity.class));
                                            finish();
                                        }else{
                                            Toast.makeText(TakePhotoActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();
                                        }
                                    }
                                },new String[]{"mid",mid}
                                ,new String[]{"id", StaticVariables.ID});
                    }
                }else {
                    Toast.makeText(TakePhotoActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }
        },followUpArrayList.get(0)
         ,followUpArrayList.get(1)
         ,followUpArrayList.get(2)
         ,followUpArrayList.get(3)
         ,followUpArrayList.get(4)
         ,followUpArrayList.get(5)
         ,followUpArrayList.get(6));
}

    private void UpLoadFirstInquiry() {
        Log.e("四张图片",files.length+"");
        final AlertDialog alertDialog = DataConverter.showDialog(this, "正在上传···");
        HttpUtils.upload(TakePhotoActivity.this, Url.rootUrl+"/iheimi/patient/insert", files, new HttpUtils.ResultCallback<ResultData<UploadBackVO>>() {
                    @Override
                    public void onError(int statusCode, Throwable error) {
                        alertDialog.dismiss();
                        Toast.makeText(TakePhotoActivity.this, error.getMessage()+":"+statusCode, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(ResultData<UploadBackVO> response) {
                        if (response.getSuccess()){
                            SharePreferencesUtils.putBoolean(TakePhotoActivity.this,"InputInfo",true);
                            UploadBackVO data = response.getData();
                            if (data != null){
                                String mid = String.valueOf(data.getId());
                                if (mid != null){
                                    Log.e("mid",mid);
                                    //SharePreferencesUtils.putString(TakePhotoActivity.this,"mid",mid);
                                }else {
                                    Log.e("mid","null");
                                }

                                 HttpUtils.postAsync(TakePhotoActivity.this, Url.rootUrl+"/iheimi/user/update", new HttpUtils.ResultCallback<ResultData>() {
                                            @Override
                                            public void onError(int statusCode, Throwable error) {
                                                alertDialog.dismiss();
                                                Log.e("mid","上传失败"+statusCode);
                                            }

                                            @Override
                                            public void onResponse(ResultData response) {
                                                if (response.getSuccess()){
                                                    alertDialog.dismiss();
                                                    Toast.makeText(TakePhotoActivity.this, R.string.upload_success, Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(TakePhotoActivity.this,MainActivity.class));
                                                    finish();
                                                }
                                            }
                                        },new String[]{"mid",mid}
                                        ,new String[]{"id", StaticVariables.ID});
                            }
                            /*alertDialog.dismiss();
                            Toast.makeText(TakePhotoActivity.this, R.string.upload_success, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(TakePhotoActivity.this,MainActivity.class));
                            finish();*/
                        }
                        else {
                            Toast.makeText(TakePhotoActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    }
                }
                ,firstInquiryArrayList.get(0)
                ,firstInquiryArrayList.get(1)
                ,firstInquiryArrayList.get(2)
                ,firstInquiryArrayList.get(3)
                ,firstInquiryArrayList.get(4)
                ,firstInquiryArrayList.get(5)
                ,firstInquiryArrayList.get(6)
                ,firstInquiryArrayList.get(7)
                ,firstInquiryArrayList.get(8)
                ,firstInquiryArrayList.get(9)
                ,firstInquiryArrayList.get(10)
                ,firstInquiryArrayList.get(11)
                ,firstInquiryArrayList.get(12)
                ,firstInquiryArrayList.get(13)
                ,firstInquiryArrayList.get(14)
                ,firstInquiryArrayList.get(15)
                ,firstInquiryArrayList.get(16)
                ,firstInquiryArrayList.get(17)
                ,firstInquiryArrayList.get(18)
                ,firstInquiryArrayList.get(19)
                ,firstInquiryArrayList.get(20)
                ,firstInquiryArrayList.get(21)
                ,firstInquiryArrayList.get(22)
                ,firstInquiryArrayList.get(23)
                ,firstInquiryArrayList.get(24)
                ,firstInquiryArrayList.get(25)
                ,firstInquiryArrayList.get(26)
                ,firstInquiryArrayList.get(27)
                ,firstInquiryArrayList.get(28)
                ,firstInquiryArrayList.get(29)
                ,firstInquiryArrayList.get(30)
                ,firstInquiryArrayList.get(31)
                ,firstInquiryArrayList.get(32)
                ,firstInquiryArrayList.get(33)
                ,firstInquiryArrayList.get(34)
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String picturePath = "";
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK){
                    Bitmap scaleBitmap = getScaleBitmap(this, fileName_top);
                    imgTop.setImageBitmap(scaleBitmap);
                    String s = saveBitmapToFile(file1, file1.getAbsolutePath());
                    if (s != null){
                        files[0] = new File(s);
                    }
                }
                break;
            case 2:
                if (resultCode == RESULT_OK){
                    Bitmap scaleBitmap = getScaleBitmap(this, fileName_hairline);
                    imgHairLine.setImageBitmap(scaleBitmap);
                    String s = saveBitmapToFile(file2, file2.getAbsolutePath());
                    if (s != null) {
                        files[1] = new File(s);
                    }
                }
                break;
            case 3:
                if (resultCode == RESULT_OK){
                    Bitmap scaleBitmap = getScaleBitmap(this, fileName_rear);
                    imgRear.setImageBitmap(scaleBitmap);
                    String s = saveBitmapToFile(file3, file3.getAbsolutePath());
                    if (s != null) {
                        files[2] = new File(s);
                    }
                }
                break;
            case 4:
                if (resultCode == RESULT_OK){
                    Bitmap scaleBitmap = getScaleBitmap(this, fileName_side);
                    imgSide.setImageBitmap(scaleBitmap);
                    String s = saveBitmapToFile(file4, file4.getAbsolutePath());
                    if (s != null) {
                        files[3] = new File(s);
                    }
                }
                break;
            case 5:
                if (resultCode == RESULT_OK){
                    if (MIUIUtils.isMIUI()){
                        Uri localUri = data.getData();
                        String scheme = localUri.getScheme();
                        String imagePath = "";
                        if("content".equals(scheme)){
                            String[] filePathColumns = {MediaStore.Images.Media.DATA};
                            Cursor c = getContentResolver().query(localUri, filePathColumns, null, null, null);
                            if (c != null){
                                c.moveToFirst();
                                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                                imagePath = c.getString(columnIndex);
                                c.close();
                            }
                        }else if("file".equals(scheme)){//小米4选择云相册中的图片是根据此方法获得路径
                            imagePath = localUri.getPath();
                        }
                        Log.e("picturePath",imagePath);
                        Bitmap scaleBitmap = getScaleBitmap(this, imagePath);
                        imgTop.setImageBitmap(scaleBitmap);
                        Log.e("bitmap的尺寸",scaleBitmap.getByteCount()+"");
                        files[0] = new File(imagePath);
                    }else {
                        try {
                            Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                            String[] filePathColumn = { MediaStore.Images.Media.DATA };
                            Cursor cursor =getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                            if (cursor != null){
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                picturePath = cursor.getString(columnIndex);  //获取照片路径
                                Log.e("picturePath",picturePath);
                                cursor.close();
                            }
                            if (!picturePath.isEmpty()){
                                Bitmap bitmap= getScaleBitmap(this,picturePath);
                                File f1 = new File(picturePath);
                                String s = saveBitmapToFile(f1 ,f1.getAbsolutePath());
                                imgTop.setImageBitmap(bitmap);
                                files[0] = new File(s);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case 6:
                if (resultCode == RESULT_OK){
                    if (MIUIUtils.isMIUI()){
                        Uri localUri = data.getData();
                        String scheme = localUri.getScheme();
                        String imagePath = "";
                        if("content".equals(scheme)){
                            String[] filePathColumns = {MediaStore.Images.Media.DATA};
                            Cursor c = getContentResolver().query(localUri, filePathColumns, null, null, null);
                            if (c != null){
                                c.moveToFirst();
                                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                                imagePath = c.getString(columnIndex);
                                c.close();
                            }
                        }else if("file".equals(scheme)){//小米4选择云相册中的图片是根据此方法获得路径
                            imagePath = localUri.getPath();
                        }
                        Log.e("picturePath",imagePath);
                        Bitmap scaleBitmap = getScaleBitmap(this, imagePath);
                        imgHairLine.setImageBitmap(scaleBitmap);
                        files[1] = new File(imagePath);
                        Log.e("bitmap的尺寸",scaleBitmap.getByteCount()+"");
                    }else {
                        try {
                            Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                            String[] filePathColumn = { MediaStore.Images.Media.DATA };
                            Cursor cursor =getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                            if (cursor != null){
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                picturePath = cursor.getString(columnIndex);  //获取照片路径
                                Log.e("picturePath",picturePath);
                                cursor.close();
                            }
                            if (!picturePath.isEmpty()){
                                Bitmap bitmap= getScaleBitmap(this,picturePath);
                                File f2 = new File(picturePath);
                                String s = saveBitmapToFile(f2 ,f2.getAbsolutePath());
                                imgHairLine.setImageBitmap(bitmap);
                                files[1] = new File(s);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case 7:
                if (resultCode == RESULT_OK){
                    if (MIUIUtils.isMIUI()){
                        Uri localUri = data.getData();
                        String scheme = localUri.getScheme();
                        String imagePath = "";
                        if("content".equals(scheme)){
                            String[] filePathColumns = {MediaStore.Images.Media.DATA};
                            Cursor c = getContentResolver().query(localUri, filePathColumns, null, null, null);
                            if (c != null){
                                c.moveToFirst();
                                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                                imagePath = c.getString(columnIndex);
                                c.close();
                            }
                        }else if("file".equals(scheme)){//小米4选择云相册中的图片是根据此方法获得路径
                            imagePath = localUri.getPath();
                        }
                        Log.e("picturePath",imagePath);
                        Bitmap scaleBitmap = getScaleBitmap(this, imagePath);
                        imgRear.setImageBitmap(scaleBitmap);
                        files[2] = new File(imagePath);
                        Log.e("bitmap的尺寸",scaleBitmap.getByteCount()+"");
                    }else {
                        try {
                            Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                            String[] filePathColumn = { MediaStore.Images.Media.DATA };
                            Cursor cursor =getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                            if (cursor != null){
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                picturePath = cursor.getString(columnIndex);  //获取照片路径
                                Log.e("picturePath",picturePath);
                                cursor.close();
                            }
                            if (!picturePath.isEmpty()){
                                Bitmap bitmap= getScaleBitmap(this,picturePath);
                                File f3 = new File(picturePath);
                                String s = saveBitmapToFile(f3 ,f3.getAbsolutePath());
                                imgRear.setImageBitmap(bitmap);
                                files[2] = new File(s);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case 8:
                if (resultCode == RESULT_OK){
                    if (MIUIUtils.isMIUI()){
                        Uri localUri = data.getData();
                        String scheme = localUri.getScheme();
                        String imagePath = "";
                        if("content".equals(scheme)){
                            String[] filePathColumns = {MediaStore.Images.Media.DATA};
                            Cursor c = getContentResolver().query(localUri, filePathColumns, null, null, null);
                            if (c != null){
                                c.moveToFirst();
                                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                                imagePath = c.getString(columnIndex);
                                c.close();
                            }
                        }else if("file".equals(scheme)){//小米选择云相册中的图片是根据此方法获得路径
                            imagePath = localUri.getPath();
                        }
                        Log.e("picturePath",imagePath);
                        Bitmap scaleBitmap = getScaleBitmap(this, imagePath);
                        imgSide.setImageBitmap(scaleBitmap);
                        files[3] = new File(imagePath);
                        Log.e("bitmap的尺寸",scaleBitmap.getByteCount()+"");
                    }else {
                        try {
                            Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                            String[] filePathColumn = { MediaStore.Images.Media.DATA };
                            Cursor cursor =getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                            if (cursor != null){
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                picturePath = cursor.getString(columnIndex);  //获取照片路径
                                Log.e("picturePath",picturePath);
                                cursor.close();
                            }
                            if (!picturePath.isEmpty()){
                                Bitmap bitmap= getScaleBitmap(this,picturePath);
                                File f4 = new File(picturePath);
                                String s = saveBitmapToFile(f4 ,f4.getAbsolutePath());
                                imgSide.setImageBitmap(bitmap);
                                files[3] = new File(s);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }

        if(topUri!=null){
            imgTop.setImageURI(topUri);
        }
        if(hairLineUri!=null){
            imgHairLine.setImageURI(hairLineUri);
        }
        if(rearUri!=null){
            imgRear.setImageURI(rearUri);
        }
        if(sideUri!=null){
            imgSide.setImageURI(sideUri);
        }
    }

    public static Bitmap getScaleBitmap(Context ctx, String filePath) {
        /*BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, opt);

        int bmpWidth = opt.outWidth;
        int bmpHeght = opt.outHeight;

        WindowManager windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        int heightPixels = displayMetrics.heightPixels;
        int widthPixels = displayMetrics.widthPixels;

        *//*int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();*//*

        opt.inSampleSize = 1;
        if (bmpWidth > bmpHeght) {
            if (bmpWidth > widthPixels)
                opt.inSampleSize = bmpWidth / widthPixels;
        } else {
            if (bmpHeght > heightPixels)
                opt.inSampleSize = bmpHeght / heightPixels;
        }
        opt.inJustDecodeBounds = false;

        bmp = BitmapFactory.decodeFile(filePath, opt);
        return bmp;*/


        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, UIUtils.dp2px(150), UIUtils.dp2px(150));
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeFile(filePath, options);
        return createScaleBitmap(src, UIUtils.dp2px(150), UIUtils.dp2px(200));
    }

    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth,
                                            int dstHeight) {
        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
        if (src != dst) { // 如果没有缩放，那么不回收
            src.recycle(); // 释放Bitmap的native像素数组
        }
        return dst;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


    public static String saveBitmapToFile(File file, String newpath) {
        try {
            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image
            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();
            // The new size we want to scale to
            final int REQUIRED_SIZE = 75;
            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);
            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            File aa = new File(newpath);
            FileOutputStream outputStream = new FileOutputStream(aa);
            //choose another format if PNG doesn't suit you
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            String filepath = aa.getAbsolutePath();
            Log.e("getAbsolutePath", aa.getAbsolutePath());
            return filepath;
        } catch (Exception e) {
            return null;
        }
    }
}
