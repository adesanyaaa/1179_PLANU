package com.vc.locationevent;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MapMarkerActivity extends Activity implements OnMarkerClickListener{

	GoogleMap mMap;
	int day,month,year,hour,minute;
	List<String> latitude,longitude,event_name,location_name,date,time,priority,ids,type_evnt;
	Db_adapter db;
	LatLng src;
	ListView lv;
	GPSTracker gps;
	map_list_adapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expandable);
		
		ids=new ArrayList<String>();
		type_evnt=new ArrayList<String>();
		latitude=new ArrayList<String>();
		longitude=new ArrayList<String>();
		event_name=new ArrayList<String>();
		location_name=new ArrayList<String>();
		date=new ArrayList<String>();
		time=new ArrayList<String>();
		priority=new ArrayList<String>();
		db=new Db_adapter(MapMarkerActivity.this);
		gps=new GPSTracker(MapMarkerActivity.this);
		MapFragment fragment=(MapFragment) getFragmentManager().findFragmentById(R.id.all_map);
		lv=(ListView) findViewById(R.id.map_listg);
		
			mMap=fragment.getMap();
			
			mMap.setMyLocationEnabled(true);
			
		Bundle b=getIntent().getExtras();
			day=b.getInt("day");
			month=b.getInt("month");
			year=2015;
