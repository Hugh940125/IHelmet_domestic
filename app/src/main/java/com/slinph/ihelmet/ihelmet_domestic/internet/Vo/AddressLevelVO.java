package com.slinph.ihelmet.ihelmet_domestic.internet.Vo;


import com.slinph.ihelmet.ihelmet_domestic.internet.model.AddressLevel;

import java.util.List;


public class AddressLevelVO extends AddressLevel {

	private List<AddressLevel> addressLevels;

	public void setAddressLevels(List<AddressLevel> addressLevels){
		this.addressLevels=addressLevels;
	}

	public List<AddressLevel>  getAddressLevels(){
		return addressLevels;
	}


}
