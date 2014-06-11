package fr.utt.isi.nomp_mobile.fragments.forms;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import fr.utt.isi.nomp_mobile.R;
import fr.utt.isi.nomp_mobile.models.ActorType;
import fr.utt.isi.nomp_mobile.models.Classification;
import fr.utt.isi.nomp_mobile.models.Status;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public abstract class TicketFormFragment extends Fragment {

	public static final String TAG = "TicketFormFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_ticket_form, container,
				false);

		setHasOptionsMenu(true);

		// classification update via api (just for dev)
		Button buttonClassification = (Button) view
				.findViewById(R.id.button_classification);
		buttonClassification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Classification classification = new Classification(
						getActivity());
				classification.apiGet();
			}

		});

		// classification drop down list
		Classification classification = new Classification(getActivity());
		ArrayList<Classification> parentClassifications = (ArrayList<Classification>) classification
				.parentList();
		ArrayList<String> classifications = new ArrayList<String>();
		classifications.add("Select a classification");

		for (int i = 0; i < parentClassifications.size(); i++) {
			classifications.add(parentClassifications.get(i).getName());
		}

		// set adapter for spinner
		ArrayAdapter<String> spinnerClassificationAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				classifications);
		spinnerClassificationAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spinnerClassification = (Spinner) view
				.findViewById(R.id.spinner_classification);
		spinnerClassification.setAdapter(spinnerClassificationAdapter);

		// actor type update via api (just for dev)
		Button buttonTarget = (Button) view.findViewById(R.id.button_target);
		buttonTarget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				ActorType actorType = new ActorType(getActivity());
				actorType.apiGet();
			}

		});

		// classification drop down list
		ActorType actorType = new ActorType(getActivity());
		ArrayList<ActorType> parentActorTypes = (ArrayList<ActorType>) actorType
				.parentList();
		ArrayList<String> actorTypes = new ArrayList<String>();
		actorTypes.add("Select an actor type");

		for (int i = 0; i < parentActorTypes.size(); i++) {
			actorTypes.add(parentActorTypes.get(i).getName());
		}

		// set adapter for spinner
		ArrayAdapter<String> spinnerTargetAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item, actorTypes);
		spinnerTargetAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spinnerTarget = (Spinner) view
				.findViewById(R.id.spinner_target);
		spinnerTarget.setAdapter(spinnerTargetAdapter);

		// assign actions on period buttons to show date picker
		Button buttonPeriodFrom = (Button) view
				.findViewById(R.id.button_period_from);
		Button buttonPeriodTo = (Button) view
				.findViewById(R.id.button_period_to);
		buttonPeriodFrom.setOnClickListener(new ButtonPeriodOnClickListener(
				R.id.button_period_from));
		buttonPeriodTo.setOnClickListener(new ButtonPeriodOnClickListener(
				R.id.button_period_to));

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_ticket_form, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_send:
			long ticketId = storeTicket();
			if (ticketId != -1) {
				displayTicket(ticketId);
			} else {
				Toast errorToast = Toast
						.makeText(
								getActivity(),
								"An error occured while adding ticket. Please check your information.",
								Toast.LENGTH_LONG);
				errorToast.show();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected ContentValues getBaseFieldValues() {
		Activity context = getActivity();

		EditText nameView = (EditText) context.findViewById(R.id.title);
		String name = nameView.getText().toString();

		EditText descriptionView = (EditText) context
				.findViewById(R.id.description);
		String description = descriptionView.getText().toString();

		String keywords = "";

		String classification = "s1d2f3";
		String classificationName = "test";

		String sourceActorType = "s1d2f3";
		String sourceActorTypeName = "test";

		String targetActorType = "s1d2f3";
		String targetActorTypeName = "test";

		String contactPhone = "+33674312347";
		String contactMobile = "+33674312347";
		String contactEmail = "yipeng.huang@utt.fr";

		// Calendar creationDate = new GregorianCalendar();
		// Calendar expirationDate = new GregorianCalendar();
		// expirationDate.add(Calendar.MONTH, 3);
		// Calendar updateDate = new GregorianCalendar();

		// Dates
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
		Button buttonPeriodFrom = (Button) context
				.findViewById(R.id.button_period_from);
		String startDate = (String) buttonPeriodFrom.getText();
		try {
			startDate = dateFormat.format(dateFormat.parse(startDate));
		} catch (ParseException e1) {
			// TODO: handle error
		}

		Button buttonPeriodTo = (Button) context
				.findViewById(R.id.button_period_to);
		String endDate = (String) buttonPeriodTo.getText();
		try {
			endDate = dateFormat.format(dateFormat.parse(endDate));
		} catch (ParseException e) {
			// TODO: handle error
		}

		EditText quantityView = (EditText) context.findViewById(R.id.quantity);
		int quantity = Integer.parseInt(quantityView.getText().toString());

		String geometry = "4.08, 48.3";
		String address = "sb 10000 troyes";

		boolean isActive = true;
		int statut = Status.OPEN;
		String reference = null;
		String user = null;
		String matched = null;

		ContentValues baseValues = new ContentValues();
		baseValues.put("name", name);
		baseValues.put("description", description);
		baseValues.put("keywords", keywords);
		baseValues.put("classification", classification);
		baseValues.put("classificationName", classificationName);
		baseValues.put("sourceActorType", sourceActorType);
		baseValues.put("sourceActorTypeName", sourceActorTypeName);
		baseValues.put("targetActorType", targetActorType);
		baseValues.put("targetActorTypeName", targetActorTypeName);
		baseValues.put("contactPhone", contactPhone);
		baseValues.put("contactMobile", contactMobile);
		baseValues.put("contactEmail", contactEmail);
		// baseValues.put("creationDate", creationDate.toString());
		baseValues.put("startDate", startDate);
		baseValues.put("endDate", endDate);
		// baseValues.put("expirationDate", expirationDate.toString());
		// baseValues.put("updateDate", updateDate.toString());
		baseValues.put("quantity", quantity);
		baseValues.put("geometry", geometry);
		baseValues.put("address", address);
		baseValues.put("isActive", isActive);
		baseValues.put("statut", statut);
		baseValues.put("reference", reference);
		baseValues.put("user", user);
		baseValues.put("matched", matched);

		return baseValues;
	}

	public abstract long storeTicket();

	public abstract void displayTicket(long ticketId);

	protected class ButtonPeriodOnClickListener implements OnClickListener {

		private int reactionButtonId;
		private DialogFragment datePickerFragment;

		public ButtonPeriodOnClickListener(int reactionViewId) {
			super();
			this.reactionButtonId = reactionViewId;
			this.datePickerFragment = new DatePickerFragment(reactionButtonId);
		}

		@Override
		public void onClick(View parent) {
			datePickerFragment.show(getActivity().getSupportFragmentManager(),
					"datePicker");
		}

	}

	@SuppressLint("ValidFragment")
	protected class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		public static final String TAG = "DatePicker";

		private int year;
		private int month;
		private int day;

		private int reactionButtonId;

		public DatePickerFragment(int reactionButtonId) {
			super();

			// set current date in the picker
			final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);

			this.reactionButtonId = reactionButtonId;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			this.year = year;
			this.month = month;
			this.day = day;
			Calendar date = new GregorianCalendar(year, month, day);
			Log.d(TAG, date.toString());

			Button reactionButton = (Button) getActivity().findViewById(
					reactionButtonId);
			reactionButton.setText(getDate());
		}

		public String getDate() {
			Calendar date = new GregorianCalendar(year, month, day);
			DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
			return dateFormat.format(date.getTime());
		}

	}

}
