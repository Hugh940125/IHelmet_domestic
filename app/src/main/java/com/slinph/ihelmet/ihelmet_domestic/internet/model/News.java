package com.slinph.ihelmet.ihelmet_domestic.internet.model;

import java.util.Date;


public class News {

	private Long id;

	private Integer type;

	private String url;

	private String imgUrl;

	private Integer display;

	private Date createDtm;

	private Integer position;


	public void setId(Long id){
		this.id=id;
	}

	public Long getId(){
		return id;
	}

	public void setType(Integer type){
		this.type=type;
	}

	public Integer getType(){
		return type;
	}

	public void setUrl(String url){
		this.url=url;
	}

	public String getUrl(){
		return url;
	}

	public void setImgUrl(String imgUrl){
		this.imgUrl=imgUrl;
	}

	public String getImgUrl(){
		return imgUrl;
	}

	public void setDisplay(Integer display){
		this.display=display;
	}

	public Integer getDisplay(){
		return display;
	}

	public void setCreateDtm(Date createDtm){
		this.createDtm=createDtm;
	}

	public Date getCreateDtm(){
		return createDtm;
	}

	public void setPosition(Integer position){
		this.position=position;
	}

	public Integer getPosition(){
		return position;
	}

}
