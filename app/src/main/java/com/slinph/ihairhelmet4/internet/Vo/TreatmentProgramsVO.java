package com.slinph.ihairhelmet4.internet.Vo;


import com.slinph.ihairhelmet4.internet.model.TreatmentPrograms;

import java.util.List;


public class TreatmentProgramsVO extends TreatmentPrograms {

	private List<TreatmentPrograms> treatmentProgramss;

	public void setTreatmentProgramss(List<TreatmentPrograms> treatmentProgramss){
		this.treatmentProgramss=treatmentProgramss;
	}

	public List<TreatmentPrograms>  getTreatmentProgramss(){
		return treatmentProgramss;
	}


}
