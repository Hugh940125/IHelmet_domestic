package com.slinph.ihairhelmet4.internet.Vo;


import com.slinph.ihairhelmet4.internet.model.AddressLevel;

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
