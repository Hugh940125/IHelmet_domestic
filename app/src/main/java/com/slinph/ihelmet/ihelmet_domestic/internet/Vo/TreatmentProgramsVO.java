package com.slinph.ihelmet.ihelmet_domestic.internet.Vo;


import com.slinph.ihelmet.ihelmet_domestic.internet.model.TreatmentPrograms;

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
