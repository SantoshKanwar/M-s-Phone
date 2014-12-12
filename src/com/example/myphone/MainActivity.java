package com.example.myphone;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Locale;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView.FindListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(false);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});
		
		final int[] ICONS = new int[] 
	    {	            
	            R.drawable.calllog,
	            R.drawable.contacts,	          
	    };


		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < 2; i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()					
					.setIcon(ICONS[i])
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) 
		{
			Locale l = Locale.getDefault();
			switch (position) 
			{			
			case 0:
				return "Call Log".toUpperCase(l);
			case 1:
				return "Contacts".toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		
		
		public static final String ARG_SECTION_NUMBER = "section_number";
		contact_list_adapter contacts_adpater=null;
		call_log_adapter call_log_adapter=null;
		ArrayList<contacts> contacts=new ArrayList<contacts>();
		ArrayList<call_log> call_logs=new ArrayList<call_log>();
		ArrayList<contacts> filtered_contacts=new ArrayList<contacts>();
		ListView contact_list=null;
		ListView calllog_list=null;
		int no_of_contacts=0;
		TableLayout dialler;
				
		public DummySectionFragment() 
		{			
			
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) 
		{			
			final View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			
			int section_number=getArguments().getInt(
					ARG_SECTION_NUMBER);
			
			contact_list=(ListView) rootView.findViewById(R.id.contacts);
			calllog_list=(ListView) rootView.findViewById(R.id.call_log);
			final EditText search_contact=(EditText) rootView.findViewById(R.id.search);
			dialler=(TableLayout)rootView.findViewById(R.id.tableLayout1);
			
			if(section_number==2)
			{				
				dialler.setVisibility(View.GONE);
				ArrayAdapter<String> check=new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1);							
				contact_list.setVisibility(1);				
				Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null,Phone.DISPLAY_NAME + " ASC");
				String contact_number="";
				String contact_photo="";
				String contact_name="";
				Uri contact_photo_uri=null;
				while(phones.moveToNext()) 
				{					
					contact_number=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));					
					contact_name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
					contact_photo=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
					//contact_photo_uri=Uri.parse(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)));
					contacts.add(new contacts(contact_name,contact_number,contact_photo));									
				}											
				no_of_contacts=contacts.size();
				phones.close();												
				contacts_adpater=new contact_list_adapter(getActivity().getApplicationContext(),contacts);
				contact_list.setAdapter(contacts_adpater);
				
				
				contact_list.setOnItemClickListener(new OnItemClickListener() 
				{
					@Override
					public void onItemClick(AdapterView <? > parent, View view,int position, long id) 
					{
						TextView person_name=(TextView)view.findViewById(R.id.lblMsg);
						final TextView person_mobile=(TextView)view.findViewById(R.id.lblNumber);
						ImageView person_phpto=(ImageView) view.findViewById(R.id.file_image);
						final Intent callIntent = new Intent(Intent.ACTION_CALL);
						//AlertDialog.Builder builder=new Builder(getActivity());
						//AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.CenterJustifyTheme3));
						AlertDialog.Builder builder = new Builder(getActivity());
						builder.setTitle("Call Confirmation ! ");
						
						TextView message=new TextView(getActivity().getApplicationContext());
						message.setText("\n\tAre You sure want to call "+person_name.getText().toString()+" ?\n\n");
						
						Typeface font_family=Typeface.createFromAsset(getActivity().getAssets(),"fonts/NEXA LIGHT.OTF");
						message.setTypeface(Typeface.SANS_SERIF,Typeface.BOLD);
						message.setTextSize(16);
						message.setTextColor(Color.rgb(209,71,46));
						
						builder.setView(message);
						builder.setIcon(R.drawable.ic_launcher);
						builder.setPositiveButton("Call",new DialogInterface.OnClickListener() 
						{
							@Override
							public void onClick(DialogInterface arg0, int arg1) 
							{	
								callIntent.setData(Uri.parse("tel:"+person_mobile.getText().toString()));
								startActivity(callIntent);
							}			
						});
						builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() 
						{						
							@Override
							public void onClick(DialogInterface arg0, int arg1) 
							{
								arg0.dismiss();
							}
						});
												
						Dialog d = builder.show();
						int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
						View divider = d.findViewById(dividerId);
						int tid=d.getContext().getResources().getIdentifier("android:id/Message",null,null);
						View content = d.findViewById(dividerId);
						divider.setBackgroundColor(getResources().getColor(android.R.color.transparent));
					}
				});
				contact_list.setOnTouchListener(new OnSwipeTouchListener(getActivity().getApplicationContext())
				{
					@Override
					public void onSwipeLeft()
					{
						search_contact.setVisibility(View.VISIBLE);						
					}
				});
				search_contact.addTextChangedListener(new TextWatcher()
				{
					@Override
					public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) 
					{
						String contact_to_search=arg0.toString();
						if(contact_to_search!="")
						{
							filter_contacts(contact_to_search);
						}
					}
					
					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
							int arg3) 
					{
						
					}
					
					@Override
					public void afterTextChanged(Editable arg0) 
					{
					
					}
					
				});
				contact_list.setOnScrollListener(new OnScrollListener()
				{				
					@Override
					public void onScrollStateChanged(AbsListView arg0, int arg1) 
					{
					
					}							
					@Override
					public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
					{						
						if (firstVisibleItem == 0) 
						{			                
			                View v = contact_list.getChildAt(0);
			                int offset = (v == null) ? 0 : v.getTop();
			                if (offset == 0) 
			                {			   
			                	if(contact_list.getCount()==no_of_contacts)
			                	{
			                		search_contact.setVisibility(View.GONE);			                		
			                	}
			                	else
			                	{
			                		search_contact.setVisibility(View.VISIBLE);
			                	}
			                    return;
			                } 			                
			            } 
						else if (totalItemCount - visibleItemCount == firstVisibleItem)
						{
			                View v =  contact_list.getChildAt(totalItemCount-1);
			                int offset = (v == null) ? 0 : v.getTop();
			                if (offset == 0) 
			                {				                	
			                    return;
			                }
			            }               
					}	
				});
			}
			else if(section_number==1)
			{				
				dialler.setVisibility(View.GONE);
				Cursor managedCursor = getActivity().managedQuery( CallLog.Calls.CONTENT_URI,null, null,null, null);
				int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER );
				int type = managedCursor.getColumnIndex( CallLog.Calls.TYPE );
				int date = managedCursor.getColumnIndex( CallLog.Calls.DATE);
				int duration = managedCursor.getColumnIndex( CallLog.Calls.DURATION);
				while (managedCursor.moveToNext()) 
				{
					String phNumber = managedCursor.getString(number);
					String callType = managedCursor.getString(type);
					String callDate = managedCursor.getString(date);
					Date callDayTime = new Date(Long.valueOf(callDate));
					String callDuration = managedCursor.getString(duration);
					call_logs.add(new call_log(phNumber,callType,callDate,callDuration));
					//call_logs.add(new call_log("7878303073","Missed","4/11/2014","00:53"));
				}
				managedCursor.close();
				calllog_list.setVisibility(View.VISIBLE);
				call_log_adapter=new call_log_adapter(getActivity().getApplicationContext(),call_logs);
				calllog_list.setAdapter(call_log_adapter);									
			}
		
			
			return rootView;			
		}
		public void add_all_contacts()
		{				
			ArrayAdapter<String> check=new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1);							
			contact_list.setVisibility(1);				
			Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null,Phone.DISPLAY_NAME + " ASC");
			String contact_number="";
			String contact_photo="";
			String contact_name="";
			Uri contact_photo_uri=null;
			while(phones.moveToNext()) 
			{					
				contact_number=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));					
				contact_name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				contact_photo=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));				
				contacts.add(new contacts(contact_name,contact_number,contact_photo));									
			}											
			phones.close();												
			contacts_adpater=new contact_list_adapter(getActivity().getApplicationContext(),contacts);
			contact_list.setAdapter(contacts_adpater);
		}
		public void filter_contacts(String charText) 
	    {		
			contact_list.setTextFilterEnabled(true);			
	        charText = charText.toLowerCase(Locale.getDefault());	        
	        if (charText.length() == 0) 
	        {	        	
	        	contacts_adpater.clear();
	        	add_all_contacts();
	        }	
	        else
	        {	        	
	        	contacts_adpater.getFilter().filter(charText);
	        }	        
	    }
	}	
}

