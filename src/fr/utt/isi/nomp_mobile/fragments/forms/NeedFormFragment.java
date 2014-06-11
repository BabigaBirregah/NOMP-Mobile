package fr.utt.isi.nomp_mobile.fragments.forms;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import fr.utt.isi.nomp_mobile.R;
import fr.utt.isi.nomp_mobile.activities.TicketPageActivity;
import fr.utt.isi.nomp_mobile.models.Need;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class NeedFormFragment extends TicketFormFragment {

	public static final String TAG = "NeedFormFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		
		// update field label for budget
		TextView priceLabelView = (TextView) view
				.findViewById(R.id.label_price);
		priceLabelView.setText(R.string.text_label_price_budget);

		// update field placeholder for budget
		EditText priceView = (EditText) view.findViewById(R.id.price);
		priceView.setHint(R.string.text_hint_price_budget);

		return view;
	}

	@Override
	public long storeTicket() {
		// get basic common fields
		ContentValues baseValues = getBaseFieldValues();

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
		EditText budgetView = (EditText) getActivity().findViewById(R.id.price);
		int budget = Integer.parseInt(budgetView.getText().toString());

		// build need
		Need need = new Need(getActivity(), baseValues.getAsString("name"),
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
				baseValues.getAsString("matched"), budget);

		// store need in local database
		long _id = need.store();
		Log.d(TAG, "_id inserted=" + _id);

		return _id;

	}
	
	@Override
	public void displayTicket(long ticketId) {
		Intent intent = new Intent(getActivity().getBaseContext(), TicketPageActivity.class);
		
		// put arguments for display, eventually the ticket id
		intent.putExtra("ticketId", ticketId);
		
		// start the page activity for display
		getActivity().startActivity(intent);
	}

}
