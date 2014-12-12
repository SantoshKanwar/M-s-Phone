package com.example.myphone;

import android.content.Context;

public class call_log 
{
	private String phone_number;
	private String call_type;
	private String call_date;
	private String call_duration;
	public call_log(String phone_number,String call_type,String call_date,String call_duration)
	{
		super();
		this.phone_number=phone_number;
		this.call_type=call_type;
		this.call_date=call_date;
		this.call_duration=call_duration;
	}
	public String getphone_number()
	{
		return this.phone_number;
	}
	public void setPhone_number(String phone_number)
	{
		this.phone_number=phone_number;		
	}
	public String getCall_Type()
	{
		return this.call_type;
	}
	public void setCall_Type(String call_type)
	{
		this.call_type=call_type;		
	}
	public String getCall_Date()
	{
		return this.call_date;
	}
	public void setCall_Date(String call_date)
	{
		this.call_date=call_date;		
	}
	public String getCall_Duration()
	{
		return this.call_duration;
	}
	public void setCall_Duration(String call_duration)
	{
		this.call_duration=call_duration;	
	}
}
