package com.vc.locationevent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.parse.ParsePush;
import com.parse.ParseUser;
import com.tyczj.extendedcalendarview.CalendarAdapter;
import com.tyczj.extendedcalendarview.Day;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;
import com.tyczj.extendedcalendarview.ExtendedCalendarView.OnDayClickListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.Toast;
import android.widget.CalendarView.OnDateChangeListener;

public class MainActivity extends Activity {

//	CalendarView cv;
	ParseUser currentUser;
	ExtendedCalendarView calendar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		currentUser=ParseUser.getCurrentUser();
		
		calendar = (ExtendedCalendarView)findViewById(R.id.calendar);
		
		calendar.setOnDayClickListener(new OnDayClickListener() {
			
			@Override
			public void onDayClicked(AdapterView<?> adapter, View view, int position,
					long id, Day day) {
				// TODO Auto-generated method stub
					Toast.makeText(MainActivity.this, day.getDay()+"/"+day.getMonth(), Toast.LENGTH_LONG).show();
					
					Intent intent=new Intent(MainActivity.this,MapMarkerActivity.class);
						intent.putExtra("day", day.getDay());
						intent.putExtra("month", day.getMonth()+1);
					startActivity(intent);
			}
		});
		
		calendar.setMonthTextBackgroundColor(Color.RED);
		
//		calendar.setColorForCell(7);
//		cv=(CalendarView) findViewById(R.id.calendarView1);
//		cv.setShowWeekNumber(false);
//		cv.setOnDateChangeListener(new OnDateChangeListener() {
//			
//			
//			@Override
//			public void onSelectedDayChange(CalendarView view, int year, int month,
//					int dayOfMonth) {
//				// TODO Auto-generated method stub
//				Calendar c=Calendar.getInstance();
//				SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy");
//				String strDate = sdf.format(c.getTime());
//				String[] d=strDate.split(":");
//				
//				Toast.makeText(MainActivity.this, "date :-"+d[0]+"/"+d[1]+"/"+d[2]+" \ncurrent day :-"+dayOfMonth+"/"+month+"/"+year, Toast.LENGTH_LONG).show();
//				if(Integer.parseInt(d[0])==dayOfMonth && Integer.parseInt(d[1])==month+1 && Integer.parseInt(d[2])==year)
//				{
//					
//				}
//				else
//				{
//					Intent intent=new Intent(MainActivity.this, ViewActivity.class);
//					startActivity(intent);
//				}
//				
//			}
//		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	
		Calendar c=Calendar.getInstance();
		long millitime=c.getTimeInMillis();
//			cv.setDate(millitime);
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==R.id.mpersonal){
			Intent i=new Intent(MainActivity.this, AddActivity.class);
			startActivity(i);
		}
		else if(item.getItemId()==R.id.mgroup){
			Intent i=new Intent(MainActivity.this, GroupEventActivity.class);
			startActivity(i);
		}
		return super.onOptionsItemSelected(item);
	}
}
