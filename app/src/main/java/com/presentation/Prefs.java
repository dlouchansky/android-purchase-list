package com.presentation;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Prefs {
	public static String restUrl = "http://kotov.lv/JavaguryServices/purchases/";
	public static Boolean sync = false;
	public static String restKey = "";
	
	public static Boolean set(Activity a) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(a);
		sync = pref.getBoolean("sync", false);
		restKey = pref.getString("restkey", "");
		restUrl = restUrl + restKey;
		
		return true;
	}
}
