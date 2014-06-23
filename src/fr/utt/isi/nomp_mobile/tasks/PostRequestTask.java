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
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import fr.utt.isi.nomp_mobile.config.Config;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
			
			// cookies
			CookieStore cookieStore = new BasicCookieStore();
			
			SharedPreferences userInfo = context.getSharedPreferences(Config.PREF_NAME_USER, Context.MODE_PRIVATE);
			if (userInfo.getString(Config.PREF_KEY_USER_CONNECTION_COOKIE_NAME, null) != null && 
					userInfo.getString(Config.PREF_KEY_USER_CONNECTION_COOKIE_VALUE, null) != null) {
				
				BasicClientCookie cookie = new BasicClientCookie(
						userInfo.getString(Config.PREF_KEY_USER_CONNECTION_COOKIE_NAME, null), 
						userInfo.getString(Config.PREF_KEY_USER_CONNECTION_COOKIE_VALUE, null)
				);
				
				if (userInfo.getString(Config.PREF_KEY_USER_CONNECTION_COOKIE_DOMAIN, null) != null) {
					cookie.setDomain(userInfo.getString(Config.PREF_KEY_USER_CONNECTION_COOKIE_DOMAIN, null));
				}
				
				if (userInfo.getString(Config.PREF_KEY_USER_CONNECTION_COOKIE_PATH, null) != null) {
					cookie.setPath(userInfo.getString(Config.PREF_KEY_USER_CONNECTION_COOKIE_PATH, null));
				}
				
				cookieStore.addCookie(cookie);
			}
			
			// request context with cookies
		    HttpContext httpContext = new BasicHttpContext();
		    httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

			// execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost, httpContext);
			
			Cookie cookie = cookieStore.getCookies().get(0);
			if (cookie != null) {
				Editor editor = userInfo.edit();
				editor.putString(Config.PREF_KEY_USER_CONNECTION_COOKIE_NAME, cookie.getName());
				editor.putString(Config.PREF_KEY_USER_CONNECTION_COOKIE_VALUE, cookie.getValue());
				editor.putString(Config.PREF_KEY_USER_CONNECTION_COOKIE_DOMAIN, cookie.getDomain());
				editor.putString(Config.PREF_KEY_USER_CONNECTION_COOKIE_PATH, cookie.getPath());
				editor.commit();
			}
			
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
