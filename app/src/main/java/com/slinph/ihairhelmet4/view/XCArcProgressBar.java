package com.slinph.ihairhelmet4.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.slinph.ihairhelmet4.R;
import com.slinph.ihairhelmet4.fragment.TreatFragment;

import java.io.InputStream;


/**
 * 开口圆环类型的进度条：带进度百分比显示的进度条，线程安全的View，可直接在线程中更新进度
 * @author caizhiming
 *
 */
@SuppressWarnings("deprecation")
public class XCArcProgressBar extends View {

	private Paint paint;//画笔对象的引用
	private int textColor;//中间进度百分比字符串的颜色
	private float textSize ;//中间进度百分比字符串的字体
	private int max;//最大进度
	private String electricity_title;
	private int progress;//当前进度
	private boolean isDisplayText;//是否显示中间百分比进度字符串
	private String title;//标题
	private Bitmap bmpTemp = null;
	private int res;
	private int degrees;

	public XCArcProgressBar(Context context){
		this(context, null);
	}
	public XCArcProgressBar(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	public XCArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		degrees =  0;
		paint  =  new Paint();
		//从attrs.xml中获取自定义属性和默认值
		TypedArray typedArray  = context.obtainStyledAttributes(attrs, R.styleable.XCRoundProgressBar);
		textColor  =typedArray.getColor(R.styleable.XCRoundProgressBar_textColor, Color.RED);
		textSize = typedArray.getDimension(R.styleable.XCRoundProgressBar_textSize, 50);
		max = typedArray.getInteger(R.styleable.XCRoundProgressBar_max, 100);
		isDisplayText  =typedArray.getBoolean(R.styleable.XCRoundProgressBar_textIsDisplayable, false);
		typedArray.recycle();
	}

	public static Bitmap decodeCustomRes(Context c, int res) {
		InputStream is = c.getResources().openRawResource(res);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = 1;//原尺寸加载图片
		Bitmap bmp = BitmapFactory.decodeStream(is, null, options);
		return bmp;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		int width = getWidth();
		int height = getHeight();
		int centerX = getWidth() / 2;// 获取中心点X坐标
		int centerY = getHeight() / 2;// 获取中心点Y坐标

		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas can = new Canvas(bitmap);
		// 绘制底部背景图
		bmpTemp = decodeCustomRes(TreatFragment.mContext, R.mipmap.gray_03);
		float dstWidth = (float) width;
		float dstHeight = (float) height;
		int srcWidth = bmpTemp.getWidth();
		int srcHeight = bmpTemp.getHeight();

		can.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG));// 抗锯齿

		Bitmap bmpBg = Bitmap.createScaledBitmap(bmpTemp, width, height, true);
		can.drawBitmap(bmpBg, 0, 0, null);
		/**
		 * 绘制剩余时间图片
		 * */
		Bitmap timeBitmap=decodeCustomRes(TreatFragment.mContext, R.mipmap.time);
		int timeWidth= timeBitmap.getWidth();
		int timeHeight=timeBitmap.getHeight();
		Matrix timeMatrix=new Matrix();
		timeMatrix.postScale(dstWidth / srcWidth, dstHeight / srcWidth);
		can.drawBitmap(timeBitmap, centerX-(timeWidth/2), centerY/2, paint);
		/**
		 * 绘制电量图标
		 * */
		if(res!=0){
			Bitmap cellBitmap=decodeCustomRes(TreatFragment.mContext, res);
			int cellwidth=cellBitmap.getWidth();
			int cellheight=cellBitmap.getHeight();
			Matrix cellMatrix=new Matrix();
			cellMatrix.postScale(dstWidth / srcWidth, dstHeight / srcWidth);
			can.drawBitmap(cellBitmap, centerX-(cellwidth/2), getHeight()-centerY/3, paint);
		}
		;
		/**
		 * 分钟
		 * */
		Bitmap minBitmap=decodeCustomRes(TreatFragment.mContext,R.mipmap.mins);
		Matrix minMatrix=new Matrix();
		int minwidth=minBitmap.getWidth();
		int minheight=minBitmap.getHeight();
		minMatrix.postScale(dstWidth / srcWidth, dstHeight / srcWidth);
		can.drawBitmap(minBitmap, centerX-(minwidth/2), getHeight()-centerY/2-centerY/7, paint);

 		// 绘制进度前景图
		Matrix matrixProgress = new Matrix();
		matrixProgress.postScale(dstWidth / srcWidth, dstHeight / srcWidth);
		bmpTemp = decodeCustomRes(TreatFragment.mContext, R.mipmap.blue_03);

		Bitmap bmpProgress = Bitmap.createBitmap(bmpTemp, 0, 0, srcWidth, srcHeight, matrixProgress, true);
		//进度为18.51.69时会图片溢出,需要特殊处理
		if(progress==18||progress==51||progress==69){
			progress++;
		}
		degrees = progress * 270 / max - 270;
		//遮罩处理前景图和背景图
		can.save();
		can.rotate(degrees, centerX, centerY);
		paint.setAntiAlias(true);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
		can.drawBitmap(bmpProgress, 0, 0, paint);
		can.restore();
		
		if ((-degrees) >= 85) {
			int posX = 0;
			int posY = 0;
			if ((-degrees) >= 270) {
				posX = 0;
				posY = 0;
			} else if ((-degrees) >= 225) {
				posX = centerX / 2;
				posY = 0;
			} else if ((-degrees) >= 180) {
				posX = centerX;
				posY = 0;
			} else if ((-degrees) >= 135) {
				posX = centerX;
				posY = 0;
			} else if ((-degrees) >= 85) {
				posX = centerX;
				posY = centerY;
			}
			
			if ((-degrees) >= 225) {
				can.save();
				Bitmap dst = bitmap
						.createBitmap(bitmap, 0, 0, centerX, centerX);
				paint.setAntiAlias(true);
				paint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
				Bitmap src = bmpBg.createBitmap(bmpBg, 0, 0, centerX, centerX);
				can.drawBitmap(src, 0, 0, paint);
				can.restore();

				can.save();
				dst = bitmap.createBitmap(bitmap, centerX, 0, centerX, height);
				paint.setAntiAlias(true);
				paint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
				src = bmpBg.createBitmap(bmpBg, centerX, 0, centerX, height);
				can.drawBitmap(src, centerX, 0, paint);
				can.restore();

			} else {
				can.save();
				Bitmap dst = bitmap.createBitmap(bitmap, posX, posY, width
						- posX, height - posY);
				paint.setAntiAlias(true);
				paint.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
				Bitmap src = bmpBg.createBitmap(bmpBg, posX, posY,
						width - posX, height - posY);
				can.drawBitmap(src, posX, posY, paint);
				can.restore();
			}
		}
		//绘制遮罩层位图
		canvas.drawBitmap(bitmap, 0, 0, null);

		// 画中间进度百分比字符串
		paint.reset();
		paint.setStrokeWidth(0);
		paint.setColor(textColor);
		paint.setTextSize(textSize);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		int percent = (int) (((float) progress / (float) max) * 100);// 计算百分比
		float textWidth = paint.measureText(percent+"");// 测量字体宽度，需要居中显示

		if (isDisplayText && percent != 0) {
			canvas.drawText(percent + "", centerX - textWidth / 2, centerX
					+ textSize / 2 +10, paint);
		}

