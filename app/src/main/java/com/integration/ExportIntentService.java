package com.integration;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

public class ExportIntentService extends IntentService {
	Handler mHandler;
	
	private class DisplayToast implements Runnable{
		  String mText;

		  public DisplayToast(String text){
		    mText = text;
		  }

		  public void run(){
		     Toast.makeText(ExportIntentService.this, mText, Toast.LENGTH_LONG).show();
		  }
		}
	
	public ExportIntentService() {
		super("ExportIntentService");
	}
	
	public void onCreate() {
		super.onCreate();
		mHandler = new Handler();
	}
	
	protected void onHandleIntent(Intent intent) {
		String _url = intent.getExtras().getString("url");
		String _json = intent.getExtras().getString("json");
		Boolean notif = intent.getExtras().getBoolean("notif");
		HttpURLConnection conn = null;

		try {
			URL url = new URL(_url);
			conn = (HttpURLConnection) url.openConnection();
			
			byte[] post = _json.getBytes("UTF-8");
			
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-type", "application/json");
			conn.setUseCaches(false);
			
			OutputStream out = conn.getOutputStream();
			
			out.write(post);
			out.flush();
			
			conn.getResponseCode();
			
			if (notif) {
				mHandler.post(new DisplayToast("Lists exported")); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.disconnect();
		}
	}
}
