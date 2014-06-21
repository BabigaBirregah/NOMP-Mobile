package fr.utt.isi.nomp_mobile.activities;

import java.util.List;

import fr.utt.isi.nomp_mobile.R;
import fr.utt.isi.nomp_mobile.adapters.TicketListArrayAdapter;
import fr.utt.isi.nomp_mobile.models.Need;
import fr.utt.isi.nomp_mobile.models.Offer;
import fr.utt.isi.nomp_mobile.models.Ticket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TicketListActivity extends ActionBarActivity implements OnItemClickListener {
	
	public static final String TAG = "TicketListActivity";
	
	private String ticketType;
	
	private List<?> ticketList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ticket_list);
		
		Intent intent = getIntent();
		ticketType = intent.getStringExtra("ticketType") == null ? Ticket.TICKET_NEED : intent.getStringExtra("ticketType");
		
		// get ticket list
		if (ticketType.equals(Ticket.TICKET_OFFER)) {
			ticketList = new Offer(this).list(true);
		} else {
			ticketList = new Need(this).list(true);
		}
		
		// get ticket list view
		ListView ticketListView = (ListView) findViewById(R.id.ticket_list);
		
		// create adapter for list view
		ArrayAdapter<Ticket> ticketListAdapter = new TicketListArrayAdapter(this, R.layout.ticket_list_element, ticketList);
		
		ticketListView.setAdapter(ticketListAdapter);
		ticketListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Ticket ticket = (Ticket) ticketList.get(position);
		if (ticket != null) {
			Intent intent = new Intent(this, TicketPageActivity.class);
			intent.putExtra("ticketType", ticketType);
			intent.putExtra("ticketId", ticket.get_id());
			startActivity(intent);
		}
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, AccountActivity.class);
		startActivity(intent);
	}
}
