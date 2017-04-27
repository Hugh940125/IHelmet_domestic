package com.slinph.ihairhelmet4.internet.Vo;


import com.slinph.ihairhelmet4.internet.model.Address;

import java.util.List;


public class AddressVO extends Address {

	private List<Address> addresss;

	public void setAddresss(List<Address> addresss){
		this.addresss=addresss;
	}

	public List<Address>  getAddresss(){
		return addresss;
	}


}
