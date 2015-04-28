package com.vc.locationevent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class AddActivity extends Activity implements OnClickListener{

	EditText e1;
	
    Button e2,e3,location;
    
    
    private int pYear;
    private int pMonth;
    private int pDay;
    
    private double latitude,longitude;
    private String location_name;
    static final int DATE_DIALOG_ID = 0;
    private int mHour, mMinute;
    int startYear,startMonth,startDay,startHour,startMinute;
    
    List<String> event_name,locationname,date,time,priority;
    
    Db_adapter db;
    AlarmManager alarmMgr;
    PendingIntent alarmIntent;
	    
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_add);
  	
    	e1=(EditText)findViewById(R.id.ed1);
    	e2=(Button)findViewById(R.id.b6);
    	e3=(Button)findViewById(R.id.b7);
    	
    	location=(Button)findViewById(R.id.tv3);
    	
 
		db=new Db_adapter(AddActivity.this);
		event_name=new ArrayList<String>();
		locationname=new ArrayList<String>();
		date=new ArrayList<String>();
		time=new ArrayList<String>();
		priority=new ArrayList<String>();
		// TODO Auto-generated method stub
		
		 e3.setOnClickListener(this);
	
		 e2.setOnClickListener(this);
		 
		 location.setOnClickListener(this);
		 
		 Bundle b=getIntent().getExtras();
		 	if(b!=null){
		 	
		 	}
		 
    }
    @Override
    public void onClick(View v) {
    	if (v == e3) {
    		 
            // Process to get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
 
 
            
            TimePickerDialog tpd=new TimePickerDialog(AddActivity.this,new OnTimeSetListener() {
				
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					// TODO Auto-generated method stub
					e3.setText(hourOfDay + ":" + minute);
					
					startHour=hourOfDay;
					startMinute=minute;
					
					Toast.makeText(AddActivity.this, hourOfDay+" : "+minute, 10).show();
				}
			} , mHour, mMinute, true);
            
            tpd.show();
    	}
    
    	else if(v==location){
    		
    		Intent intent=new Intent(AddActivity.this, MapActivity.class);
    			intent.putExtra("id", 0);
    		startActivity(intent);
    	}
    		
    	    	
    	else if(v==e2)
    	{
    		  final Calendar c = Calendar.getInstance();
        	  final int mYear = c.get(Calendar.YEAR);
        	  final int mMonth = c.get(Calendar.MONTH);
        	  final int mDay = c.get(Calendar.DAY_OF_MONTH);
        	   
        	  DatePickerDialog dpd = new DatePickerDialog(AddActivity.this, new OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					// TODO Auto-generated method stub
					
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
						try {
							Date selected=formatter.parse(dayOfMonth+"-"+monthOfYear+"-"+year);
							Date current=formatter.parse(mDay+"-"+mMonth+"-"+mYear);
								if(selected.compareTo(current)<0){
									Toast.makeText(AddActivity.this,"Select a valid date", Toast.LENGTH_LONG).show();
								}
								else{
									e2.setText(
						                    new StringBuilder()
						                            // Month is 0 based so add 1
						                    		.append(dayOfMonth).append("/")
						                            .append(monthOfYear+1).append("/")
						                            .append(year).append(""));
									
									startYear=year;
									startMonth=monthOfYear;
									startDay=dayOfMonth;
									Toast.makeText(AddActivity.this, ""+startMonth, Toast.LENGTH_LONG).show();
								}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
				}
			}, mYear, mMonth, mDay);
        	  
        	  dpd.getDatePicker().setCalendarViewShown(false);
        	   dpd.show();

    	}
    	 
     
      
      /** Get the current date */
      final Calendar cal = Calendar.getInstance();
      pYear = cal.get(Calendar.YEAR);
      pMonth = cal.get(Calendar.MONTH);
      pDay = cal.get(Calendar.DAY_OF_MONTH);

    }
    
    

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add, menu);
		return true;
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Bundle b=getIntent().getExtras();
		
		if(b == null)
		{
		}
		else{
			
			if(b.getString("event")==null){

				latitude=b.getDouble("latitude");
				longitude=b.getDouble("longitude");
				location_name=b.getString("location");	
				location.setText(location_name);
			}
			else{
				e1.setText(b.getString("event"));
		 		e2.setText(b.getString("date"));
		 		e3.setText(b.getString("time"));
		 		location.setText(b.getString("location"));
			}
			
	 		
		}
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
	   		final String location=null;
    		final String frmtime=e3.getText().toString();
			final String evntname=e1.getText().toString();
			final String frmdate=e2.getText().toString();
			
			if(evntname.equals("")||frmdate.equals("DATE")||frmtime.equals("TIME"))
			{
				Toast.makeText(AddActivity.this, "fill all details", Toast.LENGTH_LONG).show();
			}
			else
			{
				
				
				final Calendar cal = Calendar.getInstance();
			    int  pYear = cal.get(Calendar.YEAR);
			    int  pMonth = cal.get(Calendar.MONTH)+1;
			    int  pDay = cal.get(Calendar.DAY_OF_MONTH)-1;
			    
			    String datee[]=e2.getText().toString().split("/");
				db.open();
					Cursor cr=db.selection("select * from newevnt");
					Log.d("qry", "select * from newevnt");
					while(cr.moveToNext()){
						
						String array[]=cr.getString(2).split("/");
//						5/5/2015 --4/6/2015
						Log.d("dates", datee[0]+"/"+datee[1]+"/"+datee[2]+" --"+Integer.parseInt(array[0])+"/"+array[1]+"/"+array[2]);
						if(Integer.parseInt(datee[0])==Integer.parseInt(array[0]) && Integer.parseInt(datee[1])==Integer.parseInt(array[1]) && Integer.parseInt(datee[2])==Integer.parseInt(array[2])){
							event_name.add(cr.getString(1));
							date.add(cr.getString(2));
							time.add(cr.getString(3));
							locationname.add(cr.getString(4));
							priority.add(cr.getString(5));
							
						}
					}
					db.close();
					if(!event_name.isEmpty()){
						final Dialog listDialog= new Dialog(this);
						
						listDialog.setTitle("Events on the same day");
				        
						LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				        View v = li.inflate(R.layout.alert_list, null);
				         
				        listDialog.setCancelable(true);
				         ListView list1 = (ListView) v.findViewById(R.id.alert_list_view);
				         Button bt1=(Button) v.findViewById(R.id.alert_dismiss);
				         bt1.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								listDialog.dismiss();
								
								AlertDialog.Builder builderSingle = new AlertDialog.Builder(
					                    AddActivity.this);
					            builderSingle.setIcon(R.drawable.ic_launcher);
					            builderSingle.setTitle("Select One Name:-");
					            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
					                    AddActivity.this,
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
					                                    AddActivity.this);
					                            builderInner.setMessage(strName);
					                            builderInner.setTitle("Selected priority");
					                            builderInner.setPositiveButton("Ok",
					                                    new DialogInterface.OnClickListener() {

					                                        @Override
					                                        public void onClick(
					                                                DialogInterface dialog,
					                                                int which) {
					                                            dialog.dismiss();
					                                            
					                                            db.open();
					                    						long i=db.insert_data(evntname,frmdate, frmtime, location,latitude+"",longitude+"",strName);
					                    						db.close();
					                    			    		if(i>0)
					                    			    		{
					                    			    			setAlarm(AddActivity.this, startMonth, startDay, startYear, startHour, startMinute, 1, evntname);
					                    			    			
					                    			    			Toast.makeText(AddActivity.this,"insertion success"+startDay+"/"+startMonth, 10).show();
					                    			    		}
					                                        }
					                                    });
					                            builderInner.show();
					                        }
					                    });
					            builderSingle.show();
							}
						});
				         list1.setAdapter(new alert_list_adapter(AddActivity.this, event_name, time, locationname));
				         
				         listDialog.setContentView(v);
				         
				         //now that the dialog is set up, it's time to show it
				         listDialog.show();
				         
				         
					}
					
					else{
						
						AlertDialog.Builder builderSingle = new AlertDialog.Builder(
			                    AddActivity.this);
			            builderSingle.setIcon(R.drawable.ic_launcher);
			            builderSingle.setTitle("Select One Name:-");
			            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
			                    AddActivity.this,
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
			                                    AddActivity.this);
			                            builderInner.setMessage(strName);
			                            builderInner.setTitle("Selected priority");
			                            builderInner.setPositiveButton("Ok",
			                                    new DialogInterface.OnClickListener() {

			                                        @Override
			                                        public void onClick(
			                                                DialogInterface dialog,
			                                                int which) {
			                                            dialog.dismiss();
			                                            
			                                            db.open();
			                    						long i=db.insert_data(evntname,frmdate, frmtime, location,latitude+"",longitude+"",strName);
			                    						db.close();
			                    			    		if(i>0)
			                    			    		{
			                    			    			setAlarm(AddActivity.this, startMonth, startDay, startYear, startHour, startMinute, 1, evntname);
			                    			    			
			                    			    			Toast.makeText(AddActivity.this,"insertion success"+startDay+"/"+startMonth, 10).show();
			                    			    		}
			                                        }
			                                    });
			                            builderInner.show();
			                        }
			                    });
			            builderSingle.show();

