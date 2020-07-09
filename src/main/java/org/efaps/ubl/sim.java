package org.efaps.ubl;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.efaps.ubl.extension.AdditionalInformation;
import org.efaps.ubl.extension.AdditionalProperty;

import com.helger.ubl21.UBL21NamespaceContext;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.AddressLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.AddressType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.CountryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.CustomerPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.InvoiceLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.ItemIdentificationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.ItemType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.MonetaryTotalType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyIdentificationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyLegalEntityType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyNameType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PriceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PricingReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.SupplierPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.TaxCategoryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.TaxSchemeType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.TaxSubtotalType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.TaxTotalType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.AddressTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.AllowanceTotalAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.ChargeTotalAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.DescriptionType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.DocumentCurrencyCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IdentificationCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.InvoiceTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.InvoicedQuantityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.LineExtensionAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.PayableAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.PriceAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.TaxAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.TaxExclusiveAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.TaxInclusiveAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.TaxableAmountType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_21.ExtensionContentType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_21.UBLExtensionType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_21.UBLExtensionsType;
import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.AmountType;

public class sim
{

    public static String AGENCYNAME = "PE:SUNAT";

    public static void main(final String[] args)
        throws DatatypeConfigurationException
    {
        UBL21NamespaceContext.getInstance().addMapping("sac", "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1");


        final InvoiceType invoice = new InvoiceType();
        invoice.setUBLVersionID("2.1");
        invoice.setID("F001-000126");
        final XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar("2020-06-26");
        invoice.setIssueDate(new IssueDateType(date2));
        invoice.setInvoiceTypeCode(getReceipt());
        invoice.setDocumentCurrencyCode(getDocumentCurrencyCodeType("PEN"));
        invoice.setAccountingSupplierParty(getSupplier("Tiendas Mass", "6", "20601327318", "150101", "1000", "Lima",
                        "JR. CRESPO Y CASTILLO NRO. 2087"));
        invoice.setAccountingCustomerParty(getCustomer("Tovar Lopez, Julio Odair", "1", "43289672",
                        "Av parque alto 291-A Lima - Lima - Santiago De Surco"));

        invoice.setTaxTotal(Collections.singletonList(getTaxTotal()));

        invoice.setLegalMonetaryTotal(getMonetaryTotal());

        invoice.setInvoiceLine(getInvoiceLines());

        invoice.setUBLExtensions(getWordsForAmount(new BigDecimal("2133")));

       new Builder()
                        .setCharset(StandardCharsets.UTF_8)
                        .setFormattedOutput(true)
                        .write(invoice, new File("target/dummy-invoice.xml"));
    }

    private static UBLExtensionsType getWordsForAmount(final BigDecimal amount) {
        final var ret = new UBLExtensionsType();
        final var ublExtension = new UBLExtensionType();

        final var extension = new ExtensionContentType();
        ublExtension.setExtensionContent(extension);
        final var additionalInformation = new AdditionalInformation();
        extension.setAny(additionalInformation);
        final var additionalProperty = new AdditionalProperty();
        additionalInformation.setAdditionalProperty(additionalProperty);
        additionalProperty.setId("1000");
        additionalProperty.setValue(getWords4Number(amount));
        ret.addUBLExtension(ublExtension);
        return ret;
    }

    protected static String getWords4Number(final BigDecimal _amount)

    {
        return new StringBuilder().append(org.efaps.number2words.Converter.getMaleConverter(
                        new Locale("es")).convert(_amount.longValue())).append(" y ")
                        .append(_amount.setScale(2, RoundingMode.HALF_UP).toPlainString().replaceAll("^.*\\.", ""))
                        .append("/100 ").toString().toUpperCase();
    }


