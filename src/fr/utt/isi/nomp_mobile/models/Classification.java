package fr.utt.isi.nomp_mobile.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.utt.isi.nomp_mobile.config.Config;
import fr.utt.isi.nomp_mobile.database.NOMPDataContract;
import fr.utt.isi.nomp_mobile.tasks.RequestTask;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

public class Classification extends Type {

	public static final String TAG = "Classification";

	public Classification(Context context) {
		super(context);
	}

	@Override
	public String toString() {
		return "Classification (id=" + _id + ", nompId=" + nompId + ", name="
				+ name + ", parent=" + parent + ", parentName=" + parentName
				+ ", isParent=" + isParent + ")";
	}

	public void apiGet() {
		new RequestTask(context, "GET") {

			@Override
			public void onPostExecute(String result) {
				try {
					JSONArray jsonArray = new JSONArray(result);
					if (jsonArray.length() > 0) {
						Type[] classifications = new Classification[jsonArray
								.length()];
						for (int i = 0; i < jsonArray.length(); i++) {
							// parse json object
							JSONObject jsonObject = jsonArray.getJSONObject(i);

							Classification classification = new Classification(
									context);
							classification.setNompId(jsonObject
									.getString("_id"));
							classification
									.setName(jsonObject.getString("name"));
							classification.setParent(jsonObject
									.getBoolean("is_parent"));
							if (!isParent) {
								if (jsonObject.has("parent")) {
									classification.setParent(jsonObject
											.getString("parent"));
								}
								if (jsonObject.has("parent_name")) {
									classification.setParentName(jsonObject
											.getString("parent_name"));
								}
							}

							classifications[i] = classification;
						}

						// delete all data from database to simply avoid
						// update/insert decisions
						deleteAll();
						classifications = insertAll(classifications);
					} else {
						Toast errorToast = Toast.makeText(context,
								"No available classifications on server.",
								Toast.LENGTH_LONG);
						errorToast.show();
					}
				} catch (JSONException e) {
					Toast errorToast = Toast.makeText(context,
							"Failed to parse response from server.",
							Toast.LENGTH_LONG);
					errorToast.show();
					e.printStackTrace();
				}

			}

		}.execute(Config.NOMP_API_ROOT + "classification/list");
	}

	public List<?> handleCursor2List(Cursor c) {
		ArrayList<Classification> classificationList = new ArrayList<Classification>(
				c.getCount());
		if (c.moveToFirst()) {
			do {
				Classification classification = new Classification(context);

				classification.set_id(c.getInt(c
						.getColumnIndex(NOMPDataContract.Classification._ID)));
				classification
						.setNompId(c.getString(c
								.getColumnIndex(NOMPDataContract.Classification.COLUMN_NAME_NOMP_ID)));
				classification
						.setName(c.getString(c
								.getColumnIndex(NOMPDataContract.Classification.COLUMN_NAME_NAME)));
				classification
						.setParent(c.getString(c
								.getColumnIndex(NOMPDataContract.Classification.COLUMN_NAME_PARENT)));
				classification
						.setParentName(c.getString(c
								.getColumnIndex(NOMPDataContract.Classification.COLUMN_NAME_PARENT_NAME)));
				classification
						.setParent(c.getInt(c
								.getColumnIndex(NOMPDataContract.Classification.COLUMN_NAME_IS_PARENT)) == 1 ? true
								: false);

				classificationList.add(classification);
				classification = null;
			} while (c.moveToNext());
		}
		c.close();

		return classificationList;
	}

	public List<?> parentList() {
		Cursor c = listParentCursor();

		return handleCursor2List(c);
	}

	public List<?> childrenList(String parent) {
		Cursor c = listChildrenCursor(parent);

		return handleCursor2List(c);
	}

	@Override
	public List<?> list() {
		Cursor c = listCursor();

		return handleCursor2List(c);
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
