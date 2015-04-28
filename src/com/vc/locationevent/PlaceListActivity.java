package com.vc.locationevent;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class PlaceListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.place_list, menu);
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
	
	class webservice extends AsyncTask<String, String, String>
	{
		
		// GPS Location
	    GPSTracker gps;
	    
	    String email;
	    
	    

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
//			Toast.makeText(Login.this, "webservice", 10).show();
			// creating GPS Class object
	        
		}
		@Override
		protected String doInBackground(String... params) {
			
			
//			Request request=Request.newMeRequest(session, callback)
			RestClient client=new RestClient("https://maps.googleapis.com/maps/api/place/textsearch/xml?query=Muvatupuzha & key=AddYourOwnKeyHere");
		
		try {

			client.Execute(com.vc.locationevent.RestClient.RequestMethod.POST);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			
			super.onPostExecute(result);
			
			
		}

		
		
	}

}
