package fr.utt.isi.nomp_mobile.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.utt.isi.nomp_mobile.config.Config;
import fr.utt.isi.nomp_mobile.database.NOMPDataContract;
import fr.utt.isi.nomp_mobile.tasks.GetRequestTask;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.widget.Toast;

public class Matching extends BaseModel {

	public static final String TAG = "Matching";

	private long _id;

	private String nompId;

	private String sourceId;

	private String sourceType;

	private boolean isMatch;

	private String results;

	public Matching(Context context) {
		super(context);

		this._id = -1;
		this.nompId = null;
		this.sourceId = "";
		this.sourceType = "";
		this.isMatch = false;
		this.results = "";
	}

	public void apiGet(final String ticketType, final String ticketNompId) {
		new GetRequestTask(context) {

			@Override
			public void onPostExecute(String response) {
				if (response == null) {
					Toast errorToast = Toast.makeText(context,
							"Failed to retrieve matching results from server",
							Toast.LENGTH_LONG);
					errorToast.show();
				} else {
					Ticket ticket;
					if (ticketType.equals(Ticket.TICKET_NEED)) {
						ticket = new Need(context).retrieve(ticketNompId);
					} else {
						ticket = new Offer(context).retrieve(ticketNompId);
					}

					if (ticket == null) {
						Toast errorToast = Toast
								.makeText(
										context,
										"Failed to find the ticket from local database",
										Toast.LENGTH_LONG);
						errorToast.show();
					} else {
						try {
							// parse result as a json array
							JSONArray jsonArray = new JSONArray(response);

							// store the number of matched tickets
							int length = jsonArray.length();

							if (length > 0) {
								// prepare an array to store matched tickets'
								// ids
								ArrayList<Long> matchedTicketIds = new ArrayList<Long>(
										length);

								// parse every matched ticket
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject jsonObject = jsonArray
											.getJSONObject(i);
									Ticket matchedTicket = null;

									// parse the json object to a ticket object
									// (reversed! need -> offer, offer -> need)
									if (ticketType.equals(Ticket.TICKET_NEED)) {
										matchedTicket = new Offer(context)
												.parseJson(jsonObject.optJSONObject("ticket"));
									} else {
										matchedTicket = new Need(context)
												.parseJson(jsonObject.optJSONObject("ticket"));
									}

									// store the matched ticket in local
									// database and the local id
									if (matchedTicket != null) {
										long matchedTicketId = matchedTicket
												.store();
										if (matchedTicketId != -1) {
											matchedTicketIds
													.add(matchedTicketId);
										}
									}
								}

								matchedTicketIds.trimToSize();

								if (matchedTicketIds.size() > 0) {
									// build the key/value pair for updating
									// "matched" field
									ContentValues updateValues = new ContentValues();
									updateValues.put("matched", TextUtils.join(
											",", matchedTicketIds));

									// update the ticket
									ticket.update(updateValues);
								}
							}
						} catch (JSONException e) {
							Toast errorToast = Toast
									.makeText(
											context,
											"Failed to parse result received from server",
											Toast.LENGTH_LONG);
							errorToast.show();
						}
					}
				}

			}

		}.execute(Config.NOMP_API_ROOT + ticketType + "/matching/"
				+ ticketNompId);
	}

	@Override
	public long store() {
		// build the content values for insert
		ContentValues mContentValues = new ContentValues();
		mContentValues.put(NOMPDataContract.Matching.COLUMN_NAME_NOMP_ID,
				nompId);
		mContentValues.put(NOMPDataContract.Matching.COLUMN_NAME_SOURCE_ID,
				sourceId);
		mContentValues.put(NOMPDataContract.Matching.COLUMN_NAME_SOURCE_TYPE,
				sourceType);
		mContentValues.put(NOMPDataContract.Matching.COLUMN_NAME_IS_MATCH,
				isMatch ? 1 : 0);
		mContentValues.put(NOMPDataContract.Matching.COLUMN_NAME_RESULTS,
				results);

		// insert
		SQLiteDatabase writable = this.getWritableDatabase();
		_id = writable.insert(NOMPDataContract.Matching.TABLE_NAME, null,
				mContentValues);

		return _id;
	}

	@Override
	public Matching retrieve(long matchingId) {
		// prepare the query
		String query = "SELECT * FROM " + NOMPDataContract.Matching.TABLE_NAME
				+ " WHERE _id=" + matchingId + " ORDER BY _id DESC LIMIT 1";

		SQLiteDatabase readable = this.getReadableDatabase();
		Cursor c = readable.rawQuery(query, null);

		if (c.moveToFirst()) {
			this.set_id(c.getInt(c
					.getColumnIndex(NOMPDataContract.Matching._ID)));
			this.setNompId(c.getString(c
					.getColumnIndex(NOMPDataContract.Matching.COLUMN_NAME_NOMP_ID)));
			this.setSourceId(c.getString(c
					.getColumnIndex(NOMPDataContract.Matching.COLUMN_NAME_SOURCE_ID)));
			this.setSourceType(c.getString(c
					.getColumnIndex(NOMPDataContract.Matching.COLUMN_NAME_SOURCE_TYPE)));
			this.setMatch(c.getInt(c
					.getColumnIndex(NOMPDataContract.Matching.COLUMN_NAME_IS_MATCH)) == 1 ? true
					: false);
			this.setResults(c.getString(c
					.getColumnIndex(NOMPDataContract.Matching.COLUMN_NAME_RESULTS)));
		}

		c.close();
		readable.close();

		return this;
	}

	@Override
	public List<?> list() {
		// prepare the query
		String query = "SELECT * FROM " + NOMPDataContract.Matching.TABLE_NAME
				+ " ORDER BY _id DESC";

		SQLiteDatabase readable = this.getReadableDatabase();
		Cursor c = readable.rawQuery(query, null);

		ArrayList<Matching> matchingList = new ArrayList<Matching>(c.getCount());
		if (c.moveToFirst()) {
			do {
				Matching matching = new Matching(context);

				matching.set_id(c.getInt(c
						.getColumnIndex(NOMPDataContract.Matching._ID)));
				matching.setNompId(c.getString(c
						.getColumnIndex(NOMPDataContract.Matching.COLUMN_NAME_NOMP_ID)));
				matching.setSourceId(c.getString(c
						.getColumnIndex(NOMPDataContract.Matching.COLUMN_NAME_SOURCE_ID)));
				matching.setSourceType(c.getString(c
						.getColumnIndex(NOMPDataContract.Matching.COLUMN_NAME_SOURCE_TYPE)));
				matching.setMatch(c.getInt(c
						.getColumnIndex(NOMPDataContract.Matching.COLUMN_NAME_IS_MATCH)) == 1 ? true
						: false);
				matching.setResults(c.getString(c
						.getColumnIndex(NOMPDataContract.Matching.COLUMN_NAME_RESULTS)));

				matchingList.add(matching);
				matching = null;
			} while (c.moveToNext());
		}
		c.close();
		readable.close();

		return matchingList;
	}

	@Override
	public int update(ContentValues values) {
		if (_id == -1) {
			return 0;
		}

		// TODO: check the content value input

		SQLiteDatabase writable = this.getWritableDatabase();
		int nbLines = writable.update(NOMPDataContract.Matching.TABLE_NAME,
				values, "_id=?", new String[] { "" + _id });

		writable.close();
		return nbLines;
	}

	@Override
	public int delete() {
		SQLiteDatabase writable = this.getWritableDatabase();
		int nbLines = writable.delete(NOMPDataContract.Matching.TABLE_NAME,
				"_id=?", new String[] { "" + _id });

		writable.close();
		return nbLines;
	}

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public String getNompId() {
		return nompId;
	}

	public void setNompId(String nompId) {
		this.nompId = nompId;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public boolean isMatch() {
		return isMatch;
	}

	public void setMatch(boolean isMatch) {
		this.isMatch = isMatch;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

}
