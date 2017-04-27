package com.slinph.ihairhelmet4.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.slinph.ihairhelmet4.R;
import com.slinph.ihairhelmet4.activity.QuestionDeteilActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/9.
 *
 */

public class MyExpandAdapter extends BaseExpandableListAdapter {

    List<String> group_head;
    List<List<String>> child;
    QuestionDeteilActivity context;

    public MyExpandAdapter(Context content){
        //初始化组、子列表项
        group_head = new ArrayList<String>();
        child = new ArrayList<List<String>>();
    }

    public MyExpandAdapter(QuestionDeteilActivity context,List<String> group_head,
                           List<List<String>> child){
        this.context = context;
        //初始化组、子列表项
        this.group_head = group_head;
        this.child = child;
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return this.group_head.size();
    }

    @Override
    public int getChildrenCount(int position) {
        // TODO Auto-generated method stub
        if(position<0||position>=this.child.size())
            return 0;
        return child.get(position).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return group_head.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return child.get(childPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        //获取文本
        String text = group_head.get(groupPosition);
        if(convertView == null){
            //组列表界面只有一个文本框，直接生成
            convertView = new TextView(context);
            //设定界面，AbsListView:用于实现条目的虚拟列表的基类
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//dip2px(context,45)
            ((TextView) convertView).setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT); //文本框放在中央
            convertView.setPadding(25, 0, 0, 0);                               //设置文本里那个下拉的图标远一点
            convertView.setLayoutParams(lp);
            //Log.d("Group", text);
        }
        ((TextView) convertView).setText(text);
        ((TextView) convertView).setTextSize(dip2px(context,8));
        return convertView;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        //子列表控件通过界面文件设计
        if(convertView ==null){//convert在运行中会重用，如果不为空，则表明不用重新获取
            LayoutInflater layoutInflater;//使用这个来载入界面
            layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.plain_text, null);
        }
        TextView tv = (TextView)convertView.findViewById(R.id.textView_plain_text);
        String text = child.get(groupPosition).get(childPosition);
        tv.setText(text);
        //获取文本控件，并设置值
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        //调用Activity里的ChildSelect方法
        context.childSelect(groupPosition,childPosition);
        return true;
    }
}
