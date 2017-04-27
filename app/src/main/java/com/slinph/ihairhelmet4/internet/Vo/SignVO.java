package com.slinph.ihairhelmet4.internet.Vo;


import com.slinph.ihairhelmet4.internet.model.Sign;

import java.util.List;


public class SignVO extends Sign {

	private List<Sign> signs;

	public void setSigns(List<Sign> signs){
		this.signs=signs;
	}

	public List<Sign>  getSigns(){
		return signs;
	}


}
