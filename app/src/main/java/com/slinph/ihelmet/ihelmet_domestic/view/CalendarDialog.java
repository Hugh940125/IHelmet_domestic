package com.slinph.ihelmet.ihelmet_domestic.view;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.slinph.ihelmet.ihelmet_domestic.R;
import com.slinph.ihelmet.ihelmet_domestic.utils.CustomDate;
import com.slinph.ihelmet.ihelmet_domestic.utils.DateUtil;
import com.slinph.ihelmet.ihelmet_domestic.utils.StaticVariables;

import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/2/16.
 */
public class CalendarDialog extends Dialog {
    private static Context dialogcontext = null;
    private TextView showYearView;
    private TextView showMonthView;
    private TextView showWeekView;
    private TextView tv_prompt;//下方提示文字
    private View view;//dialog布局
    private CalendarView calendarView;
    private int mLightHistoryCount;
    private int mLightPlanCount;

    public CalendarDialog(Context context, boolean isTouch) {
        super(context, R.style.Base_Theme_AppCompat_Dialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.dialogcontext = context;

        //加载布局文件
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.layout_calendar, null);
        initCalendarDialog();
        if(isTouch){//需要直接设置子控件不可点击,onTouchListener的onTouch方法优先级比onTouchEvent高，会先触发。
            calendarView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });
        }else{
            calendarView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
        }
        setContentView(view);
    }

    private void initCalendarDialog(){
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        showMonthView = (TextView) view.findViewById(R.id.show_month_view);
        showYearView = (TextView) view.findViewById(R.id.show_year_view);
        showWeekView = (TextView) view.findViewById(R.id.show_week_view);
        tv_prompt = (TextView) view.findViewById(R.id.tv_prompt);
        calendarView.setOnCallBackListener(new CalendarView.onCallBackListener() {
            @Override
            public void changeDate(CustomDate date) {
                setShowDateViewText(date.year, date.month, date.day);
            }

            @Override
            public void onMesureCellHeight(int cellSpace) {

            }

            @Override
            public void clickDate(CustomDate date) {
                if (mCallBack != null) {
                    mCallBack.clickDate(date);
                }
            }

            @Override
            public void cilckDateList(List<Integer> list) {
                mLightPlanCount=list.size();
                setBottonDes("Plans");
            }
        });
    }

    /**
     * 回调接口
     */
    private onCallBackListener mCallBack;
    /**
     * 获取接口对象
     */
    public void setOnCallBackListener(onCallBackListener listener) {
        mCallBack = listener;
    }
    /**
     * 回调接口
     */
    public interface onCallBackListener {
        void clickDate(CustomDate date);//回调点击的日期
        void cilckDateList(List<CustomDate> list);//回调点击的日期集合
    }

    /**
     * 显示日历上的年月日
     * @param year
     * @param month
     */
    public void setShowDateViewText(int year ,int month,int day) {
        showYearView.setText(String.valueOf(year) + "年");
        StringBuilder sb=new StringBuilder();
        sb.append(month)
                .append("月")
                .append(day)
                .append("日");
        showMonthView.setText(sb.toString());
        showWeekView.setText(DateUtil.weekName[DateUtil.getWeekDay() - 1]);
    }

    /**
     * 设置每月签到记录
     * @param dayList
     */
    public void setDayList(List<Integer> dayList){
        calendarView.setSignInDay(dayList);
        calendarView.update();
    }

    /**
     * 设置每月光照记录
     * @param set
     */
    public void setLightHistorySet(Set<Integer> set){
        mLightHistoryCount=set.size();
        calendarView.setLightDay(set);
        calendarView.update();
    }

    /**
     * 显示光照推送日期
     * @param planList
     */
    public void setLightPlanList(List<Integer> planList){
        mLightPlanCount=planList.size();
        calendarView.setPlanDay(planList);
        calendarView.update();
    }

    /**
     * 设置下方提示文字
     */
    public void setBottonDes(String des){
        if("计划".equals(des)){
            StringBuilder sb=new StringBuilder();
            sb.append("本月光照计划为:<font color='#FF7F27' ><big>")
                    .append(StaticVariables.USER_SUGGEST_LIGHT_TIMES)//橙色
                    .append("</big></font>次<br>本月已完成光照:<font color='#47EA1B' ><big>")
                    .append(mLightHistoryCount)//绿色
                    .append("</big></font>次<br>已设置光照计划:<font color='#00A2E8' ><big>")
                    .append(mLightPlanCount)//蓝色
                    .append("</big></font>次<br>建议将光照间隔均匀排列,每天最多一次.");
            CharSequence charSequence= Html.fromHtml(sb.toString());
            tv_prompt.setText(charSequence);
            //该语句在设置后必加，不然没有任何效果
            tv_prompt.setMovementMethod(LinkMovementMethod.getInstance());
        }else {
            tv_prompt.setText(des);
        }
    }

    @Override
    public void show() {
        super.show();
        //设置dialog的显示长宽
        Window window = getWindow();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = dp2px(350);
        params.height = dp2px(450);
        window.setAttributes(params);
    }

    //dp--->px
    public static int dp2px(int dp){
        //获取dip和px的比例关系
        float d = dialogcontext.getResources().getDisplayMetrics().density;
        // (int)(80.4+0.5)   (int)(80.6+0.5)
        return (int)(dp*d+0.5);
    }
}
