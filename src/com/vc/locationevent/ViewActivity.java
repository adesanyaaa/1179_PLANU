package com.vc.locationevent;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ViewActivity extends Activity {

	ListView lv;
	Db_adapter db;
	List<String> list1,list2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);
		
		lv=(ListView) findViewById(R.id.grp_list);
		list1=new ArrayList<String>();
		list2=new ArrayList<String>();
		
		db=new Db_adapter(ViewActivity.this);
		
		db.open();
		Cursor cr=db.selection("select * from newevnt");
		
		while(cr.moveToNext())
		{
			list1.add(cr.getString(0));
			list2.add(cr.getString(1));
		}
		
		lv.setAdapter(new adapter(ViewActivity.this,list1,list2));
		
		db.close();
		
		
		
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		lv.setAdapter(new adapter(ViewActivity.this,list1,list2));
	}
	class adapter extends ArrayAdapter<String>
	{

		Context con;
		List<String> list1,list2;
		
		public adapter(Context context, List<String> objects,List<String> object) {
			super(context,R.layout.event_list, objects);
			// TODO Auto-generated constructor stub
			
			con=context;
			list1=objects;
			list2=object;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			LayoutInflater inflater=(LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView=inflater.inflate(R.layout.event_list, null);
				TextView tv=(TextView) convertView.findViewById(R.id.event_title);
				TextView tv1=(TextView) convertView.findViewById(R.id.event_date);
				
				tv.setText(list1.get(position));
				tv1.setText(list2.get(position));
			return convertView;
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view, menu);
		return true;
	}
	
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent=new Intent(ViewActivity.this, AddActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
