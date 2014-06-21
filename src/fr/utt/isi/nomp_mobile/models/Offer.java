package fr.utt.isi.nomp_mobile.models;

import java.util.ArrayList;
import java.util.List;

import fr.utt.isi.nomp_mobile.config.Config;
import fr.utt.isi.nomp_mobile.database.NOMPDataContract;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Offer extends Ticket {

	public static final String TAG = "Offer";

	private int cost;

	public Offer(Context context) {
		super(context);
		this.cost = 0;
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
	public Offer(Context context, String name, String classification,
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
		this.cost = 0;
	}

	public Offer(Context context, String name, String classification,
			String classificationName, String sourceActorType,
			String sourceActorTypeName, String targetActorType,
			String targetActorTypeName, String contactPhone,
			String contactMobile, String contactEmail, int quantity,
			String description, String keywords, String address,
			String geometry, String creationDate, String endDate,
			String startDate, String expirationDate, String updateDate,
			boolean isActive, int statut, String reference, String user,
			String matched, int cost) {
		super(context, name, classification, classificationName,
				sourceActorType, sourceActorTypeName, targetActorType,
				targetActorTypeName, contactPhone, contactMobile, contactEmail,
				quantity, description, keywords, address, geometry,
				creationDate, endDate, startDate, expirationDate, updateDate,
				isActive, statut, reference, user, matched);
		this.cost = cost;
	}

	public Offer(Context context, String nompId, String name,
			String classification, String classificationName,
			String sourceActorType, String sourceActorTypeName,
			String targetActorType, String targetActorTypeName,
			String contactPhone, String contactMobile, String contactEmail,
			int quantity, String description, String keywords, String address,
			String geometry, String creationDate, String endDate,
			String startDate, String expirationDate, String updateDate,
			boolean isActive, int statut, String reference, String user,
			String matched, int cost) {
		super(context, nompId, name, classification, classificationName,
				sourceActorType, sourceActorTypeName, targetActorType,
				targetActorTypeName, contactPhone, contactMobile, contactEmail,
				quantity, description, keywords, address, geometry,
				creationDate, endDate, startDate, expirationDate, updateDate,
				isActive, statut, reference, user, matched);
		this.cost = cost;
	}

	public Offer(Context context, long _id, String nompId, String name,
			String classification, String classificationName,
			String sourceActorType, String sourceActorTypeName,
			String targetActorType, String targetActorTypeName,
			String contactPhone, String contactMobile, String contactEmail,
			int quantity, String description, String keywords, String address,
			String geometry, String creationDate, String endDate,
			String startDate, String expirationDate, String updateDate,
			boolean isActive, int statut, String reference, String user,
			String matched, int cost) {
		super(context, _id, nompId, name, classification, classificationName,
				sourceActorType, sourceActorTypeName, targetActorType,
				targetActorTypeName, contactPhone, contactMobile, contactEmail,
				quantity, description, keywords, address, geometry,
				creationDate, endDate, startDate, expirationDate, updateDate,
				isActive, statut, reference, user, matched);
		this.cost = cost;
	}

	@Override
	public String getTableName() {
		return NOMPDataContract.Offer.TABLE_NAME;
	}

	@Override
	public long store() {
		// build the content values for insert
		ContentValues mContentValues = getBaseContentValues();
		mContentValues.put(NOMPDataContract.Offer.COLUMN_NAME_COST, cost);

		// insert
		SQLiteDatabase writable = this.getWritableDatabase();
		_id = writable.insert(NOMPDataContract.Offer.TABLE_NAME, null,
				mContentValues);

		writable.close();
		return _id;
	}

	@Override
	public Offer retrieve(long ticketId) {
		Cursor c = retrieveBase(ticketId);

		if (c.moveToFirst()) {
			this.setCost(c.getInt(c
					.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_COST)));
		}

		return this;
	}

	@Override
	public List<Offer> list() {
		return list(false);
	}

	public List<Offer> list(boolean isForMe) {
		// prepare the query
		String query = "SELECT * FROM " + NOMPDataContract.Offer.TABLE_NAME;
		if (isForMe) {
			SharedPreferences userInfo = context.getSharedPreferences(Config.PREF_NAME_USER, Context.MODE_PRIVATE);
			if (userInfo.getBoolean(Config.PREF_KEY_USER_IS_LOGGED, false)) {
				query += " WHERE user='" + userInfo.getString(Config.PREF_KEY_USER_NOMP_ID, "-1") + "'";
			}
		}
		query += " ORDER BY _id DESC";

		SQLiteDatabase readable = this.getReadableDatabase();
		Cursor c = readable.rawQuery(query, null);

		ArrayList<Offer> offerList = new ArrayList<Offer>(c.getCount());

		if (c.moveToFirst()) {
			do {
				Offer offer = new Offer(context);

				offer.setNompId(c.getString(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_NOMP_ID)));

				offer.set_id(c.getInt(c
						.getColumnIndex(NOMPDataContract.Offer._ID)));

				offer.setName(c.getString(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_NAME)));

				offer.setClassification(c.getString(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_CLASSIFICATION)));
				offer.setClassificationName(c.getString(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_CLASSIFICATION_NAME)));

				offer.setSourceActorType(c.getString(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_SOURCE_ACTOR_TYPE)));
				offer.setSourceActorTypeName(c.getString(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_SOURCE_ACTOR_TYPE_NAME)));
				offer.setTargetActorType(c.getString(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_TARGET_ACTOR_TYPE)));
				offer.setTargetActorTypeName(c.getString(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_TARGET_ACTOR_TYPE_NAME)));

				offer.setContactPhone(c.getString(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_CONTACT_PHONE)));
				offer.setContactMobile(c.getString(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_CONTACT_MOBILE)));
				offer.setContactEmail(c.getString(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_CONTACT_EMAIL)));

				offer.setQuantity(c.getInt(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_QUANTITY)));
				offer.setCost(c.getInt(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_COST)));

				offer.setDescription(c.getString(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_DESCRIPTION)));
				offer.setKeywords(c.getString(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_KEYWORDS)));

				offer.setAddress(c.getString(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_ADDRESS)));
				offer.setGeometry(c.getString(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_GEOMETRY)));

				// DATES
				offer.setCreationDate(c.getString(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_CREATION_DATE)));
				offer.setEndDate(c.getString(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_END_DATE)));
				offer.setStartDate(c.getString(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_START_DATE)));
				offer.setExpirationDate(c.getString(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_EXPIRATION_DATE)));
				offer.setUpdateDate(c.getString(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_UPDATE_DATE)));

				offer.setActive(c.getInt(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_IS_ACTIVE)) == 1 ? true
						: false);

				offer.setStatut(c.getInt(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_STATUT)));

				offer.setReference(c.getString(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_REFERENCE)));

				offer.setUser(c.getString(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_USER)));

				offer.setMatched(c.getString(c
						.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_MATCHED)));

				offerList.add(offer);
				offer = null;
			} while (c.moveToNext());
		}
		c.close();
		readable.close();

		return offerList;
	}

	@Override
	public int update(ContentValues values) {
		if (_id == -1) {
			return 0;
		}

		// TODO: check the content value input

		SQLiteDatabase writable = this.getWritableDatabase();
		int nbLines = writable.update(NOMPDataContract.Offer.TABLE_NAME,
				values, "_id=?", new String[] { "" + _id });

		writable.close();
		return nbLines;
	}

	@Override
	public int delete() {
		SQLiteDatabase writable = this.getWritableDatabase();
		int nbLines = writable.delete(NOMPDataContract.Offer.TABLE_NAME,
				"_id=?", new String[] { "" + _id });

		writable.close();
		return nbLines;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

}
