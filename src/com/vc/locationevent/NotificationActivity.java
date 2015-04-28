package com.vc.locationevent;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseInstallation;
import com.parse.ParsePush;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationActivity extends Activity {

	Db_adapter db;
    AlarmManager alarmMgr;
    
    TextView event,date,time,user,location;
    Button accept,decline;
    SharedPreferences shed;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
		db=new Db_adapter(getApplicationContext());
		shed=getSharedPreferences("planu", Context.MODE_PRIVATE);
		event=(TextView) findViewById(R.id.noti_name);
		date=(TextView) findViewById(R.id.noti_date);
		time=(TextView) findViewById(R.id.noti_time);
		location=(TextView) findViewById(R.id.noti_loc);
		user=(TextView) findViewById(R.id.noti_by);
		accept=(Button) findViewById(R.id.accept_noti);
		decline=(Button) findViewById(R.id.decline_noti);
		
		
		Bundle b=getIntent().getExtras();
		
		
		Log.d("data", b.getString("com.parse.Data"));
		try {
			
				String data=b.getString("com.parse.Data");
				JSONObject object=new JSONObject(data);
				final String evnt=object.getString("event");
				final String dt=object.getString("date");
				final String tm=object.getString("time");
				String by=object.getString("by");
				final String loc=object.getString("location");
				final String lat=object.getString("latitude");
				final String lng=object.getString("longitude");
				final String chan=object.getString("chan");
				event.setText(evnt);
				date.setText("on "+dt);
				time.setText("at "+tm);
				user.setText("by "+by);
				location.setText(loc);
				

			
			Log.d("data", event+"  ----  "+date+"   ----   "+time);
			
			accept.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String array[]=dt.split("/");
					String art[]=tm.split(":");

					db.open();
					long i=db.insert_grp_event(array[0], dt, tm, loc,lat, lng, evnt,"4");
					db.close();
					setAlarm(NotificationActivity.this, (Integer.parseInt(array[1])-1),Integer.parseInt(array[0]) , Integer.parseInt(array[2]), Integer.parseInt(art[0]), Integer.parseInt(art[1]), (int)i, evnt);
					JSONObject obj=new JSONObject();
					try {
						
						obj.put("accept", ""+1);
						obj.put("alert", shed.getString("username", "")+" accepted");
						ParsePush push=new ParsePush();
						push.setChannel(chan);
						push.setData(obj);
						push.sendInBackground();
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
					Toast.makeText(NotificationActivity.this, "You set the alarm", Toast.LENGTH_LONG).show();
				}
			});
			
			decline.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
						try {
							JSONObject obj=new JSONObject();
							obj.put("alert", shed.getString("username", "")+" cancelled");
							obj.put("accept", ""+0);
							ParsePush push=new ParsePush();
							push.setChannel(chan);
							push.setData(obj);
							push.sendInBackground();
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
					Toast.makeText(NotificationActivity.this, "You cancelled the event", Toast.LENGTH_LONG).show();
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
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

}