//		画底部开口处标题文字（已注销）
//		paint.setTextSize(textSize);
		textWidth = paint.measureText(title);
		canvas.drawText(title, centerX - textWidth / 2, centerY + centerY / 4, paint);
		Paint pan=new Paint();
		pan.setColor(getResources().getColor(R.color.electricity));
		pan.setTypeface(Typeface.SANS_SERIF);
		pan.setTextSize(27);
		if(electricity_title!=null){
			float panWidth=pan.measureText(electricity_title);
			canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
					| Paint.FILTER_BITMAP_FLAG));// 抗锯齿
			canvas.drawText(electricity_title,centerX-panWidth/2+dp2px(5),centerY+centerY-dp2px(5),pan);
		}
	}
	
	public Paint getPaint() {
		return paint;
	}
	public void setPaint(Paint paint) {
		this.paint = paint;
	}
	public int getTextColor() {
		return textColor;
	}
	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}
	public float getTextSize() {
		return textSize;
	}
	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}
	public synchronized int getMax() {
		return max;
	}
	public synchronized void setMax(int max) {
		if(max < 0){
			throw new IllegalArgumentException("max must more than 0");
		}
		this.max = max;
	}
	public synchronized int getProgress() {
		return progress;
	}
	/**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     * @author caizhiming
     */ 
	public synchronized void setProgress(int progress) {
		if(progress < 0){
			throw new IllegalArgumentException("progress must more than 0");
		}
		if(progress > max){
			this.progress = progress;
		}
		if(progress <= max){
			this.progress = progress;
			postInvalidate();
		}
	}

	public String getElectricity_title() {
		return electricity_title;
	}

	public void setElectricity_title(String electricity_title) {
		this.electricity_title = electricity_title;
	}

	public void setImages(int res){
		this.res=res;
	}
	public boolean isDisplayText() {
		return isDisplayText;
	}
	public void setDisplayText(boolean isDisplayText) {
		this.isDisplayText = isDisplayText;
	}
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public float dp2px(float dp) {
		final float scale = getResources().getDisplayMetrics().density;
		return dp * scale + 0.5f;
	}

}
