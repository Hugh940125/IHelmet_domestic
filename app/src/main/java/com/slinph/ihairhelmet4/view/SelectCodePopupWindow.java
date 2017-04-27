package com.slinph.ihairhelmet4.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.slinph.ihairhelmet4.R;


/**
 * 从底部弹出或滑出选择菜单或窗口  
 */
public class SelectCodePopupWindow extends PopupWindow {

	/**
	 * 图库选择
	 */
	private Button takeCodeBtn;
	/**
	 * 拍照
	 */
	private Button pickCodeBtn;
	/**
	 * 取消
	 */
	private Button cancelCodeBtn;
	private View mMenuView;

	@SuppressLint("InflateParams")
	public SelectCodePopupWindow(Context context, OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.layout_popupwindow_code, null);
		takeCodeBtn = (Button) mMenuView.findViewById(R.id.takeCodeBtn);
		pickCodeBtn = (Button) mMenuView.findViewById(R.id.pickCodeBtn);
		cancelCodeBtn = (Button) mMenuView.findViewById(R.id.cancelCodeBtn);
		// 设置按钮监听
		cancelCodeBtn.setOnClickListener(itemsOnClick);
		pickCodeBtn.setOnClickListener(itemsOnClick);
		takeCodeBtn.setOnClickListener(itemsOnClick);
		
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.PopupAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x80000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			@Override
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pop_code_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}

}
