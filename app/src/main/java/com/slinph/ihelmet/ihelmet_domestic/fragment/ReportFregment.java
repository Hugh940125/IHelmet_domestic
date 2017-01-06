package com.slinph.ihelmet.ihelmet_domestic.fragment;

import android.app.Activity;
import android.app.Fragment;
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
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.slinph.ihelmet.ihelmet_domestic.R;
import com.slinph.ihelmet.ihelmet_domestic.activity.FollowUpActivity;
import com.slinph.ihelmet.ihelmet_domestic.activity.ImagePagerActivity;
import com.slinph.ihelmet.ihelmet_domestic.internet.Url;
import com.slinph.ihelmet.ihelmet_domestic.internet.Vo.QualifiedVO;
import com.slinph.ihelmet.ihelmet_domestic.internet.model.Patient;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.DataConverter;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.HttpUtils;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.ResultData;
import com.slinph.ihelmet.ihelmet_domestic.utils.MIUIUtils;
import com.slinph.ihelmet.ihelmet_domestic.utils.StaticVariables;
import com.slinph.ihelmet.ihelmet_domestic.utils.TimeUtils;
import com.slinph.ihelmet.ihelmet_domestic.utils.UIUtils;
import com.slinph.ihelmet.ihelmet_domestic.view.ImageGridAdapter_1;
import com.slinph.ihelmet.ihelmet_domestic.view.ImageGridAdapter_2;
import com.slinph.ihelmet.ihelmet_domestic.view.NoScrollGridView;
import com.slinph.ihelmet.ihelmet_domestic.view.SelectPicPopupWindow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/25.
 * h
 */
public class ReportFregment extends Fragment {

