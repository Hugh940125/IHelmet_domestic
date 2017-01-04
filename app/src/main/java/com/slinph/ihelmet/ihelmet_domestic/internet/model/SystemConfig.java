package com.slinph.ihelmet.ihelmet_domestic.internet.model;

import java.util.Date;


public class SystemConfig {

	private Long id;

	private String propertyKey;

	private String propertyValue;

	private String remark;

	private Date createDtm;


	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return id;
	}

	public void setPropertyKey(String propertyKey){
		this.propertyKey=propertyKey;
	}

	public String getPropertyKey(){
		return propertyKey;
	}

	public void setPropertyValue(String propertyValue){
		this.propertyValue=propertyValue;
	}

	public String getPropertyValue(){
		return propertyValue;
	}

	public void setRemark(String remark){
		this.remark=remark;
	}

	public String getRemark(){
		return remark;
	}

	public void setCreateDtm(Date createDtm){
		this.createDtm=createDtm;
	}

	public Date getCreateDtm(){
		return createDtm;
	}

}
