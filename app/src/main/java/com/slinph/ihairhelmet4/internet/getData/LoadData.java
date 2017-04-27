package com.slinph.ihairhelmet4.internet.getData;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.slinph.ihairhelmet4.internet.Url;
import com.slinph.ihairhelmet4.internet.Vo.FollowUpVO;
import com.slinph.ihairhelmet4.internet.Vo.TreatmentProgramsVO;
import com.slinph.ihairhelmet4.internet.model.Page;
import com.slinph.ihairhelmet4.internet.net_utis.DataConverter;
import com.slinph.ihairhelmet4.internet.net_utis.HttpUtils;
import com.slinph.ihairhelmet4.internet.net_utis.ResultData;
import com.slinph.ihairhelmet4.utils.StaticVariables;
import com.slinph.ihairhelmet4.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/13.
 *
 */
public class LoadData {
    /**
     * 获取网络图片
     * @param context
     */
    public static void getPhotos(final Context context){
            HttpUtils.postAsync(context, Url.rootUrl+"/iheimi/treatmentPrograms/selectUserEffect", new HttpUtils.ResultCallback<ResultData<List<FollowUpVO>>>() {
                @Override
                public void onError(int statusCode, Throwable error) {
                   // Toast.makeText(context, R.string.net_error, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(ResultData<List<FollowUpVO>> response) {
                    List<FollowUpVO> data = response.getData();
                    SimpleDateFormat Sdf = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                    if (data != null){
                        StaticVariables.IS_UPLOAD_INFO = true;
                        for (int i = 0;i <data.size();i ++){
                            if (i == data.size() - 1){
                                StaticVariables.USER_HAIR_TOP_PHOTO_URL = data.get(i).getTopViewUrl();//头顶照片
                                StaticVariables.USER_HAIR_LINE_PHOTO_URL = data.get(i).getHairlineUrl();//发际线图片
                                StaticVariables.USER_HAIR_AFTER_PHOTO_URL = data.get(i).getAfterViewUrl();//后脑勺图片
                                StaticVariables.USER_HAIR_PARTIAL_PHOTO_URL = data.get(i).getPartialViewUrl();//侧面图片
                            }
                                String topViewUrl = data.get(i).getTopViewUrl();
                                String hairlineUrl = data.get(i).getHairlineUrl();
                                String afterViewUrl = data.get(i).getAfterViewUrl();
                                String partialViewUrl = data.get(i).getPartialViewUrl();
                                Date createDtm = data.get(i).getCreateDtm();
                                //将Date转换为year-month-day的形式
                                String format = Sdf.format(createDtm);
                                StaticVariables.PHOTOS_URL.add(topViewUrl);
                                StaticVariables.PHOTOS_URL.add(hairlineUrl);
                                StaticVariables.PHOTOS_URL.add(afterViewUrl);
                                StaticVariables.PHOTOS_URL.add(partialViewUrl);
                                StaticVariables.PHOTOS_URL.add(format);
                        }
                    }
                    String s = StaticVariables.PHOTOS_URL.toString();
                    Log.e("PHOTOS_URL",s);
                }
            });
        }

