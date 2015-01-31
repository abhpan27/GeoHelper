package com.abhpan91.emergencyhelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class log extends Activity implements OnClickListener {

	String[] columns;
	ArrayList<person> checkedstr;
	phoneblock ph = new phoneblock(this);
	 private ArrayList<Boolean> itemChecked = new ArrayList<Boolean>();
	ListView lv;
	int numbe;
	int type;
	int date;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		String strorder=android.provider.CallLog.Calls.DEFAULT_SORT_ORDER;
		@SuppressWarnings("deprecation")
		Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,
				null, null, strorder);
		startManagingCursor(managedCursor);
		Log.d("here", "got here");
		numbe = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
		type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
		date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
		checkedstr = new ArrayList<person>();
		checkedstr=new ArrayList<person>();
        
        int j=(managedCursor.getCount());
	
	for(int i=0;i<j;i++)
	{
		itemChecked.add(i, false);
	}

		setContentView(R.layout.phonelist);
		lv = (ListView) findViewById(R.id.phonelist);

		myadapter my = new myadapter(this, managedCursor);
		lv.setAdapter(my);
		Button bt = (Button) findViewById(R.id.done);
		bt.setOnClickListener(this);

	}

	public class myadapter extends CursorAdapter {

		LayoutInflater inflater;

		public myadapter(Context context, Cursor c) {
			super(context, c);
			inflater = LayoutInflater.from(context);
			// TODO Auto-generated constructor stub
		}

		@SuppressWarnings("deprecation")
		@Override
		public void bindView(View arg0, Context arg1, Cursor arg2) {
			// TODO Auto-generated method stub
			final int pos = arg2.getPosition();
			TextView callnumber = (TextView) arg0.findViewById(R.id.textView1);
			TextView calltype = (TextView) arg0.findViewById(R.id.textView2);

			TextView calltime = (TextView) arg0.findViewById(R.id.textView4);
			String callDate = arg2.getString(date);
			Date callDayTime = new Date(Long.valueOf(callDate));
			final String nm = arg2.getString(numbe);

			String callType = arg2.getString(type);
			CheckBox chk = (CheckBox) arg0.findViewById(R.id.checkBox1);
			String dir = null;
			int dircode = Integer.parseInt(callType);
			switch (dircode) {
			case CallLog.Calls.OUTGOING_TYPE:
				dir = "OUTGOING";
				break;

			case CallLog.Calls.INCOMING_TYPE:
				dir = "INCOMING";
				break;

			case CallLog.Calls.MISSED_TYPE:
				dir = "MISSED";
				break;
			}
			final String num = dir;
        if(getname(nm)!=null)
			callnumber.setText(getname(nm));
        else
        	callnumber.setText(nm);
			calltype.setText("" + "(" + num + ")");

			calltime.setText(callDayTime.toLocaleString());

			chk.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					CheckBox chk = (CheckBox) v.findViewById(R.id.checkBox1);
					if (chk.isChecked()) {
						if(getname(nm)!=null)
						checkedstr.add(new person(getname(nm), nm));
						else
							checkedstr.add(new person("From logs", nm));
						itemChecked.set(pos, true);
						
					} else if (!chk.isChecked()) {
						{

							for (Iterator<person> it = checkedstr.iterator(); it
									.hasNext();) {
								person pr = it.next();
								if (pr.getPhoneNumber().equals(num)) {
									it.remove();
								}
							}
						}
						itemChecked.set(pos, false);
						Log.d("removing", "" + num + "from" + pos);

					}
				}
			});
			chk.setChecked(itemChecked.get(pos));
		}

		@Override
		public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			return inflater.inflate(R.layout.logrow, arg2, false);

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		boolean bb=false;
		if(sendmessage.servicerunning)
		{stopService(new Intent(log.this,sendmessage.class));
			bb=true;
			}
		phoneblock pb = new phoneblock(this);
		if (v.getId() == R.id.done) {
			boolean b=false;

			for (person str : checkedstr) {
                 b=true;
				pb.addContact(str);
			}
             if(b)
			Toast.makeText(getApplicationContext(),
					"numbers added to helper list", Toast.LENGTH_LONG).show();
             else
            	 Toast.makeText(getApplicationContext(),
     					"nothing added to helper list", Toast.LENGTH_LONG).show();

		}
		if(bb)
		startService(new Intent(log.this,sendmessage.class));
	}
	public String getname(String number) {
		String name=null;
		Cursor cursor=null;
		try{
		   Uri uri=Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
		   cursor=getContentResolver().query(uri,new String[]{PhoneLookup.DISPLAY_NAME},null,null, null);
		   if(cursor.moveToFirst()){
			 name=cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
			   
			   
		   }}
		catch(Exception e){
			
		}
		finally{
			
			 if( cursor != null && !cursor.isClosed() )
			        cursor.close();
		}
		return name;	   
			
		}
}
