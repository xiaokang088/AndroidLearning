package com.example.adapterexample;

import java.util.Date;

 

public class PlanItem {

	String from;
	String to;
	Date created;
	
	public String getFrom(){
		return from;
	}
	
	public String getTo(){
		return to;
	}
	
	public Date getCreated(){
		return created;
	}
	
	 
	public PlanItem(String from, String to, java.util.Date testDate){
		this.from = from;
		this.to = to;
		this.created = testDate;
	}
	
}
