package com.abhpan91.emergencyhelper;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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

public class message extends Activity implements OnClickListener {
	String[] columns = new String[] { "address", "thread_id", "date", "body",
			"type" };
	ArrayList<person> checkedstr;
	phoneblock ph = new phoneblock(this);
	ListView lv;
	private ArrayList<Boolean> itemChecked = new ArrayList<Boolean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Uri uri = Uri.parse("content://sms");
		Cursor cursor = getContentResolver().query(
				uri,
				null, null, null, null);
		startManagingCursor(cursor);
		checkedstr = new ArrayList<person>();

		int j = (cursor.getCount());

		for (int i = 0; i < j; i++) {
			itemChecked.add(i, false);
		}

		setContentView(R.layout.phonelist);
		lv = (ListView) findViewById(R.id.phonelist);

		myadapter my = new myadapter(this, cursor);
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
		public void bindView(final View arg0, Context arg1, final Cursor arg2) {
			// TODO Auto-generated method stub
			final int pos = arg2.getPosition();
			TextView name = (TextView) arg0.findViewById(R.id.name);
			TextView number = (TextView) arg0.findViewById(R.id.number);

			final String nm = arg2.getString(arg2.getColumnIndex(columns[0]));

			final String num = arg2.getString(arg2.getColumnIndex(columns[4]));

			CheckBox chk = (CheckBox) arg0.findViewById(R.id.checkBox);
			final String use = getname(nm);
			if (use != null)
				name.setText(use);
			else
				name.setText(nm);
			int type = Integer.parseInt(num);
			String display = null;
			switch (type) {
			case 1:
				display = "Inbox";
				break;
			case 2:
				display = "sent";
				break;
			case 3:
				display = "Draft";
				break;
			case 4:
				display = "outbox";
				break;
			case 5:
				display = "failed";
				break;
			case 6:
				display = "Queued";
				break;

			}

			number.setText(display);

			chk.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					CheckBox chk = (CheckBox) arg0.findViewById(R.id.checkBox);
					if (chk.isChecked()) {
						if (use != null)
							checkedstr.add(new person(use, nm));
						else
							checkedstr.add(new person("From messagelog", nm));
						itemChecked.set(pos, true);
						Log.d("adding", "" + num + "at" + pos + "");
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
			return inflater.inflate(R.layout.row, arg2, false);

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		boolean bb=false;
		if(sendmessage.servicerunning)
		{stopService(new Intent(message.this,sendmessage.class));
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
		startService(new Intent(message.this,sendmessage.class));
	}

	public String getname(String number) {
		String name = null;
		Cursor cursor = null;
		try {
			Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
					Uri.encode(number));
			cursor = getContentResolver()
					.query(uri, new String[] { PhoneLookup.DISPLAY_NAME },
							null, null, null);
			if (cursor.moveToFirst()) {
				name = cursor.getString(cursor
						.getColumnIndex(PhoneLookup.DISPLAY_NAME));

			}
		} catch (Exception e) {

		} finally {

			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}
		return name;

	}
}
