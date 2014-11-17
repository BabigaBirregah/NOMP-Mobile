package fr.utt.isi.nomp_mobile.activities;

import java.util.ArrayList;
import java.util.List;

import fr.utt.isi.nomp_mobile.R;
import fr.utt.isi.nomp_mobile.adapters.TicketListArrayAdapter;
import fr.utt.isi.nomp_mobile.config.Config;
import fr.utt.isi.nomp_mobile.models.ActorType;
import fr.utt.isi.nomp_mobile.models.Classification;
import fr.utt.isi.nomp_mobile.models.Need;
import fr.utt.isi.nomp_mobile.models.Offer;
import fr.utt.isi.nomp_mobile.models.Ticket;
import fr.utt.isi.nomp_mobile.tasks.PauserTask;
import fr.utt.isi.nomp_mobile.tasks.RequestTask;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TicketListActivity extends ActionBarActivity implements
		OnItemClickListener {

	public static final String TAG = "TicketListActivity";

	private String ticketType;

	private long[] ticketIds = null;

	private List<?> ticketList;

	private static ProgressDialog loading = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ticket_list);

		Intent intent = getIntent();
		ticketType = intent.getStringExtra("ticketType") == null ? Ticket.TICKET_NEED
				: intent.getStringExtra("ticketType");
		ticketIds = intent.getLongArrayExtra("ticketIds");

		if (ticketIds != null) {

			setTitle(getString(R.string.title_activity_matched_ticket_list));

			// get tickets of specified ids
			if (ticketType.equals(Ticket.TICKET_OFFER)) {
				List<Offer> ticketList = new ArrayList<Offer>();

				for (int i = 0; i < ticketIds.length; i++) {
					if (ticketIds[i] != -1) {
						ticketList.add(new Offer(this).retrieve(ticketIds[i]));
					}
				}

				this.ticketList = ticketList;
			} else {
				List<Need> ticketList = new ArrayList<Need>();

				for (int i = 0; i < ticketIds.length; i++) {
					if (ticketIds[i] != -1) {
						ticketList.add(new Need(this).retrieve(ticketIds[i]));
					}
				}

				this.ticketList = ticketList;
			}

		} else {

			setTitle(getString(R.string.title_activity_ticket_list));

			// get all my ticket list
			if (ticketType.equals(Ticket.TICKET_OFFER)) {
				ticketList = new Offer(this).list(true);
			} else {
				ticketList = new Need(this).list(true);
			}

		}

		updateList();
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
		if (ticketIds == null) {
			Intent intent = new Intent(this, AccountActivity.class);
			startActivity(intent);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ticket_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_reset:
			if (ticketList.size() == 0) {
				return false;
			}

			loading.show();
			new PauserTask() {

				@Override
				public void onPostExecute(Void result) {
					if (loading != null) {
						loading.dismiss();
					}
				}

			}.execute(2000);

			StringBuilder deleteQueryBuilder = new StringBuilder();

			// base uri
			deleteQueryBuilder.append(Config.NOMP_API_ROOT);
			deleteQueryBuilder.append(ticketType);
			deleteQueryBuilder.append("/delete");

			// query arguments
			deleteQueryBuilder.append('?');
			for (int i = 0; i < ticketList.size(); i++) {
				deleteQueryBuilder.append("ticket_id=");
				deleteQueryBuilder.append(((Ticket) ticketList.get(i))
						.getNompId());
				if (i != ticketList.size() - 1) {
					deleteQueryBuilder.append('&');
				}
			}

			// api request
			new RequestTask(this, "DELETE") {

				@Override
				public void onPostExecute(String result) {
					Log.d(TAG, "request result: " + result);
				}

			}.execute(deleteQueryBuilder.toString());

			// local db deletion
			// (large number of db query, ugly but quick fix)
			for (int i = 0; i < ticketList.size(); i++) {
				if (ticketType.equals("need")) {
					((Need) ticketList.get(i)).delete();
				} else {
					((Offer) ticketList.get(i)).delete();
				}
			}

			// update ticket list
			ticketIds = null;
			ticketList.clear();
			updateList();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void updateList() {
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

		if (loading == null) {
			loading = new ProgressDialog(this);
			loading.setTitle("Loading");
			loading.setMessage("Please wait");
		}

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

}
