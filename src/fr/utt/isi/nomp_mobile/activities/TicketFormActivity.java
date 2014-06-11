package fr.utt.isi.nomp_mobile.activities;

import fr.utt.isi.nomp_mobile.R;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

public class TicketFormActivity extends ActionBarActivity {
	
	public static final String TAG = "TicketFormActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ticket_form);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ticket_form, menu);
		return true;
	}

}
