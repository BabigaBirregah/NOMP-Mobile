package fr.utt.isi.nomp_mobile.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class Type extends BaseModel {

	protected long _id;

	protected String nompId;

	protected String name;

	protected String parent;
	protected String parentName;
	protected boolean isParent;

	public Type(Context context) {
		super(context);

		this._id = -1;
		this.nompId = null;
		this.name = "";
		this.parent = null;
		this.parentName = null;
		this.isParent = false;
	}

	public abstract String getTableName();

	public abstract String getColumnNameNompId();

	public abstract String getColumnNameName();

	public abstract String getColumnNameParent();

	public abstract String getColumnNameParentName();

	public abstract String getColumnNameIsParent();

	public Cursor listCursor() {
		// prepare the query
		String query = "SELECT * FROM " + getTableName() + " ORDER BY _id DESC";
		
		SQLiteDatabase readable = this.getReadableDatabase();
		Cursor c = readable.rawQuery(query, null);
		
		readable.close();
		return c;
	}

	@Override
	public long store() {
		// build the content values for insert
		ContentValues mContentValues = new ContentValues();
		mContentValues.put(getColumnNameNompId(), nompId);
		mContentValues.put(getColumnNameName(), name);
		mContentValues.put(getColumnNameParent(), parent);
		mContentValues.put(getColumnNameParentName(), parentName);
		mContentValues.put(getColumnNameIsParent(), isParent);

		// insert
		SQLiteDatabase writable = this.getWritableDatabase();
		_id = writable.insert(getTableName(), null, mContentValues);

		return _id;
	}

	@Override
	public int update(ContentValues values) {
		if (_id == -1) {
			return 0;
		}

		// TODO: check the content value input

		SQLiteDatabase writable = this.getWritableDatabase();
		int nbLines = writable.update(getTableName(), values, "_id=?",
				new String[] { "" + _id });
		
		writable.close();
		return nbLines;
	}

	@Override
	public int delete() {
		SQLiteDatabase writable = this.getWritableDatabase();
		int nbLines = writable.delete(getTableName(), "_id=?",
				new String[] { "" + _id });
		
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public boolean isParent() {
		return isParent;
	}

	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}

}
