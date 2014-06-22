package fr.utt.isi.nomp_mobile.models;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import fr.utt.isi.nomp_mobile.database.NOMPDataContract;

public abstract class Ticket extends BaseModel {

	public static final String TICKET_NEED = "need";
	public static final String TICKET_OFFER = "offer";

	protected long _id;

	protected String nompId;

	protected String name;

	protected String classification;
	protected String classificationName;

	protected String sourceActorType;
	protected String sourceActorTypeName;
	protected String targetActorType;
	protected String targetActorTypeName;

	protected String contactPhone;
	protected String contactMobile;
	protected String contactEmail;

	protected int quantity;

	protected String description;
	protected String keywords;

	protected String address;
	protected String geometry;

	// TODO: photo

	protected String creationDate;
	protected String endDate;
	protected String startDate;
	protected String expirationDate;
	protected String updateDate;

	protected boolean isActive;

	protected int statut;

	protected String reference;

	protected String user;

	protected String matched;

	/**
	 * Constructor without any fields
	 * 
	 * @param context
	 */
	public Ticket(Context context) {
		super(context);

		this._id = -1;

		this.nompId = null;

		this.name = "";

		this.classification = "";
		this.classificationName = "";

		this.sourceActorType = "";
		this.sourceActorTypeName = "";
		this.targetActorType = "";
		this.targetActorTypeName = "";

		this.contactPhone = null;
		this.contactMobile = null;
		this.contactEmail = null;

		this.quantity = 0;

		this.description = "";

		this.keywords = "";

		this.address = "";
		this.geometry = "";

		Calendar currentCalendar = new GregorianCalendar();
		this.creationDate = DateFormat.getDateInstance(DateFormat.MEDIUM)
				.format(currentCalendar.getTime());
		this.updateDate = creationDate;
		this.startDate = creationDate;
		currentCalendar.add(Calendar.MONTH, 3);
		this.expirationDate = DateFormat.getDateInstance(DateFormat.MEDIUM)
				.format(currentCalendar.getTime());
		this.endDate = expirationDate;

		this.isActive = true;

		this.statut = Status.OPEN;

		this.reference = "";

		this.user = "";

		this.matched = null;
	}

	/**
	 * Constructor without _id and nompId
	 * 
	 * @param context
	 * @param name
	 * @param classification
	 * @param classificationName
	 * @param sourceActorType
	 * @param sourceActorTypeName
	 * @param targetActorType
	 * @param targetActorTypeName
	 * @param contactPhone
	 * @param contactMobile
	 * @param contactEmail
	 * @param quantity
	 * @param description
	 * @param keywords
	 * @param address
	 * @param geometry
	 * @param creationDate
	 * @param endDate
	 * @param startDate
	 * @param expirationDate
	 * @param updateDate
	 * @param isActive
	 * @param statut
	 * @param reference
	 * @param user
	 * @param matched
	 */
	public Ticket(Context context, String name, String classification,
			String classificationName, String sourceActorType,
			String sourceActorTypeName, String targetActorType,
			String targetActorTypeName, String contactPhone,
			String contactMobile, String contactEmail, int quantity,
			String description, String keywords, String address,
			String geometry, String creationDate, String endDate,
			String startDate, String expirationDate, String updateDate,
			boolean isActive, int statut, String reference, String user,
			String matched) {
		super(context);
		this._id = -1;
		this.nompId = null;
		this.name = name;
		this.classification = classification;
		this.classificationName = classificationName;
		this.sourceActorType = sourceActorType;
		this.sourceActorTypeName = sourceActorTypeName;
		this.targetActorType = targetActorType;
		this.targetActorTypeName = targetActorTypeName;
		this.contactPhone = contactPhone;
		this.contactMobile = contactMobile;
		this.contactEmail = contactEmail;
		this.quantity = quantity;
		this.description = description;
		this.keywords = keywords;
		this.address = address;
		this.geometry = geometry;
		this.creationDate = creationDate;
		this.endDate = endDate;
		this.startDate = startDate;
		this.expirationDate = expirationDate;
		this.updateDate = updateDate;
		this.isActive = isActive;
		this.statut = statut;
		this.reference = reference;
		this.user = user;
		this.matched = matched;
	}

