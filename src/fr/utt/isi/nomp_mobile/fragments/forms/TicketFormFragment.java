package fr.utt.isi.nomp_mobile.fragments.forms;

import java.util.Calendar;

import fr.utt.isi.nomp_mobile.R;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

public class TicketFormFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_ticket_form, container,
				false);
		
		// assign actions on period buttons to show date picker
		Button buttonPeriodFrom = (Button) view.findViewById(R.id.button_period_from);
		Button buttonPeriodTo = (Button) view.findViewById(R.id.button_period_to);
		buttonPeriodFrom.setOnClickListener(new ButtonPeriodOnClickListener(R.id.button_period_from));
		buttonPeriodTo.setOnClickListener(new ButtonPeriodOnClickListener(R.id.button_period_to));

		return view;
	}
	
	private class ButtonPeriodOnClickListener implements OnClickListener {
		
		private int reactionButtonId;
		private DialogFragment datePickerFragment;
		
		public ButtonPeriodOnClickListener(int reactionViewId) {
			super();
			this.reactionButtonId = reactionViewId;
			this.datePickerFragment = new DatePickerFragment(reactionButtonId);
		}

		@Override
		public void onClick(View parent) {
			datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
		}
		
	}
	
	@SuppressLint("ValidFragment")
	private class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
		
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
			Log.d(TAG, day + "-" + month + "-" + year);
			
			Button reactionButton = (Button) getActivity().findViewById(reactionButtonId);
			reactionButton.setText(getDate());
		}
		
		public String getDate() {
			return day + "-" + "0" + (month + 1) + "-" + year;
		}

	}

}
