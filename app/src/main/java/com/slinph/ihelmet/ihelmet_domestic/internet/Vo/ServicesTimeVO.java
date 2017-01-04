package com.slinph.ihelmet.ihelmet_domestic.internet.Vo;


import com.slinph.ihelmet.ihelmet_domestic.internet.model.ServicesTime;

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
