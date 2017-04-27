package com.slinph.ihairhelmet4.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slinph.ihairhelmet4.R;

/**
 * Created by Administrator on 2016/9/27.
 *
 */
public class DeviceInfoItem extends LinearLayout {

    private TextView tv_value;
    private TextView tv_key;
    private String key;
    private String value;

    public DeviceInfoItem(Context context) {
        super(context);
    }

    public DeviceInfoItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitView(context);
        key = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","ValueName");
        //value = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","Value");
        tv_key.setText(key);
        //tv_value.setText(value);
    }

    public void setValue(String value){
        tv_value.setText(value);
    }

    public void InitView(Context con){
        View inflate = View.inflate(con, R.layout.equipment_info_item, this);
        tv_value = (TextView) inflate.findViewById(R.id.tv_value);
        tv_key = (TextView) inflate.findViewById(R.id.tv_key);
    }
}
