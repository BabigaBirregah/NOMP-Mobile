package fr.utt.isi.nomp_mobile.tasks;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public abstract class PostRequestTask extends AsyncTask<String, Void, String> {
	
	public static final String TAG = "PostRequestTask";
	
	private Context context;
	
	private ContentValues queryValues;
	
	public PostRequestTask(Context context, String... params) {
		this.setContext(context);
		this.queryValues = new ContentValues();
		for (int i = 0; i < params.length; i = i + 2) {
			if (params.length <= i + 1) {
				break;
			}
			
			String key = params[i];
			String value = params[i + 1];
			this.queryValues.put(key, value);
		}
	}
	
	public PostRequestTask(Context context, ContentValues queryValues) {
		super();
		this.setContext(context);
		this.queryValues = queryValues;
	}

	@Override
	protected String doInBackground(String... params) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(params[0]);

		try {
			// query data collection
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

			// add every query data as key/value pair
			Set<Entry<String, Object>> valueSet = queryValues.valueSet();
			Iterator<Entry<String, Object>> i = valueSet.iterator();
			while (i.hasNext()) {
				Entry<String, Object> valueEntry = i.next();
				nameValuePairs.add(new BasicNameValuePair(valueEntry.getKey(), (String) valueEntry.getValue()));
			}
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			
			Log.d(TAG, "post response status=" + response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return null;
			}

			// output the response entity
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			response.getEntity().writeTo(out);
			String responseString = out.toString();
			out.close();
			return responseString;

		} catch (ClientProtocolException e) {
			Log.d(TAG, "client protocol exception");
			return null;
		} catch (IOException e) {
			Log.d(TAG, "io exception");
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected abstract void onPostExecute(String response);
	
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public ContentValues getQueryValues() {
		return queryValues;
	}

	public void setQueryValues(ContentValues queryValues) {
		this.queryValues = queryValues;
	}

}
