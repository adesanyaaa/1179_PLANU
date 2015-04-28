package com.vc.locationevent;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SendCallback;
import com.parse.SignUpCallback;
import com.vc.locationevent.GroupActivity.Item1;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.IntentSender.SendIntentException;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.*;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class GroupEventActivity extends Activity implements OnClickListener{

	String latitude,longitude;
	String location_name="";
	Button date,time,location,selected_group,new_group;
	EditText event;
	ListView lv;
	static List<String> groupList;
	static List<String> groupid;
	ArrayList<Item1> groups;
	ContentResolver cResolver;
	List<String> C_ID;
	static List<String> C_NAME;
	List<String> C_NUMBER;
	int no_of_groups=0;
	String group_selected="";
	String selected_id="";
	String selected_number;
	int flag=0;
	
    int startYear,startMonth,startDay,startHour,startMinute;
    
    HashMap<String, ArrayList<String>> group_data;
    AlarmManager alarmMgr;
    
	Db_adapter db;
	SharedPreferences shed;
	ArrayList<String> nub;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_event);
		
		shed=getSharedPreferences("planu", Context.MODE_PRIVATE);
		String id=shed.getString("password", "");
		date=(Button) findViewById(R.id.grp_date);
		time=(Button) findViewById(R.id.grp_time);
		location=(Button) findViewById(R.id.grp_location);
		selected_group=(Button) findViewById(R.id.selected_group);
		new_group=(Button) findViewById(R.id.new_group);
		event=(EditText) findViewById(R.id.grp_evt);
		lv=(ListView) findViewById(R.id.grp_list);
		db=new Db_adapter(GroupEventActivity.this);
		groupList=new ArrayList<String>();
		groupid=new ArrayList<String>();
		nub=new ArrayList<String>();
		group_data=new HashMap<String, ArrayList<String>>();
		
		C_ID=new ArrayList<String>();
		C_NAME=new ArrayList<String>();
		C_NUMBER=new ArrayList<String>();
		ParseUser user=ParseUser.getCurrentUser();
		
		
		
//		PushService.

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
//				Toast.makeText(GroupEventActivity.this, "Phone no"+groupid.get(arg2), Toast.LENGTH_LONG).show();
				
				
				selected_id=groupid.get(arg2);
				
