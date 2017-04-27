package com.slinph.ihairhelmet4.internet.model;

import java.util.Date;


public class TreatmentRecords {

	private Long id;

	private Long userId;

	private String grease;

	private String temperature;

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

	public void setGrease(String grease){
		this.grease=grease;
	}

	public String getGrease(){
		return grease;
	}

	public void setTemperature(String temperature){
		this.temperature=temperature;
	}

	public String getTemperature(){
		return temperature;
	}

	public void setCreateDtm(Date createDtm){
		this.createDtm=createDtm;
	}

	public Date getCreateDtm(){
		return createDtm;
	}

}
