package com.slinph.ihelmet.ihelmet_domestic.internet.Vo;


import com.slinph.ihelmet.ihelmet_domestic.internet.model.Admin;

import java.util.List;


public class AdminVO extends Admin {

	private List<Admin> admins;

	public void setAdmins(List<Admin> admins){
		this.admins=admins;
	}

	public List<Admin>  getAdmins(){
		return admins;
	}


}
