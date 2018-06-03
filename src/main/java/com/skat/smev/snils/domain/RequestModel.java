package com.skat.smev.snils.domain;

/**
 * 
 * Модель json-запроса
 *
 */
public class RequestModel {

	private String familyName;
	private String firstName;
	private String patronymic;
	private Gender gender;
	private String birthDate;
	private BirthPlace birthPlace;
	private IdentityDocument identityDocument;

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getPatronymic() {
		return patronymic;
	}

	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public BirthPlace getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(BirthPlace birthPlace) {
		this.birthPlace = birthPlace;
	}

	public IdentityDocument getIdentityDocument() {
		return identityDocument;
	}

	public void setIdentityDocument(IdentityDocument identityDocument) {
		this.identityDocument = identityDocument;
	}
}