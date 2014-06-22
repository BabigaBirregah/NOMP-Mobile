package fr.utt.isi.nomp_mobile.fragments.forms;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.utt.isi.nomp_mobile.R;
import fr.utt.isi.nomp_mobile.adapters.TypeSpinnerArrayAdapter;
import fr.utt.isi.nomp_mobile.config.Config;
import fr.utt.isi.nomp_mobile.models.ActorType;
import fr.utt.isi.nomp_mobile.models.Classification;
import fr.utt.isi.nomp_mobile.models.Status;
import fr.utt.isi.nomp_mobile.models.Type;
import fr.utt.isi.nomp_mobile.tasks.PauserTask;
import fr.utt.isi.nomp_mobile.tasks.RequestTask;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public abstract class TicketFormFragment extends Fragment {

	public static final String TAG = "TicketFormFragment";

	protected static boolean isGPSChecked = true;

	protected static String road = "";
	protected static String postalCode = "";
	protected static String city = "";
	protected static String country = "";

	protected static ProgressDialog loading = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_ticket_form, container,
				false);

		setHasOptionsMenu(true);

		// classification parent list
		Classification classification = new Classification(getActivity());
		ArrayList<Classification> parentClassifications = (ArrayList<Classification>) classification
				.parentList();

		// target actor type parent list
		ActorType actorType = new ActorType(getActivity());
		ArrayList<ActorType> parentActorTypes = (ArrayList<ActorType>) actorType
				.parentList();

		// populate spinner
		if (parentClassifications.size() > 0 && parentActorTypes.size() > 0) {
			populateTypeParentList(Type.TYPE_CLASSIFICATION,
					parentClassifications, view);
			populateTypeParentList(Type.TYPE_ACTOR_TYPE, parentActorTypes, view);
		} else {
			loading = new ProgressDialog(getActivity());
			loading.setTitle("Loading");
			loading.setMessage("Please wait");
			loading.show();

			new PauserTask() {

				@Override
				protected void onPostExecute(Void result) {
					// classification parent list
					Classification classification = new Classification(
							getActivity());
					ArrayList<Classification> parentClassifications = (ArrayList<Classification>) classification
							.parentList();

					// target actor type parent list
					ActorType actorType = new ActorType(getActivity());
					ArrayList<ActorType> parentActorTypes = (ArrayList<ActorType>) actorType
							.parentList();

					populateTypeParentList(Type.TYPE_CLASSIFICATION,
							parentClassifications, null);
					populateTypeParentList(Type.TYPE_ACTOR_TYPE,
							parentActorTypes, null);

					if (loading != null) {
						loading.dismiss();
					}
				}

			}.execute(2000);
		}

		// assign actions on period buttons to show date picker
		Button buttonPeriodFrom = (Button) view
				.findViewById(R.id.button_period_from);
		Button buttonPeriodTo = (Button) view
				.findViewById(R.id.button_period_to);
		buttonPeriodFrom.setOnClickListener(new ButtonPeriodOnClickListener(
				R.id.button_period_from));
		buttonPeriodTo.setOnClickListener(new ButtonPeriodOnClickListener(
				R.id.button_period_to));

		// location
		SharedPreferences localizationPrefereces = getActivity()
				.getSharedPreferences(Config.PREF_NAME_LOCALIZATION,
						Context.MODE_PRIVATE);
		if (localizationPrefereces.getBoolean(Config.PREF_KEY_LOCALIZATION_GPS,
				true)) {
			populateLocationByGPS(view);
		}

		ImageView gpsLoader = (ImageView) view.findViewById(R.id.gps_loader);
		gpsLoader.setClickable(true);
		gpsLoader.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				populateLocationByGPS(null);
			}

		});

		// address pop-up
		EditText addressView = (EditText) view.findViewById(R.id.location);
		addressView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO: disable the original keyboard input of addressView

				// build a pop-up dialog for address input
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());

				// get layout inflater
				LayoutInflater inflater = getActivity().getLayoutInflater();

				// set a layout
				builder.setView(postInflateDialogView(inflater.inflate(
						R.layout.dialog_address, null)));

				builder.setTitle(R.string.text_address_dialog_title);

				// set a positive button to submit the input address
				builder.setPositiveButton(
						R.string.text_address_dialog_button_positive,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								AlertDialog dialogView = (AlertDialog) dialog;

								road = ((EditText) dialogView
										.findViewById(R.id.address_dialog_road))
										.getText().toString();
								postalCode = ((EditText) dialogView
										.findViewById(R.id.address_dialog_postal_code))
										.getText().toString();
								city = ((EditText) dialogView
										.findViewById(R.id.address_dialog_city))
										.getText().toString();
								country = ((EditText) dialogView
										.findViewById(R.id.address_dialog_country))
										.getText().toString();

								((EditText) getActivity().findViewById(
										R.id.location)).setText(road + ", "
										+ postalCode + " " + city + ", "
										+ country);
								dialog.dismiss();
							}

						});

				// set a negative button to cancel the dialog
				builder.setNegativeButton(
						R.string.text_address_dialog_button_negative,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});

				builder.create().show();
			}

		});

		return view;
	}

	protected void populateTypeParentList(String typeType,
			List<?> typeParentList, View parentView) {
		// create an adapter for spinner
		TypeSpinnerArrayAdapter spinnerTypeAdapter = new TypeSpinnerArrayAdapter(
				getActivity(), android.R.layout.simple_spinner_item,
				typeParentList);
		spinnerTypeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		int spinnerViewId = typeType.equals(Type.TYPE_CLASSIFICATION) ? R.id.spinner_classification
				: R.id.spinner_target;

		// get the right spinner view
		Spinner spinnerType = (Spinner) (parentView == null ? getActivity()
				.findViewById(spinnerViewId) : parentView
				.findViewById(spinnerViewId));

		// set adapter
		spinnerType.setAdapter(spinnerTypeAdapter);

		// set listener to spinner to show correspondent sub-spinner
		spinnerType
				.setOnItemSelectedListener(new TypeSpinnerOnItemSelectedListener(
						typeType,
						typeType.equals(Type.TYPE_CLASSIFICATION) ? R.id.spinner_sub_classification
								: R.id.spinner_sub_target));
	}

	protected void checkGPS() {
		LocationManager locationManager = (LocationManager) getActivity()
				.getSystemService(Context.LOCATION_SERVICE);
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			isGPSChecked = false;

			// notice user to turn on the GPS
			Toast gpsRequiredToast = Toast
					.makeText(
							getActivity(),
							"Application need GPS service for localization. Please turn on the GPS service.",
							Toast.LENGTH_LONG);
			gpsRequiredToast.show();

			// redirect to the system settings of toggling GPS service
			startActivityForResult(new Intent(
					android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
					0);
		} else {
			isGPSChecked = true;
		}
	}

	protected void populateLocationByGPS(View view) {
		checkGPS();

		// location service
		LocationManager locationManager = (LocationManager) getActivity()
				.getSystemService(Context.LOCATION_SERVICE);

		// define the listener
		LocationListener locationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
					populateAddressByLocation(location);
					populateGeometryByLocation(location);
				}
			}

			@Override
			public void onProviderDisabled(String provider) {

			}

			@Override
			public void onProviderEnabled(String provider) {

			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {

			}

		};

		String locationProvider = LocationManager.GPS_PROVIDER;

		locationManager.requestLocationUpdates(locationProvider, 0, 0,
				locationListener);

		Location lastKnownLocation = locationManager
				.getLastKnownLocation(locationProvider);

		if (lastKnownLocation == null) {
			locationProvider = LocationManager.NETWORK_PROVIDER;

			lastKnownLocation = locationManager
					.getLastKnownLocation(locationProvider);

			locationManager.requestLocationUpdates(locationProvider, 0, 0,
					locationListener);
		}

		if (lastKnownLocation != null) {
			// Log.d(TAG, "lat=" + lastKnownLocation.getLatitude() + ", lon=" +
			// lastKnownLocation.getLongitude());
			populateAddressByLocation(lastKnownLocation);
			populateGeometryByLocation(lastKnownLocation, view);
		}

		// stop tracking the location service
		locationManager.removeUpdates(locationListener);
	}

	protected void populateGeometryByLocation(Location location) {
		populateGeometryByLocation(location, null);
	}

	protected void populateGeometryByLocation(Location location, View view) {
		if (location == null) {
			return;
		}

		// Log.d(TAG, "\tpopulate: lat=" + location.getLatitude() + ", lon=" +
		// location.getLongitude());
		TextView geometryView = (TextView) (view == null ? getActivity()
				.findViewById(R.id.geometry) : view.findViewById(R.id.geometry));
		geometryView.setText(location.getLatitude() + ","
				+ location.getLongitude());
	}

	protected void populateAddressByLocation(Location location) {
		if (location == null) {
			return;
		}

		new RequestTask(getActivity(), "GET") {

			@Override
			public void onPostExecute(String result) {
				if (result == null) {
					Toast errorToast = Toast.makeText(getActivity(),
							"Some error occurs during request for address.",
							Toast.LENGTH_LONG);
					errorToast.show();
				} else if (result.equals(RequestTask.MAL_FORMED_URL_EXCEPTION)) {
					Toast errorToast = Toast.makeText(getActivity(),
							"Request server not found for address.",
							Toast.LENGTH_LONG);
					errorToast.show();
				} else if (result.equals(RequestTask.IO_EXCEPTION)) {
					Toast errorToast = Toast
							.makeText(
									getActivity(),
									"Unable to retrieve address from server. Please try again later.",
									Toast.LENGTH_LONG);
					errorToast.show();
				} else {
					try {
						JSONObject jsonObject = new JSONObject(result);

						// check if the result is ok
						if (jsonObject.has("results")
								&& jsonObject.has("status")
								&& jsonObject.getString("status").equals("OK")) {

							// get the first result in list
							JSONObject address = new JSONArray(
									jsonObject.getString("results"))
									.getJSONObject(0);

							if (address.has("formatted_address")) {
								// get the formatted address
								EditText addressView = (EditText) getActivity()
										.findViewById(R.id.location);
								addressView.setText(address
										.getString("formatted_address"));

								// get address components
								JSONArray addressComponents = new JSONArray(
										address.getString("address_components"));
								for (int i = 0; i < addressComponents.length(); i++) {
									JSONObject addressComponent = addressComponents
											.getJSONObject(i);
									JSONArray componentTypes = new JSONArray(
											addressComponent.getString("types"));
									if (componentTypes.length() > 0) {
										String componentType = componentTypes
												.getString(0);
										if (componentType.equals("route")) {
											road = addressComponent
													.getString("long_name");
										} else if (componentType
												.equals("postal_code")) {
											postalCode = addressComponent
													.getString("long_name");
										} else if (componentType
												.equals("locality")) {
											city = addressComponent
													.getString("long_name");
										} else if (componentType
												.equals("country")) {
											country = addressComponent
													.getString("long_name");
										}
									}
								}
							}
						} else {
							Toast errorToast = Toast.makeText(getActivity(),
									"Failed to get the address.",
									Toast.LENGTH_LONG);
							errorToast.show();
						}
					} catch (JSONException e) {
						Toast errorToast = Toast.makeText(getActivity(),
								"Failed to parse response from server.",
								Toast.LENGTH_LONG);
						errorToast.show();
					}
				}
			}

		}.execute(String
				.format(Locale.ENGLISH,
						"http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$f,%2$f&sensor=false",
						location.getLatitude(), location.getLongitude()));
	}

	protected void populateGeometryByAddress(String address) {
		loading = new ProgressDialog(getActivity());
		loading.setTitle("Geocoding");
		loading.setMessage("Please wait");
		loading.show();

		new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... params) {
				Geocoder geocoder = new Geocoder(getActivity(), Locale.ENGLISH);
				try {
					List<Address> addresses = geocoder.getFromLocationName(
							params[0], 1);
					if (addresses.size() > 0) {
						Address result = addresses.get(0);
						if (result.hasLatitude() && result.hasLongitude()) {
							return result.getLatitude() + ","
									+ result.getLongitude();
						} else {
							return null;
						}
					} else {
						return null;
					}
				} catch (IOException e) {
					return null;
				}
			}

			@Override
			protected void onPostExecute(String result) {
				loading.dismiss();
				if (result == null) {
					Toast errorToast = Toast.makeText(getActivity(),
							"Failed to geocode the address", Toast.LENGTH_LONG);
					errorToast.show();
				} else {
					TextView geometryView = (TextView) getActivity()
							.findViewById(R.id.geometry);
					geometryView.setText(result);

					Toast noticeToast = Toast.makeText(getActivity(),
							"Please press again to submit", Toast.LENGTH_LONG);
					noticeToast.show();
				}
			}

		}.execute(address);
	}

	// append already input address elements
	protected View postInflateDialogView(View view) {
		if (view == null) {
			view = getActivity().getLayoutInflater().inflate(
					R.layout.dialog_address, null);
		}

		if (!road.equals("")) {
			((EditText) view.findViewById(R.id.address_dialog_road))
					.setText(road);
		}

		if (!postalCode.equals("")) {
			((EditText) view.findViewById(R.id.address_dialog_postal_code))
					.setText(postalCode);
		}

		if (!city.equals("")) {
			((EditText) view.findViewById(R.id.address_dialog_city))
					.setText(city);
		}

		if (!country.equals("")) {
			((EditText) view.findViewById(R.id.address_dialog_country))
					.setText(country);
		}

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		if (!isGPSChecked) {
			checkGPS();
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_ticket_form, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_send:
			// pre-check for geometry
			TextView geometryView = (TextView) getActivity().findViewById(
					R.id.geometry);
			String geometry = (String) geometryView.getText();
			EditText addressView = (EditText) getActivity().findViewById(
					R.id.location);
			String address = addressView.getText().toString();

			if (address == null || address.equals("")) {
				addressView.setError(getActivity().getString(
						R.string.error_field_required));
				return true;
			} else if (geometry == null || geometry.equals("")) {
				// geocode the input address to populate the geometry
				populateGeometryByAddress(address);

				// force the user to submit again to ensure that the geometry is
				// populated
				return true;
			}

			storeTicket();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected ContentValues getBaseFieldValues() {
		Activity context = getActivity();
		String textErrorFieldRequired = context
				.getString(R.string.error_field_required);

		TextView geometryView = (TextView) context.findViewById(R.id.geometry);
		String geometry = (String) geometryView.getText();
		EditText addressView = (EditText) context.findViewById(R.id.location);
		String address = addressView.getText().toString();

		// handle errors
		addressView.setError(null);
		if (address == null || address.equals("")) {
			addressView.setError(textErrorFieldRequired);
			return null;
		} else if (geometry == null || geometry.equals("")) {
			addressView.setError(textErrorFieldRequired);
			return null;
		}

		EditText nameView = (EditText) context.findViewById(R.id.title);
		String name = nameView.getText().toString();

		// handle errors
		nameView.setError(null);
		if (name == null || name.equals("")) {
			nameView.setError(textErrorFieldRequired);
			return null;
		}

		EditText descriptionView = (EditText) context
				.findViewById(R.id.description);
		String description = descriptionView.getText().toString();

		// handle errors
		descriptionView.setError(null);
		if (description == null || description.equals("")) {
			descriptionView.setError(textErrorFieldRequired);
			return null;
		}

		String keywords = "";

		// get sub classification spinner
		Spinner subClassificationSpinner = (Spinner) context
				.findViewById(R.id.spinner_sub_classification);

		// get classification item
		Classification classificationItem = (Classification) subClassificationSpinner
				.getSelectedItem();

		// handle errors
		TextView labelClassificationView = (TextView) context
				.findViewById(R.id.label_classification);
		labelClassificationView.setError(null);
		if (classificationItem == null) {
			labelClassificationView.setError(textErrorFieldRequired);
			return null;
		}

		String classification = classificationItem.getNompId();
		String classificationName = classificationItem.getName();
		
		// get sub target actor type spinner
		Spinner subTargetSpinner = (Spinner) context
				.findViewById(R.id.spinner_sub_target);

		// get target actor type item
		ActorType actorTypeItem = (ActorType) subTargetSpinner
				.getSelectedItem();

		// handle errors
		TextView labelTargetView = (TextView) context
				.findViewById(R.id.label_target);
		labelTargetView.setError(null);
		if (actorTypeItem == null) {
			labelTargetView.setError(textErrorFieldRequired);
			return null;
		}

		String targetActorType = actorTypeItem.getNompId();
		String targetActorTypeName = actorTypeItem.getName();

		// Dates
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
		Button buttonPeriodFrom = (Button) context
				.findViewById(R.id.button_period_from);
		String startDate = (String) buttonPeriodFrom.getText();
		try {
			startDate = dateFormat.format(dateFormat.parse(startDate));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

		Button buttonPeriodTo = (Button) context
				.findViewById(R.id.button_period_to);
		String endDate = (String) buttonPeriodTo.getText();
		try {
			endDate = dateFormat.format(dateFormat.parse(endDate));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

		// handle errors
		TextView labelPeriodView = (TextView) context
				.findViewById(R.id.label_period);
		labelPeriodView.setError(null);
		if (startDate == null || startDate.equals("") || endDate == null
				|| endDate.equals("")) {
			labelPeriodView.setError(textErrorFieldRequired);
		}

		EditText quantityView = (EditText) context.findViewById(R.id.quantity);
		String quantityString = quantityView.getText().toString();
		if (quantityString == null || quantityString.equals("")) {
			quantityString = "0";
		}
		int quantity = Integer.parseInt(quantityString);
		
		// info about user
		String user = null;
		String sourceActorType = "s1d2f3";
		String sourceActorTypeName = "test";
		String contactPhone = "";
		String contactMobile = "";
		String contactEmail = "yipeng.huang@utt.fr";
		
		SharedPreferences userInfo = getActivity().getSharedPreferences(Config.PREF_NAME_USER, Context.MODE_PRIVATE);
		if (userInfo.getBoolean(Config.PREF_KEY_USER_IS_LOGGED, false)) {
			user = userInfo.getString(Config.PREF_KEY_USER_NOMP_ID, null);
			sourceActorType = userInfo.getString(Config.PREF_KEY_USER_ACTOR_TYPE, null);
			sourceActorTypeName = userInfo.getString(Config.PREF_KEY_USER_ACTOR_TYPE_NAME, null);
			contactPhone = "";
			contactMobile = "";
			contactEmail = userInfo.getString(Config.PREF_KEY_USER_EMAIL, null);
		}

		boolean isActive = true;
		int statut = Status.OPEN;
		String reference = null;
		String matched = null;

		ContentValues baseValues = new ContentValues();
		baseValues.put("name", name);
		baseValues.put("description", description);
		baseValues.put("keywords", keywords);
		baseValues.put("classification", classification);
		baseValues.put("classification_name", classificationName);
		baseValues.put("source_actor_type", sourceActorType);
		baseValues.put("source_actor_type_name", sourceActorTypeName);
		baseValues.put("target_actor_type", targetActorType);
		baseValues.put("target_actor_type_name", targetActorTypeName);
		baseValues.put("contact_phone", contactPhone);
		baseValues.put("contact_mobile", contactMobile);
		baseValues.put("contact_email", contactEmail);
		// baseValues.put("creationDate", creationDate.toString());
		baseValues.put("start_date", startDate);
		baseValues.put("end_date", endDate);
		// baseValues.put("expirationDate", expirationDate.toString());
		// baseValues.put("updateDate", updateDate.toString());
		baseValues.put("quantity", quantity);
		baseValues.put("geometry", geometry);
		baseValues.put("address", address);
		baseValues.put("is_active", isActive);
		baseValues.put("statut", statut);
		baseValues.put("reference", reference);
		baseValues.put("user", user);
		baseValues.put("matched", matched);

		return baseValues;
	}

	public abstract void storeTicket();

	public abstract void displayTicket(long ticketId);

	protected class TypeSpinnerOnItemSelectedListener implements
			OnItemSelectedListener {

		private String type;

		private int reactionSpinnerId;

		public TypeSpinnerOnItemSelectedListener(String type,
				int reactionSpinnerId) {
			super();
			this.type = type;
			this.reactionSpinnerId = reactionSpinnerId;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			Spinner subTypeSpinner = (Spinner) getActivity().findViewById(
					reactionSpinnerId);

			// get the type item
			Type typeItem = (Type) parent.getItemAtPosition(pos);

			// get children items
			ArrayList<?> childrenClassifications = null;
			if (type.equals(Type.TYPE_CLASSIFICATION)) {
				childrenClassifications = (ArrayList<Classification>) new Classification(
						getActivity()).childrenList(typeItem.getNompId());
			} else if (type.equals(Type.TYPE_ACTOR_TYPE)) {
				childrenClassifications = (ArrayList<ActorType>) new ActorType(
						getActivity()).childrenList(typeItem.getNompId());
			}

			// set adapter with the children items to spinner
			TypeSpinnerArrayAdapter spinnerSubClassificationAdapter = new TypeSpinnerArrayAdapter(
					getActivity(), android.R.layout.simple_spinner_item,
					childrenClassifications);
			spinnerSubClassificationAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			subTypeSpinner.setAdapter(spinnerSubClassificationAdapter);

			// show the sub-type spinner
			subTypeSpinner.setVisibility(View.VISIBLE);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}

	}

	protected class ButtonPeriodOnClickListener implements OnClickListener {

		private int reactionButtonId;
		private DialogFragment datePickerFragment;

		public ButtonPeriodOnClickListener(int reactionViewId) {
			super();
			this.reactionButtonId = reactionViewId;
			this.datePickerFragment = new DatePickerFragment(reactionButtonId);
		}

		@Override
		public void onClick(View parent) {
			datePickerFragment.show(getActivity().getSupportFragmentManager(),
					"datePicker");
		}

	}

	@SuppressLint("ValidFragment")
	protected class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		public static final String TAG = "DatePicker";

		private int year;
		private int month;
		private int day;

		private int reactionButtonId;

		public DatePickerFragment(int reactionButtonId) {
			super();

			// set current date in the picker
			final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);

			this.reactionButtonId = reactionButtonId;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			this.year = year;
			this.month = month;
			this.day = day;

			Button reactionButton = (Button) getActivity().findViewById(
					reactionButtonId);
			reactionButton.setText(getDate());
		}

		public String getDate() {
			Calendar date = new GregorianCalendar(year, month, day);
			DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
			return dateFormat.format(date.getTime());
		}

	}

}
