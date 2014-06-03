package fr.utt.isi.nomp_mobile.models;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.ContentValues;
import android.content.Context;
import fr.utt.isi.nomp_mobile.database.NOMPDataContract;

public abstract class Ticket extends BaseModel {

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

	protected Calendar creationDate;
	protected Calendar endDate;
	protected Calendar startDate;
	protected Calendar expirationDate;
	protected Calendar updateDate;

	protected boolean isActive;

	protected int statut;

	protected String reference;

	protected String user;

	protected String matched;

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

		this.creationDate = new GregorianCalendar();
		this.updateDate = new GregorianCalendar();
		this.expirationDate = new GregorianCalendar();
		this.expirationDate.add(Calendar.MONTH, 3);
		this.startDate = new GregorianCalendar();
		this.endDate = new GregorianCalendar();
		this.endDate.add(Calendar.MONTH, 3);

		this.isActive = true;

		this.statut = Status.OPEN;

		this.reference = "";

		this.user = "";

		this.matched = null;
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

		mContentValues.put(
				NOMPDataContract.Ticket.COLUMN_NAME_CREATION_DATE,
				creationDate.get(Calendar.YEAR) + "-"
						+ creationDate.get(Calendar.MONTH) + "-"
						+ creationDate.get(Calendar.DAY_OF_MONTH));
		mContentValues.put(NOMPDataContract.Ticket.COLUMN_NAME_END_DATE,
				endDate.get(Calendar.YEAR) + "-" + endDate.get(Calendar.MONTH)
						+ "-" + endDate.get(Calendar.DAY_OF_MONTH));
		mContentValues.put(
				NOMPDataContract.Ticket.COLUMN_NAME_START_DATE,
				startDate.get(Calendar.YEAR) + "-"
						+ startDate.get(Calendar.MONTH) + "-"
						+ startDate.get(Calendar.DAY_OF_MONTH));
		mContentValues.put(
				NOMPDataContract.Ticket.COLUMN_NAME_EXPIRATION_DATE,
				expirationDate.get(Calendar.YEAR) + "-"
						+ expirationDate.get(Calendar.MONTH) + "-"
						+ expirationDate.get(Calendar.DAY_OF_MONTH));
		mContentValues.put(
				NOMPDataContract.Ticket.COLUMN_NAME_UPDATE_DATE,
				updateDate.get(Calendar.YEAR) + "-"
						+ updateDate.get(Calendar.MONTH) + "-"
						+ updateDate.get(Calendar.DAY_OF_MONTH));

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

	public Calendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public Calendar getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Calendar expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Calendar getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Calendar updateDate) {
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
