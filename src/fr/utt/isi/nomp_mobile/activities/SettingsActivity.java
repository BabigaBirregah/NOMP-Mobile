package fr.utt.isi.nomp_mobile.activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import java.util.List;

import fr.utt.isi.nomp_mobile.R;
import fr.utt.isi.nomp_mobile.config.Config;
import fr.utt.isi.nomp_mobile.models.ActorType;
import fr.utt.isi.nomp_mobile.models.Classification;
import fr.utt.isi.nomp_mobile.models.Type;
import fr.utt.isi.nomp_mobile.tasks.PauserTask;

public class SettingsActivity extends PreferenceActivity {

	public static final String TAG = "SettingsActivity";

	/**
	 * Determines whether to always show the simplified settings UI, where
	 * settings are presented in a single list. When false, settings are shown
	 * as a master/detail two-pane view on tablets. When true, a single pane is
	 * shown on tablets.
	 */
	private static final boolean ALWAYS_SIMPLE_PREFS = false;

	private static PreferenceActivity context;

	private static String[] classificationEntries;
	private static String[] classificationEntryValues;

	private static String[] actorTypeEntries;
	private static String[] actorTypeEntryValues;

	private static SharedPreferences localizationPreferences;
	private static SharedPreferences classificationPreferences;
	private static SharedPreferences actorTypePreferences;

	private static ProgressDialog loading = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;

		populateParentLists();

		if (classificationEntries.length == 0 || actorTypeEntries.length == 0) {
			loading = new ProgressDialog(this);
			loading.setTitle("Loading");
			loading.setMessage("Please wait");
			loading.show();

			new PauserTask() {

				@SuppressWarnings("deprecation")
				@Override
				protected void onPostExecute(Void result) {
					populateParentLists();
					((ListPreference) context
							.findPreference("classification_list"))
							.setEntries(classificationEntries);
					((ListPreference) context
							.findPreference("classification_list"))
							.setEntryValues(classificationEntryValues);
					((ListPreference) context.findPreference("target_list"))
							.setEntries(actorTypeEntries);
					((ListPreference) context.findPreference("target_list"))
							.setEntryValues(actorTypeEntryValues);

					if (loading != null) {
						loading.dismiss();
					}
				}

			}.execute(2000);
		}

