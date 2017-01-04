package com.slinph.ihelmet.ihelmet_domestic.internet.Vo;


import com.slinph.ihelmet.ihelmet_domestic.internet.model.News;

import java.util.List;


public class NewsVO extends News {

	private List<News> newss;

	public void setNewss(List<News> newss){
		this.newss=newss;
	}

	public List<News>  getNewss(){
		return newss;
	}


}
