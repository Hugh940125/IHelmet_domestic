package com.slinph.ihairhelmet4.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @auther JQ
 */
public class NoScrollGridView extends GridView{

    public NoScrollGridView(Context context) {
        super(context);
    }

    public NoScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 此方法用于测量控件的高宽
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	// 有三种布局模式：
    	// EXACTLY: 当布局中用match_parent或写死尺寸大小时，使用该模式
    	// AT_MOST: 当布局中用wrap_content时，使用该模式
    	// UNSPECIFIED: 此模式暂时未用到
    	
    	// 指定GridView可以显示的最大高度，一般来说是不会达到此最大值的。 
    	// [注意]：
    	// 该值只能为30位以内的数，不能指定为整数最大值；
    	// 因为Android用了一个32位整数来记录布局模式和布局尺寸两个信息，
    	// 其中最高两位用来记录布局模式，剩余的低30位用来记录布局尺寸大小。
    	int maxHeight = Integer.MAX_VALUE >> 2 ; // 1000000; 
    	
    	// 重新指定GridView的布局模式为包裹内容（AT_MOST模式），也就是内容有多高就显示多高,
    	// 这样GridView也就不会再有滚动的效果了
        int heightMeasureSpec2 = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec2); 
        
        // 打印整数所描述的信息：布局模式和布局尺寸
    	String info = MeasureSpec.toString(heightMeasureSpec2);
//    	Lg.e("----------maxHeight : " ,""+ maxHeight);
//        Lg.e("----------info :      ",""+ info);
    }
}
