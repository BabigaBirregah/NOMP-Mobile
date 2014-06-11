package fr.utt.isi.nomp_mobile.database;

import android.provider.BaseColumns;

public final class NOMPDataContract {

	private static final String COMMA_SEP = ",";

	public NOMPDataContract() {
	}

	/**
	 * Global schema of a ticket (need/offer) providing some common columns.
	 * This does not provide SQL queries for creating/dropping table which
	 * should be specified in child classes.
	 * 
	 * @author Administrator
	 * 
	 */
	public static abstract class Ticket implements BaseColumns {
		public static final String COLUMN_NAME_NOMP_ID = "nomp_id";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_CLASSIFICATION = "classification";
		public static final String COLUMN_NAME_CLASSIFICATION_NAME = "classification_name";
		public static final String COLUMN_NAME_SOURCE_ACTOR_TYPE = "source_actor_type";
		public static final String COLUMN_NAME_SOURCE_ACTOR_TYPE_NAME = "source_actor_type_name";
		public static final String COLUMN_NAME_TARGET_ACTOR_TYPE = "target_actor_type";
		public static final String COLUMN_NAME_TARGET_ACTOR_TYPE_NAME = "target_actor_type_name";
		public static final String COLUMN_NAME_CONTACT_PHONE = "contact_phone";
		public static final String COLUMN_NAME_CONTACT_MOBILE = "contact_mobile";
		public static final String COLUMN_NAME_CONTACT_EMAIL = "contact_email";
		public static final String COLUMN_NAME_QUANTITY = "quantity";
		public static final String COLUMN_NAME_DESCRIPTION = "description";
		public static final String COLUMN_NAME_KEYWORDS = "keywords";
		public static final String COLUMN_NAME_ADDRESS = "address";
		public static final String COLUMN_NAME_GEOMETRY = "geometry";
		// TODO: photo
		public static final String COLUMN_NAME_CREATION_DATE = "creation_date";
		public static final String COLUMN_NAME_END_DATE = "end_date";
		public static final String COLUMN_NAME_START_DATE = "start_date";
		public static final String COLUMN_NAME_EXPIRATION_DATE = "expiration_date";
		public static final String COLUMN_NAME_UPDATE_DATE = "update_date";
		public static final String COLUMN_NAME_IS_ACTIVE = "is_active";
		public static final String COLUMN_NAME_STATUT = "statut"; // "status"
		public static final String COLUMN_NAME_REFERENCE = "reference";
		public static final String COLUMN_NAME_USER = "user";
		public static final String COLUMN_NAME_MATCHED = "matched";

		public static String[] getFields() {
			return new String[] { COLUMN_NAME_NOMP_ID, COLUMN_NAME_NAME,
					COLUMN_NAME_CLASSIFICATION,
					COLUMN_NAME_CLASSIFICATION_NAME,
					COLUMN_NAME_SOURCE_ACTOR_TYPE,
					COLUMN_NAME_SOURCE_ACTOR_TYPE_NAME,
					COLUMN_NAME_TARGET_ACTOR_TYPE,
					COLUMN_NAME_TARGET_ACTOR_TYPE_NAME,
					COLUMN_NAME_CONTACT_PHONE, COLUMN_NAME_CONTACT_MOBILE,
					COLUMN_NAME_CONTACT_EMAIL, COLUMN_NAME_QUANTITY,
					COLUMN_NAME_DESCRIPTION, COLUMN_NAME_KEYWORDS,
					COLUMN_NAME_ADDRESS, COLUMN_NAME_GEOMETRY,
					COLUMN_NAME_CREATION_DATE, COLUMN_NAME_END_DATE,
					COLUMN_NAME_START_DATE, COLUMN_NAME_EXPIRATION_DATE,
					COLUMN_NAME_UPDATE_DATE, COLUMN_NAME_IS_ACTIVE,
					COLUMN_NAME_STATUT, COLUMN_NAME_REFERENCE,
					COLUMN_NAME_USER, COLUMN_NAME_MATCHED };
		}

