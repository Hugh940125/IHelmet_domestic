package com.slinph.ihairhelmet4.internet.Vo;

import com.slinph.ihairhelmet4.internet.model.Qualified;

import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class QualifiedVO extends Qualified {

    private List<Qualified> qualifieds;

    public void setQualifieds(List<Qualified> qualifieds){
        this.qualifieds=qualifieds;
    }

    public List<Qualified>  getQualifieds(){
        return qualifieds;
    }
}