    private static List<InvoiceLineType> getInvoiceLines()
    {
        final var ret = new ArrayList<InvoiceLineType>();

        final var invoiceLine = new InvoiceLineType();
        ret.add(invoiceLine);
        invoiceLine.setID("1");
        invoiceLine.setInvoicedQuantity(getInvoicedQuantity(new BigDecimal("2"), "NIU"));
        invoiceLine.setLineExtensionAmount(getAmount(LineExtensionAmountType.class, new BigDecimal("339")));

        final var pricingReference = new PricingReferenceType();
        final var priceType = new PriceType();
        priceType.setPriceAmount(getAmount(PriceAmountType.class, new BigDecimal("400.02")));
        priceType.setPriceTypeCode("01");
        pricingReference.setAlternativeConditionPrice(Collections.singletonList(priceType));
        invoiceLine.setPricingReference(pricingReference);
        invoiceLine.setTaxTotal(Collections.singletonList(getTaxTotal()));
        invoiceLine.setItem(getItem());

        final var priceType2 = new PriceType();
        priceType2.setPriceAmount(getAmount(PriceAmountType.class, new BigDecimal("339")));
        invoiceLine.setPrice(priceType2);

        return ret;
    }

    public static ItemType getItem() {
        final var ret = new ItemType();
        final var description = new DescriptionType();
        description.setValue("Lenovo Tablet 7\" TB-7305F 1GB / 16GB Cam post 2MP / Front 2MP");
        ret.setDescription(Collections.singletonList(description));
        final var itemIdentificationType = new ItemIdentificationType();
        itemIdentificationType.setID("1010204.0013");
        ret.setSellersItemIdentification(itemIdentificationType);
        return ret;
    }

    public static InvoicedQuantityType getInvoicedQuantity(final BigDecimal quantity, final String unitCode) {
        final var ret = new InvoicedQuantityType();
        ret.setUnitCode(unitCode);
        ret.setUnitCodeListAgencyName("United Nations Economic Commission for Europe");
        ret.setUnitCodeListID("UN/ECE rec 20");
        ret.setValue(quantity);
        return ret;
    }


    public static MonetaryTotalType getMonetaryTotal()
    {
        final var ret = new MonetaryTotalType();
        ret.setLineExtensionAmount(getAmount(LineExtensionAmountType.class, new BigDecimal("2133")));
        ret.setTaxExclusiveAmount(getAmount(TaxExclusiveAmountType.class, new BigDecimal("1807.63")));
        ret.setTaxInclusiveAmount(getAmount(TaxInclusiveAmountType.class, new BigDecimal("2516.94")));
        ret.setAllowanceTotalAmount(getAmount(AllowanceTotalAmountType.class, new BigDecimal("0")));
        ret.setChargeTotalAmount(getAmount(ChargeTotalAmountType.class, new BigDecimal("0")));
        ret.setPayableAmount(getAmount(PayableAmountType.class, new BigDecimal("2133")));
        return ret;
    }

    public static TaxTotalType getTaxTotal()
    {
        final var ret = new TaxTotalType();
        ret.setTaxAmount(getAmount(TaxAmountType.class, BigDecimal.TEN));
        ret.setTaxSubtotal(Collections.singletonList(getTaxSubtotal()));
        return ret;
    }

    public static <T extends AmountType> T getAmount(final Class<T> type, final BigDecimal amount) {
        T ret = null;
        try {
            ret = type.getConstructor().newInstance();
            ret.setCurrencyID("PEN");
            ret.setValue(amount);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                        | NoSuchMethodException | SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
    }

    public static TaxSubtotalType getTaxSubtotal()
    {
        final var ret = new TaxSubtotalType();
        ret.setTaxableAmount(getAmount(TaxableAmountType.class, new BigDecimal("1807.63")));
        ret.setTaxAmount(getAmount(TaxAmountType.class, new BigDecimal("325.37")));
        ret.setPercent(new BigDecimal("18"));
        ret.setTaxCategory(getTaxCategory());
        return ret;
    }

    public static TaxCategoryType getTaxCategory() {
        final var ret = new TaxCategoryType();
        final var taxScheme = new TaxSchemeType();
        final var idType = new IDType();
        idType.setSchemeAgencyID("6");
        idType.setSchemeID("UN/ECE 5153");
        idType.setValue("1000");
        taxScheme.setID(idType);
        taxScheme.setName("IGV");
        taxScheme.setTaxTypeCode("VAT");
        ret.setTaxScheme(taxScheme);
        return ret;
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

    public static SupplierPartyType getSupplier(final String name, final String doiType, final String doi,
                                                final String ubigeo,
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

    public static CountryType getCountryType(final String isoCode)
    {
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
