package fr.utt.isi.nomp_mobile.fragments.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fr.utt.isi.nomp_mobile.R;
import fr.utt.isi.nomp_mobile.models.Need;
import fr.utt.isi.nomp_mobile.models.Ticket;

public class NeedPageFragment extends TicketPageFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);

		// get arguments
		Bundle args = getArguments();
		long ticketId = args.getLong("ticketId");

		// get ticket by id
		Need need = (Need) getTicket(ticketId);
		
		// set particular view of need
		TextView budgetLabelView = (TextView) view.findViewById(R.id.label_price);
		budgetLabelView.setText(R.string.text_label_price_budget);
		TextView budgetView = (TextView) view.findViewById(R.id.ticket_price);
		budgetView.setText("" + need.getBudget());

		return view;
	}

	@Override
	protected Ticket getTicket(long ticketId) {
		Need need = new Need(getActivity());
		return need.retrieve(ticketId);
	}

}