    //获取最新的报告
    public static void isReportBack(final Context context){
        HttpUtils.postAsync(context,Url.rootUrl+"/iheimi/treatmentPrograms/selectOneNew",new HttpUtils.ResultCallback<ResultData<TreatmentProgramsVO>>(){

            @Override
            public void onError(int statusCode, Throwable error) {
               // Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ResultData<TreatmentProgramsVO> response) {
                Boolean success = response.getSuccess();
                if (success){
                    TreatmentProgramsVO data = response.getData();
                    if (data == null){
                        StaticVariables.USER_IS_SHOW_WAIT = true;
                        Log.e("报告","空");
                    }else{
                        Long id = data.getPatientId();
                        Log.e("id",id+"");
                        Log.e("mid",StaticVariables.MID+"");
                        Log.e("PATIENT_ID",StaticVariables.PATIENT_ID+"");
                        if (String.valueOf(id).equals(StaticVariables.MID)){
                            StaticVariables.USER_IS_SHOW_WAIT = false;
                        }else {
                            StaticVariables.USER_IS_SHOW_WAIT = true;
                        }
                        String hairLossSortUrl = data.getHairLossSortUrl();
                        String allStatus = data.getAllStatus();
                        Integer hairLossDegree = data.getHairLossDegree();
                        String graphic = data.getGraphic();
                        Integer hairLossDisease = data.getHairLossDisease();
                        Integer hairLossType = data.getHairLossType();
                        Date createDtm = data.getCreateDtm();
                        Integer currentStatus = data.getCurrentStatus();
                        Log.e("StaticVariables",StaticVariables.USER_IS_SHOW_WAIT+"");
                        Log.e("treatmentProgramss",hairLossSortUrl);
                        Log.e("allStatus",allStatus);
                        Log.e("hairLossDegree",hairLossDegree + "");
                        Log.e("graphic",graphic);
                        Log.e("hairLossDisease",hairLossDisease + "");
                        Log.e("hairLossType",hairLossType + "");
                        Log.e("createDtm",createDtm.toString());
                        Log.e("currentStatus",currentStatus+"");

                        String[] split = allStatus.split(",");

                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i=0;i<split.length;i++){
                            switch (split[i]){
                                case "0":
                                   if (currentStatus == Integer.valueOf(split[i])){
                                       StaticVariables.USER_CURRENT_TREATMENT_PROGRAMS_INT = i;
                                   }
                                   stringBuilder.append("消炎,");
                                    break;
                                case "1":
                                    if (currentStatus == Integer.valueOf(split[i])){
                                        StaticVariables.USER_CURRENT_TREATMENT_PROGRAMS_INT = i;
                                    }
                                   stringBuilder.append("控油,");
                                    break;
                                case "2":
                                    if (currentStatus == Integer.valueOf(split[i])){
                                        StaticVariables.USER_CURRENT_TREATMENT_PROGRAMS_INT = i;
                                    }
                                    stringBuilder.append("止脱,");
                                    break;
                                case "3":
                                    if (currentStatus == Integer.valueOf(split[i])){
                                        StaticVariables.USER_CURRENT_TREATMENT_PROGRAMS_INT = i;
                                    }
                                    stringBuilder.append("生发,");
                                    break;
                                case "4":
                                    if (currentStatus == Integer.valueOf(split[i])){
                                        StaticVariables.USER_CURRENT_TREATMENT_PROGRAMS_INT = i;
                                    }
                                    stringBuilder.append("粗发");
                                    break;
                            }
                        }
                        String[] schduleName = stringBuilder.toString().split(",");

                        StaticVariables.USER_REPORT_TIME = TimeUtils.getDateToString(createDtm.getTime());
                        StaticVariables.USER_ALL_TREATMENT_PROGRAMS = schduleName;
                        StaticVariables.USER_EXPERT_SUGGEST = graphic;
                        StaticVariables.USER_CLASSIFICATION_PHOTO_URL = hairLossSortUrl;
                        //StaticVariables.USER_CURRENT_TREATMENT_PROGRAMS_INT = currentStatus;
                        //脱发类型
                        StaticVariables.USER_HAIR_LOSS_TYPE = DataConverter.getType(hairLossType);
                        //脱发等级
                        StaticVariables.USER_HAIR_LOSS_DEGREE = DataConverter.getHairLossDegree(hairLossDegree);
                        //脱发伴随症状
                        StaticVariables.USER_HAIR_LOSS_DISEASE = DataConverter.getDisease(hairLossDisease);
                    }
                }else{
                    Toast.makeText(context, response.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //获取报告的列表
    static List<TreatmentProgramsVO> list;
    public static void getReportList(final Context context, String limit){
        HttpUtils.postAsync(context, Url.rootUrl+"/iheimi/treatmentPrograms/selectByExample", new HttpUtils.ResultCallback<ResultData<Page<TreatmentProgramsVO>>>() {
            @Override
            public void onError(int statusCode, Throwable error) {

            }

            @Override
            public void onResponse(ResultData<Page<TreatmentProgramsVO>> response) {
                if (response.getSuccess()){
                    Page<TreatmentProgramsVO> data = response.getData();
                    StaticVariables.REPORT_LIST = data.getList();
                }
            }
        },new String[]{"limit",limit});
    }

    //审核状况
    public void followUpOrNot(final Context context,Date lastReportDate){

    }
}
