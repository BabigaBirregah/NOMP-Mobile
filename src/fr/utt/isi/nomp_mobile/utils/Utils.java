package fr.utt.isi.nomp_mobile.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import android.content.ContentValues;
import android.util.Log;

public class Utils {

	public static final String TAG = "Utils";

	private Utils() {
	}

	public static String parseQuery(ContentValues keyValuePairs) {
		Set<Entry<String, Object>> set = keyValuePairs.valueSet();
		Iterator<Entry<String, Object>> i = set.iterator();

		int count = 0;
		StringBuilder queryStringBuilder = new StringBuilder();
		while (i.hasNext()) {
			if (count != 0) {
				queryStringBuilder.append("&");
			}

			count++;

			Entry<String, Object> entry = i.next();
			try {
				queryStringBuilder.append(URLEncoder.encode(entry.getKey(),
						"UTF-8"));
				queryStringBuilder.append("=");
				queryStringBuilder.append(URLEncoder.encode(
						"" + entry.getValue(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				Log.d(TAG, "UnsupportedEncodingException");
				continue;
			}
		}

		if (count == 0) {
			return null;
		} else {
			return queryStringBuilder.toString();
		}
	}

	public static String getHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

}
