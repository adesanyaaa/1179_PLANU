//package com.vc.locationevent;
//
//import android.content.Context;
//import android.content.Intent;
//import android.widget.Toast;
//
//import com.parse.ParsePushBroadcastReceiver;
//
//public class parseReciever extends ParsePushBroadcastReceiver{
//	
//	@Override
//	protected void onPushOpen(Context context, Intent intent) {
//		// TODO Auto-generated method stub
//		super.onPushOpen(context, intent);
//		
//		Intent i=new Intent(context,GroupEventActivity.class);
//		i.putExtras(intent.getExtras());
//		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			context.startActivity(i);
//	}
//	
//	@Override
//	protected void onPushReceive(Context context, Intent intent) {
//		// TODO Auto-generated method stub
//		super.onPushReceive(context, intent);
//		
//		Intent i=new Intent(context,GroupEventActivity.class);
//		i.putExtras(intent.getExtras());
//		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			context.startActivity(i);
//	}
//	
//	
//
//}
