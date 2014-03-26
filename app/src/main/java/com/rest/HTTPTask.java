package com.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.purchaselist.MainActivity;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class HTTPTask extends AsyncTask<String, Void, String> {
	private MainActivity a;
	private String url;
	
	public HTTPTask(String _url, MainActivity _a) {
		url = _url;
		a =_a;
	}

	private final static int TIMEOUT_MILLIS = 1000;

	@Override
	protected String doInBackground(String... params) {

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		final String urlString = url;

		HttpURLConnection urlConnection = null;
		String response = "";

		try {
			final URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setConnectTimeout(TIMEOUT_MILLIS);
			urlConnection.setReadTimeout(TIMEOUT_MILLIS);

			if (HttpURLConnection.HTTP_OK == urlConnection.getResponseCode()) {
				final InputStream istream = urlConnection.getInputStream();
				response = processStream(istream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (urlConnection != null)
					urlConnection.disconnect();
			} catch (Exception e) {
				
			}
		}
		
		return response;
	}

	private static String processStream(final InputStream istream) {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(istream));
		final StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				istream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

	@Override
	protected void onPostExecute(String response) {
		super.onPostExecute(response);	
		
		if (response.length() > 0 && ORMAdapter.JSONtoORM(response) && a.updateList() && PreferenceManager.getDefaultSharedPreferences(a).getBoolean("notif", false)) {
			Toast.makeText(a, "Lists imported", Toast.LENGTH_SHORT).show();
		} else if (response.length() == 0 && PreferenceManager.getDefaultSharedPreferences(a).getBoolean("notif", false)) {
			Toast.makeText(a, "No internet connection, sync cancelled", Toast.LENGTH_SHORT).show();
		}
	}

};