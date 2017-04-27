package com.slinph.ihairhelmet4.internet.model;

import java.util.Date;


public class Admin {

	private Long id;

	private String adminUser;

	private String adminName;

	private String adminPassword;

	private String adminUrl;

	private Integer adminType;

	private Integer del;

	private Date delDtm;

	private Date createDtm;


	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return id;
	}

	public void setAdminUser(String adminUser){
		this.adminUser=adminUser;
	}

	public String getAdminUser(){
		return adminUser;
	}

	public void setAdminName(String adminName){
		this.adminName=adminName;
	}

	public String getAdminName(){
		return adminName;
	}

	public void setAdminPassword(String adminPassword){
		this.adminPassword=adminPassword;
	}

	public String getAdminPassword(){
		return adminPassword;
	}

	public void setAdminUrl(String adminUrl){
		this.adminUrl=adminUrl;
	}

	public String getAdminUrl(){
		return adminUrl;
	}

	public void setAdminType(Integer adminType){
		this.adminType=adminType;
	}

	public Integer getAdminType(){
		return adminType;
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
