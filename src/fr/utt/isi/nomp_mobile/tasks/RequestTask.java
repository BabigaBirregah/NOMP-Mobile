package fr.utt.isi.nomp_mobile.tasks;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public abstract class RequestTask extends AsyncTask<String, Void, String> {
	
	public static final String TAG = "RequestTask";
	
	public static final String MAL_FORMED_URL_EXCEPTION = "MalformedURLException";
	
	public static final String IO_EXCEPTION = "IOException";
	
	public static final String REQUEST_ERROR = "RequestError";
	
	private Context context;
	
	private String requestMethod;
	
	private String requestQuery;

	public RequestTask(Context context, String requestMethod) {
		super();
		this.context = context;
		this.requestMethod = requestMethod;
		this.requestQuery = null;
	}
	
	public RequestTask(Context context, String requestMethod, String requestQuery) {
		super();
		this.context = context;
		this.requestMethod = requestMethod;
		this.requestQuery = requestQuery;
	}

	@SuppressLint("DefaultLocale")
	@Override
	protected String doInBackground(String... urls) {
		try {
			// open a connection for http request
			URL url = new URL(urls[0]);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			
			// handle the input request method
			String mRequestMethod = requestMethod.toUpperCase(Locale.US);
			if (mRequestMethod.equals("POST")) {
				// output the query to connection
				urlConnection.setDoOutput(true);
				if (requestQuery != null) {
					Log.d(TAG, requestQuery);
					OutputStream out = urlConnection.getOutputStream();
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
					writer.write(requestQuery);
					writer.flush();
					writer.close();
					out.close();
				}
			} else if (mRequestMethod.equals("PUT") || mRequestMethod.equals("DELETE")) {
				urlConnection.setRequestMethod(mRequestMethod);
			}
			
			int responseCode = urlConnection.getResponseCode();
			Log.d(TAG, "ok? " + responseCode);
			
			String response = null;
			if (responseCode == 200) {
				// get response input stream
				InputStream in = new BufferedInputStream(urlConnection.getInputStream());
				
				// handle the response
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in), 32768);
				StringBuilder stringBuilder = new StringBuilder();
				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					Log.d(TAG, line);
					stringBuilder.append(line);
				}
				
				response = stringBuilder.toString();
			} else {
				response = REQUEST_ERROR;
			}
			
			urlConnection.disconnect();
			return response;
			
		} catch (MalformedURLException e) {
			return MAL_FORMED_URL_EXCEPTION;
		} catch (IOException e) {
			return IO_EXCEPTION;
		}
	}
	
	/**
	 * Callback
	 */
	@Override
	public abstract void onPostExecute(String result);
	
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	
	public String getRequestQuery() {
		return requestQuery;
	}

	public void setRequestQuery(String requestQuery) {
		this.requestQuery = requestQuery;
	}

}
