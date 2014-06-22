package fr.utt.isi.nomp_mobile.fragments.forms;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.json.JSONException;
import org.json.JSONObject;

import fr.utt.isi.nomp_mobile.R;
import fr.utt.isi.nomp_mobile.activities.TicketPageActivity;
import fr.utt.isi.nomp_mobile.config.Config;
import fr.utt.isi.nomp_mobile.models.Offer;
import fr.utt.isi.nomp_mobile.tasks.PostRequestTask;
import fr.utt.isi.nomp_mobile.tasks.RequestTask;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
	public void storeTicket() {
		// get basic common fields
		ContentValues baseValues = getBaseFieldValues();

		if (baseValues == null) {
			return;
		}

		// build dates
		Calendar currentCalendar = new GregorianCalendar();
		String creationDate = DateFormat.getDateInstance(
				DateFormat.MEDIUM).format(currentCalendar.getTime());
		String updateDate = creationDate;
		currentCalendar.add(Calendar.MONTH, 3);
		String expirationDate = DateFormat.getDateInstance(
				DateFormat.MEDIUM).format(currentCalendar.getTime());

		// get budget
		EditText costView = (EditText) getActivity().findViewById(R.id.price);
		int cost = Integer.parseInt(costView.getText().toString());

		// clean the key/values to prepare for POST
		// add fields
		baseValues.put("creation_date", creationDate);
		baseValues.put("expiration_date", expirationDate);
		baseValues.put("update_date", updateDate);
		baseValues.put("cost", cost);

		// remove unexpected fields
		baseValues.remove("keywords");
		baseValues.remove("reference");
		baseValues.remove("matched");

		// build JSON string for geometry
		String[] latlon = baseValues.getAsString("geometry").split(",");
		baseValues.put("geometry", "{\"lat\":" + latlon[0] + ",\"lon\":"
				+ latlon[1] + "}");
		Log.d(TAG, "content for post: " + baseValues);

		loading = new ProgressDialog(getActivity());
		loading.setTitle("Loading");
		loading.setMessage("Please wait");
		loading.show();

		new PostRequestTask(getActivity(), baseValues) {

			@Override
			protected void onPostExecute(String response) {
				// TODO: parse response
				String offerNompId = "";

				new RequestTask(getContext(), "GET") {

					@Override
					public void onPostExecute(String result) {
						try {
							// parse result as json object
							JSONObject jsonObject = new JSONObject(result);

							// parse json object as offer object
							Offer offer = new Offer(getContext())
									.parseJson(jsonObject);

							// store offer in database
							long offerId = offer.store();

							if (offerId != -1) {
								displayTicket(offerId);
							} else {
								Toast errorToast = Toast
										.makeText(
												getActivity(),
												"An error occured while adding ticket. Please check your information.",
												Toast.LENGTH_LONG);
								errorToast.show();
							}
						} catch (JSONException e) {
							Toast errorToast = Toast.makeText(getContext(),
									"Failed to parse response from server.",
									Toast.LENGTH_LONG);
							errorToast.show();
							e.printStackTrace();
						} finally {
							if (loading != null) {
								loading.dismiss();
							}
						}
					}

				}.execute(Config.NOMP_API_ROOT + "offer/" + offerNompId
						+ "/json");

			}

		}.execute(Config.NOMP_API_ROOT + "offer/create");

	}

	@Override
	public void displayTicket(long ticketId) {
		Intent intent = new Intent(getActivity().getBaseContext(),
				TicketPageActivity.class);

		// put arguments for display, eventually the ticket type and ticket id
		intent.putExtra("parentActivity", "TicketFormActivity");
		intent.putExtra("ticketType", "offer");
		intent.putExtra("ticketId", ticketId);

		// start the page activity for display
		getActivity().startActivity(intent);
	}

}
