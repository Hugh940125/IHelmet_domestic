package com.slinph.ihelmet.ihelmet_domestic.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.slinph.ihelmet.ihelmet_domestic.R;
import com.slinph.ihelmet.ihelmet_domestic.activity.MainActivity;
import com.slinph.ihelmet.ihelmet_domestic.internet.Url;
import com.slinph.ihelmet.ihelmet_domestic.internet.Vo.PushVO;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.HttpUtils;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.ResultData;
import com.slinph.ihelmet.ihelmet_domestic.utils.SharePreferencesUtils;
import com.slinph.ihelmet.ihelmet_domestic.utils.TimeUtils;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;

/**
 * Created by Administrator on 2016/7/5.
 *
 */
public class RemindFragment extends Fragment implements View.OnClickListener{


    private MainActivity mActivity;
    private NumberPickerView picker_hour;
    private NumberPickerView picker_minute;
    private NumberPickerView picker_half_day;
    private Button btn_save;
    private CheckBox cb_isRemind;
    private SharedPreferences mSP;
    private int hour;
    private int minute;
    private int day;
    private int hour_24;
    private String hourFromNet = "";
    private String minuteFromNet = "";
    private String dateToString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
        originalState();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            originalState();
        }
    }

    private void originalState() {
        HttpUtils.postAsync(mActivity, Url.rootUrl+"/iheimi/servicesTime/selectOneByExample", new HttpUtils.ResultCallback<ResultData<PushVO>>() {
            @Override
            public void onError(int statusCode, Throwable error) {

            }

            @Override
            public void onResponse(ResultData<PushVO> response) {
                if (response.getSuccess()){
                    PushVO data = response.getData();
                    if (data != null){
                        String serviceTime = data.getServiceTime();
                        Integer days1 = data.getDays();
                        String substring = serviceTime.substring(11, 16);
                        String[] split = substring.split(":");
                        hourFromNet = split[0];
                        minuteFromNet = split[1];
                        Log.e("存在的推送",hourFromNet+"_"+minuteFromNet+"_"+days1);
                        initTime();
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("温馨提示：")
                                .setMessage("您还未设置过提醒时间，请设置")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                    }
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view_reminder = View.inflate(mActivity,R.layout.fragment_reminder,null);

        picker_hour = (NumberPickerView)view_reminder.findViewById(R.id.picker_hour);
        picker_minute = (NumberPickerView)view_reminder.findViewById(R.id.picker_minute);
        //picker_half_day = (NumberPickerView)view_reminder.findViewById(R.id.picker_half_day);

        cb_isRemind = (CheckBox) view_reminder.findViewById(R.id.cb_isRemind);

        btn_save = (Button)view_reminder.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);

        return view_reminder;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cb_isRemind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //状态改变后保存更改的状态
                SharePreferencesUtils.putBoolean(mActivity,"isRemind",isChecked);
                //根据checkbox的选中状态来开启或者关闭定时服务
            }
        });

        initCheckbox();
    }

    private void initCheckbox() {
        boolean isRemind = SharePreferencesUtils.getBoolean(mActivity,"isRemind", true);
        cb_isRemind.setChecked(isRemind);
    }

    private void initTime(){
        int h = -1;
        int m = -1;
        int d;
        int hour = SharePreferencesUtils.getInt(mActivity, "hour", -1);
        int minute = SharePreferencesUtils.getInt(mActivity, "minute", -1);
        Log.e("保存的时间",hour+"_"+minute);
        if (!hourFromNet.isEmpty() && !minuteFromNet.isEmpty()){
            h = Integer.valueOf(hourFromNet);
            m = Integer.valueOf(minuteFromNet);
            Log.e("网络的时间",h+"_"+m);
        }else if (hour != -1&&minute != -1){
            h = hour;
            m = minute;
        }else {
            h = 0;
            m = 0;
        }
       //d = SharePreferencesUtils.getInt(mActivity,"day",h>12 ? 1 : 0 );

        /*int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);
        int d = h < 12 ? 0 : 1;*/

        Log.e("初始化时间选择器",h+"_"+m);

        setData(picker_hour, 0, 23, h);
        setData(picker_minute, 0, 59, m);
        //setData(picker_half_day, 0, 1, d);
    }

    private void setData(NumberPickerView picker, int minValue, int maxValue, int value){
        picker.setMinValue(minValue);
        picker.setMaxValue(maxValue);
        picker.setValue(value);
    }

    @Override
    public void onClick(View v) {

        long currentTimeMillis = System.currentTimeMillis();
        dateToString = TimeUtils.getDateToString(currentTimeMillis);
        Log.e("dateToString", dateToString);

        String h = picker_hour.getContentByCurrValue();
                String m = picker_minute.getContentByCurrValue();
                //String d = picker_half_day.getContentByCurrValue();

                //Log.e("获取显示的内容",h+"-"+m+"-"+d);

                hour = Integer.parseInt(h);
                minute = Integer.parseInt(m);
               /* if ("PM".equals(d)) {
                    day = 1;
                    hour_24 = hour + 12;
                }
                else if ("AM".equals(d)){
                    day = 0;
                    hour_24 = hour;
                }*/
                //保存设定的时间，用于初始化显示下设定的时间
               // SharedPreferences.Editor edit = mSP.edit();
                SharePreferencesUtils.putInt(mActivity,"hour",hour);
                SharePreferencesUtils.putInt(mActivity,"minute",minute);
                //SharePreferencesUtils.putInt(mActivity,"day",day);
                //记录24小时制的时间用于设置提醒
                //SharePreferencesUtils.putInt(mActivity,"hour_24",hour_24);
                Log.e("保存的定时时间",hour + ":"+ minute + ":"+ day);

               // edit.commit();

                if(cb_isRemind.isChecked()) {
                    uploadOpenPushTime("1","1","2");
                }
                else {
                    uploadOpenPushTime("0","1","2");
                }
               // Toast.makeText(getActivity(), R.string.save_success,Toast.LENGTH_LONG).show();
        }

    public String CompleteMinute(int minute){
        if (minute<10){
            return "0"+minute;
        }else {
            return minute+"";
        }
    }

    private void setPush(String isOpen,String type,String days) {
        HttpUtils.postAsync(mActivity, Url.rootUrl+"/iheimi/servicesTime/insert", new HttpUtils.ResultCallback<ResultData>() {
            @Override
            public void onError(int statusCode, Throwable error) {
                //Toast.makeText(mActivity, "网络错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ResultData response) {
                if (response.getSuccess()){
                    Toast.makeText(mActivity, "时间设置成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mActivity, response.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        },new String[]{"serviceTime",dateToString+" "+CompleteMinute(hour)+":"+CompleteMinute(minute)}
        ,new String[]{"type",type}
        ,new String[]{"isOpen",isOpen}
        ,new String[]{"days",days});
    }

    private void updatePush(String isOpen,String type,String days,String id){
        Log.e("设定的时间",hour+":"+minute);
        HttpUtils.postAsync(mActivity, Url.rootUrl+"/iheimi/servicesTime/update", new HttpUtils.ResultCallback<ResultData>() {
                    @Override
                    public void onError(int statusCode, Throwable error) {
                        //Toast.makeText(mActivity, "网络错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(ResultData response) {
                        if (response.getSuccess()){
                            Toast.makeText(mActivity, "时间修改成功", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(mActivity, response.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },new String[]{"serviceTime",dateToString+" "+CompleteMinute(hour)+":"+CompleteMinute(minute)}
                ,new String[]{"type",type}
                ,new String[]{"isOpen",isOpen}
                ,new String[]{"days",days});
    }

    private void uploadOpenPushTime(final String isOpen, final String type, final String days) {
        HttpUtils.postAsync(mActivity, Url.rootUrl+"/iheimi/servicesTime/selectOneByExample", new HttpUtils.ResultCallback<ResultData<PushVO>>() {
            @Override
            public void onError(int statusCode, Throwable error) {

            }

            @Override
            public void onResponse(ResultData<PushVO> response) {
                if (response.getSuccess()){
                    PushVO data = response.getData();
                    if (data != null){
                        String serviceTime = data.getServiceTime();
                        Integer days1 = data.getDays();
                        Integer id = data.getId();
                        //Log.e("存在的推送",serviceTime+"_"+days1);
                        updatePush(isOpen,type,days,id+"");
                    }else {
                        setPush(isOpen,type,days);
                    }
                }
            }
        });
    }
}
