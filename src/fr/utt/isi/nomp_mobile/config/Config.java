package fr.utt.isi.nomp_mobile.config;

public class Config {
	
	public static final String NOMP_API_ROOT = "http://saphir.utt.fr:3000/";
	
	// interval (on days) for update of the classifications&actor types via API
	public static final int NOMP_API_UPDATE_INTERVAL = 30;
	
	/* Shared preferences: pref names and pref keys */
	// names
	public static final String PREF_NAME_LOCALIZATION = "pref_localization";
	public static final String PREF_NAME_CLASSIFICATION = "pref_classification";
	public static final String PREF_NAME_ACTOR_TYPE = "pref_actor_type";
	public static final String PREF_NAME_USER = "pref_user";
	
	// keys
	public static final String PREF_KEY_LOCALIZATION_GPS = "enable_gps";
	public static final String PREF_KEY_TYPE_IS_UPDATED = "is_updated";
	public static final String PREF_KEY_TYPE_UPDATED_AT = "updated_at";
	public static final String PREF_KEY_CLASSIFICATION_PREFERRED = "preferred_classification";
	public static final String PREF_KEY_SUB_CLASSIFICATION_PREFERRED = "preferred_sub_classification";
	public static final String PREF_KEY_TARGET_ACTOR_PREFERRED = "preferred_target_actor";
	public static final String PREF_KEY_SUB_TARGET_ACTOR_PREFERRED = "preferred_sub_target_actor";
	public static final String PREF_KEY_USER_IS_LOGGED = "is_logged";
	public static final String PREF_KEY_USER_NOMP_ID = "nomp_id";
	public static final String PREF_KEY_USER_NAME = "name";
	public static final String PREF_KEY_USER_EMAIL = "email";
	public static final String PREF_KEY_USER_USERNAME = "username";
	public static final String PREF_KEY_USER_PASSWORD = "password";
	public static final String PREF_KEY_USER_ACTOR_TYPE = "actor_type";
	public static final String PREF_KEY_USER_ACTOR_TYPE_NAME = "actor_type_name";
	/* ******************************************************** */

	private Config() {
	}

}
