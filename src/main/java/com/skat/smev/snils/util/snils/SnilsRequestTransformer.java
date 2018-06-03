package com.skat.smev.snils.util.snils;

import com.skat.smev.snils.domain.BirthPlace;
import com.skat.smev.snils.domain.IdentityDocument;
import com.skat.smev.snils.domain.RequestModel;
import com.skat.smev.snils.model.*;
import com.skat.smev.snils.util.DateUtil;
import com.skat.smev.snils.util.XmlUtil;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;

/**
 *  Класс для преобразования json-запроса в xml о схеме вида сведений
 */
public class SnilsRequestTransformer {
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * Метод преобразования запроса из JSON-модели XML-модель формата вида сведения
     * @param model модель запроса из JSON
     * @return XML-модель вида сведения
     * @throws ParseException
     * @throws DatatypeConfigurationException
     */
    public SnilsByAdditionalDataRequest createSnilsAdditionalByDataRequest(
            RequestModel model) throws ParseException,
            DatatypeConfigurationException {
        ObjectFactory factory = new ObjectFactory();
        SnilsByAdditionalDataRequest snilsByDataRequest = factory.createSnilsByAdditionalDataRequest();
        XMLGregorianCalendar formattedBirthDate = DateUtil.parseXMLGregorianCalendar(
                model.getBirthDate(), DATE_FORMAT);
        snilsByDataRequest.setBirthDate(formattedBirthDate);
        snilsByDataRequest.setFamilyName(model.getFamilyName());
        snilsByDataRequest.setFirstName(model.getFirstName());
        snilsByDataRequest.setPatronymic(model.getPatronymic());
        snilsByDataRequest.setGender(model.getGender() == null? GenderType.MALE :
                GenderType.FEMALE.value().equalsIgnoreCase(model.getGender().toString()) ?
                        GenderType.FEMALE : GenderType.MALE);

        final BirthPlace birthPlace = model.getBirthPlace();
        BirthPlaceType birthPlaceType = factory.createBirthPlaceType();
        birthPlaceType.setCountry(birthPlace.getCountry());
        birthPlaceType.setDistrict(birthPlace.getDistrict());
        birthPlaceType.setPlaceType(birthPlace.getPlaceType());
        birthPlaceType.setRegion(birthPlace.getRegion());
        birthPlaceType.setSettlement(birthPlace.getSettlement());
        snilsByDataRequest.setBirthPlace(birthPlaceType);

        if(model.getIdentityDocument() != null){
            final IdentityDocument identityDocument = model.getIdentityDocument();
            XMLGregorianCalendar issueDateFormatted = DateUtil.parseXMLGregorianCalendar(
                    identityDocument.getIssueDate(), DATE_FORMAT);
            final String series = identityDocument.getSeries() ;
            final String number = identityDocument.getNumber();
            final String issuer = identityDocument.getIssuer();
            switch (identityDocument.getDocType()) {
                case BIRTH_CERTIFICATE:
                    SovietPassportType birthCertType = factory.createSovietPassportType();
                    birthCertType.setIssueDate(issueDateFormatted);
                    birthCertType.setIssuer(issuer);
                    birthCertType.setSeries(series);
                    birthCertType.setNumber(number);
                    snilsByDataRequest.setBirthCertificate(birthCertType);
                    break;
                case PASSPORT_RF:
                    PassportRFType passportRFType = factory.createPassportRFType();
                    passportRFType.setIssueDate(issueDateFormatted);
                    passportRFType.setIssuer(issuer);
                    passportRFType.setSeries(series);
                    passportRFType.setNumber(number);
                    snilsByDataRequest.setPassportRF(passportRFType);
                    break;
                case DRIVING_LICENSE_RF:
                    DrivingLicenseRFType drivingLicenseRFType = factory.createDrivingLicenseRFType();
                    drivingLicenseRFType.setIssueDate(issueDateFormatted);
                    drivingLicenseRFType.setIssuer(issuer);
                    drivingLicenseRFType.setSeries(series);
                    drivingLicenseRFType.setNumber(number);
                    snilsByDataRequest.setDrivingLicenseRF(drivingLicenseRFType);
                    break;
                case FOREIGN_PASSPORT:
                    NotRestrictedDocumentType foreignPassport = factory.createNotRestrictedDocumentType();
                    foreignPassport.setIssueDate(issueDateFormatted);
                    foreignPassport.setIssuer(issuer);
                    foreignPassport.setSeries(series);
                    foreignPassport.setNumber(number);
                    snilsByDataRequest.setForeignPassport(foreignPassport);
                    break;
                case INTERNATIONAL_PASSPORT_RF:
                    InternationalPassportRFType internationalPassportRFType = factory.createInternationalPassportRFType();
                    internationalPassportRFType.setIssueDate(issueDateFormatted);
                    internationalPassportRFType.setIssuer(issuer);
                    internationalPassportRFType.setSeries(series);
                    internationalPassportRFType.setNumber(number);
                    snilsByDataRequest.setInternationalPassportRF(internationalPassportRFType);
                    break;
                case FORM_9_CERTIFICATE:
                    NotRestrictedDocumentType form9certificate = factory.createNotRestrictedDocumentType();
                    form9certificate.setIssueDate(issueDateFormatted);
                    form9certificate.setIssuer(issuer);
                    form9certificate.setSeries(series);
                    form9certificate.setNumber(number);
                    snilsByDataRequest.setForm9Certificate(form9certificate);
                    break;
                case MILITARY_PASSPORT:
                    MilitaryPassportDocumentType militaryPassportDocumentType = factory.createMilitaryPassportDocumentType();
                    militaryPassportDocumentType.setIssueDate(issueDateFormatted);
                    militaryPassportDocumentType.setIssuer(issuer);
                    militaryPassportDocumentType.setSeries(series);
                    militaryPassportDocumentType.setNumber(number);
                    snilsByDataRequest.setMilitaryPassport(militaryPassportDocumentType);
                    break;
                case PASSPORT_LOSS_CERTIFICATE:
                    NotRestrictedDocumentType passportLossCertificate = factory.createNotRestrictedDocumentType();
                    passportLossCertificate.setIssueDate(issueDateFormatted);
                    passportLossCertificate.setIssuer(issuer);
                    passportLossCertificate.setSeries(series);
                    passportLossCertificate.setNumber(number);
                    snilsByDataRequest.setPassportLossCertificate(passportLossCertificate);
                    break;
//                case PFR_IDENTIFICATION_DOCUMENT:
//                    IdentificationDocumentType pfrIdentityDoc = factory.createIdentificationDocumentType();
//                    pfrIdentityDoc.setIssueDate();
//                    pfrIdentityDoc.setIssuer(issuer);
//                    pfrIdentityDoc.setSeries(series);
//                    pfrIdentityDoc.setNumber(number);
//                    snilsByDataRequest.setPfrIdentificationDocument(pfrIdentityDoc);
//                    break;
                case RELEASE_CERTIFICATE:
                    NotRestrictedDocumentType releaseCertificate = factory.createNotRestrictedDocumentType();
                    releaseCertificate.setIssueDate(issueDateFormatted);
                    releaseCertificate.setIssuer(issuer);
                    releaseCertificate.setSeries(series);
                    releaseCertificate.setNumber(number);
                    snilsByDataRequest.setReleaseCertificate(releaseCertificate);
                    break;
                case RESIDENCE_PERMIT_RF:
                    InternationalPassportRFType recidencePermitType = factory.createInternationalPassportRFType();
                    recidencePermitType.setIssueDate(issueDateFormatted);
                    recidencePermitType.setIssuer(issuer);
                    recidencePermitType.setSeries(series);
                    recidencePermitType.setNumber(number);
                    snilsByDataRequest.setResidencePermitRF(recidencePermitType);
                    break;
                case SAILOR_PASSPORT:
                    MilitaryPassportDocumentType sailorPassport = factory.createMilitaryPassportDocumentType();
                    sailorPassport.setIssueDate(issueDateFormatted);
                    sailorPassport.setIssuer(issuer);
                    sailorPassport.setSeries(series);
                    sailorPassport.setNumber(number);
                    snilsByDataRequest.setSailorPassport(sailorPassport);
                    break;
                case SOVIET_PASSPORT:
                    SovietPassportType sovietPassport = factory.createSovietPassportType();
                    sovietPassport.setIssueDate(issueDateFormatted);
                    sovietPassport.setIssuer(issuer);
                    sovietPassport.setSeries(series);
                    sovietPassport.setNumber(number);
                    snilsByDataRequest.setSovietPassport(sovietPassport);
                    break;
                case TEMPORARY_IDENTITY_CARD_RF:
                    NotRestrictedDocumentType temporaryIdentityCardRF = factory.createNotRestrictedDocumentType();
                    temporaryIdentityCardRF.setIssueDate(issueDateFormatted);
                    temporaryIdentityCardRF.setIssuer(issuer);
                    temporaryIdentityCardRF.setSeries(series);
                    temporaryIdentityCardRF.setNumber(number);
                    snilsByDataRequest.setTemporaryIdentityCardRF(temporaryIdentityCardRF);
                    break;
            }
        }
        return snilsByDataRequest;
    }

    /**
     * Метод преобразования ответа от сервиса
     * @param xml тело xml-ответа
     * @return возвращает сущность ответа необходимого вида сведений
     * @throws Exception
     */
    public SnilsByAdditionalDataResponse parseContent(String xml)
            throws Exception {
        return XmlUtil.unmarshal(xml, SnilsByAdditionalDataResponse.class);
    }
}
