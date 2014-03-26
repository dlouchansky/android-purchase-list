package com.purchaselist;

import com.purchaselist.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.KeyEvent;
import android.view.WindowManager;

public class PrefActivity extends PreferenceActivity {
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.activity_pref);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
	    	finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
