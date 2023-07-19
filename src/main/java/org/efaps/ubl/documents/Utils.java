/*
 * Copyright 2003 - 2023 The eFaps Team
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

import org.apache.commons.lang3.StringUtils;
import org.efaps.ubl.extension.BillingPaymentType;
import org.efaps.ubl.extension.SummaryDocumentsLineType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.AddressLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.AddressType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.AttachmentType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.BillingReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.CountryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.CreditNoteLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.CustomerPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.DespatchLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.DocumentReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.ExternalReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.InvoiceLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.ItemIdentificationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.ItemPropertyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.ItemType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.OrderLineReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyIdentificationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyLegalEntityType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyNameType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PaymentTermsType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PersonType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PriceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PricingReferenceType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.ResponseType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.SignatureType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.StatusType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.SupplierPartyType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.AddressTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.AllowanceChargeReasonCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.ConditionCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.CreditNoteTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.CreditedQuantityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.DeliveredQuantityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.DescriptionType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.DocumentCurrencyCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.DocumentTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IdentificationCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.InstructionIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.InvoiceTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.InvoicedQuantityType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.LineExtensionAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.LineIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.NameCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.NoteType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.PaidAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.PaymentDueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.PaymentMeansIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.PriceAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.PriceTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.TotalAmountType;
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

    public static CreditNoteTypeCodeType getCreditNoteType(final String type)
    {
        final var ret = new CreditNoteTypeCodeType();
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

    public static <T extends AmountType> T getAmount(final Class<T> type,
                                                     final BigDecimal amount)
    {
        return getAmount(type, amount, "PEN");
    }

    public static <T extends AmountType> T getAmount(final Class<T> type,
                                                     final BigDecimal amount,
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

    public static PartyType getCarrier(final ICarrier carrier)
    {
        final var ret = new PartyType();
        ret.setPartyIdentification(Collections.singletonList(getPartyIdentificationType(carrier)));
        ret.setPartyLegalEntity(
                        Collections.singletonList(getPartyLegalEntityType(carrier)));
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
        if (!(party instanceof ICarrier)) {
            ret.setRegistrationAddress(getAddress(party));
        }
        if (party.getCompanyId() != null) {
            ret.setCompanyID(party.getCompanyId());
        }
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
            // /Invoice/cac:InvoiceLine/cbc:LineExtensionAmount --> n(12,2)
            invoiceLine.setLineExtensionAmount(getAmount(LineExtensionAmountType.class,
                            line.getNetPrice().setScale(2, RoundingMode.HALF_UP)));

            final var pricingReference = new PricingReferenceType();
            // Precio de Venta Unitario = (Valor de venta por ítem + Monto total
            // de tributos del ítem
            // + Cargo no afecto por ítem - Descuento no afecto por ítem) /
            // Cantidad de unidades por ítem
            final var conditionPrice = line.getNetPrice()
                            .add(line.getTaxEntries().stream()
                                            .map(entry -> entry.isFreeOfCharge() ? BigDecimal.ZERO : entry.getAmount())
                                            .reduce(BigDecimal.ZERO, BigDecimal::add))
                            .add(line.getAllowancesCharges().stream().map(IAllowanceChargeEntry::getAmount).reduce(
                                            BigDecimal.ZERO, BigDecimal::add))
                            .divide(line.getQuantity(), RoundingMode.HALF_UP);

            final var priceType = new PriceType();
            priceType.setPriceAmount(getAmount(PriceAmountType.class, conditionPrice));

            priceType.setPriceTypeCode(getPriceTypeCode(line));
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

    public static PriceTypeCodeType getPriceTypeCode(final ILine line)
    {
        final var ret = new PriceTypeCodeType();
        ret.setListAgencyName(AGENCYNAME);
        ret.setListName("Tipo de Precio");
        ret.setListURI("urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo16");
        ret.setValue(line.getPriceType());
        return ret;
    }

    public static List<CreditNoteLineType> getCreditNoteLines(final List<ILine> lines)
    {
        final var ret = new ArrayList<CreditNoteLineType>();
        var idx = 1;
        for (final var line : lines) {
            final var creditNoteLine = new CreditNoteLineType();
            ret.add(creditNoteLine);
            creditNoteLine.setID(String.valueOf(idx));
            creditNoteLine.setCreditedQuantity(getCreditedQuantity(line));
            // //CreditNote/cac:CreditNoteLine/cbc:LineExtensionAmount  --> n(12,2)
            creditNoteLine.setLineExtensionAmount(getAmount(LineExtensionAmountType.class,
                            line.getNetPrice().setScale(2, RoundingMode.HALF_UP)));

            final var pricingReference = new PricingReferenceType();
            // Precio de Venta Unitario = (Valor de venta por ítem + Monto total
            // de tributos del ítem
            // + Cargo no afecto por ítem - Descuento no afecto por ítem) /
            // Cantidad de unidades por ítem
            final var conditionPrice = line.getNetPrice()
                            .add(line.getTaxEntries().stream().map(ITaxEntry::getAmount).reduce(BigDecimal.ZERO,
                                            BigDecimal::add))
                            .add(line.getAllowancesCharges().stream().map(IAllowanceChargeEntry::getAmount).reduce(
                                            BigDecimal.ZERO, BigDecimal::add))
                            .divide(line.getQuantity(), RoundingMode.HALF_UP);

            final var priceType = new PriceType();
            priceType.setPriceAmount(getAmount(PriceAmountType.class, conditionPrice));
            priceType.setPriceTypeCode(getPriceTypeCode(line));
            pricingReference.setAlternativeConditionPrice(Collections.singletonList(priceType));
            creditNoteLine.setPricingReference(pricingReference);

            creditNoteLine.setTaxTotal(Taxes.getTaxTotal(line.getTaxEntries(), true));
            creditNoteLine.setItem(getItem(line));

            final var priceType2 = new PriceType();
            priceType2.setPriceAmount(getAmount(PriceAmountType.class, line.getNetUnitPrice()));
            creditNoteLine.setPrice(priceType2);
            idx++;
        }
        return ret;
    }

    public static List<DespatchLineType> getDeliveryNoteLines(final List<ILine> lines)
    {
        final var ret = new ArrayList<DespatchLineType>();
        final var idx = 1;
        for (final var line : lines) {
            final var deliveryNoteLine = new DespatchLineType();
            ret.add(deliveryNoteLine);
            deliveryNoteLine.setID(String.valueOf(idx));
            deliveryNoteLine.setDeliveredQuantity(getDeliveredQuantity(line));
            final var orderLineRef = new OrderLineReferenceType();
            orderLineRef.setLineID(String.valueOf(idx));
            deliveryNoteLine.setOrderLineReference(Collections.singletonList(orderLineRef));
            deliveryNoteLine.setItem(getItem(line));
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

    public static CreditedQuantityType getCreditedQuantity(final ILine line)
    {
        final var ret = new CreditedQuantityType();
        ret.setUnitCode(line.getUoMCode());
        ret.setUnitCodeListAgencyName("United Nations Economic Commission for Europe");
        ret.setUnitCodeListID("UN/ECE rec 20");
        ret.setValue(line.getQuantity());
        return ret;
    }

    public static DeliveredQuantityType getDeliveredQuantity(final ILine line)
    {
        final var ret = new DeliveredQuantityType();
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

        for (final var prop: line.getAdditionalItemProperties()) {
            ret.addAdditionalItemProperty(getAdditionalItemProperty(prop));
        }
        return ret;
    }

    public static ItemPropertyType getAdditionalItemProperty(final IAdditionalItemProperty prop)
    {
        final var itemPropertyType = new ItemPropertyType();
        switch (prop.type()) {
            case NORMALIZED: {
                itemPropertyType.setName("Indicador de bien normalizado");
                final var nameCodeType = new NameCodeType();
                nameCodeType.setListAgencyName(AGENCYNAME);
                nameCodeType.setListName(Catalogs.ITEMPROP.getName());
                nameCodeType.setListURI(Catalogs.ITEMPROP.getURI());
                nameCodeType.setValue("7022");
                itemPropertyType.setNameCode(nameCodeType);
                itemPropertyType.setValue("0");
            }
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + prop.type());
        }
        return itemPropertyType;
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
                    paymentMeansIDType4Installment.setValue(String.format("Cuota%03d", i));
                    paymentTermsType4Installment
                                    .setPaymentMeansID(Collections.singletonList(paymentMeansIDType4Installment));
                    paymentTermsType4Installment.setAmount(getAmount(
                                    oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.AmountType.class,
                                    installment.getAmount(), installment.getCurrencyId()));
                    paymentTermsType4Installment.setPaymentDueDate(
                                    new PaymentDueDateType(installment.getDueDate()));
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

    public static List<BillingReferenceType> getBillingReferenceType(final Reference reference)
    {
        final var ret = new ArrayList<BillingReferenceType>();
        final var billingReferenceType = new BillingReferenceType();
        final var documentReferenceType = new DocumentReferenceType();
        billingReferenceType.setInvoiceDocumentReference(documentReferenceType);
        documentReferenceType.setID(reference.getNumber());
        documentReferenceType.setIssueDate(reference.getDate());
        final var documentTypeCodeType = new DocumentTypeCodeType(reference.getDocType());
        documentReferenceType.setDocumentTypeCode(documentTypeCodeType);
        ret.add(billingReferenceType);
        return ret;
    }

    public static List<ResponseType> getDiscrepancyResponse(final CreditNoteTypeCode creditNoteTypeCode)
    {
        final var ret = new ArrayList<ResponseType>();
        final var responseType = new ResponseType();
        responseType.setResponseCode(creditNoteTypeCode.getCode());
        final var description = new DescriptionType();
        description.setValue(creditNoteTypeCode.getDescription());
        responseType.setDescription(Collections.singletonList(description));
        ret.add(responseType);
        return ret;
    }

    public static List<SummaryDocumentsLineType> getSummaryLines(final List<ISummaryLine> lines)
    {
        final var ret = new ArrayList<SummaryDocumentsLineType>();
        var idx = 1;
        for (final var line: lines) {
            final var type = new SummaryDocumentsLineType();
            ret.add(type);
            type.setLineId(new LineIDType(String.valueOf(idx)));
            type.setDocumentTypeCode(new DocumentTypeCodeType(line.getDocType()));
            type.setId(new IDType(line.getNumber()));
            type.setAccountingCustomerParty(Utils.getCustomer(line.getCustomer()));
            final var status = new StatusType();
            status.setConditionCode(new ConditionCodeType(String.valueOf(line.getStatusCode())));
            type.setStatus(status);
            type.setTotalAmount(getAmount(TotalAmountType.class, line.getCrossTotal()));

            final var billingPayments = new ArrayList<BillingPaymentType>();
            //01: Valor de venta de las operaciones gravadas con el IGV
            final var billingPayment1 =  new BillingPaymentType();
            billingPayment1.setPaidAmountType(getAmount(PaidAmountType.class, line.getNetTotal()));
            billingPayment1.setInstructionID(new InstructionIDType("01"));
            billingPayments.add(billingPayment1);
            // 02: Valores de venta de las operaciones exoneradas del IGV
            final var billingPayment2 =  new BillingPaymentType();
            billingPayment2.setPaidAmountType(getAmount(PaidAmountType.class, BigDecimal.ZERO));
            billingPayment2.setInstructionID(new InstructionIDType("02"));
            billingPayments.add(billingPayment2);
            // 03: Valores de venta de las operaciones inafectas del IGV
            final var billingPayment3 =  new BillingPaymentType();
            billingPayment3.setPaidAmountType(getAmount(PaidAmountType.class, BigDecimal.ZERO));
            billingPayment3.setInstructionID(new InstructionIDType("03"));
            billingPayments.add(billingPayment3);
            // 04: Valor de venta de las exportaciones del item
            final var billingPayment4 =  new BillingPaymentType();
            billingPayment4.setPaidAmountType(getAmount(PaidAmountType.class, BigDecimal.ZERO));
            billingPayment4.setInstructionID(new InstructionIDType("04"));
            billingPayments.add(billingPayment4);
            /**
            // 05: Valor de venta de las operaciones gratuitas  (Condicional)
            final var billingPayment5 =  new BillingPaymentType();
            billingPayment5.setPaidAmountType(getAmount(PaidAmountType.class, BigDecimal.ZERO));
            billingPayment5.setInstructionID(new InstructionIDType("05"));
            billingPayments.add(billingPayment5);
             **/
            type.setBillingPayments(billingPayments);
            type.setTaxTotals(Taxes.getTaxTotal(line.getTaxEntries(), true));
            idx++;
        }

        return ret;
    }

    public static PersonType getPerson(final IPerson person) {
        final var personType = new PersonType();
        final var idType = new IDType();
        idType.setSchemeAgencyName(AGENCYNAME);
        idType.setSchemeID(person.getDoiType());
        idType.setSchemeName(Catalogs.DOI.getName());
        idType.setSchemeURI(Catalogs.DOI.getURI());
        idType.setValue(person.getDOI());
        personType.setID(idType);

        personType.setFirstName(person.getFirstName());
        personType.setFamilyName(person.getFamilyName());

        if (person instanceof IDriver) {
            final var documentReferenceType = new DocumentReferenceType();
            documentReferenceType.setID(((IDriver) person).getLicense());
            personType.setIdentityDocumentReference(Collections.singletonList(documentReferenceType));
        }
        return personType;
    }
}