	/**
	 * Constructor without _id
	 * 
	 * @param context
	 * @param nompId
	 * @param name
	 * @param classification
	 * @param classificationName
	 * @param sourceActorType
	 * @param sourceActorTypeName
	 * @param targetActorType
	 * @param targetActorTypeName
	 * @param contactPhone
	 * @param contactMobile
	 * @param contactEmail
	 * @param quantity
	 * @param description
	 * @param keywords
	 * @param address
	 * @param geometry
	 * @param creationDate
	 * @param endDate
	 * @param startDate
	 * @param expirationDate
	 * @param updateDate
	 * @param isActive
	 * @param statut
	 * @param reference
	 * @param user
	 * @param matched
	 */
	public Ticket(Context context, String nompId, String name,
			String classification, String classificationName,
			String sourceActorType, String sourceActorTypeName,
			String targetActorType, String targetActorTypeName,
			String contactPhone, String contactMobile, String contactEmail,
			int quantity, String description, String keywords, String address,
			String geometry, String creationDate, String endDate,
			String startDate, String expirationDate, String updateDate,
			boolean isActive, int statut, String reference, String user,
			String matched) {
		super(context);
		this._id = -1;
		this.nompId = nompId;
		this.name = name;
		this.classification = classification;
		this.classificationName = classificationName;
		this.sourceActorType = sourceActorType;
		this.sourceActorTypeName = sourceActorTypeName;
		this.targetActorType = targetActorType;
		this.targetActorTypeName = targetActorTypeName;
		this.contactPhone = contactPhone;
		this.contactMobile = contactMobile;
		this.contactEmail = contactEmail;
		this.quantity = quantity;
		this.description = description;
		this.keywords = keywords;
		this.address = address;
		this.geometry = geometry;
		this.creationDate = creationDate;
		this.endDate = endDate;
		this.startDate = startDate;
		this.expirationDate = expirationDate;
		this.updateDate = updateDate;
		this.isActive = isActive;
		this.statut = statut;
		this.reference = reference;
		this.user = user;
		this.matched = matched;
	}

	/**
	 * Constructor with all fields
	 * 
	 * @param context
	 * @param _id
	 * @param nompId
	 * @param name
	 * @param classification
	 * @param classificationName
	 * @param sourceActorType
	 * @param sourceActorTypeName
	 * @param targetActorType
	 * @param targetActorTypeName
	 * @param contactPhone
	 * @param contactMobile
	 * @param contactEmail
	 * @param quantity
	 * @param description
	 * @param keywords
	 * @param address
	 * @param geometry
	 * @param creationDate
	 * @param endDate
	 * @param startDate
	 * @param expirationDate
	 * @param updateDate
	 * @param isActive
	 * @param statut
	 * @param reference
	 * @param user
	 * @param matched
	 */
	public Ticket(Context context, long _id, String nompId, String name,
			String classification, String classificationName,
			String sourceActorType, String sourceActorTypeName,
			String targetActorType, String targetActorTypeName,
			String contactPhone, String contactMobile, String contactEmail,
			int quantity, String description, String keywords, String address,
			String geometry, String creationDate, String endDate,
			String startDate, String expirationDate, String updateDate,
			boolean isActive, int statut, String reference, String user,
			String matched) {
		super(context);
		this._id = _id;
		this.nompId = nompId;
		this.name = name;
		this.classification = classification;
		this.classificationName = classificationName;
		this.sourceActorType = sourceActorType;
		this.sourceActorTypeName = sourceActorTypeName;
		this.targetActorType = targetActorType;
		this.targetActorTypeName = targetActorTypeName;
		this.contactPhone = contactPhone;
		this.contactMobile = contactMobile;
		this.contactEmail = contactEmail;
		this.quantity = quantity;
		this.description = description;
		this.keywords = keywords;
		this.address = address;
		this.geometry = geometry;
		this.creationDate = creationDate;
		this.endDate = endDate;
		this.startDate = startDate;
		this.expirationDate = expirationDate;
		this.updateDate = updateDate;
		this.isActive = isActive;
		this.statut = statut;
		this.reference = reference;
		this.user = user;
		this.matched = matched;
	}

	public Ticket[] insertAll(Ticket[] tickets) {
		// open database writable connection
		SQLiteDatabase writable = this.getWritableDatabase();

		// prepare the insert query
		String query = "INSERT INTO "
				+ getTableName()
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, )";

		// compile the query
		SQLiteStatement statement = writable.compileStatement(query);

		// begin transaction
		writable.beginTransaction();

