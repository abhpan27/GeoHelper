package com.abhpan91.emergencyhelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class logs extends Activity {
	ListView lv;
	Button clear;
	private SimpleCursorAdapter dataAdapter;
	messagedb msghelper;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        
        setContentView(R.layout.logs);
        clear=(Button) findViewById(R.id.clearall);
        lv=(ListView) findViewById(R.id.messagelist);
        msghelper=new messagedb(this);
        
        Cursor cr=msghelper.getAllContacts();
        String[] columns = new String[] {
    		    msghelper.KEY_MESSAGE ,
    		    msghelper.KEY_PH_NO,
    		    msghelper.KEY_TIME
    		    
    		  };
        int[] to = new int[] { 
        	    R.id.msgdata,
        	    R.id.msgnumber,
        	    R.id.timedate
        	  };
        dataAdapter = new SimpleCursorAdapter(
           	    this, R.layout.msglog, 
           	    cr, 
           	    columns, 
           	    to,
           	    0);  
            lv.setAdapter(dataAdapter);
            clear.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(logs.this);
					 
					// set title
					alertDialogBuilder.setTitle("clear log?");
		 
					// set dialog message
					alertDialogBuilder
						.setMessage("click yes to clear log")
						.setCancelable(false)
						.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, close
								// current activity
								msghelper.deleteall();
								Toast.makeText(getApplicationContext(),
										"message log cleared", 200)
										.show();
								
								Cursor cursor = msghelper.getAllContacts();
							    dataAdapter.changeCursor(cursor);
							}
						  })
						.setNegativeButton("No",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
								
							}
						});
		 
						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
		 
						// show it
						alertDialog.show();
					
				}
			});
        
    }
	@Override
	protected void onResume() {
		 Cursor cursor = msghelper.getAllContacts();
		    dataAdapter.changeCursor(cursor);

		    super.onResume();
	}

}
