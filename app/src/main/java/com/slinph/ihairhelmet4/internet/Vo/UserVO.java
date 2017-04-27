package com.slinph.ihairhelmet4.internet.Vo;


import com.slinph.ihairhelmet4.internet.model.User;

import java.util.List;


public class UserVO extends User {

	private List<User> users;

	public void setUsers(List<User> users){
		this.users=users;
	}

	public List<User> getUsers(){
		return users;
	}
}
