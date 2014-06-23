package fr.utt.isi.nomp_mobile.tasks;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import fr.utt.isi.nomp_mobile.config.Config;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public abstract class GetRequestTask extends AsyncTask<String, Void, String> {

	public static final String TAG = "GetRequestTask";

	private Context context;

	public GetRequestTask(Context context) {
		super();
		this.context = context;
	}

	@Override
	protected String doInBackground(String... params) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(params[0]);

		// cookies
		CookieStore cookieStore = new BasicCookieStore();

		SharedPreferences userInfo = context.getSharedPreferences(
				Config.PREF_NAME_USER, Context.MODE_PRIVATE);
		if (userInfo.getString(Config.PREF_KEY_USER_CONNECTION_COOKIE_NAME,
				null) != null
				&& userInfo.getString(
						Config.PREF_KEY_USER_CONNECTION_COOKIE_VALUE, null) != null) {

			BasicClientCookie cookie = new BasicClientCookie(
					userInfo.getString(
							Config.PREF_KEY_USER_CONNECTION_COOKIE_NAME, null),
					userInfo.getString(
							Config.PREF_KEY_USER_CONNECTION_COOKIE_VALUE, null));

			if (userInfo.getString(
					Config.PREF_KEY_USER_CONNECTION_COOKIE_DOMAIN, null) != null) {
				cookie.setDomain(userInfo.getString(
						Config.PREF_KEY_USER_CONNECTION_COOKIE_DOMAIN, null));
			}

			if (userInfo.getString(Config.PREF_KEY_USER_CONNECTION_COOKIE_PATH,
					null) != null) {
				cookie.setPath(userInfo.getString(
						Config.PREF_KEY_USER_CONNECTION_COOKIE_PATH, null));
			}

			cookieStore.addCookie(cookie);
		}

		// request context with cookies
		HttpContext httpContext = new BasicHttpContext();
		httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

		// execute HTTP Post Request
		try {
			HttpResponse response = httpclient.execute(httpget, httpContext);
			
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
	public abstract void onPostExecute(String response);

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

}
