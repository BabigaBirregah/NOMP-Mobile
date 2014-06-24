package fr.utt.isi.nomp_mobile.models;

import java.text.DateFormat;
import java.text.ParseException;
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
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class Need extends Ticket {

	public static final String TAG = "Need";

	private int budget;

	public Need(Context context) {
		super(context);
		this.budget = 0;
	}

	/**
	 * Constructor without _id, nompId and budget
	 * 
	 * @param context
	 * @param name
	 * @param classification
	 * @param classificationName
	 * @param sourceActorType
	 * @param sourceActorTypeName
	 * @param targetActorType
	 * @param targetActorTypeName
	 * @param contactPhone
	 * @param contactMobile
	 * @param contactEmail
	 * @param quantity
	 * @param description
	 * @param keywords
	 * @param address
	 * @param geometry
	 * @param creationDate
	 * @param endDate
	 * @param startDate
	 * @param expirationDate
	 * @param updateDate
	 * @param isActive
	 * @param statut
	 * @param reference
	 * @param user
	 * @param matched
	 */
	public Need(Context context, String name, String classification,
			String classificationName, String sourceActorType,
			String sourceActorTypeName, String targetActorType,
			String targetActorTypeName, String contactPhone,
			String contactMobile, String contactEmail, int quantity,
			String description, String keywords, String address,
			String geometry, String creationDate, String endDate,
			String startDate, String expirationDate, String updateDate,
			boolean isActive, int statut, String reference, String user,
			String matched) {
		super(context, name, classification, classificationName,
				sourceActorType, sourceActorTypeName, targetActorType,
				targetActorTypeName, contactPhone, contactMobile, contactEmail,
				quantity, description, keywords, address, geometry,
				creationDate, endDate, startDate, expirationDate, updateDate,
				isActive, statut, reference, user, matched);
		this.budget = 0;
	}

	public Need(Context context, String name, String classification,
			String classificationName, String sourceActorType,
			String sourceActorTypeName, String targetActorType,
			String targetActorTypeName, String contactPhone,
			String contactMobile, String contactEmail, int quantity,
			String description, String keywords, String address,
			String geometry, String creationDate, String endDate,
			String startDate, String expirationDate, String updateDate,
			boolean isActive, int statut, String reference, String user,
			String matched, int budget) {
		super(context, name, classification, classificationName,
				sourceActorType, sourceActorTypeName, targetActorType,
				targetActorTypeName, contactPhone, contactMobile, contactEmail,
				quantity, description, keywords, address, geometry,
				creationDate, endDate, startDate, expirationDate, updateDate,
				isActive, statut, reference, user, matched);
		this.budget = budget;
	}

	public Need(Context context, String nompId, String name,
			String classification, String classificationName,
			String sourceActorType, String sourceActorTypeName,
			String targetActorType, String targetActorTypeName,
			String contactPhone, String contactMobile, String contactEmail,
			int quantity, String description, String keywords, String address,
			String geometry, String creationDate, String endDate,
			String startDate, String expirationDate, String updateDate,
			boolean isActive, int statut, String reference, String user,
			String matched, int budget) {
		super(context, nompId, name, classification, classificationName,
				sourceActorType, sourceActorTypeName, targetActorType,
				targetActorTypeName, contactPhone, contactMobile, contactEmail,
				quantity, description, keywords, address, geometry,
				creationDate, endDate, startDate, expirationDate, updateDate,
				isActive, statut, reference, user, matched);
		this.budget = budget;
	}

	public Need(Context context, long _id, String nompId, String name,
			String classification, String classificationName,
			String sourceActorType, String sourceActorTypeName,
			String targetActorType, String targetActorTypeName,
			String contactPhone, String contactMobile, String contactEmail,
			int quantity, String description, String keywords, String address,
			String geometry, String creationDate, String endDate,
			String startDate, String expirationDate, String updateDate,
			boolean isActive, int statut, String reference, String user,
			String matched, int budget) {
		super(context, _id, nompId, name, classification, classificationName,
				sourceActorType, sourceActorTypeName, targetActorType,
				targetActorTypeName, contactPhone, contactMobile, contactEmail,
				quantity, description, keywords, address, geometry,
				creationDate, endDate, startDate, expirationDate, updateDate,
				isActive, statut, reference, user, matched);
		this.budget = budget;
	}
	
	@Override
	public String getTicketType() {
		return Ticket.TICKET_NEED;
	}

	@Override
	public String getTableName() {
		return NOMPDataContract.Need.TABLE_NAME;
	}

	@Override
	public int getPrice() {
		return budget;
	}

	@Override
	public long store() {
		// check if the need is already in database
		long existence = exists(this.nompId);
		if (existence != -1) {
			return existence;
		}
		
		// build the content values for insert
		ContentValues mContentValues = getBaseContentValues();
		mContentValues.put(NOMPDataContract.Need.COLUMN_NAME_BUDGET, budget);

		// insert
		SQLiteDatabase writable = this.getWritableDatabase();
		_id = writable.insert(NOMPDataContract.Need.TABLE_NAME, null,
				mContentValues);

		writable.close();
		return _id;
	}
	
	public Need retrieve(String ticketNompId) {
		Cursor c = retrieveBase(ticketNompId);

		if (c.moveToFirst()) {
			this.setBudget(c.getInt(c
					.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_BUDGET)));
		} else {
			return null;
		}
		
		c.close();

		return this;
	}

	@Override
	public Need retrieve(long ticketId) {
		Cursor c = retrieveBase(ticketId);

		if (c.moveToFirst()) {
			this.setBudget(c.getInt(c
					.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_BUDGET)));
		} else {
			return null;
		}
		
		c.close();

		return this;
	}

	@Override
	public List<Need> list() {
		return list(false);
	}

	public List<Need> list(boolean isForMe) {
		// prepare the query
		String query = "SELECT * FROM " + NOMPDataContract.Need.TABLE_NAME;
		if (isForMe) {
			SharedPreferences userInfo = context.getSharedPreferences(
					Config.PREF_NAME_USER, Context.MODE_PRIVATE);
			if (userInfo.getBoolean(Config.PREF_KEY_USER_IS_LOGGED, false)) {
				query += " WHERE user='"
						+ userInfo
								.getString(Config.PREF_KEY_USER_NOMP_ID, "-1")
						+ "'";
			}
		}
		query += " ORDER BY _id DESC";

		SQLiteDatabase readable = this.getReadableDatabase();
		Cursor c = readable.rawQuery(query, null);

		ArrayList<Need> needList = new ArrayList<Need>(c.getCount());

		if (c.moveToFirst()) {
			do {
				Need need = new Need(context);

				need.setNompId(c.getString(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_NOMP_ID)));

				need.set_id(c.getInt(c
						.getColumnIndex(NOMPDataContract.Need._ID)));

				need.setName(c.getString(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_NAME)));

				need.setClassification(c.getString(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_CLASSIFICATION)));
				need.setClassificationName(c.getString(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_CLASSIFICATION_NAME)));

				need.setSourceActorType(c.getString(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_SOURCE_ACTOR_TYPE)));
				need.setSourceActorTypeName(c.getString(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_SOURCE_ACTOR_TYPE_NAME)));
				need.setTargetActorType(c.getString(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_TARGET_ACTOR_TYPE)));
				need.setTargetActorTypeName(c.getString(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_TARGET_ACTOR_TYPE_NAME)));

				need.setContactPhone(c.getString(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_CONTACT_PHONE)));
				need.setContactMobile(c.getString(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_CONTACT_MOBILE)));
				need.setContactEmail(c.getString(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_CONTACT_EMAIL)));

				need.setQuantity(c.getInt(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_QUANTITY)));
				need.setBudget(c.getInt(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_BUDGET)));

				need.setDescription(c.getString(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_DESCRIPTION)));
				need.setKeywords(c.getString(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_KEYWORDS)));

				need.setAddress(c.getString(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_ADDRESS)));
				need.setGeometry(c.getString(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_GEOMETRY)));

				// DATES
				need.setCreationDate(c.getString(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_CREATION_DATE)));
				need.setEndDate(c.getString(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_END_DATE)));
				need.setStartDate(c.getString(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_START_DATE)));
				need.setExpirationDate(c.getString(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_EXPIRATION_DATE)));
				need.setUpdateDate(c.getString(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_UPDATE_DATE)));

				need.setActive(c.getInt(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_IS_ACTIVE)) == 1 ? true
						: false);

				need.setStatut(c.getInt(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_STATUT)));

				need.setReference(c.getString(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_REFERENCE)));

				need.setUser(c.getString(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_USER)));

				need.setMatched(c.getString(c
						.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_MATCHED)));

				needList.add(need);
				need = null;
			} while (c.moveToNext());
		}
		c.close();
		readable.close();

		return needList;
	}

	@Override
	public int update(ContentValues values) {
		if (_id == -1) {
			return 0;
		}

		// TODO: check the content value input

		SQLiteDatabase writable = this.getWritableDatabase();
		int nbLines = writable.update(NOMPDataContract.Need.TABLE_NAME, values,
				"_id=?", new String[] { "" + _id });

		writable.close();
		return nbLines;
	}

	@Override
	public int delete() {
		SQLiteDatabase writable = this.getWritableDatabase();
		int nbLines = writable.delete(NOMPDataContract.Need.TABLE_NAME,
				"_id=?", new String[] { "" + _id });

		writable.close();
		return nbLines;
	}

	public void apiGet() {
		SharedPreferences userInfo = context.getSharedPreferences(
				Config.PREF_NAME_USER, Context.MODE_PRIVATE);
		String userNompId = userInfo
				.getString(Config.PREF_KEY_USER_NOMP_ID, "");

		new GetRequestTask(context) {

			@Override
			public void onPostExecute(String result) {
				if (result == null) {
					Toast errorToast = Toast.makeText(context,
							"Failed to request needs on server.",
							Toast.LENGTH_LONG);
					errorToast.show();
				} else {

					try {
						JSONArray jsonArray = new JSONArray(result);
						if (jsonArray.length() > 0) {
							Need[] needs = new Need[jsonArray.length()];
							for (int i = 0; i < jsonArray.length(); i++) {
								// parse json object
								JSONObject jsonObject = jsonArray
										.getJSONObject(i);

								needs[i] = parseJson(jsonObject);
							}

							// delete all data from database to simply avoid
							// update/insert decisions
							deleteAll();
							needs = (Need[]) insertAll(needs);
						} else {
							Toast errorToast = Toast.makeText(context,
									"No available needs on server.",
									Toast.LENGTH_LONG);
							errorToast.show();
						}
					} catch (JSONException e) {
						Toast errorToast = Toast.makeText(context,
								"Failed to parse response from server.",
								Toast.LENGTH_LONG);
						errorToast.show();
						e.printStackTrace();
					}

				}

			}

		}.execute(Config.NOMP_API_ROOT + "user/" + userNompId + "/need/list");
	}

	public Need parseJson(JSONObject jsonObject) {
		Need need = new Need(context);
		need.setNompId(jsonObject.optString("_id"));
		need.setName(jsonObject.optString("name"));
		need.setClassification(jsonObject.optString("classification"));
		need.setClassificationName(jsonObject.optString("classification_name"));
		need.setSourceActorType(jsonObject.optString("source_actor_type"));
		need.setSourceActorTypeName(jsonObject
				.optString("source_actor_type_name"));
		need.setTargetActorType(jsonObject.optString("target_actor_type"));
		need.setTargetActorTypeName(jsonObject
				.optString("target_actor_type_name"));
		need.setContactPhone(jsonObject.optString("contact_phone"));
		need.setContactMobile(jsonObject.optString("contact_mobile"));
		need.setContactEmail(jsonObject.optString("contact_email"));
		need.setQuantity(Integer.parseInt(jsonObject.optString("quantity") == null ? "0" : jsonObject.optString("quantity")));
		need.setDescription(jsonObject.optString("description"));
		need.setKeywords(jsonObject.optString("keywords"));
		need.setAddress(jsonObject.optString("address"));
		need.setGeometry(jsonObject.optString("geometry"));

		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
		try {
			need.setCreationDate(dateFormat.format(dateFormat.parse(jsonObject
					.optString("creation_date"))));
			need.setEndDate(dateFormat.format(dateFormat.parse(jsonObject
					.optString("end_date"))));
			need.setStartDate(dateFormat.format(dateFormat.parse(jsonObject
					.optString("start_date"))));
			need.setExpirationDate(dateFormat.format(dateFormat
					.parse(jsonObject.optString("expiration_date"))));
			need.setUpdateDate(dateFormat.format(dateFormat.parse(jsonObject
					.optString("update_date"))));
		} catch (ParseException e) {

		}

		// should be always true, or do
		// jsonObject.optString("is_active").equals("true")
		// ? true : false
		need.setActive(true);
		need.setStatut(Integer.parseInt(jsonObject.optString("statut")));
		need.setReference(jsonObject.optString("reference"));
		need.setUser(jsonObject.optString("user"));
		need.setMatched(jsonObject.optString("matched"));

		need.setBudget(Integer.parseInt(jsonObject.optString("budget") == null ? "0" : jsonObject.optString("budget")));

		return need;
	}

	public int getBudget() {
		return budget;
	}

	public void setBudget(int budget) {
		this.budget = budget;
	}

}
