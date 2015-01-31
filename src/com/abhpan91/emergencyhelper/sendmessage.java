package com.abhpan91.emergencyhelper;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class sendmessage extends Service implements SensorEventListener {
	private PowerManager.WakeLock wl;
	int count = 0;
	boolean avail;
	String message;
	boolean b;
	String adrs;
	String city;
	String country;
	Geocoder geocoder;
	List<Address> address;
	mylocationlistner listner;
	mylocationlistner listner2;
	double latitude;
	double longitude;
	phoneblock ph;
	Cursor cursor;
	ArrayList<String> numbers;
	static boolean servicerunning=false;
	private SensorManager mSensorManager;
	ConnectivityManager connectivityManager;
	LocationManager locationManager;
	SmsManager smsManager;
	SharedPreferences shr;
	Location loc;
	Location loc1;
	NetworkInfo ntw;
	messagedb msgdb;
	Date d;
	
	private sendmessage mSensorListener;
	/** Minimum movement force to consider. */
	private static final int MIN_FORCE = 7;

	/**
	 * Minimum times in a shake gesture that the direction of movement needs to
	 * change.
	 */
	private static final int MIN_DIRECTION_CHANGE = 12;

	/** Maximum pause between movements. */
	private static final int MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE = 200;

	/** Maximum allowed time for shake gesture. */
	private static final int MAX_TOTAL_DURATION_OF_SHAKE = 1000;

	/** Time when the gesture started. */
	private long mFirstDirectionChangeTime = 0;

	/** Time when the last movement started. */
	private long mLastDirectionChangeTime;

	/** How many movements are considered so far. */
	private int mDirectionChangeCount = 0;

	/** The last x position. */
	private float lastX = 0;

	/** The last y position. */
	private float lastY = 0;

	/** The last z position. */
	private float lastZ = 0;

	/** OnShakeListener that is called when shake is detected. */
	private OnShakeListener mShakeListener;
	private LocationListener listener;

	/**
	 * Interface for shake gesture.
	 */
	public interface OnShakeListener {

		/**
		 * Called when shake gesture is detected.
		 */
		void onShake();
	}

	public void setOnShakeListener(OnShakeListener listener) {
		mShakeListener = listener;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {

		/*
		 * Criteria criteria = new Criteria();
		 * criteria.setAccuracy(Criteria.ACCURACY_FINE);
		 * criteria.setAltitudeRequired(false);
		 * criteria.setBearingRequired(false); criteria.setCostAllowed(true);
		 * criteria.setPowerRequirement(Criteria.POWER_LOW); String provider =
		 * locationManager.getBestProvider(criteria, true); Location
		 * GlobalLocation = locationManager.getLastKnownLocation(provider);
		 */
		// locationManager.requestLocationUpdates(provider, 2000, 10, listner);
        msgdb=new messagedb(sendmessage.this);
        d=new Date();
		shr = getSharedPreferences("helper", MODE_PRIVATE);
		message = shr.getString("message", "I NEED HELP");
		b = shr.getBoolean("checked", false);
		connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		ntw=connectivityManager.getActiveNetworkInfo();
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		listner = new mylocationlistner();
		// listner2 = new mylocationlistner();
		avail = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
				|| locationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			locationManager.requestLocationUpdates(
					locationManager.GPS_PROVIDER, 0, 0, listner);
		else if (locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
			locationManager.requestLocationUpdates(
					locationManager.NETWORK_PROVIDER, 0, 0, listner);

		else
			avail = false;
		
		 loc = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		 loc1=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if(loc!=null){
		latitude = loc.getLatitude();
		longitude = loc.getLongitude();}
		else if(loc1!=null)
			{
			latitude = loc1.getLatitude();
			longitude = loc1.getLongitude();
			}
		else
			;
		
		
		smsManager = SmsManager.getDefault();
		geocoder = new Geocoder(sendmessage.this);
		numbers = new ArrayList<String>();
		servicerunning = true;

		ph = new phoneblock(this);
		try {
			cursor = ph.getAllContacts();

			if (cursor != null) {
				while (cursor.isAfterLast() == false) {

					numbers.add(cursor.getString(2));

					cursor.moveToNext();
				}
			}
			cursor.close();
		} catch (Exception e) {
			Log.d("error", e.toString());
		}

		// TODO Auto-generated method stub
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensorListener = new sendmessage();
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "partial wakelock");
		wl.acquire();
		
		mSensorListener.setOnShakeListener(new sendmessage.OnShakeListener() {

			public void onShake() {
				Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				// Vibrate for 500 milliseconds
				v.vibrate(500);

				sendtoall();
				sendmessages();
			}
		});
		super.onCreate();

	}

	@Override
	public void onDestroy() {
		servicerunning = false;
		// TODO Auto-generated method stub
		mSensorManager.unregisterListener(mSensorListener);
		try {
			if (wl.isHeld())
				wl.release();
		} catch (Exception e) {
			Log.d("wakelock", "chill be");
		}

		try {
			this.locationManager.removeUpdates(listner);
			this.locationManager.removeUpdates(listner2);
		} catch (Exception e) {
			Log.d("msg", "chill be");

		}
		locationManager = null;

		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		mSensorManager.registerListener(mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
		return Service.START_STICKY;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent se) {
		// get sensor data
		float x = se.values[SensorManager.DATA_X];
		float y = se.values[SensorManager.DATA_Y];
		float z = se.values[SensorManager.DATA_Z];

		// calculate movement
		float totalMovement = Math.abs(x + y + z - lastX - lastY - lastZ);

		if (totalMovement > MIN_FORCE) {

			// get time
			long now = System.currentTimeMillis();

			// store first movement time
			if (mFirstDirectionChangeTime == 0) {
				mFirstDirectionChangeTime = now;
				mLastDirectionChangeTime = now;
			}

			// check if the last movement was not long ago
			long lastChangeWasAgo = now - mLastDirectionChangeTime;
			if (lastChangeWasAgo < MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE) {

				// store movement data
				mLastDirectionChangeTime = now;
				mDirectionChangeCount++;

				// store last sensor data
				lastX = x;
				lastY = y;
				lastZ = z;

				// check how many movements are so far
				if (mDirectionChangeCount >= MIN_DIRECTION_CHANGE) {

					// check total duration
					long totalDuration = now - mFirstDirectionChangeTime;
					if (totalDuration < MAX_TOTAL_DURATION_OF_SHAKE) {
						mShakeListener.onShake();
						resetShakeParameters();
					}
				}

			} else {
				resetShakeParameters();
			}
		}
	}

	/**
	 * Resets the shake parameters to their default values.
	 */
	private void resetShakeParameters() {
		mFirstDirectionChangeTime = 0;
		mDirectionChangeCount = 0;
		mLastDirectionChangeTime = 0;
		lastX = 0;
		lastY = 0;
		lastZ = 0;
	}

	private void sendtoall() {
            if(latitude!=0.0 && (ntw!=null) && ntw.isConnected() ){
            	long currenttime=System.currentTimeMillis();
            	while(address==null && ((System.currentTimeMillis()-currenttime)<=5000)){
		try {
			address = geocoder.getFromLocation(latitude, longitude, 1);
			adrs = address.get(0).getAddressLine(0);
			city = address.get(0).getAddressLine(1);
			country = address.get(0).getAddressLine(2);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
	}}}
		if (avail)
			// /while (latitude == 0)
			;
		else
			Toast.makeText(
					getApplicationContext(),
					"No location provider avilable,please enable gps or network provider,sending messgae without location",
					1000).show();

	}

	public void sendmessages() {
		if (numbers.isEmpty())
			Toast.makeText(getApplicationContext(),
					"no numbers in helper list", 1000).show();
		else {
			Toast.makeText(getApplicationContext(), "message sending started",
					1000).show();
			for (Iterator<String> it = numbers.iterator(); it.hasNext();) {
				String pr = it.next();
				sendsms(pr);

			}

			Toast.makeText(getApplicationContext(),
					"messages have been sent to everyone in helperlist", 1000)
					.show();
		}

	}

	public void sendsms(String number) {

		String temp = message;
		String geocoding = "";
		if (adrs != null)
		{
			if(city==null)
				city="";
			if(country==null)
				country="";
			geocoding = geocoding + " it is near :" + adrs + "," + city + ","
					+ country;}

		if (b && avail)
			temp = temp + " i am at latitude :" + latitude + " longitude : "
					+ longitude + geocoding;

		 ArrayList<String> parts = smsManager.divideMessage(temp);
		smsManager.sendMultipartTextMessage(number, null, parts, null, null);
		//Toast.makeText(getApplicationContext(), temp + " to " + number, 1000)
		//		.show();
		if(getname(number)!=null)
			number=getname(number);
		msgdb.insertmessage(temp, number,DateFormat.getDateTimeInstance().format(new Date()) );
		// Log.d("here", message);

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

	public class mylocationlistner implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			longitude = location.getLongitude();
			latitude = location.getLatitude();
			// Toast.makeText(getApplicationContext(), "" + latitude +
			// longitude,
			// 1000).show();

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

	}

}