		// prepare shared preferences
		localizationPreferences = getSharedPreferences(
				Config.PREF_NAME_LOCALIZATION, Context.MODE_PRIVATE);
		classificationPreferences = getSharedPreferences(
				Config.PREF_NAME_CLASSIFICATION, Context.MODE_PRIVATE);
		actorTypePreferences = getSharedPreferences(
				Config.PREF_NAME_ACTOR_TYPE, Context.MODE_PRIVATE);
	}

	@Override
	public void onBackPressed() {
		SharedPreferences userPreferences = getSharedPreferences(
				Config.PREF_NAME_USER, Context.MODE_PRIVATE);
		String userFullName = userPreferences.getString(
				Config.PREF_KEY_USER_NAME, null);
		if (userFullName != null) {
			Intent intent = new Intent(this, AccountActivity.class);
			intent.putExtra(Config.PREF_KEY_USER_NAME, userFullName);
			startActivity(intent);
		} else {
			super.onBackPressed();
		}
	}

	@SuppressWarnings("unchecked")
	private static void populateParentLists() {
		// get parent list for classification and actor type
		List<Classification> classificationList = (List<Classification>) new Classification(
				context).parentList();
		List<ActorType> actorTypeList = (List<ActorType>) new ActorType(context)
				.parentList();

		// convert to a list of names and a list of values
		classificationEntries = typeListToEntries(classificationList);
		classificationEntryValues = typeListToEntryValues(classificationList);
		actorTypeEntries = typeListToEntries(actorTypeList);
		actorTypeEntryValues = typeListToEntryValues(actorTypeList);
	}

	private static String[] typeListToEntries(List<?> typeList) {
		int length = typeList.size();
		String[] entries = new String[length];

		for (int i = 0; i < length; i++) {
			entries[i] = ((Type) typeList.get(i)).getName();
		}

		return entries;
	}

	private static String[] typeListToEntryValues(List<?> typeList) {
		int length = typeList.size();
		String[] entries = new String[length];

		for (int i = 0; i < length; i++) {
			entries[i] = ((Type) typeList.get(i)).getNompId();
		}

		return entries;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		setupSimplePreferencesScreen();
	}

	/**
	 * Shows the simplified settings UI if the device configuration if the
	 * device configuration dictates that a simplified, single-pane UI should be
	 * shown.
	 */
	@SuppressWarnings("deprecation")
	private void setupSimplePreferencesScreen() {
		if (!isSimplePreferences(this)) {
			return;
		}

		// In the simplified UI, fragments are not used at all and we instead
		// use the older PreferenceActivity APIs.

		// Add 'localization' preferences
		addPreferencesFromResource(R.xml.pref_localization);

		// Add 'classification' preferences, and a corresponding header.
		PreferenceCategory fakeHeader = new PreferenceCategory(this);
		fakeHeader.setTitle(R.string.pref_header_classification);
		getPreferenceScreen().addPreference(fakeHeader);
		addPreferencesFromResource(R.xml.pref_classification);

		// Add 'target actor' preferences, and a corresponding header.
		fakeHeader = new PreferenceCategory(this);
		fakeHeader.setTitle(R.string.pref_header_target);
		getPreferenceScreen().addPreference(fakeHeader);
		addPreferencesFromResource(R.xml.pref_target_actor);

		// get the preference objects
		CheckBoxPreference gpsCheckBoxPreference = (CheckBoxPreference) findPreference("gps_checkbox");
		ListPreference classificationListPreference = (ListPreference) findPreference("classification_list");
		ListPreference subClassificationListPreference = (ListPreference) findPreference("sub_classification_list");
		ListPreference targetListPreference = (ListPreference) findPreference("target_list");
		ListPreference subTargetListPreference = (ListPreference) findPreference("sub_target_list");

		// set on change listener for checkbox
		gpsCheckBoxPreference
				.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

		// set entries and entry values for classification list
		classificationListPreference.setEntries(classificationEntries);
		classificationListPreference.setEntryValues(classificationEntryValues);

		// set entries and entry values for actor type list
		targetListPreference.setEntries(actorTypeEntries);
		targetListPreference.setEntryValues(actorTypeEntryValues);

		// parse stored preferences as default values
		// gps enabled
		gpsCheckBoxPreference.setDefaultValue(localizationPreferences
				.getBoolean(Config.PREF_KEY_LOCALIZATION_GPS, true));

		// preferred classification
		String classificationPreferred = classificationPreferences.getString(
				Config.PREF_KEY_CLASSIFICATION_PREFERRED, "-1");
		if (!classificationPreferred.equals("-1")) {
			// get firstly stored sub classification preference
			String subClassificationPreferred = classificationPreferences
					.getString(Config.PREF_KEY_SUB_CLASSIFICATION_PREFERRED,
							"-1");

			classificationListPreference.setValue(classificationPreferred);
			bindPreferenceSummaryToValue(classificationListPreference);

			// preferred sub classification
			if (!subClassificationPreferred.equals("-1")) {
				subClassificationListPreference
						.setValue(subClassificationPreferred);
				bindPreferenceSummaryToValue(subClassificationListPreference);
			}
		} else {
			bindPreferenceSummaryToValue(classificationListPreference);
			bindPreferenceSummaryToValue(subClassificationListPreference);
		}

		// preferred target actor
		String targetActorPreferred = actorTypePreferences.getString(
				Config.PREF_KEY_TARGET_ACTOR_PREFERRED, "-1");
		if (!targetActorPreferred.equals("-1")) {
			// get firstly stored target actor preference
			String subTargetActorPreferred = actorTypePreferences.getString(
					Config.PREF_KEY_SUB_TARGET_ACTOR_PREFERRED, "-1");

			targetListPreference.setValue(targetActorPreferred);
			bindPreferenceSummaryToValue(targetListPreference);

			// preferred sub classification
			if (!subTargetActorPreferred.equals("-1")) {
				subTargetListPreference.setValue(subTargetActorPreferred);
				bindPreferenceSummaryToValue(subTargetListPreference);
			}
		} else {
			bindPreferenceSummaryToValue(targetListPreference);
			bindPreferenceSummaryToValue(subTargetListPreference);
		}
	}

	@Override
	public boolean onIsMultiPane() {
		return isXLargeTablet(this) && !isSimplePreferences(this);
	}

	/**
	 * Helper method to determine if the device has an extra-large screen. For
	 * example, 10" tablets are extra-large.
	 */
	private static boolean isXLargeTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

	/**
	 * Determines whether the simplified settings UI should be shown. This is
	 * true if this is forced via {@link #ALWAYS_SIMPLE_PREFS}, or the device
	 * doesn't have newer APIs like {@link PreferenceFragment}, or the device
	 * doesn't have an extra-large screen. In these cases, a single-pane
	 * "simplified" settings UI should be shown.
	 */
	private static boolean isSimplePreferences(Context context) {
		return ALWAYS_SIMPLE_PREFS
				|| Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
				|| !isXLargeTablet(context);
	}

	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onBuildHeaders(List<Header> target) {
		if (!isSimplePreferences(this)) {
			loadHeadersFromResource(R.xml.pref_headers, target);
		}
	}

	/**
	 * A preference value change listener that updates the preference's summary
	 * to reflect its new value.
	 */
	private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@SuppressWarnings({ "unchecked", "deprecation" })
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();

			if (preference instanceof ListPreference) {
				// For list preferences, look up the correct display value in
				// the preference's 'entries' list.
				ListPreference listPreference = (ListPreference) preference;
				int index = listPreference.findIndexOfValue(stringValue);

				// Set the summary to reflect the new value.
				preference
						.setSummary(index >= 0 ? listPreference.getEntries()[index]
								: null);

				// setup children ListPreference entries/entry values
				if (preference.getKey().equals("classification_list")) {
					// get children list preference
					ListPreference childrenList = ((ListPreference) context
							.findPreference("sub_classification_list"));

					// get children classifications
					List<Classification> subClassificationList = (List<Classification>) new Classification(
							context).childrenList(stringValue);

					// setup entries and entry values
					childrenList
							.setEntries(typeListToEntries(subClassificationList));
					childrenList
							.setEntryValues(typeListToEntryValues(subClassificationList));

					// reset value
					childrenList.setValue("-1");
					bindPreferenceSummaryToValue(childrenList);
				} else if (preference.getKey().equals("target_list")) {
					// get children list preference
					ListPreference childrenList = ((ListPreference) context
							.findPreference("sub_target_list"));

					// get children actor types
					List<ActorType> subActorTypeList = (List<ActorType>) new ActorType(
							context).childrenList(stringValue);

					// setup entries and entry values
					childrenList
							.setEntries(typeListToEntries(subActorTypeList));
					childrenList
							.setEntryValues(typeListToEntryValues(subActorTypeList));

					// reset value
					childrenList.setValue("-1");
					bindPreferenceSummaryToValue(childrenList);
				}
			}

			// store changed values in shared preferences for future uses
			Editor editor = null;
			if (preference.getKey().equals("gps_checkbox")) {
				editor = localizationPreferences.edit();
				editor.putBoolean(Config.PREF_KEY_LOCALIZATION_GPS,
						stringValue.equals("true"));
			} else if (preference.getKey().equals("classification_list")) {
				editor = classificationPreferences.edit();
				editor.putString(Config.PREF_KEY_CLASSIFICATION_PREFERRED,
						stringValue);
			} else if (preference.getKey().equals("sub_classification_list")) {
				editor = classificationPreferences.edit();
				editor.putString(Config.PREF_KEY_SUB_CLASSIFICATION_PREFERRED,
						stringValue);
			} else if (preference.getKey().equals("target_list")) {
				editor = actorTypePreferences.edit();
				editor.putString(Config.PREF_KEY_TARGET_ACTOR_PREFERRED,
						stringValue);
			} else if (preference.getKey().equals("sub_target_list")) {
				editor = actorTypePreferences.edit();
				editor.putString(Config.PREF_KEY_SUB_TARGET_ACTOR_PREFERRED,
						stringValue);
			}

			if (editor != null) {
				editor.commit();
			}

			return true;
		}
	};

	/**
	 * Binds a preference's summary to its value. More specifically, when the
	 * preference's value is changed, its summary (line of text below the
	 * preference title) is updated to reflect the value. The summary is also
	 * immediately updated upon calling this method. The exact display format is
	 * dependent on the type of preference.
	 * 
	 * @see #sBindPreferenceSummaryToValueListener
	 */
	private static void bindPreferenceSummaryToValue(Preference preference) {
		// Set the listener to watch for value changes.
		preference
				.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

		// Trigger the listener immediately with the preference's
		// current value.
		sBindPreferenceSummaryToValueListener.onPreferenceChange(
				preference,
				PreferenceManager.getDefaultSharedPreferences(
						preference.getContext()).getString(preference.getKey(),
						""));
	}

	/**
	 * This fragment shows localization(GPS) preferences only. It is used when
	 * the activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class GeneralPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_localization);
		}
	}

	/**
	 * This fragment shows classification preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class NotificationPreferenceFragment extends
			PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_classification);

			// Bind the summaries of classifications preferences to their
			// values.
			bindPreferenceSummaryToValue(findPreference("classification_list"));
			bindPreferenceSummaryToValue(findPreference("sub_classification_list"));
		}
	}

	/**
	 * This fragment shows target actor preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class DataSyncPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_target_actor);

			// Bind the summaries of classifications preferences to their
			// values.
			bindPreferenceSummaryToValue(findPreference("target_list"));
			bindPreferenceSummaryToValue(findPreference("sub_target_list"));
		}
	}

}
