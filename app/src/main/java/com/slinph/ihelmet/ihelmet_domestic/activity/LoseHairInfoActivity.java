package com.slinph.ihelmet.ihelmet_domestic.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.slinph.ihelmet.ihelmet_domestic.R;
import com.slinph.ihelmet.ihelmet_domestic.utils.SerializableMap;
import com.slinph.ihelmet.ihelmet_domestic.utils.StringUtils;
import com.slinph.ihelmet.ihelmet_domestic.utils.UIUtils;
import com.slinph.ihelmet.ihelmet_domestic.view.MonthProgressBar;
import com.slinph.ihelmet.ihelmet_domestic.view.flowlayout.FlowLayout;
import com.slinph.ihelmet.ihelmet_domestic.view.flowlayout.TagAdapter;
import com.slinph.ihelmet.ihelmet_domestic.view.flowlayout.TagFlowLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Set;

/**
 * 脱发用户列表
 */
public class LoseHairInfoActivity extends Activity {
    private MonthProgressBar sliderTime;//时间进度条
    private TagFlowLayout flowlayoutRelative;
    private TagFlowLayout flowlayoutSmokeQuality;
    private TagFlowLayout flowlayoutDisease;
    private TagFlowLayout flowlayoutTreatmentResult;
    private TagFlowLayout flowlayoutTreatmentMethod;
    private TagFlowLayout flowlayoutTreatmentEffect;
    private TagFlowLayout flowlayoutSocioTreatmentEffect;
    private TagFlowLayout flowlayout_socio_text;
    private LayoutInflater mInflater;
    private ImageView mBodyHairImage;//体毛图
    private SerializableMap map = new SerializableMap();
    private String mImageUrl;
    private EditText et_1, et_job_result, et_question_result, et_public_result;
    private LinearLayout ll_1, ll_2;
    private RelativeLayout rl_3;
    private TextView tv_des;
    private String Choice;
    private EditText et_system_disease;
    private String treatmentHistory = "";
    private String diseaseHistory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hair_info);
        //setToolbar(getResources().getString(R.string.alopecia_history));
        findViews();
        initData();
    }

    /*@Override
    protected int addLayoutId() {
        return R.layout.activity_hair_info;
    }*/

    private void initData() {
        map.put("hereditaryHairLoss", null);//有无家族遗传脱发史
        map.put("hairOil", null);//头油
        map.put("headCrumbs", null);//头屑
        map.put("scalpRash", null);//头皮红疹
        map.put("scalpItching", null);//头皮是否有瘙痒
        map.put("scalpPustule", null);//头皮有无脓疱

        map.put("hairDia", null);//头发直径
        map.put("whiteHair", null);//白发情况
        map.put("hairElasticity", null);//头发弹性
        map.put("hairLossOneDayNumber", null);//每日掉发量
        map.put("pullHair", null);//轻拉毛实验

        map.put("hairLossBeforeAndAfter", null);//脱发前头发数量
        map.put("hairLossSpeed", null);//脱发速度

        //生活习惯
        map.put("timeAsleep", null);//习惯的入睡时间
        map.put("sleepingQuality", null);//睡眠质量
        map.put("foodHobby", null);//是否嗜好辛辣，油炸食物
        map.put("livesOrJobStress", null);//是否感觉生活/工作压力大
        map.put("ifSmoke", null);//是否吸烟
        map.put("smokeQuality", null);//吸烟量
        map.put("ifSot", null);//是否嗜酒

        map.put("marriage", null);//婚姻状况
        map.put("giveBirth", null);//生育计划
        map.put("jobOccupation", null);//所属职业
        map.put("diseaseHistory", null);//其他系统性疾病
        map.put("publicInstitution", null);//以前是否在公立机构寻求过治疗
        map.put("treatmentHistory", null);////公立机构治疗方法
        map.put("publicDiagnosis", null);//公立机构诊断结果
        map.put("treatmentEffect", null);//公立治疗效果
        map.put("socialInstitution", null);//是否社会机构寻求治疗
        map.put("socialTreatmentHistory", null);//社会机构治疗史
        map.put("socialTreatmentEffect", null);//社会机构治疗结果
        map.put("mentalityDisease", null);//脱发导致心理问题
        map.put("attention", null);//治疗中最关心的问题

        //体毛:默认选第一张图片
        Bitmap bm = UIUtils.decodeCustomRes(LoseHairInfoActivity.this, R.mipmap.body_hair_less);
        mImageUrl = getRealPathFromURI(cacheBitmap(bm,"body_hair_less.png"));
    }

    protected void findViews() {
        ImageButton ib_info_back = (ImageButton) findViewById(R.id.ib_info_back);
        if (ib_info_back != null){
            ib_info_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoseHairInfoActivity.this);
                    builder.setTitle(R.string.tips)
                            .setMessage("跳过信息填写不能使用完整功能，只能使用体验模式，是否跳过？")
                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    startActivity(new Intent(LoseHairInfoActivity.this,MainActivity.class));
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            });
        }
        sliderTime = (MonthProgressBar) findViewById(R.id.slider_time);
        RadioGroup radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        if (radiogroup != null){
            radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    Bitmap bm;
                    switch (checkedId) {
                        case R.id.radio0:
                            mBodyHairImage.setImageResource(R.mipmap.body_hair_less);//UI更新
                            map.put("bodyHairImg", "少");
                            /*bm = UIUtils.decodeCustomRes(LoseHairInfoActivity.this, R.mipmap.body_hair_less);
                            mImageUrl = getRealPathFromURI(cacheBitmap(bm,"body_hair_less.png"));*/
                            break;
                        case R.id.radio1:
                            mBodyHairImage.setImageResource(R.mipmap.body_hair_general);
                            map.put("bodyHairImg", "中");
                            /*bm = UIUtils.decodeCustomRes(LoseHairInfoActivity.this, R.mipmap.body_hair_general);
                            mImageUrl = getRealPathFromURI(cacheBitmap(bm,"body_hair_general.png"));*/
                            break;
                        case R.id.radio2:
                            mBodyHairImage.setImageResource(R.mipmap.body_hair_more);
                            map.put("bodyHairImg", "多");
                            /*bm = UIUtils.decodeCustomRes(LoseHairInfoActivity.this, R.mipmap.body_hair_more);
                            mImageUrl = getRealPathFromURI(cacheBitmap(bm,"body_hair_more.png"));*/
                            break;
                        default:
                            break;
                    }
                }
            });
        }

        TagFlowLayout flowlayoutIsRelative = (TagFlowLayout) findViewById(R.id.flowlayout_is_relative);
        flowlayoutRelative = (TagFlowLayout) findViewById(R.id.flowlayout_relative);
        TagFlowLayout flowlayoutScalp = (TagFlowLayout) findViewById(R.id.flowlayout_scalp);
        TagFlowLayout flowlayoutDandruff = (TagFlowLayout) findViewById(R.id.flowlayout_dandruff);
        TagFlowLayout flowlayoutScalpRash = (TagFlowLayout) findViewById(R.id.flowlayout_scalp_rash);
        flowlayout_socio_text = (TagFlowLayout) findViewById(R.id.flowlayout_socio_text);
        TagFlowLayout flowlayoutScalpItching = (TagFlowLayout) findViewById(R.id.flowlayout_scalp_itching);
        TagFlowLayout flowlayoutScalpPustule = (TagFlowLayout) findViewById(R.id.flowlayout_scalp_pustule);

        TagFlowLayout flowlayoutHairDia = (TagFlowLayout) findViewById(R.id.flowlayout_hair_dia);
        TagFlowLayout flowlayoutWhiteHair = (TagFlowLayout) findViewById(R.id.flowlayout_white_hair);
        TagFlowLayout flowlayoutHairElasticity = (TagFlowLayout) findViewById(R.id.flowlayout_hair_elasticity);
        TagFlowLayout flowlayoutHairLossOneDayNumber = (TagFlowLayout) findViewById(R.id.flowlayout_hair_loss_one_day_number);
        TagFlowLayout flowlayoutLightNapExperiment = (TagFlowLayout) findViewById(R.id.flowlayout_light_nap_experiment);

        TagFlowLayout flowlayoutTimeAsleep = (TagFlowLayout) findViewById(R.id.flowlayout_time_asleep);
        TagFlowLayout flowlayoutSleepQuality = (TagFlowLayout) findViewById(R.id.flowlayout_sleep_quality);
        TagFlowLayout flowlayoutFoodHobby = (TagFlowLayout) findViewById(R.id.flowlayout_food_hobby);
        TagFlowLayout flowlayoutLivesOrJobStress = (TagFlowLayout) findViewById(R.id.flowlayout_lives_or_job_stress);
        TagFlowLayout flowlayoutIfSmoke = (TagFlowLayout) findViewById(R.id.flowlayout_if_smoke);
        flowlayoutSmokeQuality = (TagFlowLayout) findViewById(R.id.flowlayout_smoke_quality);
        TagFlowLayout flowlayoutIfSot = (TagFlowLayout) findViewById(R.id.flowlayout_if_sot);

        TagFlowLayout flowlayoutQuantity = (TagFlowLayout) findViewById(R.id.flowlayout_quantity);
        TagFlowLayout flowlayoutSpeed = (TagFlowLayout) findViewById(R.id.flowlayout_speed);
        flowlayoutDisease = (TagFlowLayout) findViewById(R.id.flowlayout_disease);
        TagFlowLayout flowlayoutIsDisease = (TagFlowLayout) findViewById(R.id.flowlayout_is_disease);
        TagFlowLayout flowlayoutMaritalCondition = (TagFlowLayout) findViewById(R.id.flowlayout_marital_condition);
        TagFlowLayout flowlayoutMaritalPlan = (TagFlowLayout) findViewById(R.id.flowlayout_marital_plan);
        flowlayoutTreatmentResult = (TagFlowLayout) findViewById(R.id.flowlayout_treatment_result);
        flowlayoutTreatmentMethod = (TagFlowLayout) findViewById(R.id.flowlayout_treatment_method);
        flowlayoutTreatmentEffect = (TagFlowLayout) findViewById(R.id.flowlayout_treatment_effect);
        TagFlowLayout flowlayoutSocioTreatment = (TagFlowLayout) findViewById(R.id.flowlayout_socio_treatment);
        flowlayoutSocioTreatmentEffect = (TagFlowLayout) findViewById(R.id.flowlayout_socio_treatment_effect);
        TagFlowLayout flowlayoutPsychologyProblem = (TagFlowLayout) findViewById(R.id.flowlayout_psychology_problem);
        TagFlowLayout flowlayoutConcernProblem = (TagFlowLayout) findViewById(R.id.flowlayout_concern_problem);
        TagFlowLayout flowlayoutOccupation = (TagFlowLayout) findViewById(R.id.flowlayout_occupation);
        TagFlowLayout flowlayoutPublicTreatment = (TagFlowLayout) findViewById(R.id.flowlayout_public_treatment);
        mBodyHairImage = (ImageView) findViewById(R.id.img_hair);

        //手动输入框
        et_1 = (EditText) findViewById(R.id.et_1);//et_system_disease
        et_system_disease = (EditText) findViewById(R.id.et_system_disease);
        //是否在机构治疗过
        ll_1 = (LinearLayout) findViewById(R.id.ll_1);
        ll_2 = (LinearLayout) findViewById(R.id.ll_2);
        rl_3 = (RelativeLayout) findViewById(R.id.rl_3);
        et_job_result = (EditText) findViewById(R.id.et_job_result);
        et_question_result = (EditText) findViewById(R.id.et_question_result);
        et_public_result = (EditText) findViewById(R.id.et_public_result);
        tv_des = (TextView) findViewById(R.id.tv_des);
        mInflater = LayoutInflater.from(LoseHairInfoActivity.this);

        fillTagAdater(flowlayoutIsRelative, getResources().getStringArray(R.array.have_no2));
        fillTagAdater(flowlayoutRelative, getResources().getStringArray(R.array.relative));
        fillTagAdater(flowlayoutScalp, getResources().getStringArray(R.array.more_less));
        fillTagAdater(flowlayoutDandruff, getResources().getStringArray(R.array.more_less));
        fillTagAdater(flowlayoutScalpRash, getResources().getStringArray(R.array.have_no));
        fillTagAdater(flowlayoutScalpItching, getResources().getStringArray(R.array.have_no));
        fillTagAdater(flowlayoutScalpPustule, getResources().getStringArray(R.array.have_no));

        fillTagAdater(flowlayoutHairDia, getResources().getStringArray(R.array.hairDia));
        fillTagAdater(flowlayoutWhiteHair, getResources().getStringArray(R.array.whiteHair));
        fillTagAdater(flowlayoutHairElasticity, getResources().getStringArray(R.array.hairElasticity));
        fillTagAdater(flowlayoutHairLossOneDayNumber, getResources().getStringArray(R.array.hairLossOneDayNumber0));
        fillTagAdater(flowlayoutLightNapExperiment, getResources().getStringArray(R.array.yin_yang));

        fillTagAdater(flowlayoutTimeAsleep, getResources().getStringArray(R.array.timeAsleep));
        fillTagAdater(flowlayoutSleepQuality, getResources().getStringArray(R.array.sleepQuality));
        fillTagAdater(flowlayoutFoodHobby, getResources().getStringArray(R.array.yes_no));
        fillTagAdater(flowlayoutLivesOrJobStress, getResources().getStringArray(R.array.yes_no));
        fillTagAdater(flowlayoutIfSmoke, getResources().getStringArray(R.array.yes_no));
        fillTagAdater(flowlayoutSmokeQuality, getResources().getStringArray(R.array.smokeQuality));
        fillTagAdater(flowlayoutIfSot, getResources().getStringArray(R.array.yes_no));

        fillTagAdater(flowlayoutQuantity, getResources().getStringArray(R.array.hair_quantity));
        fillTagAdater(flowlayoutSpeed, getResources().getStringArray(R.array.hair_speed));
        fillTagAdater(flowlayoutIsDisease, getResources().getStringArray(R.array.have_no2));
        fillTagAdater(flowlayoutDisease, getResources().getStringArray(R.array.disease));
        fillTagAdater(flowlayoutMaritalCondition, getResources().getStringArray(R.array.marital_condition));
        fillTagAdater(flowlayoutMaritalPlan, getResources().getStringArray(R.array.yes_no));
        fillTagAdater(flowlayoutTreatmentResult, getResources().getStringArray(R.array.diagnosis_result));
        fillTagAdater(flowlayoutTreatmentMethod, getResources().getStringArray(R.array.treatment_method));
        fillTagAdater(flowlayoutTreatmentEffect, getResources().getStringArray(R.array.treatment_effect));
        fillTagAdater(flowlayoutSocioTreatment, getResources().getStringArray(R.array.yes_no));
        fillTagAdater(flowlayout_socio_text, getResources().getStringArray(R.array.reatment_history));
        fillTagAdater(flowlayoutSocioTreatmentEffect, getResources().getStringArray(R.array.treatment_effect));
        fillTagAdater(flowlayoutPsychologyProblem, getResources().getStringArray(R.array.psychology_problem));
        fillTagAdater(flowlayoutConcernProblem, getResources().getStringArray(R.array.concern_treatment_problem));
        fillTagAdater(flowlayoutOccupation, getResources().getStringArray(R.array.your_occupation));
        fillTagAdater(flowlayoutPublicTreatment, getResources().getStringArray(R.array.yes_no));

        Button btn_next = (Button) findViewById(R.id.btn_next);
        if(btn_next != null){
            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //遍历map集合
                    for (String key : map.keySet()) {
                        if (map.get(key) == null) {//有未提交信息先跳出循环,提示用户
                            showEmpty(key);
                            return;
                        }
                    }
                    tv_des.setText("");
                    //禁止button再次点击
                    view.setClickable(false);
                    //以下是提交网络
                    sendData();
                    /*Intent intent = new Intent(LoseHairInfoActivity.this, TakePhotoActivity.class);
                    intent.putExtra("frominfo",true);
                    startActivity(intent);
                    finish();*/
                }
            });
        }
    }

    /**
     * 显示未选择信息
     * @param key map中的key
     */
    private void showEmpty(String key) {
        switch (key) {
            case "hereditaryHairLoss":
                tv_des.setText("\"家族遗传脱发史\"未选择");
                break;
            case "hairOil":
                tv_des.setText("\"头油\"未选择");
                break;
            case "headCrumbs":
                tv_des.setText("\"头屑\"未选择");
                break;
            case "scalpRash":
                tv_des.setText("\"头皮红疹\"未选择");
                break;
            case "scalpItching":
                tv_des.setText("\"头皮是否有瘙痒\"未选择");
                break;
            case "patientScalpPustule":
                tv_des.setText("\"头皮有无脓疱\"未选择");
                break;

            case "hairDia":
                tv_des.setText("\"头发直径\"未选择");
                break;
            case "whiteHair":
                tv_des.setText("\"白发情况\"未选择");
                break;
            case "hairElasticity":
                tv_des.setText("\"头发弹性\"未选择");
                break;
            case "hairLossOneDayNumber":
                tv_des.setText("\"每日掉发量\"未选择");
                break;
            case "pullHair":
                tv_des.setText("\"轻拉毛实验\"未选择");
                break;

            case "hairLossBeforeAndAfter":
                tv_des.setText("\"脱发前头发数量\"未选择");
                break;
            case "hairLossSpeed":
                tv_des.setText("\"脱发速度\"未选择");
                break;

            case "timeAsleep":
                tv_des.setText("\"习惯的入睡时间\"未选择");
                break;
            case "sleepingQuality":
                tv_des.setText("\"睡眠质量\"未选择");
                break;
            case "foodHobby":
                tv_des.setText("\"是否嗜好辛辣\"未选择");
                break;
            case "livesOrJobStress":
                tv_des.setText("\"是否感觉生活/工作压力大\"未选择");
                break;
            case "ifSmoke":
                tv_des.setText("\"是否吸烟\"未选择");
                break;
            case "smokeQuality":
                tv_des.setText("\"吸烟量\"未选择");
                break;
            case "ifSot":
                tv_des.setText("\"是否嗜酒\"未选择");
                break;

            case "marriage":
                tv_des.setText("\"婚姻状况\"未选择");
                break;
            case "giveBirth":
                tv_des.setText("\"生育计划\"未选择");
                break;
            case "jobOccupation":
                tv_des.setText("\"所属职业\"未选择");
                break;
            case "diseaseHistory":
                tv_des.setText("\"其他系统性疾病\"未选择");
                break;
            case "publicInstitution":
                tv_des.setText("\"以前是否在公立机构寻求过治疗\"未选择");
                break;
            case "treatmentHistory":
                tv_des.setText("\"公立机构治疗方法\"未选择");
                break;
            case "publicDiagnosis":
                tv_des.setText("\"公立机构诊断结果\"未选择");
                break;
            case "treatmentEffect":
                tv_des.setText("\"公立治疗效果\"未选择");
                break;
            case "socialInstitution":
                tv_des.setText("\"是否社会机构寻求治疗\"未选择");
                break;
            case "socialTreatmentHistory":
                tv_des.setText("\"社会机构治疗史\"未选择");
                break;
            case "socialTreatmentEffect":
                tv_des.setText("\"社会机构治疗结果\"未选择");
                break;
            case "mentalityDisease":
                tv_des.setText("\"脱发导致心理问题\"未选择");
                break;
            case "attention":
                tv_des.setText("\"治疗中最关心的问题\"未选择");
                break;
        }
    }

    /**
     * 设置点击事件
     */
    private void fillTagListener(final TagFlowLayout view, final String[] vals) {
        view.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                StringBuilder sb = new StringBuilder();
                for (Integer i : selectPosSet) {
                    sb.append(vals[i]).append(",");//用于多选,可以获取多选信息
                }
                String str = sb.toString();
                if (!TextUtils.isEmpty(str)) {
                    str = str.substring(0, str.length() - 1);//去掉最后一个逗号
                }
                switch (view.getId()) {

                    case R.id.flowlayout_is_relative:
                        if ("是".equals(str)) {
                            fillTagAdater(flowlayoutRelative, getResources().getStringArray(R.array.relative));
                            flowlayoutRelative.setVisibility(View.VISIBLE);
                        } else {
                            map.put("hereditaryHairLoss", "");//有无家族遗传脱发史
                            flowlayoutRelative.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.flowlayout_relative:
                        /*String[] split = str.split(",");
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i=0;i<split.length;i++){
                            switch (split[i]){
                                case "祖父":
                                    stringBuilder.append("0");
                                    break;
                                case "祖母":
                                    stringBuilder.append("1");
                                    break;
                                case "叔伯":
                                    stringBuilder.append("2");
                                    break;
                                case "父亲":
                                    stringBuilder.append("3");
                                    break;
                                case "亲兄弟":
                                    stringBuilder.append("4");
                                    break;
                                case "外祖父母":
                                    stringBuilder.append("5");
                                    break;
                                case "母亲":
                                    stringBuilder.append("6");
                                    break;
                                case "舅舅":
                                    stringBuilder.append("7");
                                    break;
                            }
                        }
                        String splice = StringUtils.splice(stringBuilder.toString());*/
                        map.put("hereditaryHairLoss", str);//有无家族遗传脱发史
                        break;
                    case R.id.flowlayout_scalp:
                        if ("非常多".equals(str)){
                            Choice = "0";
                        }else if("多".equals(str)) {
                            Choice = "1";
                        }else{
                            Choice = "2";
                        }
                        map.put("hairOil", Choice);//头油
                        break;
                    case R.id.flowlayout_dandruff:
                        if ("非常多".equals(str)){
                            Choice = "3";
                        }else if("多".equals(str)) {
                            Choice = "1";
                        }else{
                            Choice = "0";
                        }
                        map.put("headCrumbs", Choice);//头屑
                        break;
                    case R.id.flowlayout_scalp_rash:
                        if ("有".equals(str)){
                            Choice = "0";
                        }else if("无".equals(str)) {
                            Choice = "1";
                        }else{
                            Choice = "2";
                        }
                        map.put("scalpRash", Choice);//头皮红疹
                        break;
                    case R.id.flowlayout_scalp_itching:
                        if ("有".equals(str)){
                            Choice = "0";
                        }else{
                            Choice = "1";
                        }
                        map.put("scalpItching", Choice);//头皮是否有瘙痒
                        break;
                    case R.id.flowlayout_scalp_pustule:
                        if ("有".equals(str)){
                            Choice = "0";
                        }else if("无".equals(str)) {
                            Choice = "1";
                        }else{
                            Choice = "2";
                        }
                        map.put("scalpPustule", Choice);//头皮有无脓疱
                        break;
                    case R.id.flowlayout_hair_dia:
                        map.put("hairDia", str);//头发直径
                        break;
                    case R.id.flowlayout_white_hair:
                        map.put("whiteHair", str);//白发情况
                        break;
                    case R.id.flowlayout_hair_elasticity:
                        map.put("hairElasticity", str);//头发弹性
                        break;
                    case R.id.flowlayout_hair_loss_one_day_number:
                        /*switch (str){
                            case "30根以下":
                                Choice = "0";
                                break;
                            case "30-60根":
                                Choice = "1";
                                break;
                            case "60-100根":
                                Choice = "2";
                                break;
                            case "100根以上":
                                Choice = "3";
                                break;
                        }*/
                        map.put("hairLossOneDayNumber", str);//每日掉发量
                        break;
                    case R.id.flowlayout_light_nap_experiment:
                        if ("阳性".equals(str)){
                            Choice = "0";
                        }else{
                            Choice = "1";
                        }
                        map.put("pullHair", Choice);//轻拉毛实验
                        break;

                    case R.id.flowlayout_quantity:
                        map.put("hairLossBeforeAndAfter", str);//脱发前头发数量
                        break;
                    case R.id.flowlayout_speed:
                        if ("越脱越快".equals(str)){
                            Choice = "0";
                        }else if ("越脱越慢".equals(str)){
                            Choice = "1";
                        }else{
                            Choice = "2";
                        }
                        map.put("hairLossSpeed", Choice);//脱发速度
                        break;

                    case R.id.flowlayout_time_asleep:
                        map.put("timeAsleep", str);//习惯的入睡时间
                        break;
                    case R.id.flowlayout_sleep_quality:
                        if ("好".equals(str)){
                            Choice = "0";
                        }else if ("一般".equals(str)){
                            Choice = "1";
                        }else{
                            Choice = "2";
                        }
                        map.put("sleepingQuality", Choice);//睡眠质量
                        break;
                    case R.id.flowlayout_food_hobby:
                        if ("是".equals(str)){
                            Choice = "1";
                        }else{
                            Choice = "0";
                        }
                        map.put("foodHobby", Choice);//是否嗜好辛辣，油炸食物
                        break;
                    case R.id.flowlayout_lives_or_job_stress:
                        if ("是".equals(str)){
                            Choice = "1";
                        }else{
                            Choice = "0";
                        }
                        map.put("livesOrJobStress", Choice);//是否感觉生活/工作压力大
                        break;
                    case R.id.flowlayout_if_smoke:
                            map.put("ifSmoke", "1");//是否吸烟
                        if ("是".equals(str)) {
                            map.put("smokeQuality", "");//吸烟量
                            fillTagAdater(flowlayoutSmokeQuality, getResources().getStringArray(R.array.smokeQuality));
                            rl_3.setVisibility(View.VISIBLE);
                        } else {
                            map.put("ifSmoke", "0");//是否吸烟
                            map.put("smokeQuality", "");//吸烟量
                            rl_3.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.flowlayout_smoke_quality:
                        /*switch (str){
                            case "极少":
                                Choice = "0";
                                break;
                            case "半包/天":
                                Choice = "1";
                                break;
                            case "半包-1包/天":
                                Choice = "2";
                                break;
                            case "1包以上每天":
                                Choice = "3";
                                break;
                        }*/
                        map.put("smokeQuality", str);//吸烟量
                        break;
                    case R.id.flowlayout_if_sot:
                        if ("是".equals(str)){
                            Choice = "1";
                        }else{
                            Choice = "0";
                        }
                        map.put("ifSot", Choice);//是否嗜酒
                        break;

                    case R.id.flowlayout_marital_condition:
                        if ("已婚".equals(str)){
                            Choice = "1";
                        }else{
                            Choice = "0";
                        }
                        map.put("marriage", Choice);//婚姻状况
                        break;
                    case R.id.flowlayout_marital_plan:
                        if ("是".equals(str)){
                            Choice = "1";
                        }else{
                            Choice = "0";
                        }
                        map.put("giveBirth", Choice);//生育计划
                        break;
                    case R.id.flowlayout_occupation:
                        map.put("jobOccupation", str);//所属职业
                        if ("其他：".equals(str)) {
                            et_job_result.setVisibility(View.VISIBLE);
                        } else {
                            et_job_result.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.flowlayout_is_disease:
                        if ("是".equals(str)) {
                            fillTagAdater(flowlayoutDisease, getResources().getStringArray(R.array.disease));
                            flowlayoutDisease.setVisibility(View.VISIBLE);
                        }else {
                            map.put("diseaseHistory", "");//其他系统性疾病
                            flowlayoutDisease.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.flowlayout_disease:
                        map.put("diseaseHistory", str);//其他系统性疾病
                        diseaseHistory = str;
                        if (str.contains("其他：")) {
                            et_system_disease.setVisibility(View.VISIBLE);
                        } else {
                            et_system_disease.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.flowlayout_public_treatment:
                        if("是".equals(str)){
                            Choice = "1";
                        }else{
                            Choice = "0";
                        }
                        map.put("publicInstitution", Choice);//以前是否在公立机构寻求过治疗
                        if ("是".equals(str)) {
                            fillTagAdater(flowlayoutTreatmentResult, getResources().getStringArray(R.array.diagnosis_result));
                            fillTagAdater(flowlayoutTreatmentMethod, getResources().getStringArray(R.array.treatment_method));
                            fillTagAdater(flowlayoutTreatmentEffect, getResources().getStringArray(R.array.treatment_effect));
                            map.put("treatmentHistory", "");//公立机构治疗方法
                            map.put("publicDiagnosis", "");//公立机构诊断结果
                            map.put("treatmentEffect", "");//treatmentEffect
                            ll_1.setVisibility(View.VISIBLE);
                        } else {
                            map.put("publicInstitution", "");//以前是否在公立机构寻求过治疗
                            map.put("treatmentHistory", "");//公立机构治疗方法
                            map.put("publicDiagnosis", "");//公立机构诊断结果
                            map.put("treatmentEffect", "");//公立治疗效果
                            ll_1.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.flowlayout_treatment_method:
                        map.put("treatmentHistory", str);//公立机构治疗方法
                        treatmentHistory = str;
                        if (str.contains("其他：")) {
                            et_1.setVisibility(View.VISIBLE);
                        } else {
                            et_1.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.flowlayout_treatment_result:
                        map.put("publicDiagnosis", str);//公立机构诊断结果
                        if ("其他：".equals(str)) {
                            et_public_result.setVisibility(View.VISIBLE);
                        } else {
                            et_public_result.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.flowlayout_treatment_effect:
                        if ("有效".equals(str)){
                            Choice = "0";
                        }else if ("无效".equals(str)){
                            Choice = "2";
                        }else{
                            Choice = "1";
                        }
                        map.put("treatmentEffect", Choice);//公立治疗效果
                        break;
                    case R.id.flowlayout_socio_treatment:
                        if ("是".equals(str)) {
                            map.put("socialInstitution", "1");//是否社会机构寻求治疗
                            fillTagAdater(flowlayout_socio_text, getResources().getStringArray(R.array.reatment_history));
                            fillTagAdater(flowlayoutSocioTreatmentEffect, getResources().getStringArray(R.array.treatment_effect));
                            map.put("socialTreatmentHistory", "");//社会机构治疗史
                            map.put("socialTreatmentEffect", "");//社会机构治疗结果
                            ll_2.setVisibility(View.VISIBLE);
                        } else {
                            map.put("socialInstitution", "0");//是否社会机构寻求治疗
                            map.put("socialTreatmentHistory", "");//社会机构治疗史
                            map.put("socialTreatmentEffect", "");//社会机构治疗结果
                            ll_2.setVisibility(View.GONE);
                        }
                        break;
                    case R.id.flowlayout_socio_text:
                        String[] split1 = str.split(",");
                        StringBuilder stringBuilder1 = new StringBuilder();
                        for (int i=0;i<split1.length;i++){
                            switch (split1[i]){
                                case "章光101":
                                    stringBuilder1.append(0);
                                    break;
                                case "丝域养发":
                                    stringBuilder1.append(1);
                                    break;
                                case "植发":
                                    stringBuilder1.append(3);
                                    break;
                                case "其他护发养发机构":
                                    stringBuilder1.append(2);
                                    break;
                            }
                        }
                        String splice1 = StringUtils.splice(stringBuilder1.toString());
                        map.put("socialTreatmentHistory", splice1);//社会机构治疗史
                        break;
                    case R.id.flowlayout_socio_treatment_effect:
                        if ("有效".equals(str)){
                            Choice = "0";
                        }else if ("无效".equals(str)){
                            Choice = "2";
                        }else{
                            Choice = "1";
                        }
                        map.put("socialTreatmentEffect", Choice);//社会机构治疗结果
                        break;
                    case R.id.flowlayout_psychology_problem:
                       /* String[] split2 = str.split(",");
                        StringBuilder stringBuilder2 = new StringBuilder();
                        for (int i=0;i<split2.length;i++){
                            switch (split2[i]){
                                case "变老":
                                    stringBuilder2.append("0");
                                    break;
                                case "紧张":
                                    stringBuilder2.append("1");
                                    break;
                                case "孤独":
                                    stringBuilder2.append("2");
                                    break;
                                case "羞于见人":
                                    stringBuilder2.append("3");
                                    break;
                                case "拘束":
                                    stringBuilder2.append("4");
                                    break;
                                case "失落":
                                    stringBuilder2.append("5");
                                    break;
                                case "怕别人嘲笑":
                                    stringBuilder2.append("6");
                                    break;
                                case "担忧抑郁":
                                    stringBuilder2.append("7");
                                    break;
                                case "不被重视":
                                    stringBuilder2.append("8");
                                    break;
                                case "影响学习工作":
                                    stringBuilder2.append("9");
                                    break;
                                case "影响睡眠":
                                    stringBuilder2.append("10");
                                    break;
                            }
                        }
                        String splice2 = StringUtils.splice(stringBuilder2.toString());*/
                        map.put("mentalityDisease", str);//脱发导致心理问题
                        break;
                    case R.id.flowlayout_concern_problem:
                        map.put("attention", str);//治疗中最关心的问题
                        if (str.contains("其他：")) {
                            et_question_result.setVisibility(View.VISIBLE);
                        } else {
                            et_question_result.setVisibility(View.GONE);
                        }
                        break;
                }
            }
        });
    }

    /**
     * 填充标签数据
     *
     * @param view
     */
    private void fillTagAdater(final TagFlowLayout view, final String[] vals) {
        fillTagListener(view, vals);
        view.setAdapter(new TagAdapter<String>(vals) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                //默认为多选样式
                TextView tv = (TextView) mInflater.inflate(R.layout.flowlayout_tagview_item, view, false);
                tv.setText(s);
                //单选时样式改变
                if (view.getMaxSelectCount() == 1) {
                    //距顶端距离(内边距)
                    tv.setBackground(getResources().getDrawable(R.drawable.tagview_radio_bg));
                    tv.setTextColor(getResources().getColorStateList(R.color.tagview_edit_color));
                    //距顶端距离(内边距)
                    tv.setPadding(UIUtils.dp2px(5), UIUtils.dp2px(5), UIUtils.dp2px(5), UIUtils.dp2px(5));
                    tv.setTextSize(16);
                    //设置控件长宽
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, UIUtils.dp2px(30));
                    //外边距
                    layoutParams.setMargins(UIUtils.dp2px(5), UIUtils.dp2px(5), UIUtils.dp2px(5), UIUtils.dp2px(5));
                    tv.setLayoutParams(layoutParams);
                }
                return tv;
            }
        });
    }

    /**
     *创建本地缓存目录
     * @param bitmap
     * @param fileName
     */
    public Uri cacheBitmap(Bitmap bitmap, String fileName) {
        String filePath = getCacheDir().getAbsolutePath();//获取缓存绝对路径
        File file_less = new File(filePath + fileName);//体毛图
        if(fileIsExists(file_less)){//如果文件已存在,直接返回
            return Uri.parse(file_less.getAbsolutePath());
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//字节数组输出流
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);//png类型
        byte[] bitmap_baos = baos.toByteArray();
        try {
            FileOutputStream out = new FileOutputStream(file_less);
            out.write(bitmap_baos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Uri.parse(file_less.getAbsolutePath());
    }

    /**
     * 判断文件是否已存在
     * @return
     */
    public boolean fileIsExists(File file){
        try{
            if(!file.exists()){
                return false;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 发送数据
     */
       private void sendData() {
        if(et_job_result.getVisibility() == View.VISIBLE){
            map.put("jobOccupation", et_job_result.getText().toString().trim());//所属职业
        }
        if(et_1.getVisibility()== View.VISIBLE){
            if (!"".equals(treatmentHistory)){
                String s = et_1.getText().toString().trim() + "," + treatmentHistory;
                if (s.contains(",其他：")){
                    s=s.replace(",其他：", "");
                }
                map.put("treatmentHistory",s);//公立机构治疗方法
            }
        }
        if(et_system_disease.getVisibility()== View.VISIBLE){
            if (!"".equals(diseaseHistory)){
                String s = et_system_disease.getText().toString().trim() + "," + diseaseHistory;
                if (s.contains(",其他：")){
                    s=s.replace(",其他：", "");
                }
                map.put("diseaseHistory",s);//公立机构治疗方法
            }
        }
        if(et_public_result.getVisibility()== View.VISIBLE){
            map.put("publicDiagnosis", et_public_result.getText().toString().trim());//公立机构诊断结果
        }
        if(et_question_result.getVisibility()== View.VISIBLE){
            String str=map.get("attention");
            if(!str.contains("其他：,")){
                str=str.replace(",其他：", "");
            }else {
                str=str.replace("其他：,","");
            }
            map.put("attention", str + "," + et_question_result.getText().toString().trim());//治疗中最关心的问题
        }
        map.put("historyOfDisease", sliderTime.getMonthStr());

        map.put("bodyHairImg","少");

           for (String key : map.keySet()) {
               Log.e(key,map.get(key));
           }

           //将map保存到sp
           StringUtils.saveMapToSp(LoseHairInfoActivity.this,map);

           Intent intent = new Intent(LoseHairInfoActivity.this, TakePhotoActivity.class);
           Bundle bundle = new Bundle();
           bundle.putSerializable("First_Info",map);
           intent.putExtras(bundle);
           intent.putExtra("Where","FirstInquiry");
           startActivity(intent);
           finish();
       }

    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = this.managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

}