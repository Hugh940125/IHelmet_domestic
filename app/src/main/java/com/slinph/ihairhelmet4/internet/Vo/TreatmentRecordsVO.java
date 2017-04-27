package com.slinph.ihairhelmet4.internet.Vo;


import com.slinph.ihairhelmet4.internet.model.TreatmentRecords;

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