		for (int i = 0; i < tickets.length; i++) {
			Ticket ticket = tickets[i];

			// bind the fields to the statement
			statement.clearBindings();
			statement.bindString(2, ticket.getNompId());
			statement.bindString(3, ticket.getName());
			statement.bindString(4, ticket.getClassification());
			statement.bindString(5, ticket.getClassificationName());
			statement.bindString(6, ticket.getSourceActorType());
			statement.bindString(7, ticket.getSourceActorTypeName());
			statement.bindString(8, ticket.getTargetActorType());
			statement.bindString(9, ticket.getTargetActorTypeName());
			statement.bindString(10, ticket.getContactPhone());
			statement.bindString(11, ticket.getContactMobile());
			statement.bindString(12, ticket.getContactEmail());
			statement.bindLong(13, ticket.getQuantity());
			statement.bindString(14, ticket.getDescription());

			if (ticket.getKeywords() != null) {
				statement.bindString(15, ticket.getKeywords());
			} else {
				statement.bindNull(15);
			}

			statement.bindString(16, ticket.getAddress());

			if (ticket.getGeometry() != null) {
				statement.bindString(17, ticket.getGeometry());
			} else {
				statement.bindNull(17);
			}

			statement.bindString(18, ticket.getCreationDate());
			statement.bindString(19, ticket.getEndDate());
			statement.bindString(20, ticket.getStartDate());
			statement.bindString(21, ticket.getExpirationDate());
			statement.bindString(22, ticket.getUpdateDate());
			statement.bindLong(23, ticket.isActive() ? 1 : 0);
			statement.bindLong(24, ticket.getStatut());

			if (ticket.getReference() != null) {
				statement.bindString(25, ticket.getReference());
			} else {
				statement.bindNull(25);
			}

			if (ticket.getUser() != null) {
				statement.bindString(26, ticket.getUser());
			} else {
				statement.bindNull(26);
			}

			if (ticket.getMatched() != null) {
				statement.bindString(27, ticket.getMatched());
			} else {
				statement.bindNull(27);
			}

			statement.bindLong(28, ticket.getPrice());

			// commit the execution of statement
			tickets[i].set_id(statement.executeInsert());
		}

		// commit the transaction
		writable.setTransactionSuccessful();
		writable.endTransaction();
		writable.close();

