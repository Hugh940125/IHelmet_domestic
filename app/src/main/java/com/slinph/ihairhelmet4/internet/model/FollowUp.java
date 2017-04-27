package com.slinph.ihairhelmet4.internet.model;

import java.util.Date;


public class FollowUp {

	private Long id;

	private Long userId;

	private Long patientId;

	private Integer hairOil;

	private Integer dandruff;

	private Integer scalpItching;

	private Integer rashPustule;

	private String alopeciaSpeed;

	private String topViewUrl;

	private String hairlineUrl;

	private String afterViewUrl;

	private String partialViewUrl;

	private Integer del;

	private Date delDtm;

	private Date createDtm;


	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return id;
	}

	public void setUserId(Long userId){
		this.userId=userId;
	}

	public Long getUserId(){
		return userId;
	}

	public void setPatientId(Long patientId){
		this.patientId=patientId;
	}

	public Long getPatientId(){
		return patientId;
	}

	public void setHairOil(Integer hairOil){
		this.hairOil=hairOil;
	}

	public Integer getHairOil(){
		return hairOil;
	}

	public void setDandruff(Integer dandruff){
		this.dandruff=dandruff;
	}

	public Integer getDandruff(){
		return dandruff;
	}

	public void setScalpItching(Integer scalpItching){
		this.scalpItching=scalpItching;
	}

	public Integer getScalpItching(){
		return scalpItching;
	}

	public void setRashPustule(Integer rashPustule){
		this.rashPustule=rashPustule;
	}

	public Integer getRashPustule(){
		return rashPustule;
	}

	public void setAlopeciaSpeed(String alopeciaSpeed){
		this.alopeciaSpeed=alopeciaSpeed;
	}

	public String getAlopeciaSpeed(){
		return alopeciaSpeed;
	}

	public void setTopViewUrl(String topViewUrl){
		this.topViewUrl=topViewUrl;
	}

	public String getTopViewUrl(){
		return topViewUrl;
	}

	public void setHairlineUrl(String hairlineUrl){
		this.hairlineUrl=hairlineUrl;
	}

	public String getHairlineUrl(){
		return hairlineUrl;
	}

	public void setAfterViewUrl(String afterViewUrl){
		this.afterViewUrl=afterViewUrl;
	}

	public String getAfterViewUrl(){
		return afterViewUrl;
	}

	public void setPartialViewUrl(String partialViewUrl){
		this.partialViewUrl=partialViewUrl;
	}

	public String getPartialViewUrl(){
		return partialViewUrl;
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

}
