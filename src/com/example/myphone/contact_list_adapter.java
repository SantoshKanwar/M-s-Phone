package com.example.myphone;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Filter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.Contacts;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class contact_list_adapter extends ArrayAdapter<contacts>
{
	private final Context context;
	private ArrayList<contacts> item_array_list;
	private ArrayList<contacts> contact_list;
	private ArrayList<contacts> copy_contact_list;
	private CountryFilter filter;
	public contact_list_adapter(Context context,ArrayList<contacts> item_array_list)
	{
		super(context,R.layout.contact_list_row,item_array_list);
		this.context=context;
		this.item_array_list=item_array_list;
		this.contact_list=new ArrayList<contacts>();
		this.contact_list.addAll(item_array_list);
		this.copy_contact_list=new ArrayList<contacts>();
		this.copy_contact_list.addAll(item_array_list);
	}
	
	@Override
	  public android.widget.Filter getFilter() 
	  {
	    if (filter == null)
	    {
	    	filter  = new CountryFilter();
	    }
	    	return filter;
	  }
	
	public View getView(int position,View convertView,ViewGroup parent)
	{
		
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		View rowView=convertView;
			
		String contact_photo_uri=item_array_list.get(position).getperson_photo();
		Contacts_Holder ch;
		
		if(rowView==null)
		{			
			ch=new Contacts_Holder();
			rowView=inflater.inflate(R.layout.contact_list_row,parent,false);			
			ch.contact_name=(TextView) rowView.findViewById(R.id.lblMsg);
			ch.contact_number=(TextView) rowView.findViewById(R.id.lblNumber);
			ch.contact_photo=(ImageView) rowView.findViewById(R.id.file_image);
			ch.contact_id=(TextView) rowView.findViewById(R.id.history_id);
			rowView.setTag(ch);
		}
		else
		{
			ch=(Contacts_Holder) rowView.getTag();
		}
		
		//final TextView ch.contact_name=(TextView) rowView.findViewById(R.id.lblMsg);				
		ch.contact_name.setTextSize(18);
		ch.contact_name.setTextColor(Color.BLACK);
		ch.contact_name.setText(item_array_list.get(position).getperson_name());
		
		String person_name=ch.contact_name.getText().toString();		
		Typeface font_family=Typeface.createFromAsset(getContext().getAssets(),"fonts/NEXA LIGHT.OTF");		
		person_name=person_name.substring(0, 1).toUpperCase() + person_name.substring(1);
		ch.contact_name.setText(person_name);
		ch.contact_name.setTypeface(Typeface.SANS_SERIF);
		
		//final TextView ch.contact_number=(TextView) rowView.findViewById(R.id.lblNumber);		
		ch.contact_number.setText(item_array_list.get(position).getperson_number());		
		ch.contact_number.setTextColor(Color.GRAY);
								
		//final TextView id=(TextView) rowView.findViewById(R.id.history_id);
		//id.setText(contact_photo_url);
		ch.contact_id.setVisibility(View.INVISIBLE);
		ch.contact_id.setText(contact_photo_uri);
		Bitmap bitmap=getContactBitmapFromURI(getContext(),contact_photo_uri,ch.contact_id);
		
		if(bitmap!=null)
		{
			ch.contact_photo.getLayoutParams().height=110;
			ch.contact_photo.getLayoutParams().width=110;
			ch.contact_photo.setImageBitmap(getRoundedShape(bitmap,400));
		}
		else
		{
			ch.contact_photo.getLayoutParams().height=115;
			ch.contact_photo.getLayoutParams().width=115;
			Bitmap bm = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.defaultcontact);
	        Config config = bm.getConfig();
	        int width = bm.getWidth();
	        int height = bm.getHeight();
	        Bitmap newImage = Bitmap.createBitmap(width, height, config);
	        Canvas c = new Canvas(newImage);
	        c.drawBitmap(bm, 0, 0, null);
	        Paint paint = new Paint();
	        paint.setColor(Color.DKGRAY); 
	        paint.setStyle(Style.FILL);                
	        paint.setTextSize(100);	        	  	       
	        
	        String first_contact_char=String.valueOf(person_name.toUpperCase().charAt(0));
	        String second_character=String.valueOf(person_name.substring(person_name.indexOf(' ')+1).toUpperCase().charAt(0));
	        String person_alias=first_contact_char+second_character;
	        
	        Rect bounds = new Rect();
	        paint.getTextBounds(person_alias,0,2,bounds);
	        c.drawText(person_alias,(c.getWidth() - bounds.width()) / 2, (c.getHeight() - bounds.height()) / 1,paint);	        
	        ch.contact_photo.setImageBitmap(newImage);	        
		}		
				
		return rowView;
	}		
	
	public static Bitmap getRoundedShape(Bitmap scaleBitmapImage,int width) 
	{	
		int targetWidth = width;
		int targetHeight = width;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
		targetHeight,Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
		((float) targetHeight - 1) / 2,
		(Math.min(((float) targetWidth),
		((float) targetHeight)) / 2),
		Path.Direction.CCW);
		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap,
		new Rect(0, 0, sourceBitmap.getWidth(),
		sourceBitmap.getHeight()),
		new Rect(0, 0, targetWidth,
		targetHeight), null);
		return targetBitmap;
	}
	public static Bitmap getContactBitmapFromURI(Context context, String uri,TextView view) 
	{
        InputStream input = null;
		try 
		{
			input = context.getContentResolver().openInputStream(Uri.parse(view.getText().toString()));
		} 
		catch (FileNotFoundException e) 
		{		
			e.printStackTrace();
		}
        if(input == null) 
        {
            return null;
        }		
		view.setText(uri);	
        return BitmapFactory.decodeStream(input);
    }	
	
	private class CountryFilter extends android.widget.Filter
	  {
	 
	   @Override
	   protected FilterResults performFiltering(CharSequence constraint) 
	   {	 		  		  
		   constraint = constraint.toString().toLowerCase();		   
		   FilterResults result = new FilterResults();
		   if(constraint != null && constraint.toString().length() > 0)
		   {
			   	ArrayList<contacts> filteredItems = new ArrayList<contacts>();
			   	for(int i = 0, l = contact_list.size(); i < l; i++)
			   	{
			   		String country = contact_list.get(i).getperson_name().toString();
			   		if(country.toString().toLowerCase().contains(constraint.toString()))
			   		{			   			
			   			filteredItems.add(new contacts(contact_list.get(i).getperson_name(),contact_list.get(i).getperson_number(),contact_list.get(i).getperson_photo()));
			   		}
			   	}
			   			result.count = filteredItems.size();
			   			result.values = filteredItems;
		   	}		   		   
		   			return result;
	   	}
	   @SuppressWarnings("unchecked")
	   @Override
	   protected void publishResults(CharSequence constraint,FilterResults results) 
	   {
		   item_array_list = (ArrayList<contacts>)results.values;
		   notifyDataSetChanged();
		   clear();
		   for(int i = 0, l = item_array_list.size(); i < l; i++)
		   {
			   	add(item_array_list.get(i));
		   }		   				   		
		   		notifyDataSetInvalidated();
	   }
	  }	
	private static class Contacts_Holder
	{
	    TextView contact_name;
	    TextView contact_number;
	    TextView contact_id;
	    ImageView contact_photo;
	}
}
