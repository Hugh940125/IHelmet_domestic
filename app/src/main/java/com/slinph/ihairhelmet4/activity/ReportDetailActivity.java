package com.slinph.ihairhelmet4.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slinph.ihairhelmet4.R;
import com.slinph.ihairhelmet4.internet.Url;
import com.slinph.ihairhelmet4.internet.Vo.TreatmentProgramsVO;
import com.slinph.ihairhelmet4.internet.model.Patient;
import com.slinph.ihairhelmet4.internet.net_utis.DataConverter;
import com.slinph.ihairhelmet4.internet.net_utis.HttpUtils;
import com.slinph.ihairhelmet4.internet.net_utis.ResultData;
import com.slinph.ihairhelmet4.utils.CustomDate;
import com.slinph.ihairhelmet4.utils.TimeUtils;
import com.slinph.ihairhelmet4.utils.UIUtils;
import com.slinph.ihairhelmet4.view.ImageGridAdapter_1;
import com.slinph.ihairhelmet4.view.ImageGridAdapter_2;
import com.slinph.ihairhelmet4.view.NoScrollGridView;

import java.util.ArrayList;
import java.util.Date;

/**
 * 诊断详情
 */
public class ReportDetailActivity extends Activity {


    private TextView tv_time;
    private TextView tv_hair_loss_type;
    private TextView tv_hair_loss_degree;
    private TextView tv_hair_loss_disease;
    private TextView tv_hair_count;
    private NoScrollGridView gv_classification;
    private LinearLayout ll_my_schedule;
    private TextView tv_expert_suggest;
    private NoScrollGridView gv_photo;
    private String id;
    private ArrayList mImageUrls;
    private ImageGridAdapter_1 mImageGridAdapter;
    private ArrayList<String> mPhonoUrls;
    private ImageGridAdapter_2 mPhonoGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail_activity);
        findViews();
    }

    protected void findViews() {
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_hair_loss_type = (TextView) findViewById(R.id.tv_hair_loss_type);
        tv_hair_loss_degree = (TextView) findViewById(R.id.tv_hair_degree);
        tv_hair_loss_disease = (TextView) findViewById(R.id.tv_hair_disease);
        tv_hair_count = (TextView) findViewById(R.id.tv_hair_count);
        gv_classification = (NoScrollGridView) findViewById(R.id.gv_classification);
        ll_my_schedule = (LinearLayout) findViewById(R.id.ll_my_schedule);
        tv_expert_suggest = (TextView) findViewById(R.id.tv_hair_suggest);
        gv_photo = (NoScrollGridView) findViewById(R.id.gv_photo);
        ImageButton ib_detail_back = (ImageButton) findViewById(R.id.ib_detail_back);
        ib_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //id = Long.parseLong(getIntent().getLongExtra("id",-1L));
        Intent intent = getIntent();
        String id = String.valueOf(intent.getLongExtra("id", -1L));
        String patient_id = String.valueOf(intent.getLongExtra("patient_id", -1L));
        int type = intent.getIntExtra("type", -1);
        initData(id);
        if (type != -1){
            if (type == 1){
                showFirstPhotos(patient_id);
            }else {
                showFollowupPhotos(patient_id);
            }
        }
    }



    private void initData(String id){
        HttpUtils.postAsync(ReportDetailActivity.this, Url.rootUrl+"/iheimi/treatmentPrograms/select", new HttpUtils.ResultCallback<ResultData<TreatmentProgramsVO>>() {

            private Date createDtm;
            private Integer currentStatus;
            private Integer hairLossType;
            private Integer hairLossDisease;
            private Integer hairLossDegree;
            private String allStatus;
            private String hairLossSortUrl;
            private String graphic;

            @Override
            public void onError(int statusCode, Throwable error) {

            }

            @Override
            public void onResponse(ResultData<TreatmentProgramsVO> response) {
                if (response.getSuccess()){
                    TreatmentProgramsVO data = response.getData();
                    if (data != null){
                        hairLossSortUrl = data.getHairLossSortUrl();
                        allStatus = data.getAllStatus();
                        hairLossDegree = data.getHairLossDegree();
                        graphic = data.getGraphic();
                        hairLossDisease = data.getHairLossDisease();
                        hairLossType = data.getHairLossType();
                        createDtm = data.getCreateDtm();
                        currentStatus = data.getCurrentStatus();

                        ReportDetailActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                mImageUrls = new ArrayList();
                                mImageUrls.add(hairLossSortUrl);
                                mImageGridAdapter = new ImageGridAdapter_1(ReportDetailActivity.this);//ImageGridAdapter_3是自己的改的,原来是ImageGridAdapter
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

                                tv_time.setText(TimeUtils.getDateToString(createDtm.getTime()));//报告时间
                                tv_hair_loss_type.setText(DataConverter.getType(hairLossType));//脱发类型
                                tv_hair_loss_degree.setText(DataConverter.getHairLossDegree(hairLossDegree));//脱发等级
                                tv_hair_loss_disease.setText("伴有:" + DataConverter.getDisease(hairLossDisease));//脱发伴随症状:如红疹,脓包等
                                tv_expert_suggest.setText(graphic);//专家建议
                                if(ll_my_schedule.getChildCount()==0){
                                    String[] split = allStatus.split(",");

                                    StringBuilder stringBuilder = new StringBuilder();
                                    for (int i=0;i<split.length;i++){
                                        switch (split[i]){
                                            case "0":
                                                stringBuilder.append("消炎,");
                                                break;
                                            case "1":
                                                stringBuilder.append("控油,");
                                                break;
                                            case "2":
                                                stringBuilder.append("止脱,");
                                                break;
                                            case "3":
                                                stringBuilder.append("生发,");
                                                break;
                                            case "4":
                                                stringBuilder.append("粗发");
                                                break;
                                        }
                                    }
                                    String[] schduleName = stringBuilder.toString().split(",");
                                    treatmentProgram(schduleName,currentStatus);
                                }
                                CustomDate date = new CustomDate();
                                StringBuilder sb = new StringBuilder();
                                sb.append("")
                                        //.append(StaticVariables.USER_REPORT_TIME2)
                                        .append("每隔一天使用一次;<br>")
                                        //.append(date.getCurrentMonthLastDay())
                                        .append("使用时请将脱发部位的头皮露出,在头发干燥的情况下使用;<br>")
                                        //.append(StaticVariables.USER_SUGGEST_LIGHT_TIMES)
                                        .append("可以在&lt;我的提醒&gt;中自行设置推送提醒;");
                                CharSequence charSequence= Html.fromHtml(sb.toString());
                                tv_hair_count.setText(charSequence);
                                //该语句在设置后必加，不然没有任何效果
                                tv_hair_count.setMovementMethod(LinkMovementMethod.getInstance());
                            }
                        });
                    }
                }
            }
        },new String[]{"id",id});
    }

    public void showFirstPhotos(String patient_id){
        HttpUtils.postAsync(this, Url.rootUrl+"/iheimi/patient/select", new HttpUtils.ResultCallback<ResultData<Patient>>() {
            @Override
            public void onError(int statusCode, Throwable error) {
                Log.e("onError", "连接错误" + statusCode);
            }

            @Override
            public void onResponse(ResultData<Patient> response) {
               if (response.getSuccess()){
                   Patient data = response.getData();
                   if (data != null){
                       String topViewUrl_spc = data.getTopViewUrl();
                       String hairlineUrl_spc = data.getHairlineUrl();
                       String afterViewUrl_spc = data.getAfterViewUrl();
                       String partialViewUrl_spc = data.getPartialViewUrl();

                       mPhonoUrls = new ArrayList<>();
                       mPhonoUrls.add(topViewUrl_spc);
                       mPhonoUrls.add(hairlineUrl_spc);
                       mPhonoUrls.add(afterViewUrl_spc);
                       mPhonoUrls.add(partialViewUrl_spc);
                       mPhonoGridAdapter = new ImageGridAdapter_2(ReportDetailActivity.this);//ImageGridAdapter_3是自己的改的,原来是ImageGridAdapter
                       Log.e("mPhonoUrls",mPhonoUrls.toString());
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
                   }
               }
            }
        },new String[]{"id",patient_id});
    }

    public void showFollowupPhotos(String patient_id){
        HttpUtils.postAsync(this, Url.rootUrl+"/iheimi/followUp/select", new HttpUtils.ResultCallback<ResultData<Patient>>() {
            @Override
            public void onError(int statusCode, Throwable error) {
                Log.e("onError", "连接错误" + statusCode);
            }

            @Override
            public void onResponse(ResultData<Patient> response) {
                if (response.getSuccess()){
                    Patient data = response.getData();
                    if (data != null){
                        String topViewUrl_spc = data.getTopViewUrl();
                        String hairlineUrl_spc = data.getHairlineUrl();
                        String afterViewUrl_spc = data.getAfterViewUrl();
                        String partialViewUrl_spc = data.getPartialViewUrl();

                        mPhonoUrls = new ArrayList<>();
                        mPhonoUrls.add(topViewUrl_spc);
                        mPhonoUrls.add(hairlineUrl_spc);
                        mPhonoUrls.add(afterViewUrl_spc);
                        mPhonoUrls.add(partialViewUrl_spc);
                        mPhonoGridAdapter = new ImageGridAdapter_2(ReportDetailActivity.this);//ImageGridAdapter_3是自己的改的,原来是ImageGridAdapter
                        Log.e("mPhonoUrls",mPhonoUrls.toString());
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
                    }
                }
            }
        },new String[]{"id",patient_id});
    }
    /**
     * 更新治疗进度
     */
    protected void treatmentProgram(String[] allProgram,int currentstatus) {
        if(allProgram == null){
            return;
        }
        for (int i = 0; i < allProgram.length; i++) {
            TextView textView = new TextView(this);
            int width = UIUtils.getScreenWidth() / 5;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width - UIUtils.dp2px(3), (int) (width / 2.5));
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.setMargins(UIUtils.dp2px(1), 0, UIUtils.dp2px(1), 0);
            textView.setLayoutParams(layoutParams);
            textView.setText(allProgram[i]);
            textView.setTextSize(15);
            int resID = 0;
            switch (i) {
                case 0:
                    resID = currentstatus < i ? R.mipmap.bg_jindu_gray_1 : R.mipmap.bg_jindu_blue_1;
                    break;
                case 1:
                    resID = currentstatus < i ? R.mipmap.bg_jindu_gray_2 : R.mipmap.bg_jindu_blue_2;
                    break;
                case 2:
                    resID = currentstatus < i ? R.mipmap.bg_jindu_gray_3 : R.mipmap.bg_jindu_blue_3;
                    break;
                case 3:
                    resID = currentstatus < i ? R.mipmap.bg_jindu_gray_4 : R.mipmap.bg_jindu_blue_4;
                    break;
                case 4:
                    resID = currentstatus < i ? R.mipmap.bg_jindu_gray_5 : R.mipmap.bg_jindu_blue_5;
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
        Intent intent = new Intent(this, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(EXTRA_IMAGE_INDEX, position);
        this.startActivity(intent);
    }
}
