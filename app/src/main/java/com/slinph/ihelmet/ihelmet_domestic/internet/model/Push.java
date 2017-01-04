package com.slinph.ihelmet.ihelmet_domestic.internet.model;

import java.util.Date;

/**
 * Created by Administrator on 2016/9/19.
 *
 */
public class Push {
    private Integer id;
    private Integer userId;
    private String serviceTime;
    private Integer type;
    private Integer isOpen;
    private Integer days;
    private Integer del;
    private Date delDtm;
    private Date creatDtm;
    private Integer code;
    private String  msg;
    private Boolean success;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Integer isOpen) {
        this.isOpen = isOpen;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Date getCreatDtm() {
        return creatDtm;
    }

    public void setCreatDtm(Date creatDtm) {
        this.creatDtm = creatDtm;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
