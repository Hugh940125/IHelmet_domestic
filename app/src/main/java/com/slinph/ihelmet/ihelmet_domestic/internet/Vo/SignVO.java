package com.slinph.ihelmet.ihelmet_domestic.internet.Vo;


import com.slinph.ihelmet.ihelmet_domestic.internet.model.Sign;

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
