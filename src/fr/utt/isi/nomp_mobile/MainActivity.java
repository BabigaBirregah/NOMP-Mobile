package fr.utt.isi.nomp_mobile;

import fr.utt.isi.nomp_mobile.R;
import fr.utt.isi.nomp_mobile.activities.AccountActivity;
import fr.utt.isi.nomp_mobile.activities.LoginActivity;
import fr.utt.isi.nomp_mobile.config.Config;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

public class MainActivity extends ActionBarActivity {

	public static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// create the intent pointing to the login activity
		Intent intent;

		// set the default email/password for login if exists
		SharedPreferences userInfo = this.getSharedPreferences(
				Config.PREF_NAME_USER, Context.MODE_PRIVATE);

		if (userInfo.getBoolean(Config.PREF_KEY_USER_IS_LOGGED, false)) {
			// user is already logged in, turn to account page
			intent = new Intent(this, AccountActivity.class);
			intent.putExtra(Config.PREF_KEY_USER_NAME,
					userInfo.getString(Config.PREF_KEY_USER_NAME, null));
		} else {
			// user has not logged yet, turn to login page
			intent = new Intent(this, LoginActivity.class);
		}

		// start the login activity
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

}
