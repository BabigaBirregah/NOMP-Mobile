package fr.utt.isi.nomp_mobile.tasks;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public abstract class RequestTask extends AsyncTask<String, Void, String> {
	
	public static final String TAG = "RequestTask";
	
	private Context context;
	
	private String requestMethod;

	public RequestTask(Context context, String requestMethod) {
		super();
		this.context = context;
		this.requestMethod = requestMethod;
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
				urlConnection.setDoOutput(true);
			} else if (mRequestMethod.equals("PUT") || mRequestMethod.equals("DELETE")) {
				urlConnection.setRequestMethod(mRequestMethod);
			}
			
			// get response input stream
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			
			// handle the response
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in), 32768);
			StringBuilder stringBuilder = new StringBuilder();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
			
			return stringBuilder.toString();
		} catch (MalformedURLException e) {
			Toast errorToast = Toast.makeText(context, "Request server not found.", Toast.LENGTH_LONG);
			errorToast.show();
		} catch (IOException e) {
			Toast errorToast = Toast.makeText(context, "Unable to retrieve data from server. Please try again later.", Toast.LENGTH_LONG);
			errorToast.show();
		}
		
		return null;
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

}
