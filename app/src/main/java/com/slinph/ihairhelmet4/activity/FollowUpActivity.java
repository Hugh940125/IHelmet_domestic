package com.slinph.ihairhelmet4.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slinph.ihairhelmet4.R;
import com.slinph.ihairhelmet4.utils.SerializableMap;
import com.slinph.ihairhelmet4.utils.UIUtils;
import com.slinph.ihairhelmet4.view.flowlayout.FlowLayout;
import com.slinph.ihairhelmet4.view.flowlayout.TagAdapter;
import com.slinph.ihairhelmet4.view.flowlayout.TagFlowLayout;

import java.util.Set;

public class FollowUpActivity extends AppCompatActivity {

    private TextView tv_des;
    private LayoutInflater mInflater;
    private SerializableMap map = new SerializableMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_up);
        findViews();
        initData();
    }

    protected void findViews() {
        TagFlowLayout flowlayout_scalp = (TagFlowLayout) findViewById(R.id.flowlayout_scalp);
        TagFlowLayout flowlayout_dandruff = (TagFlowLayout) findViewById(R.id.flowlayout_dandruff);
        TagFlowLayout flowlayout_scalp_itching = (TagFlowLayout) findViewById(R.id.flowlayout_scalp_itching);
        TagFlowLayout flowlayout_loss_speed = (TagFlowLayout) findViewById(R.id.flowlayout_loss_speed);
        TagFlowLayout flowlayout_treatment_newhair = (TagFlowLayout) findViewById(R.id.flowlayout_treatment_newhair);
        TagFlowLayout flowlayout_treatment_cuzhuang = (TagFlowLayout) findViewById(R.id.flowlayout_treatment_cuzhuang);//变化效果
        TagFlowLayout flowlayout_treatment_medicine = (TagFlowLayout) findViewById(R.id.flowlayout_treatment_medicine);
        ImageButton ib_followup_back = (ImageButton) findViewById(R.id.ib_followup_back);
        if (ib_followup_back != null){
            ib_followup_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(FollowUpActivity.this,MainActivity.class));
                }
            });
        }

        tv_des = (TextView) findViewById(R.id.tv_des_followup);
        mInflater = LayoutInflater.from(this);
        fillTagAdater(flowlayout_scalp, getResources().getStringArray(R.array.answer_1));
        fillTagAdater(flowlayout_dandruff, getResources().getStringArray(R.array.answer_2));
        fillTagAdater(flowlayout_scalp_itching, getResources().getStringArray(R.array.answer_3));
        fillTagAdater(flowlayout_loss_speed, getResources().getStringArray(R.array.answer_4));
        fillTagAdater(flowlayout_treatment_newhair, getResources().getStringArray(R.array.answer_5));
        fillTagAdater(flowlayout_treatment_cuzhuang, getResources().getStringArray(R.array.answer_6));
        fillTagAdater(flowlayout_treatment_medicine, getResources().getStringArray(R.array.answer_7));
        Button btn_ne = (Button) findViewById(R.id.btn_next);
        if (btn_ne != null){
            btn_ne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //遍历map集合
                    for (String key : map.keySet()) {
                        Log.e(key+"=",map.get(key));
                        if (TextUtils.isEmpty(map.get(key))) {//有未提交信息先跳出循环,提示用户
                            showEmpty(key);
                            return;
                        } else {
                            tv_des.setText("");
                        }
                    }
                    Intent intent = new Intent(FollowUpActivity.this, TakePhotoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("FU_Info",map);
                    intent.putExtras(bundle);
                    intent.putExtra("Where","FollowUp");
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    /**
     * 初始化map的数据
     */
    private void initData() {
        map.put("hairOil", "");//头油变化
        map.put("dandruff", "");//头屑变化
        map.put("hairThick", "");//头皮粗壮
        map.put("rashPustule", "");//头皮红疹
        map.put("alopeciaSpeed", "");//脱发速度变化及效果
        map.put("useDrugs", "");//脱发速度变化及效果
        map.put("hairLong", "");//新绒毛
    }
    /**
     * 设置点击事件
     */
    private void fillTagListener(final TagFlowLayout view, final String[] vals) {
        view.setOnSelectListener(new TagFlowLayout.OnSelectListener() {

            private String yesOrNo;

            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                StringBuilder sb = new StringBuilder();
                for (Integer i : selectPosSet) {
                    sb.append(vals[i]).append(",");//用于多选,可以获取多选信息
                }
                String str = sb.toString();
                if (!TextUtils.isEmpty(str)) {
                    str = str.substring(0, str.length() - 1);//去掉最后一个逗号
                }

                switch (view.getId()) {
                    case R.id.flowlayout_scalp:
                        if ("有".equals(str)){
                            yesOrNo = "0";
                        }else if("没有".equals(str)){
                            yesOrNo = "1";
                        }else if ("本来就不油".equals(str)){
                            yesOrNo = "2";
                        }else if ("出汗多，不好判断".equals(str)){
                            yesOrNo = "3";
                        }else {
                            yesOrNo = "";
                        }
                        map.put("hairOil", yesOrNo);//头油
                        break;
                    case R.id.flowlayout_dandruff:
                        if ("有".equals(str)){
                            yesOrNo = "0";
                        }else if("没有".equals(str)){
                            yesOrNo = "1";
                        }else if ("本来就没有，不好判断".equals(str)){
                            yesOrNo = "2";
                        }else {
                            yesOrNo = "";
                        }
                        map.put("dandruff", yesOrNo);//头屑
                        break;
                    case R.id.flowlayout_scalp_itching:
                        if ("有".equals(str)){
                            yesOrNo = "0";
                        }else if("没有".equals(str)){
                            yesOrNo = "1";
                        }else if("本来就没有".equals(str)){
                            yesOrNo = "2";
                        }else {
                            yesOrNo = "";
                        }
                        map.put("rashPustule", yesOrNo);//头皮瘙痒
                        break;
                    case R.id.flowlayout_loss_speed:
                        if ("有".equals(str)){
                            yesOrNo = "0";
                        }else if("没有".equals(str)){
                            yesOrNo = "1";
                        }else  if("使用前已经基本停止掉发，不好判断".equals(str)){
                            yesOrNo = "2";
                        } else {
                            yesOrNo = "";
                        }
                        map.put("alopeciaSpeed", yesOrNo);//脱发速度
                        break;

                    case R.id.flowlayout_treatment_newhair:
                        if ("有".equals(str)){
                            yesOrNo = "0";
                        }else if ("没有".equals(str)){
                            yesOrNo = "1";
                        }
                        else {
                            yesOrNo = "";
                        }
                        map.put("hairLong", yesOrNo);//新绒毛
                        break;
                    case R.id.flowlayout_treatment_cuzhuang:
                        if ("有".equals(str)){
                            yesOrNo = "0";
                        }else if ("没有".equals(str)){
                            yesOrNo = "1";
                        }
                        else {
                            yesOrNo = "";
                        }
                        map.put("hairThick", yesOrNo);//粗壮
                        break;
                    case R.id.flowlayout_treatment_medicine:
                        if ("米诺地尔".equals(str)){
                            yesOrNo = "0";
                        }else if("非那雄胺".equals(str)){
                            yesOrNo = "1";
                        }else if ("两者都用".equals(str)){
                            yesOrNo = "2";
                        }else  if ("没有".equals(str)){
                            yesOrNo = "3";
                        }else {
                            yesOrNo = "";
                        }
                        map.put("useDrugs", yesOrNo);//用药
                        break;
                }
            }
        });
    }

    /**
     * 填充标签数据
     *
     * @param view tag
     */
    private void fillTagAdater(final TagFlowLayout view, final String[] vals) {
        fillTagListener(view, vals);
        view.setAdapter(new TagAdapter<String>(vals) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                //默认为多选样式
                TextView tv = (TextView) mInflater.inflate(R.layout.flowlayout_tagview_item, view, false);
                tv.setText(s);
                //单选时样式改变
                if (view.getMaxSelectCount() == 1) {
                    //距顶端距离(内边距)
                    tv.setBackground(getResources().getDrawable(R.drawable.tagview_radio_bg));
                    tv.setTextColor(getResources().getColorStateList(R.color.tagview_edit_color));
                    //距顶端距离(内边距)
                    tv.setPadding(UIUtils.dp2px(5), UIUtils.dp2px(5), UIUtils.dp2px(5), UIUtils.dp2px(5));
                    tv.setTextSize(16);
                    //设置控件长宽
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, UIUtils.dp2px(30));
                    //外边距
                    layoutParams.setMargins(UIUtils.dp2px(5), UIUtils.dp2px(5), UIUtils.dp2px(5), UIUtils.dp2px(5));
                    tv.setLayoutParams(layoutParams);
                }
                return tv;
            }
        });
    }

    /**
     * 显示未选择信息
     * @param key map中的key
     */
    private void showEmpty(String key) {
        switch (key) {
            case "hairOil":
                tv_des.setText("\"头油有无明显减少\"未选择");
                break;
            case "dandruff":
                tv_des.setText("\"头屑,头皮瘙痒有无明显减轻\"未选择");
                break;
            case "hairThick":
                tv_des.setText("\"头皮瘙痒有无减轻\"未选择");
                break;
            case "rashPustule":
                tv_des.setText("\"脱发速度是否减缓\"未选择");
                break;
            case "alopeciaSpeed":
                tv_des.setText("\"是否长出新生绒毛\"未选择");
                break;
            case "hairLong":
                tv_des.setText("\"头发是否变粗壮\"未选择");
                break;
            case "useDrugs":
                tv_des.setText("\"是否同时使用药物\"未选择");
                break;
        }
    }
}
