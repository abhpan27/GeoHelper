package com.abhpan91.emergencyhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class messagedb extends SQLiteOpenHelper{
	 private static final int DATABASE_VERSION = 8;
	 
	    // Database Name
	    private static final String DATABASE_NAME = "messagelog";
	 
	    // Contacts table name
	    private static final String TABLE_CONTACTS = "MESSAGE";
	 
	    // Contacts Table Columns names
	    static final String KEY_ID = "_id";
	     static final String KEY_MESSAGE = "name";
	     static final String KEY_PH_NO = "phone_number";
	     static final String KEY_TIME="time";


	public messagedb(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.d("creating table", "table created");
		 String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
	                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_MESSAGE + " TEXT,"
	                + KEY_PH_NO+" TEXT,"  + KEY_TIME + " TEXT" + ")";
		 Log.d("msg", "table created");
	        db.execSQL(CREATE_CONTACTS_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
		 
        // Create tables again
        onCreate(db);
		
		
	}
	public void insertmessage(String message,String number,String time){
		
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
	    values.put(KEY_MESSAGE, message); // Contact Name
	    values.put(KEY_TIME, time);
	    values.put(KEY_PH_NO, number);// Contact Phone Number
	 
	    // Inserting Row
	    db.insert(TABLE_CONTACTS, null, values);
	    db.close();
	    }
	 public Cursor getAllContacts() {
		    
		    // Select All Query
		 SQLiteDatabase mdb = this.getWritableDatabase();
		 Cursor mCursor = mdb.query(TABLE_CONTACTS, new String[] {KEY_ID,
				     KEY_MESSAGE, KEY_PH_NO,KEY_TIME}, 
				    null, null, null, null, KEY_ID+" DESC");
				 
				  if (mCursor != null) {
				   mCursor.moveToFirst();
				  }
				  mdb.close();
				  return mCursor;
		}
	 public void deleteall(){
		 
		 SQLiteDatabase db = this.getWritableDatabase();
		 db.execSQL("delete from "+ TABLE_CONTACTS);
		 db.close();
	 }
	 
		
		
		
	

}
