package com.slinph.ihelmet.ihelmet_domestic.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.slinph.ihelmet.ihelmet_domestic.application.MyApplication;

import java.io.InputStream;

public class UIUtils {

	private static WindowManager wm = (WindowManager) getContext()
			.getSystemService(Context.WINDOW_SERVICE);
	//提供获取上下环境方法
	public static Context getContext(){
		return MyApplication.getContext();
	}
	
	/**
	 * xml转view
	 * @param layoutId
	 * @return
	 */
	public static View inflate(int layoutId){
		return View.inflate(getContext(), layoutId, null);
	}

	/**
	 * 获取资源文件夹
	 * @return
	 */
	public static Resources getResources(){
		return getContext().getResources();
	}

	/**
	 * 获取string操作
	 * @param stringId
	 * @return
	 */
	public static String getString(int stringId){
		return getResources().getString(stringId);
	}

	/**
	 * 获取drawable
	 * @param drawableId
	 * @return
	 */
	public static Drawable getDrawable(int drawableId){
		return getResources().getDrawable(drawableId);
	}
	//获取stringArray数组
	public static String[] getStringArray(int stringArrayId){
		return getResources().getStringArray(stringArrayId);
	}
	//手机的像素密度跟文档中的最接近值
	//1dp = 0.75px
	//1dp = 1px
	//1dp = 1.5px
	//1dp = 2px
	//1dp = 3px
	
	//dp--->px
	public static int dp2px(int dp){
		//获取dip和px的比例关系
		float d = getResources().getDisplayMetrics().density;
		// (int)(80.4+0.5)   (int)(80.6+0.5)
		return (int)(dp*d+0.5);
	}
	
	//px---->dp
	public static int px2dp(int px){
		float d = getResources().getDisplayMetrics().density;
		return (int)(px/d+0.5);
	}
	
	public static ColorStateList getColorStateList(int mTabTextColorResId) {
		//根据颜色选择器id,获取颜色选择器对象的方法
		return getResources().getColorStateList(mTabTextColorResId);
	}
	//获取屏幕分辨率
	public static int getDimens(int dimenId) {
		return UIUtils.getResources().getDimensionPixelSize(dimenId);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 *
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 返回屏幕宽度
	 *
	 * @return
	 */
	public static int getScreenWidth() {
		return wm.getDefaultDisplay().getWidth();
	}

	/**
	 * 返回屏幕高度
	 *
	 * @return
	 */
	public static int getScreenHeight() {
		return wm.getDefaultDisplay().getHeight();
	}

	/**
	 * 获取整个屏幕高度，包括顶部状态栏和底部虚拟按钮
	 *
	 * @return
	 */
	public int getScreenAllHeight() {
		int heightPixels;
		Display d = wm.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		d.getMetrics(metrics);
		// since SDK_INT = 1;
		heightPixels = metrics.heightPixels;
		// includes window decorations (statusbar bar/navigation bar)
		if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
			try {
				heightPixels = (Integer) Display.class
						.getMethod("getRawHeight").invoke(d);
			} catch (Exception ignored) {
			}
			// includes window decorations (statusbar bar/navigation bar)
		else if (Build.VERSION.SDK_INT >= 17)
			try {
				android.graphics.Point realSize = new android.graphics.Point();
				Display.class.getMethod("getRealSize",
						android.graphics.Point.class).invoke(d, realSize);
				heightPixels = realSize.y;
			} catch (Exception ignored) {
			}
		return heightPixels;
	}

	/**
	 * 从Resources中获取图片资源，转化为Bitmap格式返回
	 * @param c
	 * @param res
	 * @return
	 */
	public static Bitmap decodeCustomRes(Context c, int res) {
		InputStream is = c.getResources().openRawResource(res);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = 1;//原尺寸加载图片
		Bitmap bmp = BitmapFactory.decodeStream(is, null, options);
		return bmp;
	}
}
