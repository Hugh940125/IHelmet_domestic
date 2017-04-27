package com.slinph.ihairhelmet4.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.slinph.ihairhelmet4.R;
import com.slinph.ihairhelmet4.utils.UIUtils;

import java.util.ArrayList;

/**
 * 根据实际要求,显示单张图片
 */
public class ImageGridAdapter_1 extends BaseAdapter {

	/** 上下文 */
	private Context ctx;
	/** 图片Url集合 */
	private ArrayList<String> imageUrls = new ArrayList<String>();


	public ImageGridAdapter_1(Context ctx, ArrayList<String> urls) {
		this.ctx = ctx;
		imageUrls.clear();
		this.imageUrls.addAll(urls);
	}

	public ImageGridAdapter_1(Context ctx) {
		this.ctx = ctx;
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(ctx));
	}

	@Override
	public int getCount() {
		return imageUrls == null ? 0 : imageUrls.size();
	}

	public void setUrls(ArrayList<String> urls) {
		imageUrls.clear();
		this.imageUrls.addAll(urls);
		notifyDataSetChanged();
	}
	
	public ArrayList<String> getUrls() {
		return imageUrls;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(ctx, R.layout.item_gridview, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);
		imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);//把图片按照指定的大小在View中显示
		int width= UIUtils.dp2px(280);
		int height= UIUtils.dp2px(105);
		ViewGroup.MarginLayoutParams margin9 = new ViewGroup.MarginLayoutParams(imageView.getLayoutParams());
		RelativeLayout.LayoutParams layoutParams9 = new RelativeLayout.LayoutParams(margin9);
		layoutParams9.height = height;//设置图片的高度
		layoutParams9.width = width;  //设置图片的宽度
		imageView.setLayoutParams(layoutParams9);
		ImageLoader.getInstance().displayImage(imageUrls.get(position),imageView);
		return view;
	}

	@Override
	public Object getItem(int position) {
		return imageUrls.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
