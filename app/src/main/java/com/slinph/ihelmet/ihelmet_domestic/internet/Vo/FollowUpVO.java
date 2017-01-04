package com.slinph.ihelmet.ihelmet_domestic.internet.Vo;


import com.slinph.ihelmet.ihelmet_domestic.internet.model.FollowUp;

import java.util.List;


public class FollowUpVO extends FollowUp {

	private List<FollowUp> followUps;

	public void setFollowUps(List<FollowUp> followUps){
		this.followUps=followUps;
	}

	public List<FollowUp>  getFollowUps(){
		return followUps;
	}


}
