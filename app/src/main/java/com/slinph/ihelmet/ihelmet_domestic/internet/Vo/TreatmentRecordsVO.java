package com.slinph.ihelmet.ihelmet_domestic.internet.Vo;


import com.slinph.ihelmet.ihelmet_domestic.internet.model.TreatmentRecords;

import java.util.List;


public class TreatmentRecordsVO extends TreatmentRecords {

	private List<TreatmentRecords> treatmentRecordss;

	public void setTreatmentRecordss(List<TreatmentRecords> treatmentRecordss){
		this.treatmentRecordss=treatmentRecordss;
	}

	public List<TreatmentRecords>  getTreatmentRecordss(){
		return treatmentRecordss;
	}

}
