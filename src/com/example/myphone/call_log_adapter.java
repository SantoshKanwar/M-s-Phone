package com.example.myphone;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class call_log_adapter extends ArrayAdapter<call_log> 
{	
	private Context context; 
	private ArrayList<call_log> item_array_list;
	
	public call_log_adapter(Context context,ArrayList<call_log> item_array_list) 
	{
		super(context,R.layout.call_log_list_row,item_array_list);
		this.context=context;
		this.item_array_list=item_array_list;
	}
	
	public View getView(int position,View convertView,ViewGroup parent)
	{
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		View rowView=convertView;		
		Contacts_Holder ch;
		
		if(rowView==null)
		{			
			ch=new Contacts_Holder();
			rowView=inflater.inflate(R.layout.call_log_list_row,parent,false);			
			ch.contact_number=(TextView) rowView.findViewById(R.id.lblMsg);
			ch.call_type=(TextView) rowView.findViewById(R.id.lblNumber);
			ch.contact_photo=(ImageView) rowView.findViewById(R.id.file_image);
			ch.call_date_duration=(TextView) rowView.findViewById(R.id.history_id);
			rowView.setTag(ch);
		}
		else
		{
			ch=(Contacts_Holder) rowView.getTag();
		}
			ch.contact_photo.getLayoutParams().height=110;
			ch.contact_photo.getLayoutParams().width=110;
			ch.contact_number.setTextColor(Color.DKGRAY);
			ch.contact_number.setText(item_array_list.get(position).getphone_number());
			int call_type=Integer.parseInt(item_array_list.get(position).getCall_Type());
			if(call_type==CallLog.Calls.OUTGOING_TYPE)
			{				
				ch.call_type.setTextColor(Color.BLACK);
				ch.call_type.setText("OUTGOING");
			}
			else if(call_type==CallLog.Calls.INCOMING_TYPE)
			{
				ch.call_type.setTextColor(Color.GRAY);
				ch.call_type.setText("INCOMING");
			}
			else if(call_type==CallLog.Calls.MISSED_TYPE)
			{
				ch.call_type.setTextColor(Color.RED);
				ch.call_type.setText("MISSED");
			}			
			
			ch.call_date_duration.setTextColor(Color.DKGRAY);
			ch.call_date_duration.setText(item_array_list.get(position).getCall_Duration()+" Secs | "+item_array_list.get(position).getCall_Date());
			
		return rowView;
	}
	private static class Contacts_Holder
	{
	    TextView contact_number;
	    TextView call_date_duration;
	    TextView call_type;
	    ImageView contact_photo;
	}
}
