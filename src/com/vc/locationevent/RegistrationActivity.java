package com.vc.locationevent;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SignUpCallback;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class RegistrationActivity extends Activity {

	EditText edt1,edt2;
	Button btn;
	ParseUser parse;
	ProgressBar pb;
	SharedPreferences shed;
	Editor edt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		
		shed=getSharedPreferences("planu", Context.MODE_PRIVATE);
		edt=shed.edit();
		
		edt1=(EditText) findViewById(R.id.reg_name);
		edt2=(EditText) findViewById(R.id.reg_phno);
		btn=(Button) findViewById(R.id.reg_btn);
		pb=(ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.GONE);
		
		if(shed.getString("username", "").equals("")){
			
		}
		else{
//			ParseAnalytics.trackAppOpened(getIntent());
//			ParsePush.subscribeInBackground("y"+shed.getString("password", ""));
			Toast.makeText(getApplicationContext(), "Password "+shed.getString("password", ""), Toast.LENGTH_LONG).show();
			PushService.subscribe(getApplicationContext(), "planu"+shed.getString("password", ""), NotificationActivity.class);
			Intent intent=new Intent(RegistrationActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
			
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btn.setEnabled(false);
				edt1.setEnabled(false);
				edt2.setEnabled(false);
				pb.setVisibility(View.VISIBLE);
				parse=new ParseUser();
					parse.setUsername(edt1.getText().toString());
					parse.setPassword(edt2.getText().toString());
						edt.putString("username", edt1.getText().toString());
						edt.putString("password", edt2.getText().toString());
						edt.commit();
				parse.signUpInBackground(new SignUpCallback() {
					
					@Override
					public void done(ParseException e) {
						// TODO Auto-generated method stub
						pb.setVisibility(View.VISIBLE);
						PushService.subscribe(getApplicationContext(), "planu"+edt2.getText().toString(), MainActivity.class);
						Intent intent=new Intent(RegistrationActivity.this, MainActivity.class);
							startActivity(intent);
							finish();
					}
				});
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
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
}
