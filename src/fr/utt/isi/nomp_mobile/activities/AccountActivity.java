package fr.utt.isi.nomp_mobile.activities;

import fr.utt.isi.nomp_mobile.R;
import fr.utt.isi.nomp_mobile.config.Config;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class AccountActivity extends ActionBarActivity implements
		OnClickListener {

	private long mLastPress = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);

		int defaultBridgeColor = Color.parseColor("#FFFFFF");

		// user's full name
		Intent intent = getIntent();
		((TextView) findViewById(R.id.text_welcome_full_name)).setText(intent
				.getStringExtra(Config.PREF_KEY_USER_NAME));

		// bridges to ticket form
		TextView postNeedView = (TextView) findViewById(R.id.label_post_need);
		((GradientDrawable) postNeedView.getBackground())
				.setColor(defaultBridgeColor);
		postNeedView.setClickable(true);
		postNeedView.setOnClickListener(this);

		TextView postOfferView = (TextView) findViewById(R.id.label_post_offer);
		((GradientDrawable) postOfferView.getBackground())
				.setColor(defaultBridgeColor);
		postOfferView.setClickable(true);
		postOfferView.setOnClickListener(this);

		// bridges to ticket list
		TextView myNeedsView = (TextView) findViewById(R.id.label_my_needs);
		((GradientDrawable) myNeedsView.getBackground())
				.setColor(defaultBridgeColor);
		myNeedsView.setClickable(true);
		myNeedsView.setOnClickListener(this);

		TextView myOffersView = (TextView) findViewById(R.id.label_my_offers);
		((GradientDrawable) myOffersView.getBackground())
				.setColor(defaultBridgeColor);
		myOffersView.setClickable(true);
		myOffersView.setOnClickListener(this);

		// bridge to settings
		TextView mySettingsView = (TextView) findViewById(R.id.label_my_settings);
		((GradientDrawable) mySettingsView.getBackground())
				.setColor(defaultBridgeColor);
		mySettingsView.setClickable(true);
		mySettingsView.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();

		SharedPreferences userInfo = getSharedPreferences(
				Config.PREF_NAME_USER, Context.MODE_PRIVATE);

		if (userInfo.contains(Config.PREF_KEY_USER_NAME)) {
			((TextView) findViewById(R.id.text_welcome_full_name))
					.setText(userInfo.getString(Config.PREF_KEY_USER_NAME, ""));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.accout, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_logout:
				// logout
				SharedPreferences userInfo = getSharedPreferences(Config.PREF_NAME_USER, Context.MODE_PRIVATE);
				Editor editor = userInfo.edit();

				// remove user login info
				editor.remove(Config.PREF_KEY_USER_NOMP_ID);
				editor.remove(Config.PREF_KEY_USER_ACTOR_TYPE);
				editor.remove(Config.PREF_KEY_USER_ACTOR_TYPE_NAME);
				editor.remove(Config.PREF_KEY_USER_NAME);
				editor.remove(Config.PREF_KEY_USER_USERNAME);
				editor.remove(Config.PREF_KEY_USER_EMAIL);

				// reset the flag of existence
				editor.putBoolean(Config.PREF_KEY_USER_IS_LOGGED, false);

				editor.commit();
				
				// redirect to login activity
				Intent intent = new Intent(this, LoginActivity.class);
				intent.putExtra("force", true);
				startActivity(intent);
				
				return true;
			default:
				return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public void onBackPressed() {
		// indication of exit
		Toast onBackPressedToast = Toast.makeText(this,
				R.string.press_once_again_to_exit, Toast.LENGTH_SHORT);

		long currentTime = System.currentTimeMillis();
		if (currentTime - mLastPress > 2000) {
			// if it's been too long from the last press, show toast
			onBackPressedToast.show();
			mLastPress = currentTime;
		} else {
			// cancel the toast
			onBackPressedToast.cancel();

			// redirect back to home of device
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;

		if (v.getId() == R.id.label_post_need) {
			intent = new Intent(this, TicketFormActivity.class);
			intent.putExtra("ticketType", "need");
		} else if (v.getId() == R.id.label_post_offer) {
			intent = new Intent(this, TicketFormActivity.class);
			intent.putExtra("ticketType", "offer");
		} else if (v.getId() == R.id.label_my_needs) {
			intent = new Intent(this, TicketListActivity.class);
			intent.putExtra("ticketType", "need");
		} else if (v.getId() == R.id.label_my_offers) {
			intent = new Intent(this, TicketListActivity.class);
			intent.putExtra("ticketType", "offer");
		} else if (v.getId() == R.id.label_my_settings) {
			intent = new Intent(this, SettingsActivity.class);
		} else {
			return;
		}

		if (intent != null) {
			startActivity(intent);
		}
	}
}
