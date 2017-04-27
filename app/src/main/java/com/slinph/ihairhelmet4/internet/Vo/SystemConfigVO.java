package com.slinph.ihairhelmet4.internet.Vo;


import com.slinph.ihairhelmet4.internet.model.SystemConfig;

import java.util.List;


public class SystemConfigVO extends SystemConfig {

	private List<SystemConfig> systemConfigs;

	public void setSystemConfigs(List<SystemConfig> systemConfigs){
		this.systemConfigs=systemConfigs;
	}

	public List<SystemConfig>  getSystemConfigs(){
		return systemConfigs;
	}
}
