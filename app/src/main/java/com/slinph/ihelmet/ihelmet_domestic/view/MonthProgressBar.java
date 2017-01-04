package com.slinph.ihelmet.ihelmet_domestic.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.slinph.ihelmet.ihelmet_domestic.R;


/**
 * Created by daimajia on 14-4-30.
 */
public class MonthProgressBar extends View {

    private int mMaxProgress = 177;

    /**
     * Current progress, can not exceed the max progress.
     */
    private int mCurrentProgress = 0;

    /**
     * The progress area bar color.
     */
    private int mReachedBarColor;

    /**
     * The bar unreached area color.
     */
    private int mUnreachedBarColor;

    /**
     * The progress text color.
     */
    private int mTextColor;

    /**
     * The progress text size.
     */
    private float mTextSize;

    /**
     * The height of the reached area.
     */
    private float mReachedBarHeight;

    /**
     * The height of the unreached area.
     */
    private float mUnreachedBarHeight;

    /**
     * The suffix of the number.
     */
    private String mSuffix = "%";

    /**
     * The prefix.
     */
    private String mPrefix = "";
    /**
     * "年"
     */
    private String mYear = "年";
    /**
     * "月"
     */
    private String mMonth = "月";


    private final int default_text_color = Color.rgb(66, 145, 241);
    private final int default_reached_color = Color.rgb(66, 145, 241);
    private final int default_unreached_color = Color.rgb(204, 204, 204);
    private final float default_progress_text_offset;
    private final float default_text_size;
    private final float default_reached_bar_height;
    private final float default_unreached_bar_height;

    /**
     * For save and restore instance of progressbar.
     */
    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_TEXT_COLOR = "text_color";
    private static final String INSTANCE_TEXT_SIZE = "text_size";
    private static final String INSTANCE_REACHED_BAR_HEIGHT = "reached_bar_height";
    private static final String INSTANCE_REACHED_BAR_COLOR = "reached_bar_color";
    private static final String INSTANCE_UNREACHED_BAR_HEIGHT = "unreached_bar_height";
    private static final String INSTANCE_UNREACHED_BAR_COLOR = "unreached_bar_color";
    private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_PROGRESS = "progress";
    private static final String INSTANCE_SUFFIX = "suffix";
    private static final String INSTANCE_PREFIX = "prefix";
    private static final String INSTANCE_TEXT_VISIBILITY = "text_visibility";

    private static final int PROGRESS_TEXT_VISIBLE = 0;


    /**
     * The width of the text that to be drawn.
     */
    private float mDrawTextWidth;

    /**
     * The drawn text start.
     */
    private float mDrawTextStart;

    /**
     * The drawn text end.
     */
    private float mDrawTextEnd;

    /**
     * The text that to be drawn in onDraw().
     */
    private String mCurrentDrawText;

    /**
     * The Paint of the reached area.
     */
    private Paint mReachedBarPaint;
    /**
     * The Paint of the unreached area.
     */
    private Paint mUnreachedBarPaint;
    /**
     * The Paint of the progress text.
     */
    private Paint mTextPaint;

    /**
     * Unreached bar area to draw rect.
     */
    private RectF mUnreachedRectF = new RectF(0, 0, 0, 0);
    /**
     * Reached bar area rect.
     */
    private RectF mReachedRectF = new RectF(0, 0, 0, 0);

    /**
     * The progress text offset.
     */
    private float mOffset;

    /**
     * Determine if need to draw unreached area.
     */
    private boolean mDrawUnreachedBar = true;

    private boolean mDrawReachedBar = true;

    private boolean mIfDrawText = true;

	private Bitmap mBitmap;

	private Paint mBitmapPaint;
	
	private Bitmap mBitmap2;
	
	private Paint mBitmapPaint2;

	private int mTime;

    public enum ProgressTextVisibility {
        Visible, Invisible
    }