//						db.open();
//						long i=db.insert_data(evntname,frmdate, frmtime, location,latitude+"",longitude+"","3");
//						db.close();
//			    		if(i>0)
//			    		{
//			    			setAlarm(AddActivity.this, startMonth, startDay, startYear, startHour, startMinute, 1, evntname);
//			    			
//			    			Toast.makeText(AddActivity.this,"insertion success", 10).show();
//			    		}
					}
				
				
				
				
			}

			return true;
		}
		else if(id==android.R.id.home)
		{
			Toast.makeText(AddActivity.this, "Back pressed", Toast.LENGTH_LONG).show();
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void setAlarm(Context context,int smonth,int sday,int syear,int shour,int sminute,int Pnumber,String matter) {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerReceiver.class);
        	intent.putExtra("matter", matter);
        	intent.putExtra("id", Pnumber);
        alarmIntent = PendingIntent.getBroadcast(context, Pnumber, intent, 0);

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
	
	class alert_list_adapter extends ArrayAdapter<String>{

		List<String> evt_name,time,location;
		
		public alert_list_adapter(Context context,List<String> evt_name,List<String> time,List<String> location) {
			super(context, R.layout.single_alert_list, location);
			// TODO Auto-generated constructor stub
			this.evt_name=evt_name;
			this.time=time;
			this.location=location;
		}
		
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	          view = li.inflate(R.layout.single_alert_list, null);
	         TextView txt1=(TextView) view.findViewById(R.id.alert_evnt_name);
	         TextView txt2=(TextView) view.findViewById(R.id.alert_location);
	         TextView txt3=(TextView) view.findViewById(R.id.alert_time);
	         	txt1.setText(event_name.get(position));
	         	txt2.setText(location.get(position));
	         	txt3.setText(time.get(position));
			return view;
		}
		
	}
}
