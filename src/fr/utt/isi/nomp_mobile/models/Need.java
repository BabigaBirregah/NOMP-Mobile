package fr.utt.isi.nomp_mobile.models;

import java.util.ArrayList;
import java.util.List;

import fr.utt.isi.nomp_mobile.database.NOMPDataContract;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
	public String getTableName() {
		return NOMPDataContract.Need.TABLE_NAME;
	}

	@Override
	public long store() {
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

	@Override
	public Need retrieve(long ticketId) {
		Cursor c = retrieveBase(ticketId);

		if (c.moveToFirst()) {
			this.setBudget(c.getInt(c
					.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_BUDGET)));
		}

		return this;
	}

	@Override
	public List<Need> list() {
		// prepare the query
		String query = "SELECT * FROM " + NOMPDataContract.Need.TABLE_NAME
				+ " ORDER BY _id DESC";

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

		Log.d(TAG, needList.toString());
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

	public int getBudget() {
		return budget;
	}

	public void setBudget(int budget) {
		this.budget = budget;
	}

}
