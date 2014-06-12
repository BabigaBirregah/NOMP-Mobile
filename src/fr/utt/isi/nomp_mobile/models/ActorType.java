package fr.utt.isi.nomp_mobile.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.utt.isi.nomp_mobile.config.Config;
import fr.utt.isi.nomp_mobile.database.NOMPDataContract;
import fr.utt.isi.nomp_mobile.tasks.RequestTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.widget.Toast;

public class ActorType extends Type {

	public ActorType(Context context) {
		super(context);
	}

	@Override
	public void apiGet() {
		new RequestTask(context, "GET") {

			@Override
			public void onPostExecute(String result) {
				if (result == null) {
					Toast errorToast = Toast.makeText(context,
							"Some error occurs during request.",
							Toast.LENGTH_LONG);
					errorToast.show();
				} else if (result.equals(RequestTask.MAL_FORMED_URL_EXCEPTION)) {
					Toast errorToast = Toast.makeText(context,
							"Request server not found.", Toast.LENGTH_LONG);
					errorToast.show();
				} else if (result.equals(RequestTask.IO_EXCEPTION)) {
					Toast errorToast = Toast
							.makeText(
									context,
									"Unable to retrieve data from server. Please try again later.",
									Toast.LENGTH_LONG);
					errorToast.show();
				} else {

					try {
						JSONArray jsonArray = new JSONArray(result);
						if (jsonArray.length() > 0) {
							Type[] actorTypes = new ActorType[jsonArray
									.length()];
							for (int i = 0; i < jsonArray.length(); i++) {
								// parse json object
								JSONObject jsonObject = jsonArray
										.getJSONObject(i);

								ActorType actorType = new ActorType(context);
								actorType
										.setNompId(jsonObject.getString("_id"));
								actorType.setName(jsonObject.getString("name"));
								actorType.setParent(jsonObject
										.getBoolean("is_parent"));
								if (!isParent) {
									if (jsonObject.has("parent")) {
										actorType.setParent(jsonObject
												.getString("parent"));
									}
									if (jsonObject.has("parent_name")) {
										actorType.setParentName(jsonObject
												.getString("parent_name"));
									}
								}

								actorTypes[i] = actorType;
							}

							// delete all data from database to simply avoid
							// update/insert decisions
							deleteAll();
							actorTypes = insertAll(actorTypes);

							// update shared preferences for update info
							SharedPreferences typeSettings = context
									.getSharedPreferences(
											Config.PREF_NAME_ACTOR_TYPE,
											Context.MODE_PRIVATE);
							Editor editor = typeSettings.edit();
							editor.putBoolean(Config.PREF_KEY_TYPE_IS_UPDATED,
									true);
							editor.putLong(Config.PREF_KEY_TYPE_UPDATED_AT,
									new Date().getTime());
							editor.commit();
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

			}

		}.execute(Config.NOMP_API_ROOT + "actortype/list");
	}

	public List<?> handleCursor2List(Cursor c) {
		ArrayList<ActorType> actorTypeList = new ArrayList<ActorType>(
				c.getCount());
		if (c.moveToFirst()) {
			do {
				ActorType actorType = new ActorType(context);

				actorType.set_id(c.getInt(c
						.getColumnIndex(NOMPDataContract.ActorType._ID)));
				actorType
						.setNompId(c.getString(c
								.getColumnIndex(NOMPDataContract.ActorType.COLUMN_NAME_NOMP_ID)));
				actorType
						.setName(c.getString(c
								.getColumnIndex(NOMPDataContract.ActorType.COLUMN_NAME_NAME)));
				actorType
						.setParent(c.getString(c
								.getColumnIndex(NOMPDataContract.ActorType.COLUMN_NAME_PARENT)));
				actorType
						.setParentName(c.getString(c
								.getColumnIndex(NOMPDataContract.ActorType.COLUMN_NAME_PARENT_NAME)));
				actorType
						.setParent(c.getInt(c
								.getColumnIndex(NOMPDataContract.ActorType.COLUMN_NAME_IS_PARENT)) == 1 ? true
								: false);

				actorTypeList.add(actorType);
				actorType = null;
			} while (c.moveToNext());
		}
		c.close();

		return actorTypeList;
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
	public String getType() {
		return Type.TYPE_ACTOR_TYPE;
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