    private Context mContext;
    private View inflate;
    private TextView tv_time;
    private TextView tv_hair_loss_type;
    private TextView tv_hair_loss_degree;
    private TextView tv_hair_loss_disease;
    private TextView tv_hair_count;
    private NoScrollGridView gv_classification;
    private LinearLayout ll_my_schedule;
    private TextView tv_expert_suggest;
    private NoScrollGridView gv_photo;
    private ArrayList<String> mImageUrls;
    private ImageGridAdapter_1 mImageGridAdapter;
    private TextView tv_check_status;
    private TextView tv_check1;
    private TextView tv_check2;
    private TextView tv_check3;
    private TextView tv_check4;
    private Button bt_check1;
    private Button bt_check2;
    private Button bt_check3;
    private Button bt_check4;
    private SimpleDraweeView new_check1;
    private SimpleDraweeView new_check2;
    private SimpleDraweeView new_check3;
    private SimpleDraweeView new_check4;
    private int clicked;
    private ArrayList<File> filesList = new ArrayList<>();
    private Integer report_type;
    private Button bt_updatephoto;
    private Long report_id;
    private String position;
    private List<String> typeList = new ArrayList<>();
    private int length;
    private String fileName1;
    private File imageFile1;
    private String fileName2;
    private File imageFile2;
    private String fileName3;
    private File imageFile3;
    private String fileName4;
    private File imageFile4;
    private int count;
    private File[] files;
    private AlertDialog alertDialog1;
    private AlertDialog alertDialog2;
    private Button bt_want_followup;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (StaticVariables.USER_IS_SHOW_WAIT){
            inflate = inflater.inflate(R.layout.fragment_up_data_success,container,false);
            tv_check_status = (TextView) inflate.findViewById(R.id.tv_check_status);
            tv_check1 = (TextView) inflate.findViewById(R.id.tv_check1);
            tv_check2 = (TextView) inflate.findViewById(R.id.tv_check2);
            tv_check3 = (TextView) inflate.findViewById(R.id.tv_check3);
            tv_check4 = (TextView) inflate.findViewById(R.id.tv_check4);
            bt_check1 = (Button) inflate.findViewById(R.id.bt_check1);
            bt_check2 = (Button) inflate.findViewById(R.id.bt_check2);
            bt_check3 = (Button) inflate.findViewById(R.id.bt_check3);
            bt_check4 = (Button) inflate.findViewById(R.id.bt_check4);
            new_check1 = (SimpleDraweeView) inflate.findViewById(R.id.new_check1);
            new_check2 = (SimpleDraweeView) inflate.findViewById(R.id.new_check2);
            new_check3 = (SimpleDraweeView) inflate.findViewById(R.id.new_check3);
            new_check4 = (SimpleDraweeView) inflate.findViewById(R.id.new_check4);
            bt_updatephoto = (Button) inflate.findViewById(R.id.bt_updatephoto);
        }
        if (inflate == null){
            inflate = inflater.inflate(R.layout.fragment_report, container, false);
            tv_time = (TextView) inflate.findViewById(R.id.tv_time);
            tv_hair_loss_type = (TextView) inflate.findViewById(R.id.tv_hair_loss_type);
            tv_hair_loss_degree = (TextView) inflate.findViewById(R.id.tv_hair_degree);
            tv_hair_loss_disease = (TextView) inflate.findViewById(R.id.tv_hair_disease);
            tv_hair_count = (TextView) inflate.findViewById(R.id.tv_hair_count);
            gv_classification = (NoScrollGridView) inflate.findViewById(R.id.gv_classification);
            ll_my_schedule = (LinearLayout) inflate.findViewById(R.id.ll_my_schedule);
            tv_expert_suggest = (TextView) inflate.findViewById(R.id.tv_hair_suggest);
            gv_photo = (NoScrollGridView) inflate.findViewById(R.id.gv_photo);
            bt_want_followup = (Button) inflate.findViewById(R.id.bt_want_followup);
            if (bt_want_followup != null){
                bt_want_followup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StaticVariables.TIME_DISTANCE_OK && StaticVariables.IS_CAN_FOLLOWUP){
                            startActivity(new Intent(mContext, FollowUpActivity.class));
                        }else {
                            String mes = "";
                            if (!StaticVariables.TIME_DISTANCE_OK){
                                mes = "还没到随诊时间，请耐心等待";
                            }
                            if (!StaticVariables.IS_CAN_FOLLOWUP){
                                mes = "您的随诊服务已经用完";
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("提示：")
                                   .setMessage(mes)
                                   .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {

                                       }
                                   })
                                   .show();
                        }
                    }
                });
            }
        }
        return inflate;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (StaticVariables.USER_IS_SHOW_WAIT){
            initCheckData();
        }else {
            initReportData();
        }

        if (bt_updatephoto != null){
            bt_updatephoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (file1 != null){
                        filesList.add(file1);
                    }
                    if (file2 != null){
                        filesList.add(file2);
                    }
                    if (file3 != null){
                        filesList.add(file3);
                    }
                    if (file4 != null){
                        filesList.add(file4);
                    }

                    if (filesList.size()< length){
                        Toast.makeText(mContext, "请重拍所有未审核通过的照片", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //bt_updatephoto.setClickable(false);

                    files = new File[filesList.size()];
                    for (int i=0;i<filesList.size();i++){
                        files[i] = filesList.get(i);
                    }
                    /*StringBuilder stringBuilder = new StringBuilder();
                    for (int j=0;j<typeList.size();j++){
                        int type = Integer.parseInt(typeList.get(j)) + 1;
                        stringBuilder.append(type);
                    }
                    String splice = StringUtils.splice(stringBuilder.toString());
                    Log.e("splice",splice+"_"+report_type);*/
                    count = 0;
                    updatePhoto();
                    filesList.clear();
                }
            });
        }
    }

    public void updatePhoto(){
            if (report_type == 1){
                alertDialog1 = DataConverter.showDialog(mContext, "正在上传···");
                updateType1();
            }else {
                alertDialog2 = DataConverter.showDialog(mContext, "正在上传···");
                updateType2();
            }
    }

    private void updateType2() {
            HttpUtils.upload(mContext, Url.rootUrl + "/iheimi/followUp/updatePhoto", files[count], new HttpUtils.ResultCallback<ResultData>() {
                        @Override
                        public void onError(int statusCode, Throwable error) {
                            alertDialog2.dismiss();
                            Toast.makeText(mContext, "连接错误" + statusCode, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(ResultData response) {
                            if (response.getSuccess()){
                                if (count == typeList.size()-1) {
                                    Log.e("updatePhoto", count + "张成功");
                                    alertDialog1.dismiss();
                                    Toast.makeText(mContext, "更新成功", Toast.LENGTH_SHORT).show();
                                    initCheckData();
                                    getCheckingPhoto();
                                }
                                count = count + 1;
                                if (count < typeList.size()){
                                    Log.e("updateType2","再次调用");
                                    updateType2();
                                }
                            }else {
                                Toast.makeText(mContext, response.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new String[]{"type", typeList.get(count)}
                    , new String[]{"id", StaticVariables.MID});
    }

    private void updateType1() {
            Log.e("count",count+"");
            Log.e("files", files.length+"");
            Log.e("type_list",typeList.get(count));
            Log.e("type_list",files[count].toString());

            HttpUtils.upload(mContext, Url.rootUrl+"/iheimi/patient/updatePhoto",files[count], new HttpUtils.ResultCallback<ResultData>() {
                @Override
                public void onError(int statusCode, Throwable error) {
                    alertDialog1.dismiss();
                    Toast.makeText(mContext, "连接错误" + statusCode, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(ResultData response) {
                    if (response.getSuccess()){
                        if (count == typeList.size()-1) {
                            Log.e("updatePhoto", count + "张成功");
                            alertDialog1.dismiss();
                            Toast.makeText(mContext, "更新成功", Toast.LENGTH_SHORT).show();
                            initCheckData();
                            getCheckingPhoto();
                        }
                        count = count + 1;
                        if (count < typeList.size()){
                            Log.e("updateType1","再次调用");
                            updateType1();
                        }
                    }else {
                        Toast.makeText(mContext, response.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            },new String[]{"type",typeList.get(count)}
            ,new String[]{"id", StaticVariables.MID});
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initCheckData(){
        if (StaticVariables.CHECK_TYPE != 0) {
            HttpUtils.postAsync(mContext, Url.rootUrl + "/iheimi/patientQualified/selectOneByExample", new HttpUtils.ResultCallback<ResultData<QualifiedVO>>() {
                        @Override
                        public void onError(int statusCode, Throwable error) {
                        }

                        @Override
                        public void onResponse(ResultData<QualifiedVO> response) {
                            if (response.getSuccess()) {
                                QualifiedVO data = response.getData();
                                if (data != null) {
                                    report_type = data.getType();
                                    Integer is_qualified = data.getIsQualified();
                                    Log.e("is_qualified", is_qualified + "");
                                    position = data.getPosition();
                                    report_id = data.getId();
                                    getCheckingPhoto();
                                    if (is_qualified != null) {
                                        if (is_qualified == 1) {
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    tv_check_status.setText("信息已经通过审核，请耐心等待诊断报告");
                                                    tv_check1.setText("审核通过");
                                                    tv_check2.setText("审核通过");
                                                    tv_check3.setText("审核通过");
                                                    tv_check4.setText("审核通过");
                                                }
                                            });
                                        } else {
                                            if (position != null && !position.isEmpty()) {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        bt_updatephoto.setVisibility(View.VISIBLE);
                                                        tv_check_status.setText("审核结果已返回，请按照下面的信息重新拍摄审核未通过的照片，完成所有照片拍摄后点击“确认更新”提交审核。");
                                                    }
                                                });
                                                String[] split = position.split(",");
                                                length = split.length;
                                                for (int k = 0; k < length; k++) {
                                                    typeList.add(String.valueOf(Integer.valueOf(split[k]) + 1));
                                                    if ("0".equals(split[k])) {
                                                        getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                tv_check1.setText("审核未通过");
                                                                bt_check1.setVisibility(View.VISIBLE);
                                                            }
                                                        });
                                                        bt_check1.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                showPicturePopupWindow();
                                                                clicked = 0;
                                                            }
                                                        });
                                                    } else if ("1".equals(split[k])) {
                                                        getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                tv_check2.setText("审核未通过");
                                                                bt_check2.setVisibility(View.VISIBLE);
                                                            }
                                                        });
                                                        bt_check2.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                showPicturePopupWindow();
                                                                clicked = 1;
                                                            }
                                                        });
                                                    } else if ("2".equals(split[k])) {
                                                        getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                tv_check3.setText("审核未通过");
                                                                bt_check3.setVisibility(View.VISIBLE);
                                                            }
                                                        });
                                                        bt_check3.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                showPicturePopupWindow();
                                                                clicked = 2;
                                                            }
                                                        });
                                                    } else if ("3".equals(split[k])) {
                                                        getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                tv_check4.setText("审核未通过");
                                                                bt_check4.setVisibility(View.VISIBLE);
                                                            }
                                                        });
                                                        bt_check4.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                showPicturePopupWindow();
                                                                clicked = 3;
                                                            }
                                                        });
                                                    } else {
                                                        getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                tv_check1.setText("审核通过");
                                                                tv_check2.setText("审核通过");
                                                                tv_check3.setText("审核通过");
                                                                tv_check4.setText("审核通过");
                                                            }
                                                        });
                                                    }
                                                }
                                            } else {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        bt_updatephoto.setVisibility(View.INVISIBLE);
                                                    }
                                                });
                                            }
                                        }
                                    }
                                } else {
                                    Log.e("审核信息", "null");
                                }
                            } else {
                                Toast.makeText(getActivity(), response.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new String[]{"patientId", StaticVariables.MID}
                    , new String[]{"type", String.valueOf(StaticVariables.CHECK_TYPE)});
        }
    }

    //获取正在审核的的图片
    private void getCheckingPhoto(){
        Log.e("report_type",report_type+"");
        if (report_type == 1){
            HttpUtils.postAsync(mContext, Url.rootUrl+"/iheimi/patient/selectOneByExample", new HttpUtils.ResultCallback<ResultData<Patient>>() {
                @Override
                public void onError(int statusCode, Throwable error) {
                    Toast.makeText(mContext, "找不到服务器啦"+statusCode, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(ResultData<Patient> response) {
                    if (response.getSuccess()){
                        Patient data = response.getData();
                        if (data != null){
                            final String topViewUrl = data.getTopViewUrl();
                            final String hairlineUrl = data.getHairlineUrl();
                            final String afterViewUrl = data.getAfterViewUrl();
                            final String partialViewUrl = data.getPartialViewUrl();
                            new_check1.setImageURI(topViewUrl);
                            new_check2.setImageURI(hairlineUrl);
                            new_check3.setImageURI(afterViewUrl);
                            new_check4.setImageURI(partialViewUrl);
                        }else {
                            Toast.makeText(mContext, response.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            },new String[]{"id",StaticVariables.MID});
        }else {
            HttpUtils.postAsync(mContext, Url.rootUrl+"/iheimi/followUp/selectOneByExample", new HttpUtils.ResultCallback<ResultData<Patient>>() {
                @Override
                public void onError(int statusCode, Throwable error) {
                    Toast.makeText(mContext, "找不到服务器啦"+statusCode, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(ResultData<Patient> response) {
                    if (response.getSuccess()){
                        Patient data = response.getData();
                        if (data != null){
                            final String topViewUrl = data.getTopViewUrl();
                            final String hairlineUrl = data.getHairlineUrl();
                            final String afterViewUrl = data.getAfterViewUrl();
                            final String partialViewUrl = data.getPartialViewUrl();
                            new_check1.setImageURI(topViewUrl);
                            new_check2.setImageURI(hairlineUrl);
                            new_check3.setImageURI(afterViewUrl);
                            new_check4.setImageURI(partialViewUrl);
                        }else {
                            Toast.makeText(mContext, response.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            },new String[]{"id",StaticVariables.MID});
        }

    }

    //初始化报告的数据
    private void initReportData() {
        ArrayList<String> mPhonoUrls = new ArrayList<>();
        mPhonoUrls.add(StaticVariables.USER_HAIR_TOP_PHOTO_URL);
        mPhonoUrls.add(StaticVariables.USER_HAIR_LINE_PHOTO_URL);
        mPhonoUrls.add(StaticVariables.USER_HAIR_AFTER_PHOTO_URL);
        mPhonoUrls.add(StaticVariables.USER_HAIR_PARTIAL_PHOTO_URL);
        ImageGridAdapter_2 mPhonoGridAdapter = new ImageGridAdapter_2(mContext);
        mPhonoGridAdapter.setUrls(mPhonoUrls);
        gv_photo.setAdapter(mPhonoGridAdapter);
        gv_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ArrayList<String> urls = ((ImageGridAdapter_2) parent.getAdapter()).getUrls();
                imageBrower(position, urls);
            }
        });

        mImageUrls = new ArrayList<>();
        mImageUrls.add(StaticVariables.USER_CLASSIFICATION_PHOTO_URL);
        mImageGridAdapter = new ImageGridAdapter_1(mContext);//ImageGridAdapter_3是自己的改的,原来是ImageGridAdapter
        mImageGridAdapter.setUrls(mImageUrls);
        gv_classification.setAdapter(mImageGridAdapter);
        gv_classification.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ArrayList<String> urls = ((ImageGridAdapter_1) parent.getAdapter()).getUrls();
                imageBrower(position, urls);
            }
        });

        tv_time.setText(StaticVariables.USER_REPORT_TIME);//报告时间
        tv_hair_loss_type.setText(StaticVariables.USER_HAIR_LOSS_TYPE);//脱发类型
        tv_hair_loss_degree.setText(StaticVariables.USER_HAIR_LOSS_DEGREE);//脱发等级
        tv_hair_loss_disease.setText("伴有:" + StaticVariables.USER_HAIR_LOSS_DISEASE);//脱发伴随症状:如红疹,脓包等
        tv_expert_suggest.setText(StaticVariables.USER_EXPERT_SUGGEST);//专家建议
        if(ll_my_schedule.getChildCount()==0){
            treatmentProgram(StaticVariables.USER_ALL_TREATMENT_PROGRAMS);
        }
        StringBuilder sb=new StringBuilder();
        sb.append("")
                //.append(StaticVariables.USER_REPORT_TIME2)
                .append("每隔一天使用一次;<br>")
                //.append(date.getCurrentMonthLastDay())
                .append("使用时请将脱发部位的头皮露出,在头发干燥的情况下使用;<br>")
                //.append(StaticVariables.USER_SUGGEST_LIGHT_TIMES)
                .append("可以在&lt;提醒&gt;中自行设置推送提醒;");
        CharSequence charSequence= Html.fromHtml(sb.toString());
        tv_hair_count.setText(charSequence);
        //该语句在设置后必加，不然没有任何效果
        tv_hair_count.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 更新治疗进度
     */
    protected void treatmentProgram(String[] allProgram) {
        if(allProgram == null){
            return;
        }
        for (int i = 0; i < allProgram.length; i++) {
            TextView textView = new TextView(mContext);
            int width = UIUtils.getScreenWidth() / 5;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width - UIUtils.dp2px(3), (int) (width / 2.5));
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.setMargins(UIUtils.dp2px(1), 0, UIUtils.dp2px(1), 0);
            textView.setLayoutParams(layoutParams);
            textView.setText(allProgram[i]);
            textView.setTextSize(15);
            int resID = 0;
            //Log.e("当前的阶段",StaticVariables.USER_CURRENT_TREATMENT_PROGRAMS_INT+allProgram[0]+"-"+allProgram[1]+"-"+allProgram[2]);
            switch (i) {
                case 0:
                    resID = StaticVariables.USER_CURRENT_TREATMENT_PROGRAMS_INT < i ? R.mipmap.bg_jindu_gray_1 : R.mipmap.bg_jindu_blue_1;
                    break;
                case 1:
                    resID = StaticVariables.USER_CURRENT_TREATMENT_PROGRAMS_INT < i ? R.mipmap.bg_jindu_gray_2 : R.mipmap.bg_jindu_blue_2;
                    break;
                case 2:
                    resID = StaticVariables.USER_CURRENT_TREATMENT_PROGRAMS_INT < i ? R.mipmap.bg_jindu_gray_3 : R.mipmap.bg_jindu_blue_3;
                    break;
                case 3:
                    resID = StaticVariables.USER_CURRENT_TREATMENT_PROGRAMS_INT < i ? R.mipmap.bg_jindu_gray_4 : R.mipmap.bg_jindu_blue_4;
                    break;
                case 4:
                    resID = StaticVariables.USER_CURRENT_TREATMENT_PROGRAMS_INT < i ? R.mipmap.bg_jindu_gray_5 : R.mipmap.bg_jindu_blue_5;
                    break;
            }
            textView.setBackgroundResource(resID);
            textView.setPadding(UIUtils.dp2px(25), UIUtils.dp2px(3), 0, 0);//设置左边距
            ll_my_schedule.addView(textView);
        }
    }

    /**
     * 打开图片查看器
     */
    protected void imageBrower(int position, ArrayList<String> urls2) {
        String EXTRA_IMAGE_INDEX = "image_index";
        String EXTRA_IMAGE_URLS = "image_urls";
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(EXTRA_IMAGE_INDEX, position);
        mContext.startActivity(intent);
    }

    SelectPicPopupWindow selectPicPopupWindow;
    public void showPicturePopupWindow(){
        selectPicPopupWindow = new SelectPicPopupWindow(mContext, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 隐藏弹出窗口
                selectPicPopupWindow.dismiss();
                switch (v.getId()) {
                    case R.id.takePhotoBtn:// 拍照
                        startTakePhoto();
                        break;
                    case R.id.pickPhotoBtn:// 相册选择图片
                        pickPhoto();
                        break;
                    case R.id.cancelBtn:// 取消
                        break;
                    default:
                        break;
                }
            }
        });
        selectPicPopupWindow.showAtLocation(inflate.findViewById(R.id.check_layout), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void pickPhoto(){
        boolean miui = MIUIUtils.isMIUI();
        Intent selectIntent;
        if (miui){
            selectIntent = new Intent(Intent.ACTION_GET_CONTENT);
        }else {
            selectIntent = new Intent(Intent.ACTION_PICK);
         }
        switch (clicked){
            case 0:
                selectIntent.setType("image/*");
                startActivityForResult(selectIntent, 4);
                break;
            case 1:
                selectIntent.setType("image/*");
                startActivityForResult(selectIntent, 5);
                break;
            case 2:
                selectIntent.setType("image/*");
                startActivityForResult(selectIntent, 6);
                break;
            case 3:
                selectIntent.setType("image/*");
                startActivityForResult(selectIntent, 7);
                break;
        }
    }

    //调用系统相机拍照
    private void startTakePhoto() {
        switch (clicked){
            case 0:
                fileName1 = Environment.getExternalStorageDirectory()+"/ihelmet/"+ TimeUtils.getDateToStringYYMMddHHmmss(System.currentTimeMillis())+"top.jpg";
                imageFile1 =new File(fileName1);
                // 启动系统相机
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 设置拍照后保存的图片存储在文件中
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile1));
                // 启动activity并获取返回数据
                startActivityForResult(intent1, 0);
                break;
            case 1:
                fileName2 = Environment.getExternalStorageDirectory()+"/ihelmet/"+ TimeUtils.getDateToStringYYMMddHHmmss(System.currentTimeMillis())+"hair.jpg";
                imageFile2 =new File(fileName2);
                // 启动系统相机
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 设置拍照后保存的图片存储在文件中
                intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile2));
                // 启动activity并获取返回数据
                startActivityForResult(intent2, 1);
                break;
            case 2:
                fileName3 = Environment.getExternalStorageDirectory()+"/ihelmet/"+ TimeUtils.getDateToStringYYMMddHHmmss(System.currentTimeMillis())+"rear.jpg";
                imageFile3 =new File(fileName3);
                // 启动系统相机
                Intent intent3 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 设置拍照后保存的图片存储在文件中
                intent3.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile3));
                // 启动activity并获取返回数据
                startActivityForResult(intent3, 2);
                break;
            case 3:
                fileName4 = Environment.getExternalStorageDirectory()+"/ihelmet/"+ TimeUtils.getDateToStringYYMMddHHmmss(System.currentTimeMillis())+"side.jpg";
                imageFile4 =new File(fileName4);
                // 启动系统相机
                Intent intent4 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 设置拍照后保存的图片存储在文件中
                intent4.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile4));
                // 启动activity并获取返回数据
                startActivityForResult(intent4, 3);
                break;
        }
    }

    public static Bitmap getScaleBitmap(Context ctx, String filePath) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
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

        /*int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();*/

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
        return bmp;
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


    File file1 = null;
    File file2 = null;
    File file3 = null;
    File file4 = null;
    //获取返回的图片
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String picturePath = "";
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode){
                case 0:
                    if (imageFile1.exists()) {
                        Bitmap scaleBitmap = getScaleBitmap(mContext, fileName1);
                        //imgTop.setImageBitmap(scaleBitmap);
                        new_check1.setImageBitmap(scaleBitmap);
                        String s = saveBitmapToFile(imageFile1, imageFile1.getAbsolutePath());
                        if (s!=null){
                            //filesList.add(new File(s));
                            file1 = new File(s);
                        }
                    }
                    break;
                case 1:
                    if (imageFile2.exists()) {
                        Bitmap scaleBitmap = getScaleBitmap(mContext, fileName2);
                        //imgTop.setImageBitmap(scaleBitmap);
                        new_check2.setImageBitmap(scaleBitmap);
                        String s = saveBitmapToFile(imageFile2, imageFile2.getAbsolutePath());
                        if (s!=null) {
                            //filesList.add(new File(s));
                            file2 = new File(s);
                        }
                    }
                    break;
                case 2:
                    if (imageFile3.exists()) {
                        Bitmap scaleBitmap = getScaleBitmap(mContext, fileName3);
                        //imgTop.setImageBitmap(scaleBitmap);
                        new_check3.setImageBitmap(scaleBitmap);
                        String s = saveBitmapToFile(imageFile3, imageFile3.getAbsolutePath());
                        if (s!=null) {
                            //filesList.add(new File(s));
                            file3 = new File(s);
                        }
                    }
                    break;
                case 3:
                    if (imageFile4.exists()) {
                        Bitmap scaleBitmap = getScaleBitmap(mContext, fileName4);
                        //imgTop.setImageBitmap(scaleBitmap);
                        new_check4.setImageBitmap(scaleBitmap);
                        String s = saveBitmapToFile(imageFile4, imageFile4.getAbsolutePath());
                        if (s!=null) {
                            //filesList.add(new File(s));
                            file4 = new File(s);
                        }
                    }
                    break;
                case 4:
                        try {
                            Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                            String[] filePathColumn = { MediaStore.Images.Media.DATA };
                            Cursor cursor = mContext.getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                            if (cursor != null){
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                picturePath = cursor.getString(columnIndex);  //获取照片路径
                                cursor.close();
                            }
                            if (!picturePath.isEmpty()){
                                Bitmap bitmap= BitmapFactory.decodeFile(picturePath);
                                new_check1.setImageBitmap(bitmap);
                                //filesList.add(new File(picturePath));
                                file1 = new File(picturePath);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    break;
                case 5:
                        try {
                            Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                            String[] filePathColumn = { MediaStore.Images.Media.DATA };
                            Cursor cursor =mContext.getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                            if (cursor != null){
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                picturePath = cursor.getString(columnIndex);  //获取照片路径
                                cursor.close();
                            }
                            if (!picturePath.isEmpty()){
                                Bitmap bitmap= BitmapFactory.decodeFile(picturePath);
                                new_check2.setImageBitmap(bitmap);
                                //filesList.add(new File(picturePath));
                                file2 = new File(picturePath);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    break;
                case 6:
                        try {
                            Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                            String[] filePathColumn = { MediaStore.Images.Media.DATA };
                            Cursor cursor =mContext.getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                            if (cursor != null){
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                picturePath = cursor.getString(columnIndex);  //获取照片路径
                                cursor.close();
                            }
                            if (!picturePath.isEmpty()){
                                Bitmap bitmap= BitmapFactory.decodeFile(picturePath);
                                new_check3.setImageBitmap(bitmap);
                                //filesList.add(new File(picturePath));
                                file3 = new File(picturePath);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    break;
                case 7:
                        try {
                            Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                            String[] filePathColumn = { MediaStore.Images.Media.DATA };
                            Cursor cursor = mContext.getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                            if (cursor != null){
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                picturePath = cursor.getString(columnIndex);  //获取照片路径
                                cursor.close();
                            }
                            if (!picturePath.isEmpty()){
                                Bitmap bitmap= BitmapFactory.decodeFile(picturePath);
                                new_check4.setImageBitmap(bitmap);
                                //filesList.add(new File(picturePath));
                                file4 = new File(picturePath);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    break;
            }
        }
    }
}