		protected static String buildSQLCommonFields() {
			return "" + _ID + " INTEGER PRIMARY KEY" + COMMA_SEP

			+ COLUMN_NAME_NOMP_ID + " TEXT UNIQUE DEFAULT NULL" + COMMA_SEP

			+ COLUMN_NAME_NAME + " TEXT UNIQUE" + COMMA_SEP

			+ COLUMN_NAME_CLASSIFICATION + " TEXT" + COMMA_SEP

			+ COLUMN_NAME_CLASSIFICATION_NAME + " TEXT" + COMMA_SEP

			+ COLUMN_NAME_SOURCE_ACTOR_TYPE + " TEXT" + COMMA_SEP

			+ COLUMN_NAME_SOURCE_ACTOR_TYPE_NAME + " TEXT" + COMMA_SEP

			+ COLUMN_NAME_TARGET_ACTOR_TYPE + " TEXT" + COMMA_SEP

			+ COLUMN_NAME_TARGET_ACTOR_TYPE_NAME + " TEXT" + COMMA_SEP

			+ COLUMN_NAME_CONTACT_PHONE + " TEXT DEFAULT NULL" + COMMA_SEP

			+ COLUMN_NAME_CONTACT_MOBILE + " TEXT DEFAULT NULL" + COMMA_SEP

			+ COLUMN_NAME_CONTACT_EMAIL + " TEXT DEFAULT NULL" + COMMA_SEP

			+ COLUMN_NAME_QUANTITY + " INTEGER DEFAULT 1" + COMMA_SEP

			+ COLUMN_NAME_DESCRIPTION + " TEXT" + COMMA_SEP

			+ COLUMN_NAME_KEYWORDS + " TEXT" + COMMA_SEP

			+ COLUMN_NAME_ADDRESS + " TEXT" + COMMA_SEP

			+ COLUMN_NAME_GEOMETRY + " TEXT" + COMMA_SEP

			+ COLUMN_NAME_CREATION_DATE + " TEXT" + COMMA_SEP

			+ COLUMN_NAME_END_DATE + " TEXT" + COMMA_SEP

			+ COLUMN_NAME_START_DATE + " TEXT" + COMMA_SEP

			+ COLUMN_NAME_EXPIRATION_DATE + " TEXT" + COMMA_SEP

			+ COLUMN_NAME_UPDATE_DATE + " TEXT" + COMMA_SEP

			+ COLUMN_NAME_IS_ACTIVE + " TINYINT DEFAULT 1" + COMMA_SEP

			+ COLUMN_NAME_STATUT + " TINYINT DEFAULT 0" + COMMA_SEP

			+ COLUMN_NAME_REFERENCE + " TEXT UNIQUE DEFAULT NULL" + COMMA_SEP

			+ COLUMN_NAME_USER + " TEXT DEFAULT NULL" + COMMA_SEP

			+ COLUMN_NAME_MATCHED + " TEXT DEFAULT NULL";
		}
	}

	public static abstract class Need extends Ticket {
		public static final String TABLE_NAME = "need";
		public static final String COLUMN_NAME_BUDGET = "budget";

		public static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME
				+ " ("
				+ buildSQLCommonFields()
				+ COMMA_SEP
				+ COLUMN_NAME_BUDGET + " INTEGER DEFAULT 0" + " )";

		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
				+ TABLE_NAME;
	}

	public static abstract class Offer extends Ticket {
		public static final String TABLE_NAME = "offer";
		public static final String COLUMN_NAME_COST = "cost";

		public static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME
				+ " ("
				+ buildSQLCommonFields()
				+ COMMA_SEP
				+ COLUMN_NAME_COST + " INTEGER DEFAULT 0" + " )";

		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
				+ TABLE_NAME;
	}

	public static abstract class Type implements BaseColumns {
		public static final String COLUMN_NAME_NOMP_ID = "nomp_id";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_PARENT = "parent";
		public static final String COLUMN_NAME_PARENT_NAME = "parent_name";
		public static final String COLUMN_NAME_IS_PARENT = "is_parent";

		protected static String buildSQLCommonFields() {
			return "" + _ID + " INTEGER PRIMARY KEY" + COMMA_SEP

			+ COLUMN_NAME_NOMP_ID + " TEXT UNIQUE DEFAULT NULL" + COMMA_SEP

			+ COLUMN_NAME_NAME + " TEXT" + COMMA_SEP

			+ COLUMN_NAME_PARENT + " TEXT DEFAULT NULL" + COMMA_SEP

			+ COLUMN_NAME_PARENT_NAME + " TEXT DEFAULT NULL" + COMMA_SEP

			+ COLUMN_NAME_IS_PARENT + " TINYINT DEFAULT 0";
		}
	}

	public static abstract class Classification extends Type {
		public static final String TABLE_NAME = "classification";

		public static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME + " (" + buildSQLCommonFields() + " )";

		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
				+ TABLE_NAME;
	}

	public static abstract class ActorType extends Type {
		public static final String TABLE_NAME = "actor_type";

		public static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME + " (" + buildSQLCommonFields() + " )";

		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
				+ TABLE_NAME;
	}

	public static abstract class Matching implements BaseColumns {
		public static final String TABLE_NAME = "matching";
		public static final String COLUMN_NAME_NOMP_ID = "nomp_id";
		public static final String COLUMN_NAME_SOURCE_ID = "source_id";
		public static final String COLUMN_NAME_SOURCE_TYPE = "source_type";
		public static final String COLUMN_NAME_IS_MATCH = "is_match";
		public static final String COLUMN_NAME_RESULTS = "results";

		public static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY" + COMMA_SEP

				+ COLUMN_NAME_NOMP_ID + " TEXT UNIQUE DEFAULT NULL" + COMMA_SEP

				+ COLUMN_NAME_SOURCE_ID + " TEXT" + COMMA_SEP

				+ COLUMN_NAME_SOURCE_TYPE + " TEXT" + COMMA_SEP

				+ COLUMN_NAME_IS_MATCH + " TINYINT DEFAULT 0" + COMMA_SEP

				+ COLUMN_NAME_RESULTS + " TEXT DEFAULT NULL" + " )";

		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
				+ TABLE_NAME;
	}

}
