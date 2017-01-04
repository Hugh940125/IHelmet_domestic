package com.slinph.ihelmet.ihelmet_domestic.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.slinph.ihelmet.ihelmet_domestic.R;
import com.slinph.ihelmet.ihelmet_domestic.application.MyApplication;
import com.slinph.ihelmet.ihelmet_domestic.bluetooth.BlueToothManager;
import com.slinph.ihelmet.ihelmet_domestic.internet.Url;
import com.slinph.ihelmet.ihelmet_domestic.internet.Vo.TreatmentRecordsVO;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.HttpUtils;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.ResultData;
import com.slinph.ihelmet.ihelmet_domestic.utils.DateUtil;
import com.slinph.ihelmet.ihelmet_domestic.utils.SharePreferencesConfig;
import com.slinph.ihelmet.ihelmet_domestic.utils.SharePreferencesUtils;
import com.slinph.ihelmet.ihelmet_domestic.utils.StaticVariables;
import com.slinph.ihelmet.ihelmet_domestic.utils.TimeUtils;
import com.slinph.ihelmet.ihelmet_domestic.view.NumberProgressBar;
import com.slinph.ihelmet.ihelmet_domestic.view.XCArcProgressBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import cn.aigestudio.datepicker.bizs.calendars.DPCManager;
import cn.aigestudio.datepicker.bizs.decors.DPDecor;
import cn.aigestudio.datepicker.utils.DateStaticVeriable;
import cn.aigestudio.datepicker.views.DatePicker;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class TreatFragment extends BaseFragment implements Runnable{

    private XCArcProgressBar arcProgressBarProgress;//圆形进度条
    private TextView tv_temperature, tv_humidity, tv_light_state;//温度,湿度,光照界面状态
    private int nowProcress;//圆形进度条当前进度
    private int maxTime;//光照总时间
    public NumberProgressBar numberProgressBar;//条形进度条
    public TextView tv_aggregate_scheduling;//光照总进度
    private int HUMI=0;//湿度
    private float TEMP=0;//温度
    private String mLightModeID="00";//光照模式编号,用于上传给头盔
    private boolean mIsFirstReadTemp = false;//用于设置工作模式
    public static boolean mIsLighting = false;//是否正在光照
    public static Context mContext;//上下文
    private View view;//fragment根布局
    private final String [][][] mLightWorkModeArray={//根据脱发类型选择光照模式
            {
                    {"50","50","50","28","28","28","28"},//男1*I*I型
                    {"50","50","50","50","28","28","28"},//男2*II*II型
                    {"50","50","50","50","3C","28","28"},//男3*IIA*IIa型
                    {"50","50","50","3C","28","28","28"},//男4*III*III型
                    {"50","50","50","3C","3C","28","28"},//男5*IIIvertex*IIIvertex型
                    {"50","50","50","3C","50","28","28"},//男6*IIIA*IIIa型
                    {"50","50","50","50","50","3C","28"},//男7*IV*IV型
                    {"50","50","50","50","3C","28","28"},//男8*V*V型
                    {"50","50","50","50","50","50","50"},//男9*IVA*IVa型+VI型+VII型
                    {"50","50","50","50","50","50","50"},//男10*VA*Va型
            },
            {
                    {"50","50","50","28","28","28","28"},//女1*I-1*I-1型
                    {"50","50","50","28","28","28","28"},//女2*I-2*I-2型
                    {"50","50","50","50","28","28","28"},//女3*I-3*I-3型
                    {"50","50","50","50","28","28","28"},//女4*I-4*I-4型
                    {"50","50","50","50","50","28","28"},//女5*II-1*II-1型
                    {"50","50","50","50","50","3C","3C"},//女6*II-2*II-2型
            },
            {
                    {"50","50","50","50","50","28","28"},//男/女:养护发
                    {"3C","3C","3C","3C","3C","3C","3C"},//男/女:弥漫性脱发
            }
    };

    public String secondPart;
    public String second_part;
    public String end_part;
    public Handler activity_handler;
    public String HUMIDITY_DEGREEE = "";
    public String TEMPERATURE_VALUE = "";
    public String LIGHT_STATE = "";
    public String TIME_LEFT = "";
    public int BATTERAY_LEFT = 0;
    public int NOWPROGRESS_VALUE = 0;
    private TextView loss_type;
    private int thismonth_total;
    private int totalTreatTime;
    private int monthlyTreatTime;
    private TextView tv_times;
    private DatePicker picker;
    private int currentMonthTimes;
    private ArrayList<String> YMDRecord;
    private ArrayList<Date> originalRecord;
    private String hardware;
    private String software;
    private String number;
    private String displayLossType = "";
    private boolean trymode = true;

    public static TreatFragment newInstance() {
        return new TreatFragment();
    }

    //上传光照记录
    private void sendTreatHistory(String oil,String temp,String creat_time) {
       HttpUtils.postAsync(mContext, Url.rootUrl+"/iheimi/treatmentRecords/insert", new HttpUtils.ResultCallback<ResultData<TreatmentRecordsVO>>() {
           @Override
           public void onError(int statusCode, Throwable error) {
               Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onResponse(ResultData<TreatmentRecordsVO> response) {
               if (response.getSuccess()){
                   TreatmentRecordsVO data = response.getData();
                   String s = data.getCreateDtm().toString();
                   Log.e("getCreateDtm",s);
                   LoadHistory();
               }else{
                   Toast.makeText(mContext, response.getMsg(), Toast.LENGTH_SHORT).show();
               }
           }
       },new String[]{"grease",oil}
        ,new String[]{"temperature",temp}
        ,new String[]{"create_dtm",creat_time});
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

        /*boolean isFirst = SharePreferencesUtils.getBoolean(mContext, "isFirstrun",false);
        if (!isFirst){ //说明第一次运行
            firstRun_time = System.currentTimeMillis();
            SharePreferencesUtils.putLong(mContext,"FirstRun_time", firstRun_time);
            SharePreferencesUtils.putBoolean(mContext,"isFirstrun",true);
        }
        else {
            firstRun_time = SharePreferencesUtils.getLong(mContext, "FirstRun_time",0);
        }*/
        if (StaticVariables.REGIS_TIME != -1L){
            int intMonth = TimeUtils.getIntMonth(StaticVariables.REGIS_TIME);
            int month = DateUtil.getMonth();
            Log.e("本月"+month,"注册"+intMonth);
            if (intMonth == month){
                int monthDays = DateUtil.getMonthDays(DateUtil.getYear(), DateUtil.getMonth());
                int monthDay = DateUtil.getMonthDay(StaticVariables.REGIS_TIME);
                thismonth_total = (monthDays - monthDay)/2 + 1;
                //Log.e("本月共有"+monthDays+"天","保存的时间是本月的"+monthDay+"天"+"___"+ thismonth_total);
            }else {
                thismonth_total = 15;
            }
        }

        LoadHistory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_treat, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //默认为总次数为15
        tv_temperature = (TextView) view.findViewById(R.id.tv_temperature);
        tv_humidity = (TextView) view.findViewById(R.id.tv_humidity);
        tv_light_state = (TextView) view.findViewById(R.id.tv_light_state);
        tv_aggregate_scheduling = (TextView) view.findViewById(R.id.tv_aggregate_scheduling);
        loss_type = (TextView) view.findViewById(R.id.loss_type);
        numberProgressBar = (NumberProgressBar) view.findViewById(R.id.numberbar1);
        //numberProgressBar.setProgress(progress);//默认为0

        //总次数统计
        //totaltimes = SharePreferencesUtils.getInt(mContext, "totaltimes",0);
        numberProgressBar.setMax(thismonth_total);

        //initdialogdata();
        //本月总次数的统计
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mContext.getString(R.string.total));
        stringBuilder.append(thismonth_total);
        stringBuilder.append(mContext.getString(R.string.times));
        tv_aggregate_scheduling.setText(stringBuilder.toString());
        arcProgressBarProgress = (XCArcProgressBar) view.findViewById(R.id.arcProgressBar_progress);
        initLightData();

        activity_handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 111:
                        //Log.e("handler","111收到信息");
                        String start_time1 = SharePreferencesUtils.getString(mContext, SharePreferencesConfig.LIGHT_DATE_MILLIS,"");
                        if (!start_time1.isEmpty()){
                            String temp1 = SharePreferencesUtils.getString(mContext, "temp");
                            String hum1 = SharePreferencesUtils.getString(mContext, "hum");
                            Log.e("111收到信息",temp1+"__"+hum1+"__"+start_time1);
                            sendTreatHistory(hum1,temp1,start_time1);
                        }
                        break;
                    case 222:
                        Log.e("handler","222收到信息");
                        String start_time2= SharePreferencesUtils.getString(mContext, SharePreferencesConfig.LIGHT_DATE_MILLIS,"");
                        if (!start_time2.isEmpty()) {
                            String temp2 = SharePreferencesUtils.getString(mContext, "temp");
                            String hum2 = SharePreferencesUtils.getString(mContext, "hum");
                            Log.e("222收到信息",temp2+"__"+hum2);
                            sendTreatHistory(hum2, temp2, start_time2);
                        }
                        break;
                    case 333:
                        monthlyTreatTime = StaticVariables.LIGHT_HISTORY_SET.size();
                        if (monthlyTreatTime > thismonth_total){
                            numberProgressBar.setMax(monthlyTreatTime);
                        }
                        numberProgressBar.setProgress(monthlyTreatTime);
                        Log.e("进度",monthlyTreatTime+"");
                        break;
                    case 36:
                        SharePreferencesUtils.putString(mContext, SharePreferencesConfig.DRIVACE_HARDWARE, hardware);
                        SharePreferencesUtils.putString(mContext, SharePreferencesConfig.DRIVACE_SOfTWARE, software);
                        break;
                    case 37:
                        SharePreferencesUtils.putString(mContext, SharePreferencesConfig.DRIVACE_NUMBER, number);
                        break;
                }
            }
        };

        numberProgressBar.setOnClickListener(new View.OnClickListener() {//进度条点击事件
            @Override
            public void onClick(View v) {
                showCalendar();
            }
        });
    }

    private void showCalendar() {
        View dialogView = View.inflate(mContext, R.layout.record_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(dialogView)
                .show();

        tv_times = (TextView) dialogView.findViewById(R.id.tv_times);

        picker = (DatePicker) dialogView.findViewById(R.id.dp);
        if(picker != null){
            Date date = new Date(System.currentTimeMillis());
            picker.setDate(TimeUtils.getYear(date), TimeUtils.getMonth(date));
            picker.setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
                @Override
                public void onDateSelected(List<String> date) {
                    String result = "";
                    Iterator iterator = date.iterator();
                    while (iterator.hasNext()) {
                        result += iterator.next();
                        if (iterator.hasNext()) {
                            result += "\n";
                        }
                    }
                    Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
                }
            });

            Log.e("originalRecord", originalRecord.toString());
            addCircleBG(YMDRecord);

            getCurrentPageYM();
            BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    getCurrentPageYM();
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.date.change");
            mContext.registerReceiver(broadcastReceiver,intentFilter);
            tv_times.setText(mContext.getString(R.string.monthly_records) + currentMonthTimes + mContext.getString(R.string.total_records) + YMDRecord.size());
        }
    }

    private void addCircleBG(List<String> list){
        Log.e("addCircleBG","画记录");
        DPCManager.getInstance().setDecorBG(list);
        picker.setDPDecor(new DPDecor() {
            @Override
            public void drawDecorBG(Canvas canvas, Rect rect, Paint paint) {
                paint.setColor(Color.rgb(0x00,0xFF,0x00));
                canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2.5F, paint);
            }
        });
    }

    public static int getCurrentMonthTimes(List<Date> list,String targetY,String targetM){
        Iterator iterator = list.iterator();
        int times = 0;
        while(iterator.hasNext()){
            Date next = (Date) iterator.next();
            int month = getMonth(next);
            int year = getYear(next);
            Log.e("getCurrentMonthTimes",year+"-"+month);
            if(month == Integer.valueOf(targetM) && year == Integer.valueOf(targetY)){
                times++;
            }
        }
        return times;
    }

    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getYear(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    private void getCurrentPageYM(){
        String currentMonth = picker.getCurrentMonth();
        String currentYear = picker.getCurrentYear();
        String intStringMonth = judgeMonth(currentMonth);
        Log.e("当前页面的年月份", currentYear +"-"+ intStringMonth);
        currentMonthTimes = getCurrentMonthTimes(originalRecord, currentYear, intStringMonth);
        Log.e("times", currentMonthTimes +"");
        tv_times.setText(mContext.getString(R.string.monthly_records) + currentMonthTimes + mContext.getString(R.string.total_records) + YMDRecord.size());
    }

    private String judgeMonth(String mon){
        String intString = "";
        switch (mon){
            case "一月":
                intString = "1";
                break;
            case "二月":
                intString = "2";
                break;
            case "三月":
                intString = "3";
                break;
            case "四月":
                intString = "4";
                break;
            case "五月":
                intString = "5";
                break;
            case "六月":
                intString = "6";
                break;
            case "七月":
                intString = "7";
                break;
            case "八月":
                intString = "8";
                break;
            case "九月":
                intString = "9";
                break;
            case "十月":
                intString = "10";
                break;
            case "十一月":
                intString = "11";
                break;
            case "十二月":
                intString = "12";
                break;
        }
        return intString;
    }



    //保证选择
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        initLightData();
    }

    /**
     * 初始化bluetooth数据,设置工作模式
     */
    public void initBluetoothData() {
        LIGHT_STATE = getResources().getString(R.string.connected);
        activity_handler.post(TreatFragment.this);

        mIsFirstReadTemp = true;//允许进入设置工作模式
        MyApplication.mainActivity.sendData("5A010101020000584D");
        new AsyncTask_arcProgressBarProgress(nowProcress).execute(0);//圆形进度条初始化
        Log.e("initBluetoothData","ok");
    }


    /**
     * 输入10进制数组，输出16进制格式的字符串
     *
     * @param b
     *            10进制字符数组 [90,1,1,1,1,0,0,88,77]相当于[W----XM]
     * @return 16进制格式的字符串 [5A010101010000584D]
     */
    public String printHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);// 取低8位数据，同时转换为16进制字符串
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            result += hex.toUpperCase();// 5A
        }
        return result;
    }

    /**
     * 处理接收到的bluetooth数据
     *
     * @param data
     */
    public void disposeData(String data) {
        String CMD = data.substring(8, 10);
        Log.e("**CMD", CMD);
       // Log.e("**data", data);
        switch (CMD) {
            case "01"://读取头盔的状态
                int vol = SharePreferencesUtils.getInt(mContext, "volume", -1);
                if (vol != -1){
                    StaticVariables.USER_EQUIPMENT_VOLUME = vol;
                    getActivity().invalidateOptionsMenu();
                }
                String status = data.substring(10, 12);
                Log.e("读取头盔的状态",status);
                switch (status){
                    case "01"://佩戴正常工作
                        break;
                    case "02"://佩戴未工作
                        MyApplication.mainActivity.sendData("5A010101030000584D");
                        break;
                    case "03"://未佩戴未工作
                        MyApplication.mainActivity.sendData("5A010101030000584D");
                        break;
                    case "04"://未佩戴已工作
                        break;
                }
                break;

            case "02"://上次治疗是否已完成
                String finish = data.substring(10, 12);
                Log.e("上次治疗是否已完成 02 ",finish);
                if("01".equals(finish)) {
                        Message msg = activity_handler.obtainMessage();
                        msg.what = 111;
                        activity_handler.sendMessage(msg);
                }
                Log.e("disposeData","disposeData");
                MyApplication.mainActivity.sendData("5A010101340000584D");
                //initdialogdata();
                break;
            case "03"://读取音量状态
                String volume = data.substring(10, 12);
                if (volume.equals("00")) {  //设备当前为静音状态
                    StaticVariables.USER_EQUIPMENT_VOLUME = 0;
                    getActivity().invalidateOptionsMenu();
                    SharePreferencesUtils.putInt(mContext,"volume",0);
                } else { //设备不是静音状态
                    StaticVariables.USER_EQUIPMENT_VOLUME = 1;
                    getActivity().invalidateOptionsMenu();
                    SharePreferencesUtils.putInt(mContext,"volume",1);
                }
                initBluetoothData();
                break;
            case "10"://开始治疗返回数据
                String cure = data.substring(10, 12);
                if (cure.equals("01")) {
                    MyApplication.mainActivity.sendData("5A010101360000584D");//MyApplication.mainActivity.sendData("5A010101370000584D");
                }
                break;
            case "11"://暂定治疗
                String pause = data.substring(10, 12);
                if (pause.equals("01")) {
                    ///tv_light_state.setText("光照暂停");
                    LIGHT_STATE = getResources().getString(R.string.treat_pause);
                    activity_handler.post(TreatFragment.this);

                    mIsLighting = true;
                }
                break;
            case "12": //正在光照
                String goon = data.substring(10, 12);
                if (goon.equals("01")) {
                    ///tv_light_state.setText("正在光照");
                    LIGHT_STATE = getResources().getString(R.string.treating);
                    activity_handler.post(TreatFragment.this);
                    mIsLighting = true;
                    mContext.sendBroadcast(new Intent("Sound_Button"));
                }
                break;
            case "1F"://治疗停止
                //Lg.e("**maxTime",maxTime+"");
                //Lg.e("**parseInt",Integer.parseInt(data.substring(10, 12), 16)+"");
                //if(maxTime==Integer.parseInt(data.substring(10, 12), 16)){//正常结束光照
                if(Integer.parseInt(data.substring(10, 12), 16)  >= 20){//因为有时会默认35分钟,所以大于25分钟就算正常结束
                    LIGHT_STATE = getResources().getString(R.string.treat_over);
                    activity_handler.post(TreatFragment.this);

                    Message message = activity_handler.obtainMessage();
                    message.what = 222;
                    activity_handler.sendMessage(message);
                }
                mIsLighting = false;
                mContext.sendBroadcast(new Intent("Sound_Button"));
                initLightData();//结束初始化温度湿度，进度条
                break;
            case "25"://静音设置,好像无返回
                String voice = data.substring(10, 12);
                /*if (voice.equals("00")) {
                    MyApplication.mainActivity.setToolbar(0, MyApplication.mainActivity.getResources().getString(R.string.light), R.mipmap.btn_voice2x);
                } else {
                    MyApplication.mainActivity.setToolbar(0, MyApplication.mainActivity.getResources().getString(R.string.light), R.mipmap.btn_mute2x);
                }*/
                break;
            case "26"://发送设置工作模式返回数据
                Log.e(" case 26","设置模式返回了数据"+data);
                String work = data.substring(10, 12);
                Log.e(" case 26",work);
                if (work.equals("01")) {//01说明设置成功.

                    Log.e("case 26"+work,"01说明设置成功");
                    long timeLong2 = System.currentTimeMillis();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    String timeFormat = simpleDateFormat.format(timeLong2);
                    final String hexStr = printHexString(timeFormat.getBytes());
                    String[] phone = {SharePreferencesUtils.getString(mContext, SharePreferencesConfig.SP_USER_PHONE,"")};

                    if(!TextUtils.isEmpty(phone[0])&& phone[0].length()==11){//用户识别码
                        phone[0] ="0"+ phone[0];
                    }else {
                        phone[0] ="000000000000";
                    }
                    phone[0] =printHexString(phone[0].getBytes());
                    if(mLightModeID.length()!=2){
                        mLightModeID="00";
                    }
                    StringBuilder sb=new StringBuilder();
                    sb.append("5A01011C10")
                            .append(hexStr)
                            .append(phone[0])
                            .append(mLightModeID)
                            .append("0000584D");
                    /**
                     * 6.25注释掉
                     */
                    // mBlueToothManager.sendMessage(sb.toString());//发送开始治疗//,02为治疗模式代号,1-18.
                    String msg = sb.toString();
                    //指令的前半段
                    String firstPart = msg.substring(0, 36);
                    //分出指令的后半段
                    secondPart = msg.substring(36);
                    MyApplication.mainActivity.sendData(firstPart);

                    String CurrentTime_string = String.valueOf(System.currentTimeMillis());
                    SharePreferencesUtils.putString(mContext, SharePreferencesConfig.LIGHT_DATE_MILLIS, CurrentTime_string);
                    Log.e("开始治疗返回数据","保存的开始时间"+CurrentTime_string);
                    SharePreferencesUtils.putString(mContext,"temp",TEMP+"");
                    SharePreferencesUtils.putString(mContext,"hum",HUMI+"");
                }
                break;
            case "31"://显示剩余时间5A010102311A0101584D
                int time = Integer.parseInt(data.substring(10, 12), 16);
                ///arcProgressBarProgress.setTitle(time + "");
                TIME_LEFT = time + "";
                activity_handler.post(TreatFragment.this);

                if (maxTime != 0){
                    SharePreferencesUtils.putInt(mContext, "maxTime", this.maxTime);
                }else {
                    int timeSaved = SharePreferencesUtils.getInt(mContext,"maxTime",-1);
                    if (timeSaved != -1){
                        maxTime = timeSaved;
                    }
                }

                if(maxTime==0){
                    maxTime = 35;//假如中间连接头盔,没有最大时间,默认为35分钟
                }

                Log.e("显示剩余时间",time+"-"+maxTime);
                if (maxTime == 20&&trymode){
                    trymode = false;
                    displayLossType = "体验\r\n模式";
                    activity_handler.post(TreatFragment.this);
                }
                nowProcress = time * 100 / maxTime;
                if (time <= maxTime) {
                    ///tv_light_state.setText("正在光照");
                    LIGHT_STATE = getResources().getString(R.string.treating);
                    activity_handler.post(TreatFragment.this);
                    mIsLighting = true;
                    mContext.sendBroadcast(new Intent("Sound_Button"));
                }
                Log.e("nowProcress",nowProcress+"");
                ///arcProgressBarProgress.setProgress(nowProcress);//有更新作用
                NOWPROGRESS_VALUE = nowProcress;
                activity_handler.post(TreatFragment.this);
                break;
            case "33"://显示温度
                int i = Integer.parseInt(data.substring(10, 14), 16);
                TEMP = (float) i / 10;
                if (mIsFirstReadTemp) {
                    setWorkPattern((int) TEMP);
                    mIsFirstReadTemp = false;
                    Log.e("mIsFirstReadTemp",TEMP+"");

                    TEMPERATURE_VALUE = TEMP + "";
                    activity_handler.post(TreatFragment.this);
                } else {
                    Log.e("mIsFirstReadTemp",TEMP+"");
                    TEMPERATURE_VALUE = TEMP + "";
                    activity_handler.post(TreatFragment.this);
                }
                break;
            case "34"://显示湿度
                int b = Integer.parseInt(data.substring(10, 14), 16);
                HUMI = b / 10;
                Log.e("湿度",""+HUMI);
                /*if(!mIsLighting){//假如不是正在光照,不显示湿度
                    humidity=0;
                }*/
                ///tv_humidity.setText(humidity + "%");
                HUMIDITY_DEGREEE = HUMI+"";
                activity_handler.post(TreatFragment.this);
                MyApplication.mainActivity.sendData("5A010101330000584D");
                break;
            case "36"://软硬件版本号
                Log.e("36",data);
                String str = BlueToothManager.toStringHex(data.substring(10,data.length()-8));
                String[] split = str.split("\r\n");
                String replace2 = split[1].replace(" ", "");
                String replace1 = split[0].replace(" ", "");
                Log.e("split[0]",split[0]+"123");
                hardware = replace1;
                software = replace2;
                Message mes = activity_handler.obtainMessage();
                mes.what = 36;
                activity_handler.sendMessage(mes);
                MyApplication.mainActivity.sendData("5A010101370000584D");
                break;
            case "37"://设备编号
                int length = data.length();
                Log.e("number.length",length+"");
                number = BlueToothManager.toStringHex(data.substring(10, length-8));
                Log.e("number",number);
                Message mess = activity_handler.obtainMessage();
                mess.what = 37;
                activity_handler.sendMessage(mess);
                MyApplication.mainActivity.sendData("5A010101010000584D");
                break;
            case "3B"://电量
                switch (data.substring(11, 12)) {
                    case "1":
                        ///imageRes = R.mipmap.battery_52x;
                        arcProgressBarProgress.setElectricity_title("设备电量不足，请及时充电！");

                        BATTERAY_LEFT = R.mipmap.battery_52x;
                        break;
                    case "2":
                        ///imageRes = R.mipmap.battery_252x;
                        arcProgressBarProgress.setElectricity_title("设备电量低，请在本次使用后充电");

                        BATTERAY_LEFT = R.mipmap.battery_52x;
                        break;
                    case "3":
                       /// imageRes = R.mipmap.battery_502x;

                        BATTERAY_LEFT = R.mipmap.battery_502x;
                        break;
                    case "4":
                       /// imageRes = R.mipmap.battery_752x;

                        BATTERAY_LEFT = R.mipmap.battery_752x;
                        break;
                    case "5":
                        ///imageRes = R.mipmap.battery_1002x;

                        BATTERAY_LEFT = R.mipmap.battery_1002x;
                        break;
                }
                ///arcProgressBarProgress.setImages(imageRes);
                activity_handler.post(TreatFragment.this);
                break;
        }
    }

    /**
     * 根据环境温度设置工作模式
     *
     * @param temp
     */
    public void setWorkPattern(int temp) {
        //1.假如报告没返回,直接启动养护发模式
        if(TextUtils.isEmpty(StaticVariables.USER_HAIR_LOSS_DEGREE)){
            maxTime=20;
            /**
             * 6.25 注释掉
             */
            //mBlueToothManager.sendMessage("5A01010F2650145014501450145014281428140000584D");//养护发

            //发送前半段,后半段在mainactivity的brocatreciever里发送
            MyApplication.mainActivity.sendData("5A01010F265014501450145014501428142814");

            mLightModeID="11";
            return;
        }
        //2.根据环境温度获取光照时间,单独处理弥漫性脱发
        if (temp < 20) {//25分钟-19
            maxTime = 25;
        } else if (temp > 30) {//35分钟-23
            maxTime = 35;
        } else {
            maxTime=temp + 5;
        }
        String hexTime = IntToHex(maxTime);//16进制光照时间
        if("弥漫性脱发".equals(StaticVariables.USER_HAIR_LOSS_DEGREE)) {//弥漫性脱发
            StringBuilder sb = new StringBuilder();
            sb.append("5A01010F26");
            String[] array=mLightWorkModeArray[2][1];
            for(int i=0;i<array.length;i++){
                sb.append(array[i])
                        .append(hexTime);
            }
            sb.append("0000584D");
            mLightModeID="12";
            /**
             * 6.25注释掉
             */
            //mBlueToothManager.sendMessage(sb.toString());

            //将指令分成两个部分
            String set_Mdoe = sb.toString();
            String first_part = set_Mdoe.substring(0, 32);
            second_part = set_Mdoe.substring(32);
            //发送指令的前一部分
            MyApplication.mainActivity.sendData(first_part);
            return;
        }
        //3.根据性别和脱发等级选择光照模式,默认为养护发模式
        int sexIndex=2;
        int degreeIndex=-1;
        if("男".equals(StaticVariables.USER_SEX)){
            sexIndex=0;
            switch (StaticVariables.USER_HAIR_LOSS_DEGREE) {
                case "I型":
                    degreeIndex=0;
                    mLightModeID="01";
                    break;
                case "II型":
                    degreeIndex=1;
                    mLightModeID="02";
                    break;
                case "IIa型":
                    degreeIndex=2;
                    mLightModeID="03";
                    break;
                case "III型":
                    degreeIndex=3;
                    mLightModeID="04";
                    break;
                case "IIIvertex型":
                    degreeIndex=4;
                    mLightModeID="05";
                    break;
                case "IIIa型":
                    degreeIndex=5;
                    mLightModeID="06";
                    break;
                case "IV型":
                    degreeIndex=6;
                    mLightModeID="07";
                    break;
                case "V型":
                    degreeIndex=7;
                    mLightModeID="08";
                    break;
                case "IVa型":
                case "VI型":
                case "VII型":
                    degreeIndex=8;
                    mLightModeID="09";
                    break;
                case "Va型":
                    degreeIndex=9;
                    mLightModeID="10";
                    break;
                default:
                    mLightModeID="11";
                    maxTime = 20;
                    /**
                     * hugh在6.25注释掉
                     */
                    //mBlueToothManager.sendMessage("5A01010F2650145014501450145014281428140000584D");//养护发

                    //发送养护发前半段,后半段在mainactivity的brocatreciever里发送
                    MyApplication.mainActivity.sendData("5A01010F265014501450145014501428142814");
                    //MyApplication.mainActivity.sendData("5A01010F2650145014501450145014281428140000584D");

                    return;
            }
        }else if("女".equals(StaticVariables.USER_SEX)){
            sexIndex=1;
            switch (StaticVariables.USER_HAIR_LOSS_DEGREE) {
                case "I-1型":
                    degreeIndex = 0;
                    mLightModeID="13";
                    break;
                case "I-2型":
                    degreeIndex = 1;
                    mLightModeID="14";
                    break;
                case "I-3型":
                    degreeIndex = 2;
                    mLightModeID="15";
                    break;
                case "I-4型":
                    degreeIndex = 3;
                    mLightModeID="16";
                    break;
                case "II-1型":
                    degreeIndex = 4;
                    mLightModeID="17";
                    break;
                case "II-2型":
                    degreeIndex = 5;
                    mLightModeID="18";
                    break;
                default:
                    mLightModeID="11";
                    maxTime = 20;
                    //发送养护发前半段,后半段在mainactivity的brocatreciever里发送
                    MyApplication.mainActivity.sendData("5A01010F265014501450145014501428142814");
                    return;
            }
        }else {
            mLightModeID="11";
            maxTime = 20;
            //发送养护发前半段,后半段在mainactivity的brocatreciever里发送
            MyApplication.mainActivity.sendData("5A01010F265014501450145014501428142814");
            return;
        }
        //4.组合准备发送的蓝牙代码
        StringBuilder sb = new StringBuilder();
        sb.append("5A01010F26");
        String[] array=mLightWorkModeArray[sexIndex][degreeIndex];
        for(int i=0;i<array.length;i++){
            sb.append(array[i])
                    .append(hexTime);
        }
        sb.append("0101584D");
        /**
         * 6.25注释掉
         */
       // mBlueToothManager.sendMessage(sb.toString());

        String setMode = sb.toString();
        String pre_part = setMode.substring(0, 34);
        end_part = setMode.substring(34);
        MyApplication.mainActivity.sendData(pre_part);
    }

    //10进制转16进制
    public static String IntToHex(int n){
        char[] ch = new char[20];
        int nIndex = 0;
        while ( true ){
            int m = n/16;
            int k = n%16;
            if ( k == 15 )
                ch[nIndex] = 'F';
            else if ( k == 14 )
                ch[nIndex] = 'E';
            else if ( k == 13 )
                ch[nIndex] = 'D';
            else if ( k == 12 )
                ch[nIndex] = 'C';
            else if ( k == 11 )
                ch[nIndex] = 'B';
            else if ( k == 10 )
                ch[nIndex] = 'A';
            else
                ch[nIndex] = (char)('0' + k);
            nIndex++;
            if ( m == 0 )
                break;
            n = m;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(ch, 0, nIndex);
        sb.reverse();
        return sb.toString();
    }

    /**
     * 为第三个进度条更新UI开的线程,让圆形进度条动起来,管理圆形进度条的asyncTask
     */
    private class AsyncTask_arcProgressBarProgress extends AsyncTask<Integer, Integer, Integer> {
        private int setTimer;

        public AsyncTask_arcProgressBarProgress(int timer) {
            this.setTimer = timer - 1;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {//可以使用进度条增加用户体验度。 此方法在主线程执行，用于显示任务执行的进度。
            arcProgressBarProgress.setProgress(values[0]);
        }

        @Override
        protected Integer doInBackground(Integer... params) {//后台执行
            int timer = 0;
            while (timer <= setTimer) {
                publishProgress(timer);//在执行过程中可以调用publicProgress(Progress…)来更新任务的进度。
                timer++;
                SystemClock.sleep(10);
            }
            publishProgress(timer);
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {//相当于Handler 处理UI的方式，在这里面可以使用在doInBackground 得到的结果处理操作UI。 此方法在主线程执行，任务执行的结果作为此方法的参数返回
            super.onPostExecute(integer);
        }
    }

    /**
     * 初始化圆形进度条
     */
    public void initProcess() {
        new AsyncTask_arcProgressBarProgress(nowProcress).execute(0);
    }

    /**
     * 初始化光照数据(温度湿度，两个进度条)
     */
    public void initLightData() {
        tv_humidity.setText("%");
        tv_temperature.setText("℃");
        tv_light_state.setText(getResources().getString(R.string.discounected));
        arcProgressBarProgress.setTitle("");//中心时间文字
        arcProgressBarProgress.setImages(0);//电量
        arcProgressBarProgress.setDisplayText(false);//不显示中间的百分比
        nowProcress = 0;
        arcProgressBarProgress.setProgress(nowProcress);//更新圆形进度条:更新圆形进度条需调用此方法
        loss_type.setText(StaticVariables.USER_HAIR_LOSS_DEGREE);
    }

    @Override
    public void run() {
        tv_humidity.setText(HUMIDITY_DEGREEE+"%");
        tv_temperature.setText(TEMPERATURE_VALUE+"℃");
        //暂停状态是将字体变为橙色
        if (LIGHT_STATE.equals("光照暂停")){
            tv_light_state.setTextColor(Color.rgb(255,140,0));
            tv_humidity.setTextColor(Color.rgb(255,140,0));
            tv_temperature.setTextColor(Color.rgb(255,140,0));
        }
        //其他状态字体是黑色
        else{
            tv_light_state.setTextColor(Color.BLACK);
            tv_humidity.setTextColor(Color.BLACK);
            tv_temperature.setTextColor(Color.BLACK);
        }
        tv_light_state.setText(LIGHT_STATE);
        arcProgressBarProgress.setTitle(TIME_LEFT);//中心时间文字
        arcProgressBarProgress.setImages(BATTERAY_LEFT);//电量
        arcProgressBarProgress.setDisplayText(false);//不显示中间的百分比
        //nowProcress = 0;
        arcProgressBarProgress.setProgress(NOWPROGRESS_VALUE);//更新圆形进度条:更新圆形进度条需调用此方法
        if (displayLossType.isEmpty()){
            loss_type.setText(StaticVariables.USER_HAIR_LOSS_DEGREE);
        }else {
            loss_type.setText(displayLossType);
        }

        activity_handler.postDelayed(this,1000);
    }

    public void LoadHistory(){
        StaticVariables.LIGHT_HISTORY_SET.clear();
        originalRecord = new ArrayList<>();
        YMDRecord = new ArrayList<>();
        HttpUtils.postAsync(getActivity(), Url.rootUrl+"/iheimi/treatmentRecords/selectAllByExample", new HttpUtils.ResultCallback<ResultData<List<TreatmentRecordsVO>>>() {
            @Override
            public void onError(int statusCode, Throwable error) {
                Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ResultData<List<TreatmentRecordsVO>> response) {
                if (response.getCode() == 200){
                    List<TreatmentRecordsVO> data = response.getData();
                    if (data != null) {
                        for (int i = 0;i < data.size();i ++){
                            TreatmentRecordsVO treatmentRecordsVO = data.get(i);
                            String grease = treatmentRecordsVO.getGrease();
                            String temperature = treatmentRecordsVO.getTemperature();
                            Date createDtm = treatmentRecordsVO.getCreateDtm();
                            //添加用于显示dialog的光照记录
                            String dateToYMD = TimeUtils.getDateToString(createDtm);
                            String TodayYMD = TimeUtils.getDateToString(System.currentTimeMillis());
                            if (!YMDRecord.contains(dateToYMD)){
                                YMDRecord.add(dateToYMD);
                                originalRecord.add(createDtm);
                            }
                            if (YMDRecord.contains(TodayYMD)){
                                DateStaticVeriable.setIsTodayRecord(true);
                            }
                            int day = TimeUtils.dateToCurrentMonthDay(createDtm.getTime());
                            if (day != 0){
                                StaticVariables.LIGHT_HISTORY_SET.add(day);
                            }
                            //将时间转化为天计算总治疗次数
                            String dateToString = TimeUtils.getDateToString(createDtm.getTime());
                            if (dateToString != null){
                                StaticVariables.ALL_HISTORY_SET.add(dateToString);
                            }
                            Log.e("grease =" + grease,"temperature =" + temperature + " : " + "createDtm =" + createDtm.getTime() + "");
                        }
                        Message me = activity_handler.obtainMessage();
                        me.what = 333;
                        activity_handler.sendMessage(me);
                        totalTreatTime = StaticVariables.ALL_HISTORY_SET.size();
                    }
                }
            }
        });
    }

    public void ChangeState(){
        LIGHT_STATE = "已连接";
        activity_handler.post(TreatFragment.this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
