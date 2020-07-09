package org.efaps.ubl;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.StringUtils;

import com.helger.ubl21.UBL21Writer;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.AddressLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.AddressType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.CountryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.CustomerPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyIdentificationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyLegalEntityType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyNameType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.SupplierPartyType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.AddressTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.DocumentCurrencyCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IdentificationCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.InvoiceTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;

public class sim
{

    public static String AGENCYNAME = "PE:SUNAT";


    public static void main(final String[] args)
        throws DatatypeConfigurationException
    {
        final InvoiceType invoice = new InvoiceType();
        invoice.setUBLVersionID("2.1");
        invoice.setID("F001-000126");
        final XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar("2020-06-26");
        invoice.setIssueDate(new IssueDateType(date2));
        invoice.setInvoiceTypeCode(getReceipt());
        invoice.setDocumentCurrencyCode(getDocumentCurrencyCodeType("PEN"));
        invoice.setAccountingSupplierParty(getSupplier("Tiendas Mass", "6", "20601327318", "150101", "1000", "Lima",
                        "JR. CRESPO Y CASTILLO NRO. 2087"));

        invoice.setAccountingCustomerParty(getCustomer("Tovar Lopez, Julio Odair", "1", "43289672" , "Av parque alto 291-A Lima - Lima - Santiago De Surco"));

        UBL21Writer.invoice()
            .setCharset(StandardCharsets.UTF_8)
            .setFormattedOutput(true)
            .write(invoice, new File("target/dummy-invoice.xml"));
    }

    public static InvoiceTypeCodeType getReceipt()
    {
        final var ret = new InvoiceTypeCodeType();
        ret.setListAgencyName("PE:SUNAT");
        ret.setListID("0101");
        ret.setListName("Tipo de Documento");
        ret.setListURI("urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo01");
        ret.setValue("03");
        return ret;
    }

    public static DocumentCurrencyCodeType getDocumentCurrencyCodeType(final String isoCode)
    {
        final var ret = new DocumentCurrencyCodeType();
        ret.setListAgencyName("United Nations Economic Commission for Europe");
        ret.setListID("ISO 4217 Alpha");
        ret.setListName("Currency");
        ret.setValue(isoCode);
        return ret;
    }

    public static SupplierPartyType getSupplier(final String name, final String doiType, final String doi, final String ubigeo,
                                                final String anexo, final String district, final String streetName)
    {
        final var ret = new SupplierPartyType();
        final var party = new PartyType();
        ret.setParty(party);
        party.setPartyIdentification(Collections.singletonList(getPartyIdentificationType(doiType, doi)));

        final var partyNameType = new PartyNameType();
        partyNameType.setName(name);
        party.setPartyName(Collections.singletonList(partyNameType));
        party.setPartyLegalEntity(
                        Collections.singletonList(getPartyLegalEntityType(name, ubigeo, anexo, district, streetName)));
        return ret;
    }

    public static PartyIdentificationType getPartyIdentificationType(final String doiType, final String doi)
    {
        final var ret = new PartyIdentificationType();
        final var idType = new IDType();
        idType.setSchemeAgencyName("PE:SUNAT");
        idType.setSchemeID(doiType);
        idType.setSchemeName("Documento de Identidad");
        idType.setSchemeURI("urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo06");
        idType.setValue(doi);
        ret.setID(idType);
        return ret;
    }

    public static PartyLegalEntityType getPartyLegalEntityType(final String name, final String ubigeo,
                                                               final String anexo, final String district,
                                                               final String streetName)
    {
        final var ret = new PartyLegalEntityType();
        ret.setRegistrationName(name);
        ret.setRegistrationAddress(getAddress(ubigeo, anexo, district, streetName));
        return ret;
    }

    public static AddressType getAddress(final String ubigeo, final String anexo, final String district,
                                         final String streetName)
    {
        final var ret = new AddressType();
        if (StringUtils.isNotEmpty(ubigeo)) {
            ret.setID(ubigeo);
        }
        if (StringUtils.isNotEmpty(ubigeo)) {
            ret.setAddressTypeCode(getAddressTypeCode(anexo));
        }

        if (StringUtils.isNotEmpty(district)) {
            ret.setStreetName(streetName);
            ret.setDistrict(district);
        }
        final var addressLineType = new AddressLineType();
        addressLineType.setLine(streetName);
        ret.setAddressLine(Collections.singletonList(addressLineType));
        ret.setCountry(getCountryType("PE"));
        return ret;
    }

    public static AddressTypeCodeType getAddressTypeCode(final String anexo)
    {
        final var ret = new AddressTypeCodeType();
        ret.setListAgencyName(AGENCYNAME);
        ret.setListName("Establecimientos anexos");
        ret.setValue(anexo);
        return ret;
    }

    public static CountryType getCountryType(final String isoCode) {
        final var ret = new CountryType();
        final var identificationCodeType = new IdentificationCodeType();
        identificationCodeType.setListAgencyName("United Nations Economic Commission for Europe");
        identificationCodeType.setListID("ISO 3166-1");
        identificationCodeType.setListName("Country");
        identificationCodeType.setValue(isoCode);
        ret.setIdentificationCode(identificationCodeType);
        return ret;
    }

    public static CustomerPartyType getCustomer(final String name, final String doiType, final String doi,
                                                final String address)
    {
        final var ret = new CustomerPartyType();
        final var party = new PartyType();
        ret.setParty(party);
        party.setPartyIdentification(Collections.singletonList(getPartyIdentificationType(doiType, doi)));
        party.setPartyLegalEntity(
                        Collections.singletonList(getPartyLegalEntityType(name, null, null, null, address)));
        return ret;
    }

}
