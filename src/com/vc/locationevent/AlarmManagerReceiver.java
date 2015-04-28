package com.vc.locationevent;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlarmManagerReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		String text=arg1.getExtras().getString("matter");
		int mId=arg1.getExtras().getInt("id");
		NotificationCompat.Builder notification=new NotificationCompat.Builder(arg0).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("PlanU Reminder")
				.setContentText(text);
		NotificationManager mNotificationManager =
			    (NotificationManager) arg0.getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
			mNotificationManager.notify(mId, notification.build());
	}

}
