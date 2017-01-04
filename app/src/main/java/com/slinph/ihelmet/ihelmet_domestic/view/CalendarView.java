package com.slinph.ihelmet.ihelmet_domestic.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.slinph.ihelmet.ihelmet_domestic.utils.CustomDate;
import com.slinph.ihelmet.ihelmet_domestic.utils.DateUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CalendarView extends View {

    /**
     * 设定时间的集合(也就是点击的日期)
     */
    private List<CustomDate> customDateList = new ArrayList<>();
    /**
     * 两种模式 （月份和星期）
     */
    public static final int MONTH_STYLE = 0;
    public static final int WEEK_STYLE = 1;
    /**
     * 列数
     */
    private static final int TOTAL_COL = 7;
    /**
     * 行数
     */
    private static final int TOTAL_ROW = 6;

    /**
     * 画圆画笔
     */
    private Paint mCirclePaint;
    /**
     * 文字画笔
     */
    private Paint mTextPaint;
    /**
     * 整个控件的宽度
     */
    private int mViewWidth;
    /**
     * 整个控件的高度
     */
    private int mViewHight;
    /**
     * 一个单元格的长度(单元格为正方形)
     */
    private int mCellSpace;
    /**
     * 日历的行数
     */
    private Row rows[] = new Row[TOTAL_ROW];
    /***
     * 自定义的日期  包括year month day
     */
    private static CustomDate mShowDate;
    /***
     * 显示模式
     */
    public static int style = MONTH_STYLE;
    /***
     * 保存今天日期(只显示当月是才有效)
     */
    public int todayContent;
    /***
     * 一周7天
     */
    private int WEEK = 7;
    /**
     * 保存当月单元格位置
     */
    private List<Integer> cellList = new ArrayList<>();
    /**
     * 回调接口
     */
    private onCallBackListener mCallBack;
    //从文档中看，意思应该是触发移动事件的最短距离，如果小于这个距离就不触发移动控件，如viewpager就是用这个距离来判断用户是否翻页
    private int touchSlop;
    /**
     * 每月签到日期
     */
    private List<Integer> mDayList = new ArrayList<>();
    /**
     * 每月光照记录
     */
    private Set<Integer> mLightSet = new HashSet<>();
    /**
     * 每月光照计划（推送）
     */
    private List<Integer> mPlanList = new ArrayList<>();

    /**
     * 回调接口
     */
    public interface onCallBackListener {

        void changeDate(CustomDate date);//回调滑动viewPager改变的日期

        void onMesureCellHeight(int cellSpace);//回调cell的高度确定slidingDrawer高度

        void clickDate(CustomDate date);//回调点击的日期

//        void cilckDateList(List<CustomDate> list);//回调点击的日期集合
        void cilckDateList(List<Integer> list);//回调点击的日期集合

    }

    // 获取接口对象
    public void setOnCallBackListener(onCallBackListener listener) {
        mCallBack = listener;
    }

    public CalendarView(Context context) {
        super(context);
        init(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public CalendarView(Context context, int style, onCallBackListener callBack, List<Integer> dayList) {
        super(context);
        CalendarView.style = style;
        mCallBack = callBack;
        mDayList = dayList;
        init(context);
    }

    /**
     * 初始化画笔
     *
     * @param context
     */
    private void init(Context context) {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.parseColor("#45BDE6"));
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        initDate();
    }

    /**
     * 初始化日历(根据模式显示日期)
     */
    private void initDate() {
        if (style == MONTH_STYLE) {
            mShowDate = new CustomDate();//获取当前年月日.
        } else if (style == WEEK_STYLE) {
            mShowDate = DateUtil.getNextSunday();
        }
        fillDate();
    }

    /**
     * 根据模式填充日期的数据
     */
    private void fillDate() {
        if (style == MONTH_STYLE) {
            fillMonthDate();
        } else if (style == WEEK_STYLE) {
            fillWeekDate();
        }
        if (mCallBack != null) {
            mCallBack.changeDate(mShowDate);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < TOTAL_ROW; i++) {
            if (rows[i] != null)
                rows[i].drawCells(canvas);

        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHight = h;
        mCellSpace = Math.min(mViewHight / TOTAL_ROW, mViewWidth / TOTAL_COL);
        mTextPaint.setTextSize(mCellSpace / 3);
    }

    private float mDownX;
    private float mDownY;

    /*
     * 获取点击的位置位置
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float disX = event.getX() - mDownX;
                float disY = event.getY() - mDownY;
                if (Math.abs(disX) < touchSlop && Math.abs(disY) < touchSlop) {
                    int col = (int) (mDownX / mCellSpace);
                    int row = (int) (mDownY / mCellSpace);
                    measureClickCell(col, row);
                }
                break;
        }
        return true;
    }

    /**
     * 判断点击的单元格
     *
     * @param col 列
     * @param row 行
     */
    private void measureClickCell(int col, int row) {
        if (col >= TOTAL_COL || row >= TOTAL_ROW)//越界直接返回
            return;
        int position = col + row * TOTAL_COL;//当前位置
        if (cellList.contains(position)) {//判断点击位置是否是当月日期,只有当月日期才能点击
            CustomDate date = rows[row].cells[col].date;//获取这一次点击对象
//            if (!customDateList.contains(date)) {//不包含则添加
//                rows[row].cells[col].state = State.CLICK_DAY;
//                customDateList.add(date);
//            } else {//包含则设置为普通状态
//                rows[row].cells[col].state = State.CURRENT_MONTH_DAY;
//                customDateList.remove(date);
//            }
            int day = date.day;
            if (day < todayContent) {
                return;
            }
            if (!mPlanList.contains(day)) {//不包含则添加
                rows[row].cells[col].state = State.PLAN_DAY;
                mPlanList.add(date.day);
            } else {//包含则设置为普通状态
                rows[row].cells[col].state = State.CURRENT_MONTH_DAY;
                int i = 0;
                for (int item : mPlanList) {
                    if (item == day) {//找到就删除
                        mPlanList.remove(i);
                        break;
                    } else {
                        i++;
                        continue;
                    }
                }
            }
            mCallBack.clickDate(date);
//            mCallBack.cilckDateList(customDateList);
            mCallBack.cilckDateList(mPlanList);
            invalidate();
        }
    }

    /**
     * 画行
     */
    class Row {
        public int j;

        Row(int j) {
            this.j = j;
        }

        public Cell[] cells = new Cell[TOTAL_COL];//一行7列

        //画一行的单元格(7个)
        public void drawCells(Canvas canvas) {
            for (int i = 0; i < cells.length; i++) {
                if (cells[i] != null)
                    cells[i].drawSelf(canvas);//绘制每个单元格
            }
        }
    }

    /**
     * 一个单元格
     */
    class Cell {
        public CustomDate date;//日期
        public State state;//单元格状态
        //单元格坐标
        public int x;
        public int y;

        public Cell(CustomDate date, State state, int x, int y) {
            super();
            this.date = date;
            this.state = state;
            this.x = x;
            this.y = y;
        }

        /**
         * 绘制一个单元格
         *
         * @param canvas 画布
         */
        public void drawSelf(Canvas canvas) {
            int day = date.day;
            String content = day + "";
            float drawX = (float) ((x + 0.5) * mCellSpace - mTextPaint.measureText(content) / 2);
            float drawY = (float) ((y + 0.7) * mCellSpace - mTextPaint.measureText(content, 0, 1) / 2);
            switch (state) {
                case CURRENT_MONTH_DAY://当前月的30天
                    mTextPaint.setColor(Color.parseColor("#7B7B7B"));//默认字体颜色
                    break;
                case NEXT_MONTH_DAY://下一个月
                    break;
                case PAST_MONTH_DAY://上一个月
                    break;
                case TODAY://今天
                    todayContent = day;//把今天的信息保存起来
                    break;
                case LIGHT_DAY://光照记录
                    mTextPaint.setColor(Color.parseColor("#FFFFFF"));//白色
                    mCirclePaint.setColor(Color.parseColor("#47EA1B"));//绿色
                    if (todayContent == day) {
                        canvas.drawCircle((float) (mCellSpace * (x + 0.5)),
                                (float) ((y + 0.45) * mCellSpace),
                                mCellSpace / 2, mCirclePaint);//画圆
                    } else {
                        canvas.drawCircle((float) (mCellSpace * (x + 0.5)),
                                (float) ((y + 0.5) * mCellSpace),
                                mCellSpace / 3, mCirclePaint);
                    }
                    break;
                case PLAN_DAY://计划日期
                    mTextPaint.setColor(Color.parseColor("#FFFFFF"));//白色
                    mCirclePaint.setColor(Color.parseColor("#00A2E8"));//蓝色
                    if (todayContent == day) {
                        canvas.drawCircle((float) (mCellSpace * (x + 0.5)),
                                (float) ((y + 0.45) * mCellSpace),
                                mCellSpace / 2, mCirclePaint);//画圆
                    } else {
                        canvas.drawCircle((float) (mCellSpace * (x + 0.5)),
                                (float) ((y + 0.5) * mCellSpace),
                                mCellSpace / 3, mCirclePaint);
                    }
                    break;
                case SIGN_IN_DAY://本月签到日期
                    mTextPaint.setColor(Color.parseColor("#000000"));//黑色
                    mCirclePaint.setColor(Color.parseColor("#C3C3C3"));//灰色
                    canvas.drawCircle((float) (mCellSpace * (x + 0.5)),
                            (float) ((y + 0.5) * mCellSpace),
                            mCellSpace / 3, mCirclePaint);
                    break;
                case CLICK_DAY://点击的日期
                    mTextPaint.setColor(Color.parseColor("#FFFFFF"));//白色
                    mCirclePaint.setColor(Color.parseColor("#00A2E8"));//蓝色
                    canvas.drawCircle((float) (mCellSpace * (x + 0.5)),
                            (float) ((y + 0.5) * mCellSpace),
                            mCellSpace / 3, mCirclePaint);
                    break;
            }
            //Cell的属性从TODAY变为CLICK_DAY时,画今天时仍需把字体改变
            if (todayContent == day) {
                mTextPaint.setTextSize(mCellSpace / 2);
                canvas.drawText(content, drawX - dp2px(5), drawY, mTextPaint);//执行的只能操作一次
            } else {
                canvas.drawText(content, drawX, drawY, mTextPaint);
            }
            mTextPaint.setTextSize(mCellSpace / 3);
        }
    }

    //dp--->px
    public int dp2px(int dp){
        //获取dip和px的比例关系
        float d = getResources().getDisplayMetrics().density;
        // (int)(80.4+0.5)   (int)(80.6+0.5)
        return (int)(dp*d+0.5);
    }

    /**
     * 当前月日期，过去的月的日期，下个月的日期，今天，点击的日期, 光照记录,计划日期,本月签到日期
     */
    enum State {
        CURRENT_MONTH_DAY, PAST_MONTH_DAY, NEXT_MONTH_DAY, TODAY, CLICK_DAY, LIGHT_DAY, PLAN_DAY, SIGN_IN_DAY
    }

    /**
     * 填充星期模式下的数据 默认通过当前日期得到所在星期天的日期，然后依次填充日期
     */
    private void fillWeekDate() {
        int lastMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month - 1);
        rows[0] = new Row(0);
        int day = mShowDate.day;
        for (int i = TOTAL_COL - 1; i >= 0; i--) {
            day -= 1;
            if (day < 1) {
                day = lastMonthDays;
            }
            CustomDate date = CustomDate.modifiDayForObject(mShowDate, day);
            if (DateUtil.isToday(date)) {
                rows[0].cells[i] = new Cell(date, State.CLICK_DAY, i, 0);
                continue;
            }
            rows[0].cells[i] = new Cell(date, State.CURRENT_MONTH_DAY, i, 0);
        }
    }

    /**
     * 填充月份模式下数据 通过getWeekDayFromDate得到一个月第一天是星期几就可以算出所有的日期的位置 然后依次填充
     * 这里最好重构一下
     */
    private void fillMonthDate() {
        int currentMonthDay = DateUtil.getCurrentMonthDay();//获得今天在本月的第几天(获得当前日)
        int prevMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month - 1);//获取上一个月的天数
        int currentMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month);//获取当月的天数
        int firstDayWeek = DateUtil.getWeekDayFromDate(mShowDate.year, mShowDate.month);//获取第一天在日历的位置
        boolean isCurrentMonth = false;
        if (DateUtil.isCurrentMonth(mShowDate)) {//判断mShowDate在日历中是否是当月(年月)
            isCurrentMonth = true;
        }
        int day = 0;
        for (int j = 0; j < TOTAL_ROW; j++) {//行扫描
            rows[j] = new Row(j);//创建日历行数对象
            for (int i = 0; i < TOTAL_COL; i++) {//列扫描
                int position = i + j * TOTAL_COL;//当前位置
                if (position >= firstDayWeek && position < firstDayWeek + currentMonthDays) {//当月日期
                    cellList.add(position);//保存当月日期的单元格
                    day++;
                    if (isCurrentMonth && day == currentMonthDay) {//判断是否为当天日期,true:单元格State为State.TODAY
//                        CustomDate date = CustomDate.modifiDayForObject(mShowDate, day);
//                        rows[j].cells[i] = new Cell(date, State.TODAY, i, j);
                        todayContent = day;//标记今天的日期
                    }
                    if (mDayList != null && mDayList.contains(day)) {//每月签到记录
                        rows[j].cells[i] = new Cell(CustomDate.modifiDayForObject(mShowDate, day),
                                State.SIGN_IN_DAY, i, j);
                        continue;//结束单次循环
                    }
                    if (mLightSet != null && mLightSet.contains(day)) {//每月光照记录
                        rows[j].cells[i] = new Cell(CustomDate.modifiDayForObject(mShowDate, day),
                                State.LIGHT_DAY, i, j);
                        continue;//结束单次循环
                    }
                    if (mPlanList != null && mPlanList.contains(day)) {//光照推送计划
                        rows[j].cells[i] = new Cell(CustomDate.modifiDayForObject(mShowDate, day),
                                State.PLAN_DAY, i, j);
                        continue;//结束单次循环
                    }
                    rows[j].cells[i] = new Cell(CustomDate.modifiDayForObject(mShowDate, day),
                            State.CURRENT_MONTH_DAY, i, j);
                } /*else if (position < firstDayWeek) {//上个月日期
                    rows[j].cells[i] = new Cell(new CustomDate(mShowDate.year, mShowDate.month - 1, prevMonthDays - (firstDayWeek - position - 1)), State.PAST_MONTH_DAY, i, j);
                } else if (position >= firstDayWeek + currentMonthDays) {//下个月日期
                    rows[j].cells[i] = new Cell((new CustomDate(mShowDate.year, mShowDate.month + 1, position - firstDayWeek - currentMonthDays + 1)), State.NEXT_MONTH_DAY, i, j);
                }*/
            }
        }
    }

    /**
     * 切换模式后,更新日历
     */
    public void update() {
        fillDate();
        invalidate();
    }

    /**
     * 回到当月界面(可以是操作日历后)
     */
    public void backToday() {
        initDate();
        invalidate();
    }

    /**
     * 切换style
     *
     * @param style
     */
    public void switchStyle(int style) {
        CalendarView.style = style;
        if (style == MONTH_STYLE) {
            update();
        } else if (style == WEEK_STYLE) {
            int firstDayWeek = DateUtil.getWeekDayFromDate(mShowDate.year,
                    mShowDate.month);
            int day = 1 + WEEK - firstDayWeek;
            mShowDate.day = day;
            update();
        }

    }

    /**
     * 向右滑动
     */
    public void rightSilde() {
        if (style == MONTH_STYLE) {

            if (mShowDate.month == 12) {
                mShowDate.month = 1;
                mShowDate.year += 1;
            } else {
                mShowDate.month += 1;
            }

        } else if (style == WEEK_STYLE) {
            int currentMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month);
            if (mShowDate.day + WEEK > currentMonthDays) {
                if (mShowDate.month == 12) {
                    mShowDate.month = 1;
                    mShowDate.year += 1;
                } else {
                    mShowDate.month += 1;
                }
                mShowDate.day = WEEK - currentMonthDays + mShowDate.day;
            } else {
                mShowDate.day += WEEK;
            }
        }
        update();
    }

    /**
     * 向左滑动
     */
    public void leftSilde() {
        if (style == MONTH_STYLE) {
            if (mShowDate.month == 1) {
                mShowDate.month = 12;
                mShowDate.year -= 1;
            } else {
                mShowDate.month -= 1;
            }

        } else if (style == WEEK_STYLE) {
            int lastMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month);
            if (mShowDate.day - WEEK < 1) {
                if (mShowDate.month == 1) {
                    mShowDate.month = 12;
                    mShowDate.year -= 1;
                } else {
                    mShowDate.month -= 1;
                }
                mShowDate.day = lastMonthDays - WEEK + mShowDate.day;

            } else {
                mShowDate.day -= WEEK;
            }
        }
        update();
    }

    /**
     * 设置每月签到日期
     */
    public void setSignInDay(List<Integer> list) {
        mDayList = list;
    }

    /**
     * 设置每月签到日期
     */
    public void setLightDay(Set<Integer> set) {
        mLightSet = set;
    }

    /**
     * 设置每月签到日期
     */
    public void setPlanDay(List<Integer> list) {
        mPlanList = list;
    }
}
