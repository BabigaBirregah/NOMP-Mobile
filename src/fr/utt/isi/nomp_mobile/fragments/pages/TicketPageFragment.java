package fr.utt.isi.nomp_mobile.fragments.pages;

import fr.utt.isi.nomp_mobile.R;
import fr.utt.isi.nomp_mobile.activities.TicketListActivity;
import fr.utt.isi.nomp_mobile.models.ActorType;
import fr.utt.isi.nomp_mobile.models.Classification;
import fr.utt.isi.nomp_mobile.models.Matching;
import fr.utt.isi.nomp_mobile.models.Ticket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public abstract class TicketPageFragment extends Fragment {
	
	public static final String TAG = "TicketPageFragment";
	
	private Ticket ticket = null;

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
		
		// won't show icon to display matching results by default
		setHasOptionsMenu(false);

		// get arguments
		Bundle args = getArguments();
		long ticketId = args.getLong("ticketId");

		// get ticket by id
		ticket = getTicket(ticketId);

		if (ticket != null) {
			// show the icon to display matching if it's my ticket
			if (ticket.isMine()) {
				setHasOptionsMenu(true);
			}
			
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
			
			// call for update of matching
			new Matching(getActivity()).apiGet(ticket.getTicketType(), ticket.getNompId());
		}

		return view;
	}

	protected abstract Ticket getTicket(long ticketId);
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_ticket_page, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_matched:
			// refresh the item
			ticket = getTicket(ticket.get_id());
			
			if (ticket != null) {
				if (ticket.getMatched() != null && !ticket.getMatched().equals("")) {
					String[] matchedParts = ticket.getMatched().split(",");
					int length = matchedParts.length;
					
					long[] matchedTicketIds = new long[length];
					for (int i = 0; i < length; i++) {
						matchedTicketIds[i] = Long.parseLong(matchedParts[i]);
					}
					
					Intent intent = new Intent(getActivity(), TicketListActivity.class);
					
					String ticketType = Ticket.TICKET_NEED;
					if (ticket.getTicketType().equals(Ticket.TICKET_NEED)) {
						ticketType = Ticket.TICKET_OFFER;
					}
					
					intent.putExtra("ticketType", ticketType);
					intent.putExtra("ticketIds", matchedTicketIds);
					getActivity().startActivity(intent);
				} else {
					Toast infoToast = Toast.makeText(getActivity(), "No available matching results", Toast.LENGTH_SHORT);
					infoToast.show();
				}
			}
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
