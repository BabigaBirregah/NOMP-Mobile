package fr.utt.isi.nomp_mobile.activities;

import java.util.List;

import fr.utt.isi.nomp_mobile.R;
import fr.utt.isi.nomp_mobile.adapters.TicketListArrayAdapter;
import fr.utt.isi.nomp_mobile.models.ActorType;
import fr.utt.isi.nomp_mobile.models.Classification;
import fr.utt.isi.nomp_mobile.models.Need;
import fr.utt.isi.nomp_mobile.models.Offer;
import fr.utt.isi.nomp_mobile.models.Ticket;
import fr.utt.isi.nomp_mobile.tasks.PauserTask;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TicketListActivity extends ActionBarActivity implements
		OnItemClickListener {

	public static final String TAG = "TicketListActivity";

	private String ticketType;

	private List<?> ticketList;

	private static ProgressDialog loading = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ticket_list);

		Intent intent = getIntent();
		ticketType = intent.getStringExtra("ticketType") == null ? Ticket.TICKET_NEED
				: intent.getStringExtra("ticketType");

		// get ticket list
		if (ticketType.equals(Ticket.TICKET_OFFER)) {
			ticketList = new Offer(this).list(true);
		} else {
			ticketList = new Need(this).list(true);
		}

		// get ticket list view
		final ListView ticketListView = (ListView) findViewById(R.id.ticket_list);

		// create adapter for list view
		final ArrayAdapter<Ticket> ticketListAdapter = new TicketListArrayAdapter(
				this, R.layout.ticket_list_element, ticketList);

		ticketListView.setAdapter(ticketListAdapter);
		ticketListView.setOnItemClickListener(this);

		// ensure the display of classifications and actor types
		new Classification(TicketListActivity.this).checkUpdate();
		new ActorType(TicketListActivity.this).checkUpdate();
		loading = new ProgressDialog(this);
		loading.setTitle("Loading");
		loading.setMessage("Please wait");
		loading.show();
		new PauserTask() {

			@Override
			public void onPostExecute(Void result) {
				if (loading != null) {
					loading.dismiss();
				}

				ticketListAdapter.notifyDataSetChanged();
				ticketListView.invalidate();
			}

		}.execute(2000);
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
