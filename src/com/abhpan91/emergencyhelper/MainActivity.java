package com.abhpan91.emergencyhelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnClickListener {

	ToggleButton onoff;
	ImageButton abc;
	LocationManager locationManager;
	Context cnt = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		onoff = (ToggleButton) findViewById(R.id.onoff);
		abc = (ImageButton) findViewById(R.id.abc);
		onoff.setOnClickListener(this);
		abc.setOnClickListener(this);
		if (sendmessage.servicerunning)
			onoff.setChecked(true);
		else
			onoff.setChecked(false);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.onoff:
			if (onoff.isChecked()) {

				if (!(locationManager
						.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER)))

				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							cnt);

					// set title
					alertDialogBuilder.setTitle("No location provider Enabled");

					// set dialog message
					alertDialogBuilder
							.setMessage(
									"Application will not be able to add your location in message,will you like to activate location provider (wireless or Gps)?")
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// if this button is clicked, close
											// current activity
											Intent it = new Intent(
													Settings.ACTION_LOCATION_SOURCE_SETTINGS);
											startActivityForResult(it, 1);
										}

									})
							.setNegativeButton("No",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// if this button is clicked, just
											// close
											// the dialog box and do nothing
											dialog.cancel();
											try {
												startService(new Intent(
														MainActivity.this,
														sendmessage.class));
												Toast.makeText(
														getApplicationContext(),
														"Emergency mode activated.you can exit application ",
														Toast.LENGTH_SHORT)
														.show();
											} catch (Exception e) {
												Toast.makeText(
														getApplicationContext(),
														"not sufficent memory please delete some items ",
														Toast.LENGTH_SHORT)
														.show();

											}

										}
									});

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();

					// show it
					alertDialog.show();

				} else {
					Toast.makeText(
							getApplicationContext(),
							"Emergency mode activated.you can exit application ",
							Toast.LENGTH_SHORT).show();
					try {
						startService(new Intent(MainActivity.this,
								sendmessage.class));

					} catch (Exception e) {
						Toast.makeText(
								getApplicationContext(),
								"not sufficent memory please delete some items ",
								Toast.LENGTH_SHORT).show();

					}
				}
			}

			else {
				stopService(new Intent(MainActivity.this, sendmessage.class));
				Toast.makeText(getApplicationContext(),
						"Emergency mode deactivated.", Toast.LENGTH_SHORT)
						.show();
			}

			break;
		case R.id.abc:
			startActivity(new Intent("android.intent.action.holder"));

			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		try {
			startService(new Intent(MainActivity.this, sendmessage.class));
			Toast.makeText(getApplicationContext(),
					"Emergency mode activated.you can exit application ",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"not sufficent memory please delete some items ",
					Toast.LENGTH_SHORT).show();

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater some = new MenuInflater(this);
		some.inflate(R.menu.mymenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case R.id.aboutus:
			Dialog d = new Dialog(this);
			d.setTitle("About US");
			TextView tv = new TextView(this);

			tv.setText("i am just a android loving guy dedicated to deliver quality apps");
			d.setContentView(tv);
			d.show();

			break;

		case R.id.help:

			Dialog d1 = new Dialog(this);
			d1.setTitle("HOW TO USE?");
			TextView tv1 = new TextView(this);

			tv1.setText("first make your helper list,add numbers from contacts,log or messages.then type your message you like to send, save it ,start the emergency mode on clicking on the button on home screen, you done, exit the application ,and whenever you are in need, just shake the phone  until it vibrates.once you feel that you do not need to be in emergency mode just deactivate the application on clicking on the button.\n In case if your phone is locked and in deep sleep, press power Button once and then shake the phone.");
			d1.setContentView(tv1);
			d1.show();
			break;

		}
		return false;
	}

}
