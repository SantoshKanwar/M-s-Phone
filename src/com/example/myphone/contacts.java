package com.example.myphone;

import android.net.Uri;

public class contacts 
{
	private String person_name;
	private String person_number;
	private String person_photo;
	public contacts(String person_name,String person_number,String person_photo)
	{
		super();
		this.person_name=person_name;
		this.person_number=person_number;
		this.person_photo=person_photo;
	}
	public String getperson_photo()
	{
		return this.person_photo;
	}
	public void setPerson_photo(String person_photo)
	{
		this.person_photo=person_photo;		
	}
	public String getperson_name()
	{
		return this.person_name;
	}
	public void setperson_name(String person_name)
	{
		this.person_name=person_name;
	}
	public void setperson_number(String desc)
	{
		this.person_number=desc;
	}
	public String getperson_number()
	{
		return this.person_number;
	}
}
