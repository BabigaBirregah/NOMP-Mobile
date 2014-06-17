package fr.utt.isi.nomp_mobile.activities;

import java.util.ArrayList;

import fr.utt.isi.nomp_mobile.R;
import fr.utt.isi.nomp_mobile.adapters.TypeSpinnerArrayAdapter;
import fr.utt.isi.nomp_mobile.models.ActorType;
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
		setContentView(R.layout.activity_sign_up);

		// source actor type drop down list
		ActorType actorType = new ActorType(this);
		ArrayList<ActorType> parentActorTypes = (ArrayList<ActorType>) actorType
				.parentList();

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
				Toast errorToast = Toast.makeText(this, "Your inscription contains error. Please check the highlighted input.", Toast.LENGTH_LONG);
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
		ArrayList<ActorType> childrenList = (ArrayList<ActorType>) new ActorType(this)
				.childrenList(actorTypeItem.getNompId());

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
		
		if (viewFullName.getText().toString().equals("")) {
			((TextView) this.findViewById(R.id.error_full_name)).setVisibility(View.VISIBLE);
			return false;
		} else {
			((TextView) this.findViewById(R.id.error_full_name)).setVisibility(View.GONE);
		}
		
		if (viewEmail.getText().toString().equals("")) {
			((TextView) this.findViewById(R.id.error_email)).setVisibility(View.VISIBLE);
			((TextView) this.findViewById(R.id.error_email_format)).setVisibility(View.GONE);
			return false;
		} else if (!viewEmail.getText().toString().matches("^.+@.+$")) {
			((TextView) this.findViewById(R.id.error_email)).setVisibility(View.GONE);
			((TextView) this.findViewById(R.id.error_email_format)).setVisibility(View.VISIBLE);
			return false;
		} else {
			((TextView) this.findViewById(R.id.error_email)).setVisibility(View.GONE);
			((TextView) this.findViewById(R.id.error_email_format)).setVisibility(View.GONE);
		}
		
		if (viewUsername.getText().toString().equals("")) {
			((TextView) this.findViewById(R.id.error_username)).setVisibility(View.VISIBLE);
			return false;
		} else {
			((TextView) this.findViewById(R.id.error_username)).setVisibility(View.GONE);
		}
		
		if (viewPassword.getText().toString().equals("")) {
			((TextView) this.findViewById(R.id.error_password)).setVisibility(View.VISIBLE);
			return false;
		} else {
			((TextView) this.findViewById(R.id.error_password)).setVisibility(View.GONE);
		}

		return true;
	}
	
	private String createUser() {
		return null;
	}

}
