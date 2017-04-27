package com.slinph.ihairhelmet4.internet.Vo;

import com.slinph.ihairhelmet4.internet.model.BindInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 *
 */
public class BindInfoVO extends BindInfo {
    private List<BindInfo> bindInfoss;

    public void setBindInfos(List<BindInfo> bindInfoss){
        this.bindInfoss=bindInfoss;
    }

    public List<BindInfo>  getBindInfos(){
        return bindInfoss;
    }
}