    public MonthProgressBar(Context context) {
        this(context, null);
    }

    public MonthProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.numberProgressBarStyle);
    }

    public MonthProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        default_reached_bar_height = dp2px(2);
        default_unreached_bar_height = dp2px(1);
        default_text_size = sp2px(10);
        default_progress_text_offset = dp2px(3);

        //load styled attributes.
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NumberProgressBar,
                defStyleAttr, 0);

        mReachedBarColor = attributes.getColor(R.styleable.NumberProgressBar_progress_reached_color, default_reached_color);
        mUnreachedBarColor = attributes.getColor(R.styleable.NumberProgressBar_progress_unreached_color, default_unreached_color);
        mTextColor = attributes.getColor(R.styleable.NumberProgressBar_progress_text_color, default_text_color);
        mTextSize = attributes.getDimension(R.styleable.NumberProgressBar_progress_text_size, default_text_size);

        mReachedBarHeight = attributes.getDimension(R.styleable.NumberProgressBar_progress_reached_bar_height, default_reached_bar_height);
        mUnreachedBarHeight = attributes.getDimension(R.styleable.NumberProgressBar_progress_unreached_bar_height, default_unreached_bar_height);
        mOffset = attributes.getDimension(R.styleable.NumberProgressBar_progress_text_offset, default_progress_text_offset);

        int textVisible = attributes.getInt(R.styleable.NumberProgressBar_progress_text_visibility, PROGRESS_TEXT_VISIBLE);
        if (textVisible != PROGRESS_TEXT_VISIBLE) {
            mIfDrawText = false;
        }

        setProgress(attributes.getInt(R.styleable.NumberProgressBar_progress_current, 0));
        setMax(attributes.getInt(R.styleable.NumberProgressBar_progress_max, 100));

        attributes.recycle();
        initializePainters();
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return (int) mTextSize;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return Math.max((int) mTextSize, Math.max((int) mReachedBarHeight, (int) mUnreachedBarHeight));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
    }

    private int measure(int measureSpec, boolean isWidth) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop() + getPaddingBottom();
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = isWidth ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight();
            result += padding;
            if (mode == MeasureSpec.AT_MOST) {
                if (isWidth) {
                    result = Math.max(result, size);
                } else {
                    result = Math.min(result, size);
                }
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
    	
        if (mIfDrawText) {
            calculateDrawRectF();
        } else {
            calculateDrawRectFWithoutProgressText();
        }
    	//绘制背景+文字（无矩形边框）
//       calculateDrawRectF();
        
        if (mDrawUnreachedBar) {
//            canvas.drawRect(mUnreachedRectF, mUnreachedBarPaint);
        	canvas.drawRoundRect(mUnreachedRectF, default_progress_text_offset, default_progress_text_offset, mUnreachedBarPaint);
        }

        if (mDrawReachedBar) {
//            canvas.drawRect(mReachedRectF, mReachedBarPaint);
        	canvas.drawRoundRect(mReachedRectF, default_progress_text_offset, default_progress_text_offset, mReachedBarPaint);
        }
//        System.out.println("mTime="+mTime);
        if (mIfDrawText){
        	int month=mTime%12;
        	int year=mTime/12;
        	//字符少，需要右移一些，x大些
        	if(mTime<10||(year!=1&&year<10&&month==0)){//小于10个月||小于10整年且不是1的整年
        		canvas.drawText(mCurrentDrawText, mDrawTextStart-dp2px(23), mDrawTextEnd-dp2px(40), mTextPaint);
        	}else if(mTime<=12||(year>=10&&month==0)){//小于或等于12个月||大于10整年
        		canvas.drawText(mCurrentDrawText, mDrawTextStart-dp2px(28), mDrawTextEnd-dp2px(40), mTextPaint);
        	}else if(year<10&&month<10){//小于10整年且小于10个月
        		canvas.drawText(mCurrentDrawText, mDrawTextStart-dp2px(38), mDrawTextEnd-dp2px(40), mTextPaint);
        	}else if((year<10&&month>=10)||(year>=10&&month<10)){//小于10整年且大于或等于10个月//大于或等于10整年且小于10个月
        		canvas.drawText(mCurrentDrawText, mDrawTextStart-dp2px(43), mDrawTextEnd-dp2px(40), mTextPaint);
        	}else if(year>=10&&month>=10){//大于或等于10整年且大于或等于10个月
        		canvas.drawText(mCurrentDrawText, mDrawTextStart-dp2px(53), mDrawTextEnd-dp2px(40), mTextPaint);
        	}
        	//小三角形
        	canvas.drawBitmap(mBitmap, mDrawTextStart-dp2px(16), mDrawTextEnd-dp2px(50), mBitmapPaint);
        	//小圆形
        	canvas.drawBitmap(mBitmap2, mDrawTextStart-dp2px(18), mDrawTextEnd-dp2px(18), mBitmapPaint2);
        }
    }

    private void initializePainters() {
        mReachedBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mReachedBarPaint.setColor(mReachedBarColor);

        mUnreachedBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mUnreachedBarPaint.setColor(mUnreachedBarColor);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        
        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitmapPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


    private void calculateDrawRectFWithoutProgressText() {
        mReachedRectF.left = getPaddingLeft();
        mReachedRectF.top = getHeight() / 2.0f - mReachedBarHeight / 2.0f;
        mReachedRectF.right = (getWidth() - getPaddingLeft() - getPaddingRight()) / (getMax() * 1.0f) * getProgress() + getPaddingLeft();
        mReachedRectF.bottom = getHeight() / 2.0f + mReachedBarHeight / 2.0f;

//        mUnreachedRectF.left = getPaddingLeft();//背景写固定，不移动
        mUnreachedRectF.left = mReachedRectF.right;
        mUnreachedRectF.right = getWidth() - getPaddingRight();
        mUnreachedRectF.top = getHeight() / 2.0f + -mUnreachedBarHeight / 2.0f;
        mUnreachedRectF.bottom = getHeight() / 2.0f + mUnreachedBarHeight / 2.0f;
    }

    private void calculateDrawRectF() {
    	//3个月到15年，共177个月
    	//一年内月份占的比例
    	int oneYear=12;
    	mTime = getProgress()+3;
    	if(mTime<=oneYear){
    		mCurrentDrawText = String.format("%d",mTime);
    		mCurrentDrawText = mCurrentDrawText + mMonth;
    	}else{
    		int year=mTime/12;
    		int month=mTime%12;
    		if(month==0){//整年不显示月份
    			mCurrentDrawText = year + mYear;
    		}else{
    			mCurrentDrawText = year + mYear+month+mMonth;
    		}
    	}
    	mDrawTextWidth = mTextPaint.measureText(mCurrentDrawText);

        //绘制前景
        if (getProgress() == 0) {
            mDrawReachedBar = false;
            mDrawTextStart = getPaddingLeft();
        } else {
            mDrawReachedBar = true;
            mReachedRectF.left = getPaddingLeft();
            mReachedRectF.top = getHeight() / 2.0f - mReachedBarHeight / 2.0f;
//            mReachedRectF.right = (getWidth() - getPaddingLeft() - getPaddingRight()) / (getMax() * 1.0f) * getProgress() - mOffset + getPaddingLeft();
            mReachedRectF.right = (getWidth() - getPaddingLeft() - getPaddingRight()) / (getMax() * 1.0f) * getProgress() + getPaddingLeft();
            mReachedRectF.bottom = getHeight() / 2.0f + mReachedBarHeight / 2.0f;
            mDrawTextStart = (mReachedRectF.right + mOffset);
        }

        mDrawTextEnd = (int) ((getHeight() / 2.0f) - ((mTextPaint.descent() + mTextPaint.ascent()) / 2.0f));

        //前景右限制，此处不需要
//        if ((mDrawTextStart + mDrawTextWidth) >= getWidth() - getPaddingRight()) {
//            mDrawTextStart = getWidth() - getPaddingRight() - mDrawTextWidth;
//            mReachedRectF.right = mDrawTextStart - mOffset;
//        }

//        float unreachedBarStart = mDrawTextStart + mDrawTextWidth + mOffset;
        float unreachedBarStart = mDrawTextStart + mDrawTextWidth + mOffset;
        //文字到目标就隐藏背景，此处不需要
//        if (unreachedBarStart >= getWidth() - getPaddingRight()) {
//            mDrawUnreachedBar = false;
//        } else {
//            mDrawUnreachedBar = true;
//            mUnreachedRectF.left = unreachedBarStart;
//            mUnreachedRectF.left = getPaddingLeft();//背景写固定，不移动
//            mUnreachedRectF.right = getWidth() - getPaddingRight();
//            mUnreachedRectF.top = getHeight() / 2.0f + -mUnreachedBarHeight / 2.0f;
//            mUnreachedRectF.bottom = getHeight() / 2.0f + mUnreachedBarHeight / 2.0f;
//        }
        
        //绘制图形
        mBitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.triangle);
        mBitmap2= BitmapFactory.decodeResource(getResources(), R.mipmap.btn_c);
//        mBitmap2=Bitmap.createBitmap(mBitmap2, 0, 0, dp2px(8), dp2px(8));
        
        //绘制背景
        mDrawUnreachedBar = true;
//            mUnreachedRectF.left = unreachedBarStart;
        mUnreachedRectF.left = getPaddingLeft();//背景写固定，不移动
        mUnreachedRectF.right = getWidth() - getPaddingRight();
        mUnreachedRectF.top = getHeight() / 2.0f + -mUnreachedBarHeight / 2.0f;
        mUnreachedRectF.bottom = getHeight() / 2.0f + mUnreachedBarHeight / 2.0f;
    }

    /**
     * Get progress text color.
     *
     * @return progress text color.
     */
    public int getTextColor() {
        return mTextColor;
    }

    /**
     * Get progress text size.
     *
     * @return progress text size.
     */
    public float getProgressTextSize() {
        return mTextSize;
    }

    public int getUnreachedBarColor() {
        return mUnreachedBarColor;
    }

    public int getReachedBarColor() {
        return mReachedBarColor;
    }

    public int getProgress() {
        return mCurrentProgress;
    }

    public int getMax() {
        return mMaxProgress;
    }

    public float getReachedBarHeight() {
        return mReachedBarHeight;
    }

    public float getUnreachedBarHeight() {
        return mUnreachedBarHeight;
    }

    public void setProgressTextSize(float textSize) {
        this.mTextSize = textSize;
        mTextPaint.setTextSize(mTextSize);
        invalidate();
    }

    public void setProgressTextColor(int textColor) {
        this.mTextColor = textColor;
        mTextPaint.setColor(mTextColor);
        invalidate();
    }

    public void setUnreachedBarColor(int barColor) {
        this.mUnreachedBarColor = barColor;
        mUnreachedBarPaint.setColor(mUnreachedBarColor);
        invalidate();
    }

    public void setReachedBarColor(int progressColor) {
        this.mReachedBarColor = progressColor;
        mReachedBarPaint.setColor(mReachedBarColor);
        invalidate();
    }

    public void setReachedBarHeight(float height) {
        mReachedBarHeight = height;
    }

    public void setUnreachedBarHeight(float height) {
        mUnreachedBarHeight = height;
    }

    public void setMax(int maxProgress) {
        if (maxProgress > 0) {
            this.mMaxProgress = maxProgress;
            invalidate();
        }
    }

    public void setSuffix(String suffix) {
        if (suffix == null) {
            mSuffix = "";
        } else {
            mSuffix = suffix;
        }
    }

    public String getSuffix() {
        return mSuffix;
    }

    public void setPrefix(String prefix) {
        if (prefix == null)
            mPrefix = "";
        else {
            mPrefix = prefix;
        }
    }

    public String getPrefix() {
        return mPrefix;
    }

    public void incrementProgressBy(int by) {
        if (by > 0) {
            setProgress(getProgress() + by);
        }
    }

    public void setProgress(int progress) {
        if (progress <= getMax() && progress >= 0) {
            this.mCurrentProgress = progress;
            invalidate();
        }
    }

    //时时滑动
    private float scrollX;
	private int mWidth;
    //设置点击事件，控制滑块位置
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	scrollX = event.getX()-getPaddingLeft();
		mWidth = getMeasuredWidth()-getPaddingLeft()-getPaddingRight();
		//    		System.out.println("x1="+x1);
		//    		System.out.println("getPaddingLeft()="+getPaddingLeft());
		//    		System.out.println("mWidth="+mWidth);
		if(scrollX<0){
			scrollX=0;
		}
		if(scrollX>mWidth){
			scrollX=mWidth;
		}
		setProgress((int)(scrollX/mWidth*getMax()));
    	return true;
    }
    
    
    
    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(INSTANCE_TEXT_COLOR, getTextColor());
        bundle.putFloat(INSTANCE_TEXT_SIZE, getProgressTextSize());
        bundle.putFloat(INSTANCE_REACHED_BAR_HEIGHT, getReachedBarHeight());
        bundle.putFloat(INSTANCE_UNREACHED_BAR_HEIGHT, getUnreachedBarHeight());
        bundle.putInt(INSTANCE_REACHED_BAR_COLOR, getReachedBarColor());
        bundle.putInt(INSTANCE_UNREACHED_BAR_COLOR, getUnreachedBarColor());
        bundle.putInt(INSTANCE_MAX, getMax());
        bundle.putInt(INSTANCE_PROGRESS, getProgress());
        bundle.putString(INSTANCE_SUFFIX, getSuffix());
        bundle.putString(INSTANCE_PREFIX, getPrefix());
        bundle.putBoolean(INSTANCE_TEXT_VISIBILITY, getProgressTextVisibility());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            mTextColor = bundle.getInt(INSTANCE_TEXT_COLOR);
            mTextSize = bundle.getFloat(INSTANCE_TEXT_SIZE);
            mReachedBarHeight = bundle.getFloat(INSTANCE_REACHED_BAR_HEIGHT);
            mUnreachedBarHeight = bundle.getFloat(INSTANCE_UNREACHED_BAR_HEIGHT);
            mReachedBarColor = bundle.getInt(INSTANCE_REACHED_BAR_COLOR);
            mUnreachedBarColor = bundle.getInt(INSTANCE_UNREACHED_BAR_COLOR);
            initializePainters();
            setMax(bundle.getInt(INSTANCE_MAX));
            setProgress(bundle.getInt(INSTANCE_PROGRESS));
            setPrefix(bundle.getString(INSTANCE_PREFIX));
            setSuffix(bundle.getString(INSTANCE_SUFFIX));
            setProgressTextVisibility(bundle.getBoolean(INSTANCE_TEXT_VISIBILITY) ? ProgressTextVisibility.Visible : ProgressTextVisibility.Invisible);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public int dp2px(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

    public int sp2px(int sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return (int)(sp * scale);
    }

    public void setProgressTextVisibility(ProgressTextVisibility visibility) {
        mIfDrawText = visibility == ProgressTextVisibility.Visible;
        invalidate();
    }

    public boolean getProgressTextVisibility() {
        return mIfDrawText;
    }

    /**获取总月份
     * @return
     */
    public int getMonth(){
    	return mTime;
    }

    /**获取年月
     * @return
     */
    public String getMonthStr(){
        return mCurrentDrawText;
    }
}
