package fr.utt.isi.nomp_mobile.activities;

import java.util.ArrayList;
import java.util.List;

import fr.utt.isi.nomp_mobile.R;
import fr.utt.isi.nomp_mobile.adapters.TypeSpinnerArrayAdapter;
import fr.utt.isi.nomp_mobile.config.Config;
import fr.utt.isi.nomp_mobile.models.ActorType;
import fr.utt.isi.nomp_mobile.tasks.PostRequestTask;
import fr.utt.isi.nomp_mobile.utils.Utils;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
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

	private static ProgressDialog loading = null;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		// source actor type drop down list
		ActorType actorType = new ActorType(this);
		ArrayList<ActorType> parentActorTypes = (ArrayList<ActorType>) actorType
				.parentList();

		if (parentActorTypes.size() > 0) {
			populateActorTypeParentList(parentActorTypes);
		} else {
			// pop up the progress
			loading = new ProgressDialog(this);
			loading.setTitle("Loading");
			loading.setMessage("Please wait");
			loading.show();

			new PauserTask(this).execute();
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
				createUser();
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

	private void populateActorTypeParentList(List<ActorType> parentActorTypes) {
		// create an adapter for spinner
		TypeSpinnerArrayAdapter spinnerSourceAdapter = new TypeSpinnerArrayAdapter(
				this, android.R.layout.simple_spinner_item, parentActorTypes);
		spinnerSourceAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// set adapter to spinner
		Spinner spinnerSource = (Spinner) this
				.findViewById(R.id.spinner_source);
		spinnerSource.setAdapter(spinnerSourceAdapter);

		// set listener to spinner to show correspondent sub-spinner
		spinnerSource.setOnItemSelectedListener(this);
	}

	private boolean validate() {
		// get the views
		EditText viewFullName = (EditText) this.findViewById(R.id.full_name);
		EditText viewEmail = (EditText) this.findViewById(R.id.email);
		EditText viewUsername = (EditText) this.findViewById(R.id.username);
		EditText viewPassword = (EditText) this.findViewById(R.id.password);
		TextView viewLabelSource = (TextView) this
				.findViewById(R.id.label_source);
		Spinner spinnerSource = (Spinner) this
				.findViewById(R.id.spinner_source);

		// reset error messages
		viewFullName.setError(null);
		viewEmail.setError(null);
		viewUsername.setError(null);
		viewPassword.setError(null);
		viewLabelSource.setError(null);

		// error message
		String textErrorFieldRequired = this
				.getString(R.string.error_field_required);

		if (viewFullName.getText().toString().equals("")) {
			viewFullName.setError(textErrorFieldRequired);
			viewFullName.requestFocus();
			return false;
		}

		if (viewEmail.getText().toString().equals("")) {
			viewEmail.setError(textErrorFieldRequired);
			viewEmail.requestFocus();
			return false;
		} else if (!viewEmail.getText().toString().matches("^.+@.+$")) {
			viewEmail.setError(this.getString(R.string.error_invalid_email));
			return false;
		}

		if (viewUsername.getText().toString().equals("")) {
			viewUsername.setError(textErrorFieldRequired);
			viewUsername.requestFocus();
			return false;
		}

		if (viewPassword.getText().toString().equals("")) {
			viewPassword.setError(textErrorFieldRequired);
			viewPassword.requestFocus();
			return false;
		} else if (viewPassword.getText().toString().length() < 4) {
			viewPassword.setError(this
					.getString(R.string.error_invalid_password));
			viewPassword.requestFocus();
			return false;
		}

		if (spinnerSource.getSelectedItem() == null) {
			viewLabelSource.setError(textErrorFieldRequired);
			viewLabelSource.requestFocus();
			return false;
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

	private void createUser() {
		// create user on remote server via API
		ContentValues queryValues = getBaseFieldValues();
		// String queryString = Utils.parseQuery(queryValues);
		// String hex = Utils.getHexString(("sha1" + Utils
		// .getHexString(queryString.getBytes())).getBytes());
		// queryValues.put("mobile_query_token", hex);

		loading = new ProgressDialog(this);
		loading.setTitle("Loading");
		loading.setMessage("Please wait");
		loading.show();

		new PostRequestTask(this, queryValues) {

			@Override
			protected void onPostExecute(String response) {
				if (response == null) {
					Toast errorToast = Toast
							.makeText(
									this.getContext(),
									"Failed to create new user on server. Please try again later",
									Toast.LENGTH_LONG);
					errorToast.show();
				} else {
					String userNompId = Utils.webResponse2UserNompId(response);
					if (userNompId != null) {
						// get shared preferences
						SharedPreferences userInfo = getContext()
								.getSharedPreferences(Config.PREF_NAME_USER,
										Context.MODE_PRIVATE);

						// get editor of preferences
						Editor editor = userInfo.edit();

						// put the user nomp id
						editor.putString(Config.PREF_KEY_USER_NOMP_ID,
								userNompId);

						// put the flag of existence
						editor.putBoolean(Config.PREF_KEY_USER_IS_LOGGED, true);

						// put basic user values
						ContentValues values = getQueryValues();
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

						// redirect to account page
						Intent intent = new Intent(getContext(),
								SettingsActivity.class);
						getContext().startActivity(intent);
					} else {
						Toast errorToast = Toast
								.makeText(
										this.getContext(),
										"Failed to receive the id of new user from server. Your account might be already created. Please try login or sign up again later.",
										Toast.LENGTH_LONG);
						errorToast.show();
					}
				}

				if (loading != null) {
					loading.dismiss();
				}
			}

		}.execute(Config.NOMP_API_ROOT + "user");
	}

	private class PauserTask extends AsyncTask<Void, Void, Void> {

		private Context context;

		public PauserTask(Context context) {
			this.context = context;
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				// pause 2 seconds to give a window for system to re-populate
				// the drop down list
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return null;
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onPostExecute(Void result) {
			if (loading != null) {
				loading.dismiss();
			}

			// source actor type drop down list
			ActorType actorType = new ActorType(context);
			ArrayList<ActorType> parentActorTypes = (ArrayList<ActorType>) actorType
					.parentList();
			populateActorTypeParentList(parentActorTypes);
		}

	}
}
