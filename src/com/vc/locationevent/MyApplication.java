package com.vc.locationevent;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;

import com.parse.ParseInstallation;

import com.parse.ParseUser;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

     // Add your initialization code here
        Parse.initialize(this, "PXqiY9eEkoI2EPMM0QX7cPfvjHAJuVsv0ZzAmt0N", "Nn7MdmosKMw3H7KzhwKF3TXImGoQdfSGzOtISjiw");

        //Enable to receive push
        PushService.setDefaultPushCallback(this, NotificationActivity.class);
        ParseInstallation pi = ParseInstallation.getCurrentInstallation();
        
        //Register a channel to test push channels
        Context ctx = this.getApplicationContext();
        PushService.subscribe(ctx, "planu", NotificationActivity.class);
        
        pi.saveEventually();

        
        
    }
}
