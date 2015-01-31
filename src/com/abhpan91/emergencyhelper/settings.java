package com.abhpan91.emergencyhelper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class settings extends Activity implements OnClickListener{
	 EditText message;
	 CheckBox chk;
	 SharedPreferences shr;
	 SharedPreferences.Editor edt;
	 String msg;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        
        setContentView(R.layout.settings);
        Button bt=(Button) findViewById(R.id.savethis);
         message=(EditText) findViewById(R.id.mymessage);
       chk=(CheckBox) findViewById(R.id.location);
       shr=getSharedPreferences("helper",MODE_PRIVATE);
       edt=shr.edit();
       message.setText(shr.getString("message", "I NEED HELP"));
       chk.setChecked(shr.getBoolean("checked", false));
       
       bt.setOnClickListener(this);
       
	
	
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		if(v.getId()==R.id.savethis){
			msg=message.getText().toString();
		if(msg.length()<=0){
			
			Toast.makeText(getApplicationContext(),"please enter the message ", Toast.LENGTH_SHORT).show();
		}
		else{
		if(!sendmessage.servicerunning){	
		
		
		edt.putString("message", msg);
		edt.putBoolean("checked", chk.isChecked());
		edt.commit();
		Toast.makeText(getApplicationContext(),"message saved ", Toast.LENGTH_SHORT).show();}
		else
		{stopService(new Intent(settings.this,sendmessage.class));
		
		
		edt.putString("message", msg);
		edt.putBoolean("checked", chk.isChecked());
		edt.commit();
				
			Toast.makeText(getApplicationContext(),"message saved ", Toast.LENGTH_SHORT).show();
			startService(new Intent(settings.this,
					sendmessage.class));}
			
			
		}}
			
		}
		 
			
		
	}


