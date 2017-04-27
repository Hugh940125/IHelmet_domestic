package com.slinph.ihairhelmet4.internet.model;

import java.util.Date;


public class TreatmentPrograms {

	private Long id;

	private Long patientId;

	private Long userId;

	private Integer adminId;

	private Integer hairLossType;

	private Integer hairLossDegree;

	private Integer hairLossDisease;

	private String hairLossSortUrl;

	private String graphic;

	private Integer times;

	private String allStatus;

	private Integer currentStatus;

	private String scheduleTitle;

	private Integer status;

	private Integer type;

	private Integer del;

	private Date delDtm;

	private Date createDtm;

	private Date updateDtm;


	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return id;
	}

	public void setPatientId(Long patientId){
		this.patientId=patientId;
	}

	public Long getPatientId(){
		return patientId;
	}

	public void setUserId(Long userId){
		this.userId=userId;
	}

	public Long getUserId(){
		return userId;
	}

	public void setAdminId(Integer adminId){
		this.adminId=adminId;
	}

	public Integer getAdminId(){
		return adminId;
	}

	public void setHairLossType(Integer hairLossType){
		this.hairLossType=hairLossType;
	}

	public Integer getHairLossType(){
		return hairLossType;
	}

	public void setHairLossDegree(Integer hairLossDegree){
		this.hairLossDegree=hairLossDegree;
	}

	public Integer getHairLossDegree(){
		return hairLossDegree;
	}

	public void setHairLossDisease(Integer hairLossDisease){
		this.hairLossDisease=hairLossDisease;
	}

	public Integer getHairLossDisease(){
		return hairLossDisease;
	}

	public void setHairLossSortUrl(String hairLossSortUrl){
		this.hairLossSortUrl=hairLossSortUrl;
	}

	public String getHairLossSortUrl(){
		return hairLossSortUrl;
	}

	public void setGraphic(String graphic){
		this.graphic=graphic;
	}

	public String getGraphic(){
		return graphic;
	}

	public void setTimes(Integer times){
		this.times=times;
	}

	public Integer getTimes(){
		return times;
	}

	public void setAllStatus(String allStatus){
		this.allStatus=allStatus;
	}

	public String getAllStatus(){
		return allStatus;
	}

	public void setCurrentStatus(Integer currentStatus){
		this.currentStatus=currentStatus;
	}

	public Integer getCurrentStatus(){
		return currentStatus;
	}

	public void setScheduleTitle(String scheduleTitle){
		this.scheduleTitle=scheduleTitle;
	}

	public String getScheduleTitle(){
		return scheduleTitle;
	}

	public void setStatus(Integer status){
		this.status=status;
	}

	public Integer getStatus(){
		return status;
	}

	public void setType(Integer type){
		this.type=type;
	}

	public Integer getType(){
		return type;
	}

	public void setDel(Integer del){
		this.del=del;
	}

	public Integer getDel(){
		return del;
	}

	public void setDelDtm(Date delDtm){
		this.delDtm=delDtm;
	}

	public Date getDelDtm(){
		return delDtm;
	}

	public void setCreateDtm(Date createDtm){
		this.createDtm=createDtm;
	}

	public Date getCreateDtm(){
		return createDtm;
	}

	public void setUpdateDtm(Date updateDtm){
		this.updateDtm=updateDtm;
	}

	public Date getUpdateDtm(){
		return updateDtm;
	}

}
