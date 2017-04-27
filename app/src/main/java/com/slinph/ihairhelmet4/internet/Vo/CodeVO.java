package com.slinph.ihairhelmet4.internet.Vo;


import com.slinph.ihairhelmet4.internet.model.Code;

import java.util.List;


public class CodeVO extends Code {

	private List<Code> codes;

	public void setCodes(List<Code> codes){
		this.codes=codes;
	}

	public List<Code>  getCodes(){
		return codes;
	}


}