//			hour=b.getString("hour");
//			minute=b.getString("minute");
		
			
			
			mMap.setOnMarkerClickListener(this);
			src=new LatLng(gps.getLatitude(),gps.getLongitude());
			
			
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						final int position, long id) {
					
					AlertDialog.Builder builderSingle = new AlertDialog.Builder(
		                    MapMarkerActivity.this);
		            builderSingle.setIcon(R.drawable.ic_launcher);
		            builderSingle.setTitle("Changes :-");
					
		            builderSingle.setNegativeButton("Delete",
		                    new DialogInterface.OnClickListener() {

		                        @Override
		                        public void onClick(DialogInterface dialog, int which) {
		
		                        	
		                        	db.open();
		                        	if(type_evnt.get(position).equals("personal")){
		                        		db.delete(ids.get(position)+"","newevnt");
		                        	}
		                        	else{
		                        		db.delete(ids.get(position)+"","grp_evt");
		                        	}
		                        		
		                        		cancellation(Integer.parseInt(ids.get(position)));
		                        	db.close();
		                        	event_name.remove(position);
		                        	date.remove(position);
		                        	time.remove(position);
		                        	adapter.notifyDataSetChanged();
		                        }
		                        
		                    });

		            builderSingle.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent i=null;
							db.open();
							if(type_evnt.get(position).equals("personal")){
                        		db.delete(ids.get(position)+"","newevnt");
                        		i=new Intent(MapMarkerActivity.this,AddActivity.class);
                            	
                        		i.putExtra("event", event_name.get(position));
                        		i.putExtra("date", date.get(position));
                        		i.putExtra("time", time.get(position));
                        		i.putExtra("location", location_name.get(position));
                        	
                        	}
                        	else{
                        		db.delete(ids.get(position)+"","grp_evt");
                        		i=new Intent(MapMarkerActivity.this,GroupEventActivity.class);
                            	
                        		i.putExtra("event", event_name.get(position));
                        		i.putExtra("date", date.get(position));
                        		i.putExtra("time", time.get(position));
                        		i.putExtra("location", location_name.get(position));
                        	
                        	}
                    		cancellation(Integer.parseInt(ids.get(position)));
                    	db.close();
                    	
                    			
                    	event_name.remove(position);
                    	date.remove(position);
                    	time.remove(position);
                    	adapter.notifyDataSetChanged();
                    	
                    	startActivity(i);
						}
					});
		            builderSingle.show();
				}
			});
		}
	
	public void cancellation(int id){
		Intent myIntent = new Intent(MapMarkerActivity.this, AlarmManagerReceiver.class);
		  PendingIntent pendingIntent = PendingIntent.getActivity(MapMarkerActivity.this,
		       id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		   pendingIntent.cancel();
		AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);   
		alarmMgr.cancel(pendingIntent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.expandable, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId()==R.id.personal){
			Intent i=new Intent(MapMarkerActivity.this, AddActivity.class);
			startActivity(i);
			finish();
		}
		else if(item.getItemId()==R.id.group){
			Intent i=new Intent(MapMarkerActivity.this, GroupEventActivity.class);
			startActivity(i);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		
//		ids.clear();
//		event_name.clear();
//		date.clear();
//		time.clear();
//		location_name.clear();
//		priority.clear();
//		latitude.clear();
//		longitude.clear();
		
		final Calendar cal = Calendar.getInstance();
	    int  pYear = cal.get(Calendar.YEAR);
	    int  pMonth = cal.get(Calendar.MONTH)+1;
	    int  pDay = cal.get(Calendar.DAY_OF_MONTH)-1;
		db.open();
			Cursor cr=db.selection("select * from newevnt");
			Log.d("qry", "select * from newevnt");
			while(cr.moveToNext()){
				
				String array[]=cr.getString(2).split("/");
//				5/5/2015 --4/6/2015
				Log.d("dates", pDay+"/"+pMonth+"/"+pYear+" --"+Integer.parseInt(array[0])+"/"+array[1]+"/"+array[2]);
				if(day==Integer.parseInt(array[0]) && month==Integer.parseInt(array[1]) && year==Integer.parseInt(array[2])){
					ids.add(cr.getInt(0)+"");
					type_evnt.add("personal");
					event_name.add(cr.getString(1));
					date.add(cr.getString(2));
					time.add(cr.getString(3));
					location_name.add(cr.getString(4));
					priority.add(cr.getString(5));
					latitude.add(cr.getString(6));
					longitude.add(cr.getString(7));
				}
				
				
			}
		db.close();
		
		db.open();
		Cursor c=db.selection("select * from grp_evt");
		Log.d("qry", "select * from grp_evt");
		while(c.moveToNext()){
			
			String array[]=c.getString(2).split("/");
//			5/5/2015 --4/6/2015
			Log.d("dates", pDay+"/"+pMonth+"/"+pYear+" --"+Integer.parseInt(array[0])+"/"+array[1]+"/"+array[2]);
			if(day==Integer.parseInt(array[0]) && month==Integer.parseInt(array[1]) && year==Integer.parseInt(array[2])){
				ids.add(c.getInt(0)+"");
				type_evnt.add("group");
				event_name.add(c.getString(1));
				date.add(c.getString(2));
				time.add(c.getString(3));
				location_name.add(c.getString(4));
				priority.add(c.getString(5));
				latitude.add(c.getString(6));
				longitude.add(c.getString(7));
			}
			
			
		}
	db.close();
		if(event_name.isEmpty()){
			Toast.makeText(MapMarkerActivity.this, "No events", Toast.LENGTH_LONG).show();
		}
		adapter=new map_list_adapter(MapMarkerActivity.this, event_name, date, time);
		lv.setAdapter(adapter);
		
		for(int i=0;i<event_name.size();i++){
			MarkerOptions option;
			LatLng demo=new LatLng(Double.valueOf(latitude.get(i)),Double.valueOf(longitude.get(i)));
			if(Integer.parseInt(priority.get(i))>4){
				option=new MarkerOptions()
	            .position(demo)
	            .icon(BitmapDescriptorFactory
	                    .defaultMarker(BitmapDescriptorFactory.HUE_RED))
	            .title(""+event_name.get(i));
			}
			else if(Integer.parseInt(priority.get(i))>3 && Integer.parseInt(priority.get(i))<5){
				option=new MarkerOptions()
	            .position(demo)
	            .icon(BitmapDescriptorFactory
	                    .defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
	            .title(""+event_name.get(i));
				
			}
			
			else if(Integer.parseInt(priority.get(i))>2 && Integer.parseInt(priority.get(i))<4){
				option=new MarkerOptions()
	            .position(demo)
	            .icon(BitmapDescriptorFactory
	                    .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
	            .title(""+event_name.get(i));
				
			}
			
			else if(Integer.parseInt(priority.get(i))>1 && Integer.parseInt(priority.get(i))<3){
				option=new MarkerOptions()
	            .position(demo)
	            .icon(BitmapDescriptorFactory
	                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
	            .title(""+event_name.get(i));
				
			}
			else{
				option=new MarkerOptions()
	            .position(demo)
	            .icon(BitmapDescriptorFactory
	                    .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
	            .title(""+event_name.get(i));
				
			}
			
			Marker marker=mMap.addMarker(option);
//			marker.showInfoWindow();
			
//			mMap.addMarker(new MarkerOptions()
//            .position(demo)
//            .icon(BitmapDescriptorFactory
//                    .defaultMarker(BitmapDescriptorFactory.HUE_RED))
//            .title(""+event_name.get(i)));
			
		}
	}
	
	class map_list_adapter extends ArrayAdapter<String>{

		List<String> names,dates,times;
		Context con;
		public map_list_adapter(Context context,List<String> name,List<String> date,List<String> time) {
			super(context,R.layout.singe_map_event, name);
			// TODO Auto-generated constructor stub
			con=context;
			names=name;
			dates=date;
			times=time;
		}
		
		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
		}
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater=(LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view=inflater.inflate(R.layout.singe_map_event, null);
				TextView txt1=(TextView) view.findViewById(R.id.map_evt_name);
				TextView txt2=(TextView) view.findViewById(R.id.map_date);
				TextView txt3=(TextView) view.findViewById(R.id.map_time);
					txt1.setText(names.get(position));
					txt2.setText(dates.get(position));
					txt3.setText(times.get(position));
				view.setId(position);
			return view;
		}
		
	}
	
	
	private String getDirectionsUrl(LatLng origin,LatLng dest){
		 
        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;
 
        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;
 
        // Sensor enabled
        String sensor = "sensor=false";
 
        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;
 
        // Output format
        String output = "json";
 
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
 
        return url;
    }
	
	
	/** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
 
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
 
            // Connecting to url
            urlConnection.connect();
 
            // Reading data from url
            iStream = urlConnection.getInputStream();
 
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
 
            StringBuffer sb = new StringBuffer();
 
            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }
 
            data = sb.toString();
 
            br.close();
 
        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    

 // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{
 
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
 
            // For storing data from web service
            String data = "";
 
            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }
 
        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
 
            ParserTask parserTask = new ParserTask();
 
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }
    
    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
 
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
 
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
 
            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionJSONParser parser = new DirectionJSONParser();
 
                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }
 
        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
 
            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();
 
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
 
                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);
 
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
 
                    points.add(position);
                }
 
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }
 
            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }


	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		Toast.makeText(MapMarkerActivity.this, "marker clicked ", Toast.LENGTH_LONG).show();
		LatLng dest=marker.getPosition();
		marker.showInfoWindow();
		String url = getDirectionsUrl(src, dest);
		 
        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
		return true;
	}

}
