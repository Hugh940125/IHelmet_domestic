package com.slinph.ihairhelmet4.internet.model;

import java.util.Date;


public class AddressLevel {

	private Long id;

	private Integer parentId;

	private String name;

	private Integer level;

	private Date createDtm;


	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return id;
	}

	public void setParentId(Integer parentId){
		this.parentId=parentId;
	}

	public Integer getParentId(){
		return parentId;
	}

	public void setName(String name){
		this.name=name;
	}

	public String getName(){
		return name;
	}

	public void setLevel(Integer level){
		this.level=level;
	}

	public Integer getLevel(){
		return level;
	}

	public void setCreateDtm(Date createDtm){
		this.createDtm=createDtm;
	}

	public Date getCreateDtm(){
		return createDtm;
	}

}
