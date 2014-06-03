package fr.utt.isi.nomp_mobile.models;

import java.util.ArrayList;
import java.util.List;

import fr.utt.isi.nomp_mobile.database.NOMPDataContract;

import android.content.Context;
import android.database.Cursor;

public class Classification extends Type {

	public static final String TAG = "Classification";

	public Classification(Context context) {
		super(context);
	}

	@Override
	public List<?> list() {
		Cursor c = listCursor();

		ArrayList<Classification> classificationList = new ArrayList<Classification>(c.getCount());
		if (c.moveToFirst()) {
			do {
				Classification classification = new Classification(context);
				
				classification.set_id(c.getInt(c.getColumnIndex(NOMPDataContract.Classification._ID)));
				classification.setNompId(c.getString(c.getColumnIndex(NOMPDataContract.Classification.COLUMN_NAME_NOMP_ID)));
				classification.setParent(c.getString(c.getColumnIndex(NOMPDataContract.Classification.COLUMN_NAME_PARENT)));
				classification.setParentName(c.getString(c.getColumnIndex(NOMPDataContract.Classification.COLUMN_NAME_PARENT_NAME)));
				classification.setParent(c.getInt(c.getColumnIndex(NOMPDataContract.Classification.COLUMN_NAME_IS_PARENT)) == 1 ? true : false);
				
				classificationList.add(classification);
				classification = null;
			} while (c.moveToNext());
		}
		c.close();
		
		return classificationList;
	}

	@Override
	public String getTableName() {
		return NOMPDataContract.Classification.TABLE_NAME;
	}

	@Override
	public String getColumnNameNompId() {
		return NOMPDataContract.Classification.COLUMN_NAME_NOMP_ID;
	}

	@Override
	public String getColumnNameName() {
		return NOMPDataContract.Classification.COLUMN_NAME_NAME;
	}

	@Override
	public String getColumnNameParent() {
		return NOMPDataContract.Classification.COLUMN_NAME_PARENT;
	}

	@Override
	public String getColumnNameParentName() {
		return NOMPDataContract.Classification.COLUMN_NAME_PARENT_NAME;
	}

	@Override
	public String getColumnNameIsParent() {
		return NOMPDataContract.Classification.COLUMN_NAME_IS_PARENT;
	}

}
