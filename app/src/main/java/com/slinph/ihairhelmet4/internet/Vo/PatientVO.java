package com.slinph.ihairhelmet4.internet.Vo;


import com.slinph.ihairhelmet4.internet.model.Patient;

import java.util.List;


public class PatientVO extends Patient {

	private List<Patient> patients;

	public void setPatients(List<Patient> patients){
		this.patients=patients;
	}

	public List<Patient>  getPatients(){
		return patients;
	}


}
