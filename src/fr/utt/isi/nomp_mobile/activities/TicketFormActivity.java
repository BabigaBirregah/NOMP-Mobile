package fr.utt.isi.nomp_mobile.activities;

import fr.utt.isi.nomp_mobile.R;
import fr.utt.isi.nomp_mobile.fragments.forms.NeedFormFragment;
import fr.utt.isi.nomp_mobile.fragments.forms.OfferFormFragment;
import fr.utt.isi.nomp_mobile.models.Ticket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

public class TicketFormActivity extends ActionBarActivity {
	
	public static final String TAG = "TicketFormActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ticket_form);
		
		Intent intent = getIntent();
		String ticketType = intent.getStringExtra("ticketType") == null ? Ticket.TICKET_NEED : intent.getStringExtra("ticketType");
		
		Fragment ticketFormFragment;
		if (ticketType.equals(Ticket.TICKET_OFFER)) {
			ticketFormFragment = new OfferFormFragment();
		} else {
			ticketFormFragment = new NeedFormFragment();
		}
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(android.R.id.content, ticketFormFragment);
		transaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ticket_form, menu);
		return true;
	}

}
