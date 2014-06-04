package fr.utt.isi.nomp_mobile.models;

import java.util.List;

import fr.utt.isi.nomp_mobile.database.NOMPDataContract;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class BaseModel extends SQLiteOpenHelper {
	
	public static final int DATABASE_VERSION = 3;
	
	public static final String DATABASE_NAME = "nomp";
	
	public static final String[] TABLE_NAMES = {
		NOMPDataContract.Need.TABLE_NAME,
		NOMPDataContract.Offer.TABLE_NAME,
		NOMPDataContract.Classification.TABLE_NAME,
		NOMPDataContract.ActorType.TABLE_NAME,
		NOMPDataContract.Matching.TABLE_NAME
	};
	
	public static final String[] TABLE_CREATE_QUERIES = {
		NOMPDataContract.Need.SQL_CREATE_ENTRIES,
		NOMPDataContract.Offer.SQL_CREATE_ENTRIES,
		NOMPDataContract.Classification.SQL_CREATE_ENTRIES,
		NOMPDataContract.ActorType.SQL_CREATE_ENTRIES,
		NOMPDataContract.Matching.SQL_CREATE_ENTRIES
	};
	
	public static final String[] TABLE_DELETE_QUERIES = {
		NOMPDataContract.Need.SQL_DELETE_ENTRIES,
		NOMPDataContract.Offer.SQL_DELETE_ENTRIES,
		NOMPDataContract.Classification.SQL_DELETE_ENTRIES,
		NOMPDataContract.ActorType.SQL_DELETE_ENTRIES,
		NOMPDataContract.Matching.SQL_DELETE_ENTRIES
	};
	
	protected Context context;

	public BaseModel(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (int i = 0; i < TABLE_CREATE_QUERIES.length; i++) {
			db.execSQL(TABLE_CREATE_QUERIES[i]);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropTables(db);
		onCreate(db);
	}
	
	public void dropTables(SQLiteDatabase db) {
		for (int i = 0; i < TABLE_DELETE_QUERIES.length; i++) {
			db.execSQL(TABLE_DELETE_QUERIES[i]);
		}
	}
	
	public abstract long store();
	
	public abstract BaseModel retrieve(long id);
	
	public abstract List<?> list();
	
	public abstract int update(ContentValues values);
	
	public abstract int delete();

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

}
