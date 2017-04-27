package com.slinph.ihairhelmet4.internet.model;

import java.util.*;


public class Qualified {

	private Long id;

	private Long patientId;

	private Integer isQualified;

	private String position;
	
	private Integer type;

	private Date create_dtm;


	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return id;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Integer getIsQualified() {
		return isQualified;
	}

	public void setIsQualified(Integer isQualified) {
		this.isQualified = isQualified;
	}

	public void setPosition(String position){
		this.position=position;
	}

	public String getPosition(){
		return position;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setCreate_dtm(Date create_dtm){
		this.create_dtm=create_dtm;
	}

	public Date getCreate_dtm(){
		return create_dtm;
	}

}
