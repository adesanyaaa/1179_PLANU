package com.vc.locationevent;

import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MapActivity extends Activity implements OnMapClickListener{

	GoogleMap mMap;
	AlertDialog a=null;
	String location_name="";
	
	int id=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		Bundle b=getIntent().getExtras();
		
		id=b.getInt("id");
		
		MapFragment fragment=(MapFragment) getFragmentManager().findFragmentById(R.id.map);
		
		mMap=fragment.getMap();
		
		mMap.setMyLocationEnabled(true);
		
		mMap.setOnMapClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onMapClick(LatLng point) {
		// TODO Auto-generated method stub
		
		final double lat=point.latitude;
		final double lng=point.longitude;
		
		
		Geocoder geocoder = new Geocoder(MapActivity.this, Locale.getDefault());
        String result = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(
                    lat, lng, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                location_name=address.getLocality();	
                
            }
        } catch (Exception e) {
            Log.e("Geocoder exception", "Unable connect to Geocoder", e);
        } 
//    	mMap.addMarker(new MarkerOptions()
//        .position(new LatLng(lat, lng))
//        .title(""+location_name));
    	
    	
    	AlertDialog.Builder adb = new AlertDialog.Builder(this);

	    adb.setTitle("You have selected "+location_name);


	    adb.setIcon(android.R.drawable.ic_dialog_alert);


	    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	   	
	        	if(id==0){
	        		Intent intent=new Intent(MapActivity.this, AddActivity.class);
	        			intent.putExtra("latitude", lat);
	        			intent.putExtra("longitude", lng);
	        			intent.putExtra("location", location_name);
	        		startActivity(intent);
	        	}
	        	else{
	        		Intent intent=new Intent(MapActivity.this, GroupEventActivity.class);
	        			intent.putExtra("latitude", lat);
	        			intent.putExtra("longitude", lng);
	        			intent.putExtra("location", location_name);
	        		startActivity(intent);
	        	}
	        	
	        	
	      } });


	    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	        	a.dismiss();

	      } });
		a=adb.create();    
	    adb.show();
    	
    	
	}
}
