package com.abhpan91.emergencyhelper;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class holderactivity extends TabActivity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
 
		Resources ressources = getResources(); 
		TabHost tabHost = getTabHost(); 
 
		// Android tab
		Intent intentAndroid = new Intent().setClass(this, blocklist.class);
		TabSpec tabSpecAndroid = tabHost
		  .newTabSpec("helperlist")
		  .setIndicator("helperlist",null)
		  .setContent(intentAndroid);
 
		
		
 
		// Windows tab
		Intent intentWindows = new Intent().setClass(this, settings.class);
		TabSpec tabSpecWindows = tabHost
		  .newTabSpec("Message")
		  .setIndicator("Message",null)
		  .setContent(intentWindows);
		
		Intent intentApple = new Intent().setClass(this,logs.class);
		TabSpec tabSpecApple = tabHost
		  .newTabSpec("Message log")
		  .setIndicator("Message log", null)
		  .setContent(intentApple);
		
 
		
		tabHost.addTab(tabSpecAndroid);
		tabHost.addTab(tabSpecWindows);
		tabHost.addTab(tabSpecApple);
		
		
 
		//set Windows tab as default (zero based)
		tabHost.setCurrentTab(0);

}}
