package fr.utt.isi.nomp_mobile.activities;

import java.util.ArrayList;

import fr.utt.isi.nomp_mobile.R;
import fr.utt.isi.nomp_mobile.adapters.TypeSpinnerArrayAdapter;
import fr.utt.isi.nomp_mobile.config.Config;
import fr.utt.isi.nomp_mobile.models.ActorType;
import fr.utt.isi.nomp_mobile.tasks.RequestTask;
import fr.utt.isi.nomp_mobile.utils.Utils;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

public class SignUpActivity extends ActionBarActivity implements
		OnItemSelectedListener {

	public static final String TAG = "SignUpActivity";

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!isUserExist()) {
			setContentView(R.layout.activity_sign_up);

			// source actor type drop down list
			ActorType actorType = new ActorType(this);
			ArrayList<ActorType> parentActorTypes = (ArrayList<ActorType>) actorType
					.parentList();

			// create an adapter for spinner
			TypeSpinnerArrayAdapter spinnerSourceAdapter = new TypeSpinnerArrayAdapter(
					this, android.R.layout.simple_spinner_item,
					parentActorTypes);
			spinnerSourceAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			// set adapter to spinner
			Spinner spinnerSource = (Spinner) this
					.findViewById(R.id.spinner_source);
			spinnerSource.setAdapter(spinnerSourceAdapter);

			// set listener to spinner to show correspondent sub-spinner
			spinnerSource.setOnItemSelectedListener(this);
		} else {
			// TODO: redirect to login page
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_send:
			if (validate()) {
				String userNompId = createUser();
				if (userNompId != null) {
					// TODO: redirect to user account page
				}
			} else {
				Toast errorToast = Toast
						.makeText(
								this,
								"Your inscription contains error. Please check the highlighted input.",
								Toast.LENGTH_LONG);
				errorToast.show();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// get the selected actor type item
		ActorType actorTypeItem = (ActorType) parent.getItemAtPosition(pos);

		// get children items
		ArrayList<ActorType> childrenList = (ArrayList<ActorType>) new ActorType(
				this).childrenList(actorTypeItem.getNompId());

		// create another adapter with the children items
		TypeSpinnerArrayAdapter spinnerSubSourceAdapter = new TypeSpinnerArrayAdapter(
				this, android.R.layout.simple_spinner_item, childrenList);
		spinnerSubSourceAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// set the adapter to sub actor type drop down list
		Spinner spinnerSubSource = (Spinner) this
				.findViewById(R.id.spinner_sub_source);
		spinnerSubSource.setAdapter(spinnerSubSourceAdapter);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	private boolean validate() {
		// get the views
		EditText viewFullName = (EditText) this.findViewById(R.id.full_name);
		EditText viewEmail = (EditText) this.findViewById(R.id.email);
		EditText viewUsername = (EditText) this.findViewById(R.id.username);
		EditText viewPassword = (EditText) this.findViewById(R.id.password);
		Spinner spinnerSource = (Spinner) this
				.findViewById(R.id.spinner_source);

		if (viewFullName.getText().toString().equals("")) {
			((TextView) this.findViewById(R.id.error_full_name))
					.setVisibility(View.VISIBLE);
			return false;
		} else {
			((TextView) this.findViewById(R.id.error_full_name))
					.setVisibility(View.GONE);
		}

		if (viewEmail.getText().toString().equals("")) {
			((TextView) this.findViewById(R.id.error_email))
					.setVisibility(View.VISIBLE);
			((TextView) this.findViewById(R.id.error_email_format))
					.setVisibility(View.GONE);
			return false;
		} else if (!viewEmail.getText().toString().matches("^.+@.+$")) {
			((TextView) this.findViewById(R.id.error_email))
					.setVisibility(View.GONE);
			((TextView) this.findViewById(R.id.error_email_format))
					.setVisibility(View.VISIBLE);
			return false;
		} else {
			((TextView) this.findViewById(R.id.error_email))
					.setVisibility(View.GONE);
			((TextView) this.findViewById(R.id.error_email_format))
					.setVisibility(View.GONE);
		}

		if (viewUsername.getText().toString().equals("")) {
			((TextView) this.findViewById(R.id.error_username))
					.setVisibility(View.VISIBLE);
			return false;
		} else {
			((TextView) this.findViewById(R.id.error_username))
					.setVisibility(View.GONE);
		}

		if (viewPassword.getText().toString().equals("")) {
			((TextView) this.findViewById(R.id.error_password))
					.setVisibility(View.VISIBLE);
			return false;
		} else {
			((TextView) this.findViewById(R.id.error_password))
					.setVisibility(View.GONE);
		}

		if (spinnerSource.getSelectedItem() == null) {
			((TextView) this.findViewById(R.id.error_source))
					.setVisibility(View.VISIBLE);
			return false;
		} else {
			((TextView) this.findViewById(R.id.error_source))
					.setVisibility(View.GONE);
		}

		return true;
	}

	private ContentValues getBaseFieldValues() {
		EditText viewFullName = (EditText) this.findViewById(R.id.full_name);
		String fullName = viewFullName.getText().toString();

		EditText viewEmail = (EditText) this.findViewById(R.id.email);
		String email = viewEmail.getText().toString();

		EditText viewUsername = (EditText) this.findViewById(R.id.username);
		String username = viewUsername.getText().toString();

		// TODO: find a way to avoid non-hashed password in transmission and
		// storage respecting NOMP API
		EditText viewPassword = (EditText) this.findViewById(R.id.password);
		String password = viewPassword.getText().toString();

		Spinner spinnerSubSource = (Spinner) this
				.findViewById(R.id.spinner_sub_source);
		ActorType source = null;
		if (spinnerSubSource.getSelectedItem() != null) {
			source = ((ActorType) spinnerSubSource.getSelectedItem());
		} else {
			Spinner spinnerSource = (Spinner) this
					.findViewById(R.id.spinner_source);
			if (spinnerSource.getSelectedItem() != null) {
				source = ((ActorType) spinnerSource.getSelectedItem());
			}
		}

		ContentValues values = new ContentValues();
		values.put("name", fullName);
		values.put("email", email);
		values.put("username", username);
		values.put("password", password);
		if (source != null) {
			values.put("actor_type", source.toJSONString());
			values.put("actor_type_nomp_id", source.getNompId());
			values.put("actor_type_name", source.getName());
		} else {
			values.putNull("actor_type");
			values.putNull("actor_type_nomp_id");
			values.putNull("actor_type_name");
		}

		return values;
	}

	private boolean isUserExist() {
		SharedPreferences userInfo = this.getSharedPreferences(
				Config.PREF_NAME_USER, Context.MODE_PRIVATE);
		if (userInfo.getBoolean(Config.PREF_KEY_USER_IS_LOGGED, false)) {
			Toast errorToast = Toast
					.makeText(
							this,
							"You already have an account on this device. Please sign in.",
							Toast.LENGTH_LONG);
			errorToast.show();
			return true;
		} else {
			return false;
		}
	}

	private String createUser() {
		// create user locally
		if (!isUserExist()) {
			// get shared preferences
			SharedPreferences userInfo = this.getSharedPreferences(
					Config.PREF_NAME_USER, Context.MODE_PRIVATE);

			// get editor of preferences
			Editor editor = userInfo.edit();

			// put the flag of existence
			editor.putBoolean(Config.PREF_KEY_USER_IS_LOGGED, true);

			// put basic user values
			ContentValues values = getBaseFieldValues();
			editor.putString(Config.PREF_KEY_USER_NAME,
					values.getAsString("name"));
			editor.putString(Config.PREF_KEY_USER_EMAIL,
					values.getAsString("email"));
			editor.putString(Config.PREF_KEY_USER_USERNAME,
					values.getAsString("username"));
			editor.putString(Config.PREF_KEY_USER_PASSWORD,
					values.getAsString("password"));
			editor.putString(Config.PREF_KEY_USER_ACTOR_TYPE,
					values.getAsString("actor_type_nomp_id"));
			editor.putString(Config.PREF_KEY_USER_ACTOR_TYPE_NAME,
					values.getAsString("actor_type_name"));

			// commit the preferences
			editor.commit();
		}

		// create user on remote server via API
		ContentValues queryValues = getBaseFieldValues();
		String queryString = Utils.parseQuery(queryValues);
		String hex = Utils.getHexString(("sha1" + Utils
				.getHexString(queryString.getBytes())).getBytes());
		queryValues.put("mobile_query_token", hex);

		new RequestTask(this, "POST", Utils.parseQuery(queryValues)) {

			@Override
			public void onPostExecute(String result) {
				if (result == null) {
					Toast errorToast = Toast.makeText(this.getContext(),
							"Some error occurs during request.",
							Toast.LENGTH_LONG);
					errorToast.show();
				} else if (result.equals(RequestTask.MAL_FORMED_URL_EXCEPTION)) {
					Toast errorToast = Toast.makeText(this.getContext(),
							"Request server not found.", Toast.LENGTH_LONG);
					errorToast.show();
				} else if (result.equals(RequestTask.IO_EXCEPTION)) {
					Toast errorToast = Toast
							.makeText(
									this.getContext(),
									"Unable to retrieve data from server. Please try again later.",
									Toast.LENGTH_LONG);
					errorToast.show();
				} else if (result.equals(RequestTask.REQUEST_ERROR)) {
					Toast errorToast = Toast.makeText(this.getContext(),
							"Request failed.", Toast.LENGTH_LONG);
					errorToast.show();
				} else {
					// TODO: handle result when _csrf is removed
				}
			}

		}.execute(Config.NOMP_API_ROOT + "user");

		return null;
	}
}
