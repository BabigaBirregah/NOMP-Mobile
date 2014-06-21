package fr.utt.isi.nomp_mobile.fragments.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fr.utt.isi.nomp_mobile.R;
import fr.utt.isi.nomp_mobile.models.Offer;
import fr.utt.isi.nomp_mobile.models.Ticket;

public class OfferPageFragment extends TicketPageFragment {

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
		Offer offer = (Offer) getTicket(ticketId);
		
		// set particular view of need
		TextView costLabelView = (TextView) view.findViewById(R.id.label_price);
		costLabelView.setText(R.string.text_label_price_cost);
		TextView costView = (TextView) view.findViewById(R.id.ticket_price);
		costView.setText("" + offer.getCost());

		return view;
	}

	@Override
	protected Ticket getTicket(long ticketId) {
		Offer offer = new Offer(getActivity());
		return offer.retrieve(ticketId);
	}
}
