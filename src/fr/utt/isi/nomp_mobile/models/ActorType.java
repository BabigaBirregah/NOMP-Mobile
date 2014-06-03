package fr.utt.isi.nomp_mobile.models;

import java.util.ArrayList;
import java.util.List;

import fr.utt.isi.nomp_mobile.database.NOMPDataContract;

import android.content.Context;
import android.database.Cursor;

public class ActorType extends Type {

	public ActorType(Context context) {
		super(context);
	}
	
	@Override
	public List<?> list() {
		Cursor c = listCursor();

		ArrayList<ActorType> actorTypeList = new ArrayList<ActorType>(c.getCount());
		if (c.moveToFirst()) {
			do {
				ActorType actorType = new ActorType(context);
				
				actorType.set_id(c.getInt(c.getColumnIndex(NOMPDataContract.ActorType._ID)));
				actorType.setNompId(c.getString(c.getColumnIndex(NOMPDataContract.ActorType.COLUMN_NAME_NOMP_ID)));
				actorType.setParent(c.getString(c.getColumnIndex(NOMPDataContract.ActorType.COLUMN_NAME_PARENT)));
				actorType.setParentName(c.getString(c.getColumnIndex(NOMPDataContract.ActorType.COLUMN_NAME_PARENT_NAME)));
				actorType.setParent(c.getInt(c.getColumnIndex(NOMPDataContract.ActorType.COLUMN_NAME_IS_PARENT)) == 1 ? true : false);
				
				actorTypeList.add(actorType);
				actorType = null;
			} while (c.moveToNext());
		}
		c.close();
		
		return actorTypeList;
	}

	@Override
	public String getTableName() {
		return NOMPDataContract.ActorType.TABLE_NAME;
	}

	@Override
	public String getColumnNameNompId() {
		return NOMPDataContract.ActorType.COLUMN_NAME_NOMP_ID;
	}

	@Override
	public String getColumnNameName() {
		return NOMPDataContract.ActorType.COLUMN_NAME_NAME;
	}

	@Override
	public String getColumnNameParent() {
		return NOMPDataContract.ActorType.COLUMN_NAME_PARENT;
	}

	@Override
	public String getColumnNameParentName() {
		return NOMPDataContract.ActorType.COLUMN_NAME_PARENT_NAME;
	}

	@Override
	public String getColumnNameIsParent() {
		return NOMPDataContract.ActorType.COLUMN_NAME_IS_PARENT;
	}

}
