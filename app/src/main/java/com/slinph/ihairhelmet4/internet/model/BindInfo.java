package com.slinph.ihairhelmet4.internet.model;

import java.util.Date;

/**
 * Created by Administrator on 2016/11/26.
 *
 */
public class BindInfo {
    String code;
    Date createDtm;
    Integer del;
    Date delDtm;
    Integer id;
    Integer isUse;
    Integer userId;
    Date useDtm;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getIsUse() {
        return isUse;
    }

    public void setIsUse(Integer isUse) {
        this.isUse = isUse;
    }

    public Date getUseDtm() {
        return useDtm;
    }

    public void setUseDtm(Date useDtm) {
        this.useDtm = useDtm;
    }

    public Integer getDel() {
        return del;
    }

    public void setDel(Integer del) {
        this.del = del;
    }

    public Date getDelDtm() {
        return delDtm;
    }

    public void setDelDtm(Date delDtm) {
        this.delDtm = delDtm;
    }

    public Date getCreateDtm() {
        return createDtm;
    }

    public void setCreateDtm(Date createDtm) {
        this.createDtm = createDtm;
    }
}
