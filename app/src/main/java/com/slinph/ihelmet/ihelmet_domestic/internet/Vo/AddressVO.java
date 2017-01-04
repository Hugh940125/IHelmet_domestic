package com.slinph.ihelmet.ihelmet_domestic.internet.Vo;


import com.slinph.ihelmet.ihelmet_domestic.internet.model.Address;

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
