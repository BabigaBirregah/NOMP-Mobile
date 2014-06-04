package fr.utt.isi.nomp_mobile.models;

import java.util.ArrayList;
import java.util.List;

import fr.utt.isi.nomp_mobile.database.NOMPDataContract;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
