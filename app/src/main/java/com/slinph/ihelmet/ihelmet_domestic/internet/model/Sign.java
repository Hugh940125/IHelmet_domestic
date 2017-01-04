package com.slinph.ihelmet.ihelmet_domestic.internet.model;


public class Sign {

	private Long id;

	private Long userId;

	private String signDt;


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

	public void setSignDt(String signDt){
		this.signDt=signDt;
	}

	public String getSignDt(){
		return signDt;
	}

}
