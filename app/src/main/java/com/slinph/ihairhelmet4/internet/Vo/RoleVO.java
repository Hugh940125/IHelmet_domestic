package com.slinph.ihairhelmet4.internet.Vo;


import com.slinph.ihairhelmet4.internet.model.Role;

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
