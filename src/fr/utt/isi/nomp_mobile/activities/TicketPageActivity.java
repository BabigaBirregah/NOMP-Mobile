package fr.utt.isi.nomp_mobile.activities;

import fr.utt.isi.nomp_mobile.R;
import fr.utt.isi.nomp_mobile.fragments.pages.NeedPageFragment;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

public class TicketPageActivity extends ActionBarActivity {
	
	private String parentActivity = "TicketFormActivity";
	
	private String ticketType = "need";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ticket_page);
		
		Intent intent = getIntent();
		parentActivity = intent.getStringExtra("parentActivity") == null ? "" : intent.getStringExtra("parentActivity");
		ticketType = intent.getStringExtra("ticketType") == null ? "need" : intent.getStringExtra("ticketType");
		long ticketId = intent.getLongExtra("ticketId", -1);
		
		Bundle args = new Bundle();
		args.putLong("ticketId", ticketId);
		
		Fragment ticketPageFragment;
		if (ticketType.equals("offer")) {
			ticketPageFragment = new NeedPageFragment();
		} else {
			ticketPageFragment = new NeedPageFragment();
		}
		ticketPageFragment.setArguments(args);
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(android.R.id.content, ticketPageFragment);
		transaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ticket_page, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		if (parentActivity != null && parentActivity.equals("TicketFormActivity")) {
			Intent intent = new Intent(this, TicketListActivity.class);
			intent.putExtra("ticketType", ticketType);
			startActivity(intent);
		} else {
			super.onBackPressed();
		}
	}

}
