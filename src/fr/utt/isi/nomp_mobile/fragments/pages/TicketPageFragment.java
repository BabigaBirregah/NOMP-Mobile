package fr.utt.isi.nomp_mobile.fragments.pages;

import fr.utt.isi.nomp_mobile.R;
import fr.utt.isi.nomp_mobile.models.ActorType;
import fr.utt.isi.nomp_mobile.models.Classification;
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
			// name
			TextView nameView = (TextView) view.findViewById(R.id.ticket_title);
			nameView.setText(ticket.getName());

			// classification
			Classification childClassification = (Classification) (new Classification(
					getActivity()).retrieve(ticket.getClassification()));
			String classificationLabel = childClassification.getName();
			if (!childClassification.isParent()
					&& childClassification.getParent() != null) {
				Classification parentClassification = (Classification) (new Classification(
						getActivity())
						.retrieve(childClassification.getParent()));
				classificationLabel = parentClassification.getName() + " > "
						+ classificationLabel;
			}
			TextView classificationNameView = (TextView) view
					.findViewById(R.id.ticket_classification_name);
			classificationNameView.setText(classificationLabel);

			// source actor type
			ActorType childSource = (ActorType) (new ActorType(getActivity())
					.retrieve(ticket.getSourceActorType()));
			String sourceLabel = childSource.getName();
			if (!childSource.isParent() && childSource.getParent() != null) {
				ActorType parentSource = (ActorType) (new ActorType(
						getActivity()).retrieve(childSource.getParent()));
				sourceLabel = parentSource.getName() + " > " + sourceLabel;
			}
			TextView sourceActorTypeNameView = (TextView) view
					.findViewById(R.id.ticket_source_actor_type_name);
			sourceActorTypeNameView.setText(sourceLabel);

			// target actor type
			ActorType childTarget = (ActorType) (new ActorType(getActivity())
					.retrieve(ticket.getTargetActorType()));
			String targetLabel = childTarget.getName();
			if (!childTarget.isParent() && childTarget.getParent() != null) {
				ActorType parentTarget = (ActorType) (new ActorType(
						getActivity()).retrieve(childTarget.getParent()));
				targetLabel = parentTarget.getName() + " > " + targetLabel;
			}
			TextView targetActorTypeNameView = (TextView) view
					.findViewById(R.id.ticket_target_actor_type_name);
			targetActorTypeNameView.setText(targetLabel);

			// location (address)
			TextView locationView = (TextView) view
					.findViewById(R.id.ticket_location);
			locationView.setText(ticket.getAddress()); // TODO: integrate map

			// from to
			TextView periodView = (TextView) view
					.findViewById(R.id.ticket_period);
			periodView.setText(ticket.getStartDate() + " -> "
					+ ticket.getEndDate());

			// quantity
			TextView quantityView = (TextView) view
					.findViewById(R.id.ticket_quantity);
			quantityView.setText("" + ticket.getQuantity());

			// description
			TextView descriptionView = (TextView) view
					.findViewById(R.id.ticket_description);
			descriptionView.setText(ticket.getDescription());
		}

		return view;
	}

	protected abstract Ticket getTicket(long ticketId);

}
