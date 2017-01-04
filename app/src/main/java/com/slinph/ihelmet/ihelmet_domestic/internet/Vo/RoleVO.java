package com.slinph.ihelmet.ihelmet_domestic.internet.Vo;


import com.slinph.ihelmet.ihelmet_domestic.internet.model.Role;

import java.util.List;


public class RoleVO extends Role {

	private List<Role> roles;

	public void setRoles(List<Role> roles){
		this.roles=roles;
	}

	public List<Role>  getRoles(){
		return roles;
	}


}
