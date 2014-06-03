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

public class Need extends Ticket {

	public static final String TAG = "Need";

	private int budget;

	public Need(Context context) {
		super(context);
		this.budget = 0;
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
				String[] tmpCreationDate = c
						.getString(
								c.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_CREATION_DATE))
						.split("-");
				String[] tmpEndDate = c
						.getString(
								c.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_END_DATE))
						.split("-");
				String[] tmpStartDate = c
						.getString(
								c.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_START_DATE))
						.split("-");
				String[] tmpExpirationDate = c
						.getString(
								c.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_EXPIRATION_DATE))
						.split("-");
				String[] tmpUpdateDate = c
						.getString(
								c.getColumnIndex(NOMPDataContract.Need.COLUMN_NAME_UPDATE_DATE))
						.split("-");
				need.setCreationDate(new GregorianCalendar(Integer
						.parseInt(tmpCreationDate[0]), Integer
						.parseInt(tmpCreationDate[1]), Integer
						.parseInt(tmpCreationDate[2])));
				need.setEndDate(new GregorianCalendar(Integer
						.parseInt(tmpEndDate[0]), Integer
						.parseInt(tmpEndDate[1]), Integer
						.parseInt(tmpEndDate[2])));
				need.setStartDate(new GregorianCalendar(Integer
						.parseInt(tmpStartDate[0]), Integer
						.parseInt(tmpStartDate[1]), Integer
						.parseInt(tmpStartDate[2])));
				need.setExpirationDate(new GregorianCalendar(Integer
						.parseInt(tmpExpirationDate[0]), Integer
						.parseInt(tmpExpirationDate[1]), Integer
						.parseInt(tmpExpirationDate[2])));
				need.setUpdateDate(new GregorianCalendar(Integer
						.parseInt(tmpUpdateDate[0]), Integer
						.parseInt(tmpUpdateDate[1]), Integer
						.parseInt(tmpUpdateDate[2])));

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
		int nbLines = writable.delete(NOMPDataContract.Need.TABLE_NAME, "_id=?",
				new String[] { "" + _id });
		
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