//				getlistofcontacts((long)Integer.parseInt(getGroupId(group_selected)));
				group_selected=groupList.get(arg2);
				nub=getAllNumbersFromGroupId(group_selected);
					for(int jj=0;jj<nub.size();jj++){
						Toast.makeText(getApplicationContext(), nub.get(jj), Toast.LENGTH_LONG).show();
					}
				selected_group.setText(group_selected);
				
			}
		});
		cResolver=getContentResolver();
		Cursor cr=cResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
		
		while(cr.moveToNext())
		{
			C_ID.add(cr.getString(cr.getColumnIndex(ContactsContract.Contacts._ID)));
			C_NAME.add(cr.getString(cr.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
			C_NUMBER.add(cr.getString(cr.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
		}
		
		groups=fetchGroups();
		int i=0;
		while(i<groups.size())
		{
			groupList.add(groups.get(i).name);
			i++;
		}

		
		
		db.open();
			Cursor c=db.selection("select * from grp_table");
			
			if(c!=null){
				while(c.moveToNext()){
					groupid.add(c.getInt(0)+"");
					groupList.add(c.getString(1));
				}
			}
		db.close();
		
		if(!groupid.isEmpty()){
		
			group_adapter adapter=new group_adapter(GroupEventActivity.this, groupList, groupid);
			lv.setAdapter(adapter);
		}
		
		
		date.setOnClickListener(this);
		time.setOnClickListener(this);
		location.setOnClickListener(this);
		new_group.setOnClickListener(this);
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	
		flag=0;
		Bundle b=getIntent().getExtras();
		if(b == null)
		{
		}
		else{
			
			if(b.getString("event")==null){
				latitude=b.getDouble("latitude")+"";
				longitude=b.getDouble("longitude")+"";
				location_name=b.getString("location");
				
				location.setText(location_name);
			}
			else{
				event.setText(b.getString("event"));
				date.setText(b.getString("date"));
				time.setText(b.getString("time"));
				location.setText(b.getString("location"));
			}
				
			
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group_event, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.grp_save) {
				db.open();
				
				AlertDialog.Builder builderSingle = new AlertDialog.Builder(
	                    GroupEventActivity.this);
	            builderSingle.setIcon(R.drawable.ic_launcher);
	            builderSingle.setTitle("Select One Name:-");
	            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
	                    GroupEventActivity.this,
	                    android.R.layout.select_dialog_singlechoice);
	            arrayAdapter.add("1");
	            arrayAdapter.add("2");
	            arrayAdapter.add("3");
	            arrayAdapter.add("4");
	            arrayAdapter.add("5");
	            builderSingle.setNegativeButton("cancel",
	                    new DialogInterface.OnClickListener() {

	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {
	                            dialog.dismiss();
	                        }
	                    });

	            builderSingle.setAdapter(arrayAdapter,
	                    new DialogInterface.OnClickListener() {

	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {
	                            final String strName = arrayAdapter.getItem(which);
	                            AlertDialog.Builder builderInner = new AlertDialog.Builder(
	                                    GroupEventActivity.this);
	                            builderInner.setMessage(strName);
	                            builderInner.setTitle("Selected priority");
	                            builderInner.setPositiveButton("Ok",
	                                    new DialogInterface.OnClickListener() {

	                                        @Override
	                                        public void onClick(
	                                                DialogInterface dialog,
	                                                int which) {
	                                            dialog.dismiss();
	                                            
	                                            long i=db.insert_grp_event(event.getText().toString(), date.getText().toString(), time.getText().toString(), location_name, latitude, longitude, group_selected,strName);
	                                            if(i>0){
	                        						
	                        						setAlarm(GroupEventActivity.this, startMonth, startDay, startYear, startHour, startMinute, (int)i, event.getText().toString());
	                        						
	                        						LinkedList<String> grp=new LinkedList<String>();
	                        						for(int jj=0;jj<nub.size();jj++){
	                        							
	                        							grp.add("planu"+nub.get(jj));
	                        							
	                        						}
	                        						try{
	                        							JSONObject data = new JSONObject();
	                        							data.put("alert", event.getText().toString());
	                        				            data.put("event", event.getText().toString());
	                        				            data.put("by", shed.getString("username", ""));
	                        				            data.put("date", startDay+"/"+startMonth+1+"/"+startYear);
	                        				            data.put("time", startHour+":"+startMinute);
	                        				            data.put("location", location_name);
	                        				            data.put("latitude", latitude);
	                        				            data.put("longitude", longitude);
	                        				            data.put("chan", "planu"+shed.getString("password", ""));
	                        				            ParsePush push=new ParsePush();	
	                        				            push.setChannels(grp);
	                        				            if(flag==1){
	                        				            	push.setChannel("planu"+new_group.getText().toString());
	                        				            }
	                        							push.setData(data);
	                        							push.sendInBackground(new SendCallback() {
	                        								
	                        								@Override
	                        								public void done(ParseException e) {
	                        									// TODO Auto-generated method stub
	                        									Toast.makeText(GroupEventActivity.this, "Notification has been sent to all members", Toast.LENGTH_LONG).show();
	                        								}
	                        							});
	                        							
	                        						}
	                        						catch(Exception e){
	                        							
	                        						}
	                        						
	                        			            
	                        						
	                        						Toast.makeText(GroupEventActivity.this, "Successfully Inserted", Toast.LENGTH_LONG).show();
	                        					}
	                        					else{
	                        						
	                        						Toast.makeText(GroupEventActivity.this, "Sorry some problem occured", Toast.LENGTH_LONG).show();
	                        					}

	                                        }
	                                    });
	                            builderInner.show();
	                        }
	                    });
	            builderSingle.show();

				
					
					
								return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	 
 
	
	public void setAlarm(Context context,int smonth,int sday,int syear,int shour,int sminute,int Pnumber,String matter) {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerReceiver.class);
        	intent.putExtra("matter", matter);
        	intent.putExtra("id", Pnumber);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, Pnumber, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // Set the alarm's trigger time to 8:30 a.m.
        calendar.set(Calendar.YEAR, syear);
        calendar.set(Calendar.MONTH, smonth);
        calendar.set(Calendar.DAY_OF_MONTH, sday);
        calendar.set(Calendar.HOUR_OF_DAY, shour);
        calendar.set(Calendar.MINUTE, sminute);
  
         
         
        // Set the alarm to fire at approximately 8:30 a.m., according to the device's
        // clock, and to repeat once a day.
        alarmMgr.set(AlarmManager.RTC_WAKEUP,  
                calendar.getTimeInMillis(), alarmIntent);
        
        // Enable {@code SampleBootReceiver} to automatically restart the alarm when the
        // device is rebooted.
//        ComponentName receiver = new ComponentName(context, SampleBootReceiver.class);
//        PackageManager pm = context.getPackageManager();
//
//        pm.setComponentEnabledSetting(receiver,
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP);           
    }
    // END_INCLUDE(set_alarm)

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if ( requestCode == 1 ) {

	        if ( resultCode == RESULT_OK ) {
	                Uri pickedPhoneNumber = data.getData();
	                // handle the picked phone number in here.
	                Cursor cursor = getContentResolver().query(pickedPhoneNumber, null, null, null, null);
	                cursor.moveToFirst();

	                int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
	                new_group.setText(cursor.getString(phoneIndex));
	                selected_number=cursor.getString(phoneIndex);
	            }
	        }
	    
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		final Calendar c = Calendar.getInstance();
    	  final int mYear = c.get(Calendar.YEAR);
    	  final int mMonth = c.get(Calendar.MONTH);
    	  final int mDay = c.get(Calendar.DAY_OF_MONTH);
    	
    	  int mHour = c.get(Calendar.HOUR_OF_DAY);
          int mMinute = c.get(Calendar.MINUTE);
          
		if(v==date){
		   
      	  DatePickerDialog dpd = new DatePickerDialog(GroupEventActivity.this, new OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					// TODO Auto-generated method stub
	
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
					try {
						Date selected=formatter.parse(dayOfMonth+"-"+monthOfYear+"-"+year);
						Date current=formatter.parse(mDay+"-"+mMonth+"-"+mYear);
							if(selected.compareTo(current)<0){
								Toast.makeText(GroupEventActivity.this,"Select a valid date", Toast.LENGTH_LONG).show();
							}
							else{
								startYear=year;
								startDay=dayOfMonth;
								startMonth=monthOfYear;
								
								date.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
							}
					}
					catch (Exception e) {
						// TODO: handle exception
					}
						
					
				}
			}, mYear, mMonth, mDay);
      	  
      	  dpd.getDatePicker().setCalendarViewShown(false);
      	   dpd.show();
		}
		else if(v==time){
			
			   TimePickerDialog tpd=new TimePickerDialog(GroupEventActivity.this,new OnTimeSetListener() {
					
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						// TODO Auto-generated method stub
						
						startHour=hourOfDay;
						startMinute=minute;
						
						time.setText(hourOfDay + ":" + minute);
						
						
					}
				} , mHour, mMinute, true);
	            
	            tpd.show();
			
		}
		else if(v==location){
			
			Intent intent=new Intent(GroupEventActivity.this, MapActivity.class);
				intent.putExtra("id", 1);
    		startActivity(intent);
    		
		}
		
		else if(v==new_group){
			flag=1;
			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
			startActivityForResult(intent, 1);  
		}
	}
	
	public class Item1{
	    public String name,id,phNo,phDisplayName,phType;
	    public boolean isChecked =false;
	}

	
	
	
	private ArrayList<Item1> fetchGroups(){
	    ArrayList<Item1> groupList = new ArrayList<Item1>();
	    String[] projection = new String[]{ContactsContract.Groups._ID,ContactsContract.Groups.TITLE};
	    Cursor cursor = getContentResolver().query(ContactsContract.Groups.CONTENT_URI, 
	            projection, null, null, null);
	    ArrayList<String> groupTitle = new ArrayList<String>();
	    while(cursor.moveToNext()){
	            Item1 Item1 = new Item1();
	            Item1.id = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));
	            String groupName =      cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));
	             
	        if(groupName.contains("Group:"))
	        groupName = groupName.substring(groupName.indexOf("Group:")+"Group:".length()).trim();
	             
	        if(groupName.contains("Favorite_"))
	        groupName = "Favorite";
	              
	        if(groupName.contains("Starred in Android") || groupName.contains("My Contacts"))
	        continue;
	             
	        if(groupTitle.contains(groupName)){
	            for(Item1 group:groupList){
	             if(group.name.equals(groupName)){
	                group.id += ","+Item1.id;
	                groupid.add(Item1.id);
	                break;
	             }
	           }
	        }else{
	          groupTitle.add(groupName);
	          Item1.name = groupName;
	          groupList.add(Item1);
	         }
	             
	    }
	     
	    cursor.close();
	    Collections.sort(groupList,new Comparator<Item1>() {
	        public int compare(Item1 Item11, Item1 Item12) {
	        return Item12.name.compareTo(Item11.name)<0
	                    ?0:-1;
	        }
	    });
	    return groupList;
	}
	
	
	
		
		
	
	public ArrayList<String> getAllNumbersFromGroupId(String navn)
	{
	    String selection = ContactsContract.Groups.DELETED + "=? and " + ContactsContract.Groups.GROUP_VISIBLE + "=?";
	    String[] selectionArgs = { "0", "1" };
	    Cursor cursor = getContentResolver().query(ContactsContract.Groups.CONTENT_URI, null, selection, selectionArgs, null);
	    cursor.moveToFirst();
	    int len = cursor.getCount();

	    ArrayList<String> numbers = new ArrayList<String>();
	    for (int i = 0; i < len; i++)
	    {
	        String title = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));
	        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));

	        if (title.equals(navn))
	        {
	            String[] cProjection = { Contacts.DISPLAY_NAME, GroupMembership.CONTACT_ID };

	            Cursor groupCursor = getContentResolver().query(
	                    Data.CONTENT_URI,
	                    cProjection,
	                    CommonDataKinds.GroupMembership.GROUP_ROW_ID + "= ?" + " AND "
	                            + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + "='"
	                            + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'",
	                    new String[] { String.valueOf(id) }, null);
	            if (groupCursor != null && groupCursor.moveToFirst())
	            {
	                do
	                {

	                    int nameCoumnIndex = groupCursor.getColumnIndex(Phone.DISPLAY_NAME);

	                    String name = groupCursor.getString(nameCoumnIndex);

	                    long contactId = groupCursor.getLong(groupCursor.getColumnIndex(GroupMembership.CONTACT_ID));

	                    Cursor numberCursor = getContentResolver().query(Phone.CONTENT_URI,
	                            new String[] { Phone.NUMBER }, Phone.CONTACT_ID + "=" + contactId, null, null);

	                    if (numberCursor.moveToFirst())
	                    {
	                        int numberColumnIndex = numberCursor.getColumnIndex(Phone.NUMBER);
	                        do
	                        {
	                            String phoneNumber = numberCursor.getString(numberColumnIndex);
	                            numbers.add(phoneNumber);
	                        } while (numberCursor.moveToNext());
	                        numberCursor.close();
	                    }
	                } while (groupCursor.moveToNext());
	                groupCursor.close();
	            }
	            break;
	        }

	        cursor.moveToNext();
	    }
	    cursor.close();

	    return numbers;
	}
	
	class group_adapter extends ArrayAdapter<String>{

		Context con;
		List<String> names;
		List<String> id;
		public group_adapter(Context context, List<String> names,List<String> id) {
			super(context, R.layout.single_group, names);
			// TODO Auto-generated constructor stub
			this.names=names;
			this.id=id;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			LayoutInflater inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView=inflater.inflate(R.layout.single_group, null);
					TextView txt=(TextView) convertView.findViewById(R.id.single_grp_text);
						txt.setText(names.get(position));
				convertView.setId(position);
			return convertView;
		}
		
	}
}
