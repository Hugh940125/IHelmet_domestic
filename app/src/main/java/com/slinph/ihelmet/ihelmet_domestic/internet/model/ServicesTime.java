package com.slinph.ihelmet.ihelmet_domestic.internet.model;

import java.util.Date;


public class ServicesTime {

	private Long id;

	private Long userId;

	private Date serviceTime;

	private Integer type;

	private Integer isOpen;

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

	public void setServiceTime(Date serviceTime){
		this.serviceTime=serviceTime;
	}

	public Date getServiceTime(){
		return serviceTime;
	}

	public void setType(Integer type){
		this.type=type;
	}

	public Integer getType(){
		return type;
	}

	public void setIsOpen(Integer isOpen){
		this.isOpen=isOpen;
	}

	public Integer getIsOpen(){
		return isOpen;
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
