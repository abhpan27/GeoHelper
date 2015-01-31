package com.abhpan91.emergencyhelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.PhoneLookup;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class manual extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final phoneblock pb=new phoneblock(this);
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);
		final EditText number=(EditText) findViewById(R.id.numbertoadd);
		final EditText name=(EditText) findViewById(R.id.nametoadd);
		Button bt=(Button) findViewById(R.id.yesdon);
		 
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String num=number.getText().toString();
				 String nam=name.getText().toString();
				if(num.equals(""))
					Toast.makeText(getApplicationContext(),"Please give the number atleast ", Toast.LENGTH_LONG).show();
				else
				{
					boolean bb=false;
					if(sendmessage.servicerunning)
					{stopService(new Intent(manual.this,sendmessage.class));
						bb=true;
						}
					person ph;
					if(getname(num)!=null)
						ph=new person(getname(num), num);
					else if(!nam.equals("")){
						
						
							ph=new person(nam, num);}
					else
						ph=new person("Added Manualy",num);
					
					
					pb.addContact(ph);
					
					Toast.makeText(getApplicationContext(),"number added to helperlist", Toast.LENGTH_LONG).show();
					if(bb)
						startService(new Intent(manual.this,sendmessage.class));
					
				}
			}
		});
	}
	public String getname(String number) {
		String name=null;
		   Uri uri=Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
		   Cursor cursor=getContentResolver().query(uri,new String[]{PhoneLookup.DISPLAY_NAME},null,null, null);
		   if(cursor.moveToFirst()){
			 name=cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
			   
			   
		   }
		return name;	   
			
		}

}
