package fr.utt.isi.nomp_mobile.fragments.forms;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import fr.utt.isi.nomp_mobile.R;
import fr.utt.isi.nomp_mobile.activities.TicketPageActivity;
import fr.utt.isi.nomp_mobile.models.Offer;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class OfferFormFragment extends TicketFormFragment {

	public static final String TAG = "OfferFormFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		
		// update field label for cost
		TextView priceLabelView = (TextView) view
				.findViewById(R.id.label_price);
		priceLabelView.setText(R.string.text_label_price_cost);

		return view;
	}

	@Override
	public long storeTicket() {
		// get basic common fields
		ContentValues baseValues = getBaseFieldValues();
		
		if (baseValues == null) {
			return -1;
		}

		// build dates
		Calendar currentCalendar = new GregorianCalendar();
		String creationDate = DateFormat.getDateInstance(DateFormat.MEDIUM)
				.format(currentCalendar.getTime());
		String updateDate = creationDate;
		currentCalendar.add(Calendar.MONTH, 3);
		String expirationDate = DateFormat.getDateInstance(DateFormat.MEDIUM)
				.format(currentCalendar.getTime());

		// parse start/end date
		String startDate = baseValues.getAsString("startDate");
		String endDate = baseValues.getAsString("endDate");

		// TODO: validate start/end date

		// get budget
		EditText costView = (EditText) getActivity().findViewById(R.id.price);
		int cost = Integer.parseInt(costView.getText().toString());

		// build need
		Offer offer = new Offer(getActivity(), baseValues.getAsString("name"),
				baseValues.getAsString("classification"),
				baseValues.getAsString("classificationName"),
				baseValues.getAsString("sourceActorType"),
				baseValues.getAsString("sourceActorTypeName"),
				baseValues.getAsString("targetActorType"),
				baseValues.getAsString("targetActorTypeName"),
				baseValues.getAsString("contactPhone"),
				baseValues.getAsString("contactMobile"),
				baseValues.getAsString("contactEmail"),
				baseValues.getAsInteger("quantity"),
				baseValues.getAsString("description"),
				baseValues.getAsString("keywords"),
				baseValues.getAsString("address"),
				baseValues.getAsString("geometry"), creationDate, endDate,
				startDate, expirationDate, updateDate,
				baseValues.getAsBoolean("isActive"),
				baseValues.getAsInteger("statut"),
				baseValues.getAsString("reference"),
				baseValues.getAsString("user"),
				baseValues.getAsString("matched"), cost);

		// store need in local database
		long _id = offer.store();
		Log.d(TAG, "_id inserted=" + _id);

		return _id;

	}
	
	@Override
	public void displayTicket(long ticketId) {
		Intent intent = new Intent(getActivity().getBaseContext(), TicketPageActivity.class);
		
		// put arguments for display, eventually the ticket type and ticket id
		intent.putExtra("parentActivity", "TicketFormActivity");
		intent.putExtra("ticketType", "offer");
		intent.putExtra("ticketId", ticketId);
		
		// start the page activity for display
		getActivity().startActivity(intent);
	}

}
