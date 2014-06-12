package fr.utt.isi.nomp_mobile.config;

public class Config {
	
	public static final String NOMP_API_ROOT = "http://saphir.utt.fr:3000/";
	
	// interval (on days) for update of the classifications&actor types via API
	public static final int NOMP_API_UPDATE_INTERVAL = 30;
	
	/* Shared preferences: pref names and pref keys */
	// names
	public static final String PREF_NAME_CLASSIFICATION = "pref_classification";
	public static final String PREF_NAME_ACTOR_TYPE = "pref_actor_type";
	
	// keys
	public static final String PREF_KEY_TYPE_IS_UPDATED = "is_updated";
	public static final String PREF_KEY_TYPE_UPDATED_AT = "updated_at";
	/* ******************************************************** */

	private Config() {
	}

}