		return tickets;
	}

	public ContentValues getBaseContentValues() {
		ContentValues mContentValues = new ContentValues();

		mContentValues.put(NOMPDataContract.Ticket.COLUMN_NAME_NOMP_ID, nompId);

		mContentValues.put(NOMPDataContract.Ticket.COLUMN_NAME_NAME, name);

		mContentValues.put(NOMPDataContract.Ticket.COLUMN_NAME_CLASSIFICATION,
				classification);
		mContentValues.put(
				NOMPDataContract.Ticket.COLUMN_NAME_CLASSIFICATION_NAME,
				classificationName);

		mContentValues.put(
				NOMPDataContract.Ticket.COLUMN_NAME_SOURCE_ACTOR_TYPE,
				sourceActorType);
		mContentValues.put(
				NOMPDataContract.Ticket.COLUMN_NAME_SOURCE_ACTOR_TYPE_NAME,
				sourceActorTypeName);
		mContentValues.put(
				NOMPDataContract.Ticket.COLUMN_NAME_TARGET_ACTOR_TYPE,
				targetActorType);
		mContentValues.put(
				NOMPDataContract.Ticket.COLUMN_NAME_TARGET_ACTOR_TYPE_NAME,
				targetActorTypeName);

		mContentValues.put(NOMPDataContract.Ticket.COLUMN_NAME_CONTACT_PHONE,
				contactPhone);
		mContentValues.put(NOMPDataContract.Ticket.COLUMN_NAME_CONTACT_MOBILE,
				contactMobile);
		mContentValues.put(NOMPDataContract.Ticket.COLUMN_NAME_CONTACT_EMAIL,
				contactEmail);

		mContentValues.put(NOMPDataContract.Ticket.COLUMN_NAME_QUANTITY,
				quantity);

		mContentValues.put(NOMPDataContract.Ticket.COLUMN_NAME_DESCRIPTION,
				description);
		mContentValues.put(NOMPDataContract.Ticket.COLUMN_NAME_KEYWORDS,
				keywords);

		mContentValues
				.put(NOMPDataContract.Ticket.COLUMN_NAME_ADDRESS, address);
		mContentValues.put(NOMPDataContract.Ticket.COLUMN_NAME_GEOMETRY,
				geometry);

		mContentValues.put(NOMPDataContract.Ticket.COLUMN_NAME_CREATION_DATE,
				creationDate);
		mContentValues.put(NOMPDataContract.Ticket.COLUMN_NAME_END_DATE,
				endDate);
		mContentValues.put(NOMPDataContract.Ticket.COLUMN_NAME_START_DATE,
				startDate);
		mContentValues.put(NOMPDataContract.Ticket.COLUMN_NAME_EXPIRATION_DATE,
				expirationDate);
		mContentValues.put(NOMPDataContract.Ticket.COLUMN_NAME_UPDATE_DATE,
				updateDate);

		mContentValues.put(NOMPDataContract.Ticket.COLUMN_NAME_IS_ACTIVE,
				isActive ? 1 : 0);

		mContentValues.put(NOMPDataContract.Ticket.COLUMN_NAME_STATUT, statut);

		mContentValues.put(NOMPDataContract.Ticket.COLUMN_NAME_REFERENCE,
				reference);

		mContentValues.put(NOMPDataContract.Ticket.COLUMN_NAME_USER, user);

		mContentValues
				.put(NOMPDataContract.Ticket.COLUMN_NAME_MATCHED, matched);

		return mContentValues;
	}

	protected Cursor retrieveBase(long ticketId) {
		// prepare the query
		String query = "SELECT * FROM " + getTableName() + " WHERE _id="
				+ ticketId + " ORDER BY _id DESC LIMIT 1";

		SQLiteDatabase readable = this.getReadableDatabase();
		Cursor c = readable.rawQuery(query, null);

		if (c.moveToFirst()) {
			this.setNompId(c.getString(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_NOMP_ID)));

			this.set_id(c.getInt(c.getColumnIndex(NOMPDataContract.Ticket._ID)));

			this.setName(c.getString(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_NAME)));

			this.setClassification(c.getString(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_CLASSIFICATION)));
			this.setClassificationName(c.getString(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_CLASSIFICATION_NAME)));

			this.setSourceActorType(c.getString(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_SOURCE_ACTOR_TYPE)));
			this.setSourceActorTypeName(c.getString(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_SOURCE_ACTOR_TYPE_NAME)));
			this.setTargetActorType(c.getString(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_TARGET_ACTOR_TYPE)));
			this.setTargetActorTypeName(c.getString(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_TARGET_ACTOR_TYPE_NAME)));

			this.setContactPhone(c.getString(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_CONTACT_PHONE)));
			this.setContactMobile(c.getString(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_CONTACT_MOBILE)));
			this.setContactEmail(c.getString(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_CONTACT_EMAIL)));

			this.setQuantity(c.getInt(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_QUANTITY)));

			this.setDescription(c.getString(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_DESCRIPTION)));
			this.setKeywords(c.getString(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_KEYWORDS)));

			this.setAddress(c.getString(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_ADDRESS)));
			this.setGeometry(c.getString(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_GEOMETRY)));

			// DATES
			this.setCreationDate(c.getString(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_CREATION_DATE)));
			this.setEndDate(c.getString(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_END_DATE)));
			this.setStartDate(c.getString(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_START_DATE)));
			this.setExpirationDate(c.getString(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_EXPIRATION_DATE)));
			this.setUpdateDate(c.getString(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_UPDATE_DATE)));

			this.setActive(c.getInt(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_IS_ACTIVE)) == 1 ? true
					: false);

			this.setStatut(c.getInt(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_STATUT)));

			this.setReference(c.getString(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_REFERENCE)));

			this.setUser(c.getString(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_USER)));

			this.setMatched(c.getString(c
					.getColumnIndex(NOMPDataContract.Ticket.COLUMN_NAME_MATCHED)));
		}

		readable.close();
		return c;
	}
	
	public int deleteAll() {
		SQLiteDatabase writable = this.getWritableDatabase();
		int nbLines = writable.delete(getTableName(), "1", null);

		writable.close();
		return nbLines;
	}

	public abstract String getTableName();

	// budget or cost, implemented in Need/Offer
	public abstract int getPrice();

	/*
	 * Getters and setters
	 */

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

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getClassificationName() {
		return classificationName;
	}

	public void setClassificationName(String classificationName) {
		this.classificationName = classificationName;
	}

	public String getSourceActorType() {
		return sourceActorType;
	}

	public void setSourceActorType(String sourceActorType) {
		this.sourceActorType = sourceActorType;
	}

	public String getSourceActorTypeName() {
		return sourceActorTypeName;
	}

	public void setSourceActorTypeName(String sourceActorTypeName) {
		this.sourceActorTypeName = sourceActorTypeName;
	}

	public String getTargetActorType() {
		return targetActorType;
	}

	public void setTargetActorType(String targetActorType) {
		this.targetActorType = targetActorType;
	}

	public String getTargetActorTypeName() {
		return targetActorTypeName;
	}

	public void setTargetActorTypeName(String targetActorTypeName) {
		this.targetActorTypeName = targetActorTypeName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGeometry() {
		return geometry;
	}

	public void setGeometry(String geometry) {
		this.geometry = geometry;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public int getStatut() {
		return statut;
	}

	public void setStatut(int statut) {
		this.statut = statut;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getMatched() {
		return matched;
	}

	public void setMatched(String matched) {
		this.matched = matched;
	}

}
