package fr.utt.isi.nomp_mobile.models;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import fr.utt.isi.nomp_mobile.database.NOMPDataContract;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Offer extends Ticket {

	public static final String TAG = "Offer";

	private int cost;

	public Offer(Context context) {
		super(context);
		this.cost = 0;
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
	public List<Offer> list() {
		// prepare the query
		String query = "SELECT * FROM " + NOMPDataContract.Offer.TABLE_NAME
				+ " ORDER BY _id DESC";

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
				String[] tmpCreationDate = c
						.getString(
								c.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_CREATION_DATE))
						.split("-");
				String[] tmpEndDate = c
						.getString(
								c.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_END_DATE))
						.split("-");
				String[] tmpStartDate = c
						.getString(
								c.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_START_DATE))
						.split("-");
				String[] tmpExpirationDate = c
						.getString(
								c.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_EXPIRATION_DATE))
						.split("-");
				String[] tmpUpdateDate = c
						.getString(
								c.getColumnIndex(NOMPDataContract.Offer.COLUMN_NAME_UPDATE_DATE))
						.split("-");
				offer.setCreationDate(new GregorianCalendar(Integer
						.parseInt(tmpCreationDate[0]), Integer
						.parseInt(tmpCreationDate[1]), Integer
						.parseInt(tmpCreationDate[2])));
				offer.setEndDate(new GregorianCalendar(Integer
						.parseInt(tmpEndDate[0]), Integer
						.parseInt(tmpEndDate[1]), Integer
						.parseInt(tmpEndDate[2])));
				offer.setStartDate(new GregorianCalendar(Integer
						.parseInt(tmpStartDate[0]), Integer
						.parseInt(tmpStartDate[1]), Integer
						.parseInt(tmpStartDate[2])));
				offer.setExpirationDate(new GregorianCalendar(Integer
						.parseInt(tmpExpirationDate[0]), Integer
						.parseInt(tmpExpirationDate[1]), Integer
						.parseInt(tmpExpirationDate[2])));
				offer.setUpdateDate(new GregorianCalendar(Integer
						.parseInt(tmpUpdateDate[0]), Integer
						.parseInt(tmpUpdateDate[1]), Integer
						.parseInt(tmpUpdateDate[2])));

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

		Log.d(TAG, offerList.toString());
		return offerList;
	}

	@Override
	public int update(ContentValues values) {
		if (_id == -1) {
			return 0;
		}

		// TODO: check the content value input

		SQLiteDatabase writable = this.getWritableDatabase();
		int nbLines = writable.update(NOMPDataContract.Offer.TABLE_NAME, values,
				"_id=?", new String[] { "" + _id });
		
		writable.close();
		return nbLines;
	}

	@Override
	public int delete() {
		SQLiteDatabase writable = this.getWritableDatabase();
		int nbLines = writable.delete(NOMPDataContract.Offer.TABLE_NAME, "_id=?",
				new String[] { "" + _id });
		
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
