package com.slinph.ihairhelmet4.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.slinph.ihairhelmet4.R;
import com.slinph.ihairhelmet4.internet.Vo.TreatmentProgramsVO;
import com.slinph.ihairhelmet4.internet.net_utis.DataConverter;
import com.slinph.ihairhelmet4.utils.TimeUtils;
import com.slinph.ihairhelmet4.utils.ViewHolder;

import java.util.Date;
import java.util.List;


/**
 * Created by  on 2016/9/14.
 *
 */
public class AllReportAdapter extends BaseAdapter{
    private List<TreatmentProgramsVO> list;
    private LayoutInflater inflater;
    public AllReportAdapter(Context context, List<TreatmentProgramsVO> list){
        this.list=list;
        inflater= LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null)
            convertView=inflater.inflate(R.layout.item_report,null);
        //TextView tvTitle= ViewHolder.get(convertView,R.id.tv_title);
        TextView tvTime=ViewHolder.get(convertView,R.id.tv_time);
        TextView tvLevel=ViewHolder.get(convertView,R.id.tv_level);
        TextView tvDisease=ViewHolder.get(convertView,R.id.tv_disease);
        TextView tvLossType=ViewHolder.get(convertView,R.id.tv_loss_type);
        //tvTitle.setText(list.get(position).getTitle());
        Date createDtm = list.get(position).getCreateDtm();
        tvTime.setText(TimeUtils.getDateToString(createDtm.getTime()));
        //脱发等级
        Integer hairLossDegree = list.get(position).getHairLossDegree();
        String String_degree = DataConverter.getHairLossDegree(hairLossDegree);
        tvLevel.setText(String_degree+"; ");
        //脱发病
        Integer hairLossDisease = list.get(position).getHairLossDisease();
        String disease = DataConverter.getDisease(hairLossDisease);
        tvDisease.setText("伴有"+disease);
        //脱发类型
        Integer hairLossType = list.get(position).getHairLossType();
        String type = DataConverter.getType(hairLossType);
        tvLossType.setText(type+"; ");
        return convertView;
    }
}
