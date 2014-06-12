package fr.utt.isi.nomp_mobile.tasks;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;

public abstract class GeocodeTask extends AsyncTask<Location, Void, String> {

	public static final String TAG = "GeocodeTask";

	public static final String IO_EXCEPTION = "IOException";

	private Context context;

	public GeocodeTask(Context context) {
		super();
		this.context = context;
	}

	@Override
	protected String doInBackground(Location... locations) {
		// get location and its coordinates
		Location location = locations[0];
		double lat = location.getLatitude();
		double lon = location.getLongitude();

		// initialize a geocoder
		Geocoder geocoder = new Geocoder(context, Locale.US);

		// address list
		List<Address> addressList = null;
		try {
			addressList = geocoder.getFromLocation(lat, lon, 1);
		} catch (IOException e) {
			return IO_EXCEPTION;
		}

		if (addressList != null && addressList.size() > 0) {
			Address address = addressList.get(0);

			/*
			 * Format the first line of address (if available), city, and
			 * country name.
			 */
			String addressText = String.format(
					"%s, %s, %s",

					// If there's a street address, add it
					address.getMaxAddressLineIndex() > 0 ? address
							.getAddressLine(0) : "",

					// Locality is usually a city
					address.getLocality(),

					// The country of the address
					address.getCountryName()
			);
			
			// Return the text
			return addressText;
		} else {
			return null;
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

}
