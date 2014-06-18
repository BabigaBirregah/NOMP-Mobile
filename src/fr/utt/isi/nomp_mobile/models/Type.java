package fr.utt.isi.nomp_mobile.models;

import java.util.Date;

import fr.utt.isi.nomp_mobile.config.Config;
import fr.utt.isi.nomp_mobile.database.NOMPDataContract;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public abstract class Type extends BaseModel {

	public static final String TAG = "Type";

	public static final String TYPE_CLASSIFICATION = "classification";
	public static final String TYPE_ACTOR_TYPE = "actor_type";

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

	public abstract void apiGet();

	public abstract String getType();

	public abstract String getTableName();

	public abstract String getColumnNameNompId();

	public abstract String getColumnNameName();

	public abstract String getColumnNameParent();

	public abstract String getColumnNameParentName();

	public abstract String getColumnNameIsParent();

	@Override
	public String toString() {
		return name;
	}

	public String toJSONString() {
		return "{" + "\"_id\":\"" + nompId + "\",\"name\":\"" + name + "\""
				+ "}";
	}

	@Override
	public Type retrieve(long typeId) {
		// prepare the query
		String query = "SELECT * FROM " + getTableName() + " WHERE _id="
				+ typeId + " ORDER BY _id DESC LIMIT 1";

		SQLiteDatabase readable = this.getReadableDatabase();
		Cursor c = readable.rawQuery(query, null);

		if (c.moveToFirst()) {
			this.set_id(c.getInt(c.getColumnIndex(NOMPDataContract.Type._ID)));
			this.setNompId(c.getString(c
					.getColumnIndex(NOMPDataContract.Type.COLUMN_NAME_NOMP_ID)));
			this.setParent(c.getString(c
					.getColumnIndex(NOMPDataContract.Type.COLUMN_NAME_PARENT)));
			this.setParentName(c.getString(c
					.getColumnIndex(NOMPDataContract.Type.COLUMN_NAME_PARENT_NAME)));
			this.setParent(c.getInt(c
					.getColumnIndex(NOMPDataContract.Type.COLUMN_NAME_IS_PARENT)) == 1 ? true
					: false);
		}

		c.close();
		readable.close();

		return this;
	}

	protected Cursor queryCursor(String query) {
		// check update
		SharedPreferences typeSettings = null;
		if (getType().equals(TYPE_CLASSIFICATION)) {
			typeSettings = context.getSharedPreferences(
					Config.PREF_NAME_CLASSIFICATION, Context.MODE_PRIVATE);
		} else if (getType().equals(TYPE_ACTOR_TYPE)) {
			typeSettings = context.getSharedPreferences(
					Config.PREF_NAME_ACTOR_TYPE, Context.MODE_PRIVATE);
		}

		if (typeSettings != null) {
			boolean isUpdated = typeSettings.getBoolean(
					Config.PREF_KEY_TYPE_IS_UPDATED, false);
			long updatedAt = typeSettings.getLong(
					Config.PREF_KEY_TYPE_UPDATED_AT, 0);
			long interval = (new Date().getTime() - updatedAt) / 1000;
			double margin = Config.NOMP_API_UPDATE_INTERVAL * 24 * 60 * 60;

			if (!isUpdated || interval > margin) {
				Log.d(TAG, "update");
				apiGet();
			}
		}

		SQLiteDatabase readable = this.getReadableDatabase();
		Cursor c = readable.rawQuery(query, null);

		// readable.close();
		return c;
	}

	protected Cursor listCursor() {
		// prepare the query
		String query = "SELECT * FROM " + getTableName() + " ORDER BY _id DESC";

		return queryCursor(query);
	}

	protected Cursor listParentCursor() {
		// prepare the query
		String query = "SELECT * FROM " + getTableName()
				+ " WHERE is_parent=1 ORDER BY _id DESC";

		return queryCursor(query);
	}

	protected Cursor listChildrenCursor(String parent) {
		// prepare the query
		String query = "SELECT * FROM " + getTableName()
				+ " WHERE is_parent=0 AND parent='" + parent
				+ "' ORDER BY _id DESC";
		Log.d(TAG, query);

		return queryCursor(query);
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

	public Type[] insertAll(Type[] types) {
		// open database writable connection
		SQLiteDatabase writable = this.getWritableDatabase();

		// prepare the insert query
		String query = "INSERT INTO " + getTableName()
				+ " VALUES (?, ?, ?, ?, ?, ?)";

		// compile the query
		SQLiteStatement statement = writable.compileStatement(query);

		// begin transaction
		writable.beginTransaction();

		for (int i = 0; i < types.length; i++) {
			Type type = types[i];

			// bind the fields to the statement
			statement.clearBindings();
			statement.bindString(2, type.getNompId());
			statement.bindString(3, type.getName());

			if (type.getParent() == null) {
				statement.bindNull(4);
			} else {
				statement.bindString(4, type.getParent());
			}

			if (type.getParentName() == null) {
				statement.bindNull(5);
			} else {
				statement.bindString(5, type.getParentName());
			}

			statement.bindLong(6, type.isParent() ? 1 : 0);

			// commit the execution of statement
			types[i].set_id(statement.executeInsert());
		}

		// commit the transaction
		writable.setTransactionSuccessful();
		writable.endTransaction();
		writable.close();

		return types;
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

	public int deleteAll() {
		SQLiteDatabase writable = this.getWritableDatabase();
		int nbLines = writable.delete(getTableName(), "1", null);

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
