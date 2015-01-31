package com.abhpan91.emergencyhelper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;

public class phoneblock extends SQLiteOpenHelper{
	 private static final int DATABASE_VERSION = 8;
	 
	    // Database Name
	    private static final String DATABASE_NAME = "blocking";
	 
	    // Contacts table name
	    private static final String TABLE_CONTACTS = "HELPERS";
	 
	    // Contacts Table Columns names
	     static final String KEY_ID = "_id";
	     static final String KEY_NAME = "name";
	     static final String KEY_PH_NO = "phone_number";

	public phoneblock(Context context) {
		super(context, DATABASE_NAME, null,DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("creating table", "table created");
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT UNIQUE ON CONFLICT REPLACE NOT NULL" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
		 
        // Create tables again
        onCreate(db);
		
	}
	public void addContact(person contact) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(KEY_NAME, contact.getName()); // Contact Name
	    values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone Number
	 
	    // Inserting Row
	    db.insert(TABLE_CONTACTS, null, values);
	    db.close(); // Closing database connection
	}
	 public Cursor getAllContacts() {
		    
		    // Select All Query
		 SQLiteDatabase mdb = this.getWritableDatabase();
		 Cursor mCursor = mdb.query(TABLE_CONTACTS, new String[] {KEY_ID,
				     KEY_NAME, KEY_PH_NO}, 
				    null, null, null, null, null);
				 
				  if (mCursor != null) {
				   mCursor.moveToFirst();
				  }
				  mdb.close();
				  return mCursor;
		}
	 
	    // Deleting single contact
	public void deleteContact(String contact) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_CONTACTS, KEY_PH_NO + " = ?",
	            new String[] { contact });
	    db.close();
	}
	public boolean exists(String number) {
	    SQLiteDatabase db = this.getReadableDatabase();
	 
	    Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
	            KEY_NAME, KEY_PH_NO }, KEY_PH_NO + "=?",
	            new String[] { number }, null, null, null, null);
	    db.close();
	    if (cursor != null)
	    	{cursor.close();
	        return true;}
         else
        	 {cursor.close();
        	 return false;}}
	public ArrayList<String> getallnumbers(){
		ArrayList<String> numbers=new ArrayList<String>();
		SQLiteDatabase db = this.getReadableDatabase();
		 
	    Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_PH_NO }, null, null, null, null, null);
		
	    cursor.moveToFirst();
		if(cursor!=null){
		while(cursor.isAfterLast()==false){
			
			numbers.add(cursor.getString(0));
			
			
		}}
		db.close();
		cursor.close();
		return numbers;
		
		
		
	}
	
		
}

