package com.vc.locationevent;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;

public class Db_adapter extends SQLiteOpenHelper 
{

	SQLiteDatabase db;
	
	public Db_adapter(Context context) {
		
		super(context, "planu2", null, 1);
		// TODO Auto-generated constructor stub
		
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		String qry="create table newevnt(id INTEGER PRIMARY KEY AUTOINCREMENT,evntname VARCHAR(30),frmdate String ,frmtime String,location String,priority String,latitude String,longitude String)";
		String grp_qry="create table grp_evt(id INTEGER PRIMARY KEY AUTOINCREMENT,eventname VARCHAR(20),fromdate VARCHAR(20),fromtime VARCHAR(20),location VARCHAR(20),priority VARCHAR(20),latitude VARCHAR(20),longitude VARCHAR(20),grp_id String)";
		String grp_tbl="create table grp_table(id INTEGER PRIMARY KEY AUTOINCREMENT,name String)";
		String grp_members="create table grp_members(mid String,name String,phone_no String,id INTEGER)";
		
		db.execSQL(qry);
		db.execSQL(grp_qry);
		db.execSQL(grp_tbl);
		db.execSQL(grp_members);
		
	}
	
	void open()
	{
		db=getWritableDatabase();
	}
	
	public void close()
	{
		db.close();
	}
	
	long insert_data(String evntname,String frmdate,String frmtime,String location,String latitude,String longitude,String priority)

	{
		ContentValues values=new ContentValues();
		values.put("evntname", evntname);
		values.put("frmdate", frmdate);
		values.put("frmtime", frmtime);
		values.put("location", location);
		values.put("priority", priority);
		values.put("latitude", latitude);
		values.put("longitude", longitude);
		long i=db.insert("newevnt", null, values);
		
		return i;
	}
	
	long insert_grp_event(String evntname,String frmdate,String frmtime,String location,String latitude,String longitude,String grp_id,String p){
		
		ContentValues values=new ContentValues();
		values.put("eventname", evntname);
		values.put("fromdate", frmdate);
		values.put("fromtime", frmtime);
		values.put("location", location);
		values.put("priority", p);
		values.put("latitude", latitude);
		values.put("longitude", longitude);
		values.put("grp_id", grp_id);
		long i=db.insert("grp_evt", null, values);
		
		return i;
	}
	
	long insert_grps(String name){
		ContentValues values=new ContentValues();
			
			values.put("name", name);
		long i=db.insert("grp_table", null, values);
		
		return i;
		
	}
	
	long insert_grp_members(String mid,String name,String id,String phone_no){
		
		ContentValues values=new ContentValues();
			values.put("mid", mid);
			values.put("name", name);
			values.put("phone_no", phone_no);
			values.put("id", id);
		long i=db.insert("grp_members", null, values);
		return i;
	}

	Cursor selection(String qry)
	{
		Cursor cr=db.rawQuery(qry,null);
		
		return cr;
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean  delete(String id,String table){
		
		return db.delete(table, "id="+id, null)>0;
		
	}

}
