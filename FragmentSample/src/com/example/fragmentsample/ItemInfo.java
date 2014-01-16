package com.example.fragmentsample;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ItemInfo {

	String title;
	Date createDate;
	
	public ItemInfo(String _title){
		title = _title ;
		this.createDate = new Date(System.currentTimeMillis());
	}
	
	public Date getcreateDate(){
		return createDate;
	}
	
	public String getcreateDateString(){
		SimpleDateFormat format= new SimpleDateFormat("yyyy/MM/dd hh:mm:sss");
		return format.format(createDate);
	}
	
	public String getTitle(){
		return title;
	}	
}
