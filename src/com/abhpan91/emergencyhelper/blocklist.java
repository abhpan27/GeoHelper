package com.abhpan91.emergencyhelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class blocklist extends Activity {
	private SimpleCursorAdapter dataAdapter;
	LinearLayout result;
	Button add;
	ListView   lv;
	phoneblock ph;
	Context cnt=this;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.blocklist);
		add = (Button) findViewById(R.id.add);
		
		registerForContextMenu(add);//Register a button for context events
		
		
		ph=new phoneblock(this);
		Cursor cr=ph.getAllContacts();
		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openContextMenu(v);
			}
		});
		
		  result=(LinearLayout) findViewById(R.id.result);
	       lv = (ListView) findViewById(R.id.list);
	            String[] columns = new String[] {
	    		    phoneblock.KEY_NAME ,
	    		    phoneblock.KEY_PH_NO,
	    		    
	    		  };
	            int[] to = new int[] { 
	            	    R.id.selectedname,
	            	    R.id.selectednumber,
	            	    
	            	  };
	         // This is the array adapter, it takes the context of the activity as a first // parameter, the type of list view as a second parameter and your array as a third parameter
	           dataAdapter = new SimpleCursorAdapter(
	           	    this, R.layout.blockedrow, 
	           	    cr, 
	           	    columns, 
	           	    to,
	           	    0);  
	            lv.setAdapter(dataAdapter);
	         
		
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, final View arg1,
					final int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(cnt);
		 
					// set title
					alertDialogBuilder.setTitle("Remove?");
		 
					// set dialog message
					alertDialogBuilder
						.setMessage("click yes to remove this from helper list")
						.setCancelable(false)
						.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, close
								// current activity
								TextView number=(TextView) arg1.findViewById(R.id.selectednumber);
							String num=(String) number.getText();
							if(sendmessage.servicerunning){
								stopService(new Intent(blocklist.this,sendmessage.class));
								ph.deleteContact(num);
								startService(new Intent(blocklist.this,sendmessage.class));
								}
							else
								ph.deleteContact(num);
							Cursor cursor = ph.getAllContacts();
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
				return false;
			}
		});
	         
	        
		
		
		
		
		
		
		
	}

	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.contextmenu, menu);
		menu.setHeaderTitle("Select an Option");
		
	}
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.contact:
				startActivity(new Intent("android.intent.action.phonebook"));
				 
				break;
			case R.id.log:
				startActivity(new Intent("android.intent.action.log"));
				break;
			case R.id.msg:
				startActivity(new Intent("android.intent.action.message"));
				break;
			case R.id.manualy:
				startActivity(new Intent("android.intent.action.manual"));
				break;
			}
		return super.onContextItemSelected(item);
		}

	@Override
	protected void onResume() {
		 Cursor cursor = ph.getAllContacts();
		    dataAdapter.changeCursor(cursor);

		    super.onResume();
	}
	
	

	

}
