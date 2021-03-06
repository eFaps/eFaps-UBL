/*
 * Copyright 2003 - 2020 The eFaps Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.efaps.ubl.documents;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.AddressLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.AddressType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.AttachmentType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.CountryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.CustomerPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.ExternalReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.InvoiceLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.ItemIdentificationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.ItemType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyIdentificationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyLegalEntityType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyNameType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PaymentTermsType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PriceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PricingReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.SignatureType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.SupplierPartyType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.AddressTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.AllowanceChargeReasonCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.DescriptionType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.DocumentCurrencyCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IdentificationCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.InstallmentDueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.InvoiceTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.InvoicedQuantityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.LineExtensionAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.NoteType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.PaymentMeansIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.PriceAmountType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.AmountType;

public class Utils
{

    public static String AGENCYNAME = "PE:SUNAT";

    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

    public static InvoiceTypeCodeType getInvoiceType(final String type)
    {
        final var ret = new InvoiceTypeCodeType();
        ret.setListAgencyName("PE:SUNAT");
        ret.setListID("0101");
        ret.setListName(Catalogs.TDOC.getName());
        ret.setListURI(Catalogs.TDOC.getURI());
        ret.setValue(type);
        return ret;
    }

    public static DocumentCurrencyCodeType getDocumentCurrencyCode(final String isoCode)
    {
        final var ret = new DocumentCurrencyCodeType();
        ret.setListAgencyName("United Nations Economic Commission for Europe");
        ret.setListID("ISO 4217 Alpha");
        ret.setListName("Currency");
        ret.setValue(isoCode);
        return ret;
    }

    public static NoteType getWordsForAmount(final BigDecimal amount)
    {
        final var ret = new NoteType();
        ret.setLanguageLocaleID("1000");
        ret.setValue(getWords4Number(amount));
        return ret;
    }

    public static String getWords4Number(final BigDecimal _amount)
    {
        return new StringBuilder().append(org.efaps.number2words.Converter.getMaleConverter(
                        new Locale("es")).convert(_amount.longValue())).append(" y ")
                        .append(_amount.setScale(2, RoundingMode.HALF_UP).toPlainString().replaceAll("^.*\\.", ""))
                        .append("/100 ").toString().toUpperCase();
    }

    public static <T extends AmountType> T getAmount(final Class<T> type, final BigDecimal amount)
    {
        return getAmount(type, amount, "PEN");
    }

    public static <T extends AmountType> T getAmount(final Class<T> type, final BigDecimal amount,
                                                     final String currencyId)
    {
        T ret = null;
        try {
            ret = type.getConstructor().newInstance();
            ret.setCurrencyID(currencyId);
            ret.setValue(amount);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                        | NoSuchMethodException | SecurityException e) {
            LOG.error("Catched", e);
        }
        return ret;
    }

    public static SignatureType getSignature(final ISupplier supplier)
    {
        final var ret = new SignatureType();
        ret.setID("SB001-000095");

        final var party = new PartyType();
        final var partyIdentification = new PartyIdentificationType();
        partyIdentification.setID(supplier.getDOI());
        party.setPartyIdentification(Collections.singletonList(partyIdentification));
        final var partyName = new PartyNameType();
        partyName.setName(supplier.getName());
        party.setPartyName(Collections.singletonList(partyName));
        ret.setSignatoryParty(party);
        final var attachment = new AttachmentType();
        final var externalReference = new ExternalReferenceType();
        externalReference.setURI("SB001-000095");
        attachment.setExternalReference(externalReference);
        ret.setDigitalSignatureAttachment(attachment);
        return ret;
    }

    public static SupplierPartyType getSupplier(final ISupplier supplier)
    {
        final var ret = new SupplierPartyType();
        final var party = new PartyType();
        ret.setParty(party);
        party.setPartyIdentification(Collections.singletonList(getPartyIdentificationType(supplier)));

        final var partyNameType = new PartyNameType();
        partyNameType.setName(supplier.getName());
        party.setPartyName(Collections.singletonList(partyNameType));
        party.setPartyLegalEntity(
                        Collections.singletonList(getPartyLegalEntityType(supplier)));
        return ret;
    }

    public static CustomerPartyType getCustomer(final ICustomer customer)
    {
        final var ret = new CustomerPartyType();
        final var party = new PartyType();
        ret.setParty(party);
        party.setPartyIdentification(Collections.singletonList(getPartyIdentificationType(customer)));
        party.setPartyLegalEntity(
                        Collections.singletonList(getPartyLegalEntityType(customer)));
        return ret;
    }

    public static PartyIdentificationType getPartyIdentificationType(final IParty party)
    {
        final var ret = new PartyIdentificationType();
        final var idType = new IDType();
        idType.setSchemeAgencyName(AGENCYNAME);
        idType.setSchemeID(party.getDoiType());
        idType.setSchemeName(Catalogs.DOI.getName());
        idType.setSchemeURI(Catalogs.DOI.getURI());
        idType.setValue(party.getDOI());
        ret.setID(idType);
        return ret;
    }

    public static PartyLegalEntityType getPartyLegalEntityType(final IParty party)
    {
        final var ret = new PartyLegalEntityType();
        ret.setRegistrationName(party.getName());
        ret.setRegistrationAddress(getAddress(party));
        return ret;
    }

    public static AddressType getAddress(final IParty party)
    {
        final var ret = new AddressType();
        if (party instanceof ISupplier) {
            final var supplier = (ISupplier) party;

            if (StringUtils.isNotEmpty(supplier.getUbigeo())) {
                final var id = new IDType();
                id.setSchemeAgencyName("PE:INEI");
                id.setSchemeName("Ubigeos");
                id.setValue(supplier.getUbigeo());
                ret.setID(id);
            }
            if (StringUtils.isNotEmpty(supplier.getAnexo())) {
                ret.setAddressTypeCode(getAddressTypeCode(supplier.getAnexo()));
            }
            if (StringUtils.isNotEmpty(supplier.getDistrict())) {
                ret.setDistrict(supplier.getDistrict());
            }
            if (StringUtils.isNotEmpty(supplier.getStreetName())) {
                ret.setStreetName(supplier.getStreetName());
            }
        }

        final var addressLineType = new AddressLineType();
        addressLineType.setLine(party.getAddressLine());
        ret.setAddressLine(Collections.singletonList(addressLineType));

        if (StringUtils.isNotEmpty(party.getCountry())) {
            ret.setCountry(getCountryType(party.getCountry()));
        }
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

    public static List<InvoiceLineType> getInvoiceLines(final List<ILine> lines)
    {
        final var ret = new ArrayList<InvoiceLineType>();
        var idx = 1;
        for (final var line : lines) {
            final var invoiceLine = new InvoiceLineType();
            ret.add(invoiceLine);
            invoiceLine.setID(String.valueOf(idx));
            invoiceLine.setInvoicedQuantity(getInvoicedQuantity(line));
            invoiceLine.setLineExtensionAmount(getAmount(LineExtensionAmountType.class, line.getNetPrice()));

            final var pricingReference = new PricingReferenceType();
            final var priceType = new PriceType();
            priceType.setPriceAmount(getAmount(PriceAmountType.class, line.getCrossPrice()));
            priceType.setPriceTypeCode("01");
            pricingReference.setAlternativeConditionPrice(Collections.singletonList(priceType));
            invoiceLine.setPricingReference(pricingReference);
            invoiceLine.setTaxTotal(Taxes.getTaxTotal(line.getTaxEntries(), true));
            invoiceLine.setItem(getItem(line));

            final var priceType2 = new PriceType();
            priceType2.setPriceAmount(getAmount(PriceAmountType.class, line.getNetUnitPrice()));
            invoiceLine.setPrice(priceType2);
            idx++;
        }
        return ret;
    }

    public static InvoicedQuantityType getInvoicedQuantity(final ILine line)
    {
        final var ret = new InvoicedQuantityType();
        ret.setUnitCode(line.getUoMCode());
        ret.setUnitCodeListAgencyName("United Nations Economic Commission for Europe");
        ret.setUnitCodeListID("UN/ECE rec 20");
        ret.setValue(line.getQuantity());
        return ret;
    }

    public static ItemType getItem(final ILine line)
    {
        final var ret = new ItemType();
        final var description = new DescriptionType();
        description.setValue(line.getDescription());
        ret.setDescription(Collections.singletonList(description));
        final var itemIdentificationType = new ItemIdentificationType();
        itemIdentificationType.setID(line.getSku());
        ret.setSellersItemIdentification(itemIdentificationType);
        return ret;
    }

    public static AllowanceChargeReasonCodeType getAllowanceChargeReasonCode(final String reason)
    {
        final var allowanceChargeReasonCodeType = new AllowanceChargeReasonCodeType();
        allowanceChargeReasonCodeType.setListAgencyName(AGENCYNAME);
        allowanceChargeReasonCodeType.setListName(Catalogs.CADE.getName());
        allowanceChargeReasonCodeType.setListURI(Catalogs.CADE.getURI());
        allowanceChargeReasonCodeType.setValue(reason);
        return allowanceChargeReasonCodeType;
    }

    public static List<PaymentTermsType> getPaymentTerms(final IPaymentTerms paymentTerms)
    {
        final List<PaymentTermsType> ret = new ArrayList<>();
        if (paymentTerms != null) {
            if (paymentTerms.isCredit()) {
                final var paymentTermsType = new PaymentTermsType();
                ret.add(paymentTermsType);
                paymentTermsType.setID("FormaPago");
                final var paymentMeansIDType = new PaymentMeansIDType();
                paymentMeansIDType.setValue("Credito");
                paymentTermsType.setPaymentMeansID(Collections.singletonList(paymentMeansIDType));
                paymentTermsType.setAmount(getAmount(
                                oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.AmountType.class,
                                paymentTerms.getTotal(), paymentTerms.getCurrencyId()));
                int i = 1;
                for (final var installment : paymentTerms.getInstallments()) {
                    final var paymentTermsType4Installment = new PaymentTermsType();
                    ret.add(paymentTermsType4Installment);
                    paymentTermsType4Installment.setID("FormaPago");
                    final var paymentMeansIDType4Installment = new PaymentMeansIDType();
                    paymentMeansIDType4Installment.setValue(String.format("Cuoata%03d", i));
                    paymentTermsType4Installment
                                    .setPaymentMeansID(Collections.singletonList(paymentMeansIDType4Installment));
                    paymentTermsType4Installment.setAmount(getAmount(
                                    oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.AmountType.class,
                                    installment.getAmount(), installment.getCurrencyId()));
                    try {
                        paymentTermsType4Installment.setInstallmentDueDate(
                                        new InstallmentDueDateType(DatatypeFactory.newInstance()
                                                        .newXMLGregorianCalendar(installment.getDueDate().toString())));
                    } catch (final DatatypeConfigurationException e) {
                        LOG.error("Catched", e);
                    }
                    i++;
                }
            } else {
                final var paymentTermsType = new PaymentTermsType();
                ret.add(paymentTermsType);
                paymentTermsType.setID("FormaPago");
                final var paymentMeansIDType = new PaymentMeansIDType();
                paymentMeansIDType.setValue("Contado");
                paymentTermsType.setPaymentMeansID(Collections.singletonList(paymentMeansIDType));
            }
        }
        return ret;
    }
}
