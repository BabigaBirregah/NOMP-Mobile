package fr.utt.isi.nomp_mobile.fragments.pages;

import fr.utt.isi.nomp_mobile.R;
import fr.utt.isi.nomp_mobile.models.Ticket;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class TicketPageFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_ticket_page, container,
				false);

		setHasOptionsMenu(true);

		// get arguments
		Bundle args = getArguments();
		long ticketId = args.getLong("ticketId");

		// get ticket by id
		Ticket ticket = getTicket(ticketId);

		if (ticket != null) {
			// set the views
			TextView nameView = (TextView) view.findViewById(R.id.ticket_title);
			nameView.setText(ticket.getName());
			
			TextView classificationNameView = (TextView) view.findViewById(R.id.ticket_classification_name);
			classificationNameView.setText(ticket.getClassificationName());
			
			TextView sourceActorTypeNameView = (TextView) view.findViewById(R.id.ticket_source_actor_type_name);
			sourceActorTypeNameView.setText(ticket.getSourceActorTypeName());
			
			TextView targetActorTypeNameView = (TextView) view.findViewById(R.id.ticket_target_actor_type_name);
			targetActorTypeNameView.setText(ticket.getTargetActorTypeName());
			
			TextView locationView = (TextView) view.findViewById(R.id.ticket_location);
			locationView.setText(ticket.getAddress()); // TODO: integrate map
			
			TextView startDateView = (TextView) view.findViewById(R.id.ticket_start_date);
			startDateView.setText(ticket.getStartDate());
			
			TextView endDateView = (TextView) view.findViewById(R.id.ticket_end_date);
			endDateView.setText(ticket.getEndDate());
			
			TextView quantityView = (TextView) view.findViewById(R.id.ticket_quantity);
			quantityView.setText("" + ticket.getQuantity());
			
			TextView descriptionView = (TextView) view.findViewById(R.id.ticket_description);
			descriptionView.setText(ticket.getDescription());
		}

		return view;
	}

	protected abstract Ticket getTicket(long ticketId);

}
