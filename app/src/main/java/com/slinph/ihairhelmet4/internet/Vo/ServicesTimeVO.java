package com.slinph.ihairhelmet4.internet.Vo;


import com.slinph.ihairhelmet4.internet.model.ServicesTime;

import java.util.List;


public class ServicesTimeVO extends ServicesTime {

	private List<ServicesTime> servicesTimes;

	public void setServicesTimes(List<ServicesTime> servicesTimes){
		this.servicesTimes=servicesTimes;
	}

	public List<ServicesTime>  getServicesTimes(){
		return servicesTimes;
	}


}
