package com.slinph.ihairhelmet4.internet.model;

import java.util.Date;


public class Role {

	private Long id;

	private String name;

	private String competence;

	private String remark;

	private Date createDtm;


	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return id;
	}

	public void setName(String name){
		this.name=name;
	}

	public String getName(){
		return name;
	}

	public void setCompetence(String competence){
		this.competence=competence;
	}

	public String getCompetence(){
		return competence;
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
