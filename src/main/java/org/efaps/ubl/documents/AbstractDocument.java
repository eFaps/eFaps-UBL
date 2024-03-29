/*
 * Copyright © 2003 - 2024 The eFaps Team (-)
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
 */
package org.efaps.ubl.documents;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.efaps.ubl.documents.elements.AllowancesCharges;
import org.efaps.ubl.documents.elements.Taxes;
import org.efaps.ubl.documents.elements.Utils;
import org.efaps.ubl.documents.interfaces.IAllowanceChargeEntry;
import org.efaps.ubl.documents.interfaces.ICustomer;
import org.efaps.ubl.documents.interfaces.ILine;
import org.efaps.ubl.documents.interfaces.IPaymentTerms;
import org.efaps.ubl.documents.interfaces.ISupplier;
import org.efaps.ubl.documents.interfaces.ITaxEntry;
import org.efaps.ubl.extension.Definitions;
import org.efaps.ubl.marshaller.DocumentMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.ubl21.CUBL21;
import com.helger.ubl21.UBL21NamespaceContext;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.CreditNoteLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.InvoiceLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.MonetaryTotalType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.TaxTotalType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.ChargeTotalAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.CustomizationIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.LineExtensionAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.PayableAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.PayableRoundingAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.TaxExclusiveAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.TaxInclusiveAmountType;
import oasis.names.specification.ubl.schema.xsd.creditnote_21.CreditNoteType;
import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;

public abstract class AbstractDocument<T extends AbstractDocument<T>>
{

    private static final Logger LOG = LoggerFactory.getLogger(AbstractDocument.class);

    private String currency;
    private String number;
    private LocalDate date;
    private BigDecimal netTotal;
    private BigDecimal crossTotal;
    private BigDecimal chargeTotal;
    private BigDecimal payableAmount;
    private ISupplier supplier;
    private ICustomer customer;
    private List<ITaxEntry> taxes = new ArrayList<>();
    private List<IAllowanceChargeEntry> allowancesCharges = new ArrayList<>();
    private List<ILine> lines = new ArrayList<>();
    private IPaymentTerms paymentTerms;
    private Charset encoding = StandardCharsets.UTF_8;

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(final String currency)
    {
        this.currency = currency;
    }

    public T withCurrency(final String currency)
    {
        setCurrency(currency);
        return getThis();
    }

    public String getNumber()
    {
        return number;
    }

    public void setNumber(final String number)
    {
        this.number = number;
    }

    public T withNumber(final String number)
    {
        setNumber(number);
        return getThis();
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(final LocalDate date)
    {
        this.date = date;
    }

    public T withDate(final LocalDate date)
    {
        setDate(date);
        return getThis();
    }

    public BigDecimal getCrossTotal()
    {
        return crossTotal;
    }

    public void setCrossTotal(final BigDecimal crossTotal)
    {
        this.crossTotal = crossTotal;
    }

    public T withCrossTotal(final BigDecimal crossTotal)
    {
        setCrossTotal(crossTotal);
        return getThis();
    }

    public BigDecimal getNetTotal()
    {
        return netTotal;
    }

    public void setNetTotal(final BigDecimal netTotal)
    {
        this.netTotal = netTotal;
    }

    public T withNetTotal(final BigDecimal netTotal)
    {
        setNetTotal(netTotal);
        return getThis();
    }

    public BigDecimal getPayableAmount()
    {
        return payableAmount;
    }

    public void setPayableAmount(final BigDecimal payableAmount)
    {
        this.payableAmount = payableAmount;
    }

    public T withPayableAmount(final BigDecimal payableAmount)
    {
        setPayableAmount(payableAmount);
        return getThis();
    }

    public List<ITaxEntry> getTaxes()
    {
        return taxes;
    }

    public T withTaxes(final List<ITaxEntry> taxes)
    {
        this.taxes = taxes;
        return getThis();
    }

    public T withTax(final ITaxEntry tax)
    {
        this.taxes.add(tax);
        return getThis();
    }

    public List<IAllowanceChargeEntry> getAllowancesCharges()
    {
        return allowancesCharges;
    }

    public void setAllowancesCharges(final List<IAllowanceChargeEntry> allowancesCharges)
    {
        this.allowancesCharges = allowancesCharges;
    }

    public T withAllowancesCharges(final List<IAllowanceChargeEntry> allowancesCharges)
    {
        this.allowancesCharges = allowancesCharges;
        return getThis();
    }

    public T withAllowanceCharge(final IAllowanceChargeEntry charge)
    {
        allowancesCharges.add(charge);
        return getThis();
    }

    public IPaymentTerms getPaymentTerms()
    {
        return this.paymentTerms;
    }

    public T withPaymentTerms(final IPaymentTerms paymentTerms)
    {
        this.paymentTerms = paymentTerms;
        return getThis();
    }

    public ISupplier getSupplier()
    {
        return supplier;
    }

    public void setSupplier(final ISupplier supplier)
    {
        this.supplier = supplier;
    }

    public T withSupplier(final ISupplier supplier)
    {
        setSupplier(supplier);
        return getThis();
    }

    public ICustomer getCustomer()
    {
        return customer;
    }

    public void setCustomer(final ICustomer customer)
    {
        this.customer = customer;
    }

    public T withCustomer(final ICustomer customer)
    {
        setCustomer(customer);
        return getThis();
    }

    public List<ILine> getLines()
    {
        return lines;
    }

    public void setLines(final List<ILine> lines)
    {
        this.lines = lines;
    }

    public T withLines(final List<ILine> lines)
    {
        setLines(lines);
        return getThis();
    }

    public BigDecimal getChargeTotal()
    {
        return chargeTotal;
    }

    public void setChargeTotal(final BigDecimal chargeTotal)
    {
        this.chargeTotal = chargeTotal;
    }

    public Charset getEncoding()
    {
        return encoding;
    }

    public void setEncoding(final Charset encoding)
    {
        this.encoding = encoding;
    }

    public T withEncoding(final Charset encoding)
    {
        this.encoding = encoding;
        return getThis();
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    protected abstract T getThis();

    protected abstract String getDocType();

    protected MonetaryTotalType getMonetaryTotal(final InvoiceType invoice)
    {
        final var ret = new MonetaryTotalType();
        ret.setLineExtensionAmount(Utils.getAmount(LineExtensionAmountType.class, evalLineExtensionForTotal(invoice)));
        ret.setTaxExclusiveAmount(Utils.getAmount(TaxExclusiveAmountType.class, getNetTotal()));

        // TaxExclusiveAmount + all taxes
        final var taxInclusive = getNetTotal().add(invoice.getTaxTotal().stream().map(TaxTotalType::getTaxAmountValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
        ret.setTaxInclusiveAmount(Utils.getAmount(TaxInclusiveAmountType.class, taxInclusive));

        // we do not have allowances yet
        // ret.setAllowanceTotalAmount(Utils.getAmount(AllowanceTotalAmountType.class,
        // new BigDecimal("0")));

        // 2021-09-09 Bizlinks:
        // Total precio venta + Sumatoria otros cargos - Sumatoria otros
        // descuentas (que no afecta la base imponible)
        ret.setPayableAmount(Utils.getAmount(PayableAmountType.class, getCrossTotal()));

        evalChargeTotal(ret);
        return ret;
    }

    protected MonetaryTotalType getMonetaryTotal(final CreditNoteType creditNote)
    {
        final var ret = new MonetaryTotalType();
        ret.setLineExtensionAmount(
                        Utils.getAmount(LineExtensionAmountType.class, evalLineExtensionForTotal(creditNote)));
        ret.setTaxExclusiveAmount(Utils.getAmount(TaxExclusiveAmountType.class, getNetTotal()));

        // TaxExclusiveAmount + all taxes
        final var taxInclusive = getNetTotal().add(creditNote.getTaxTotal().stream()
                        .map(TaxTotalType::getTaxAmountValue).reduce(BigDecimal.ZERO, BigDecimal::add));
        ret.setTaxInclusiveAmount(Utils.getAmount(TaxInclusiveAmountType.class, taxInclusive));

        // we do not have allowances yet
        // ret.setAllowanceTotalAmount(Utils.getAmount(AllowanceTotalAmountType.class,
        // new BigDecimal("0")));

        // 2021-09-09 Bizlinks:
        // Total precio venta + Sumatoria otros cargos - Sumatoria otros
        // descuentas (que no afecta la base imponible)
        // PayableAmount
        final var amount = getPayableAmount() == null || getPayableAmount().compareTo(BigDecimal.ZERO) == 0
                        ? getCrossTotal()
                        : getPayableAmount();
        ret.setPayableAmount(Utils.getAmount(PayableAmountType.class, amount));
        if (amount.subtract(getCrossTotal()).compareTo(BigDecimal.ZERO) != 0) {
            ret.setPayableRoundingAmount(Utils.getAmount(PayableRoundingAmountType.class,
                            amount.subtract(getCrossTotal())));
        }
        evalChargeTotal(ret);
        return ret;
    }

    // 2021-09-09 Bizlinks:
    // LegalMonetaryTotal/LineExtensionAmount = SUM of all
    // InvoiceLine/LineExtensionAmount

    // 2021-10-14
    // 'Valor de venta por ítem' (cbc:LineExtensionAmount): sumatoria de los
    // ítems con 'Código de tributo por línea'
    // igual a '1000', '1016', '9995', '9997' y '9998' y cuyo 'Monto base' es
    // mayor a cero (cbc:TaxableAmount > 0),
    // menos 'Montos de descuentos globales' (cbc:AllowanceCharge) que afectan
    // la base ('Código de motivo de descuento' igual a '02')
    // (cbc:AllowanceChargeReasonCode)
    // más 'Montos de cargos globales' (cbc:AllowanceCharge) que afectan la base
    // ('Código de motivo de cargo' igual a
    // '49')(cbc:AllowanceChargeReasonCode), con una tolerancia de + - 1
    //
    protected BigDecimal evalLineExtensionForTotal(final InvoiceType invoice)
    {
        var lineExt = invoice.getInvoiceLine().stream().map(InvoiceLineType::getLineExtensionAmountValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (invoice.hasAllowanceChargeEntries()) {
            for (final var allowanceCharge : invoice.getAllowanceCharge()) {
                if ("02".equals(allowanceCharge.getAllowanceChargeReasonCodeValue())) {
                    lineExt = lineExt.subtract(allowanceCharge.getAmountValue());
                }
                if ("49".equals(allowanceCharge.getAllowanceChargeReasonCodeValue())) {
                    lineExt = lineExt.add(allowanceCharge.getAmountValue());
                }
            }
        }
        return lineExt;
    }

    protected BigDecimal evalLineExtensionForTotal(final CreditNoteType creditNote)
    {
        var lineExt = creditNote.getCreditNoteLine().stream().map(CreditNoteLineType::getLineExtensionAmountValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (creditNote.hasAllowanceChargeEntries()) {
            for (final var allowanceCharge : creditNote.getAllowanceCharge()) {
                if ("02".equals(allowanceCharge.getAllowanceChargeReasonCodeValue())) {
                    lineExt = lineExt.subtract(allowanceCharge.getAmountValue());
                }
                if ("49".equals(allowanceCharge.getAllowanceChargeReasonCodeValue())) {
                    lineExt = lineExt.add(allowanceCharge.getAmountValue());
                }
            }
        }
        return lineExt;
    }

    protected void evalChargeTotal(final MonetaryTotalType total)
    {
        BigDecimal chargeTotalAmount = null;
        if (getChargeTotal() != null && getChargeTotal().compareTo(BigDecimal.ZERO) != 0) {
            chargeTotalAmount = getChargeTotal();

        } else if (!getAllowancesCharges().isEmpty()) {
            chargeTotalAmount = getAllowancesCharges().stream()
                            .map(entry -> {
                                if (entry.isCharge()) {
                                    return entry.getAmount();
                                } else {
                                    return BigDecimal.ZERO;
                                }
                            }).reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        if (chargeTotalAmount != null && chargeTotalAmount.compareTo(BigDecimal.ZERO) != 0) {
            total.setChargeTotalAmount(Utils.getAmount(ChargeTotalAmountType.class,
                            chargeTotalAmount.setScale(2, RoundingMode.HALF_UP)));
        }
    }

    public String getUBLXml()
    {
        if (!UBL21NamespaceContext.getInstance().getPrefixToNamespaceURIMap().containsKey("sac")) {
            UBL21NamespaceContext.getInstance().addMapping("sac", Definitions.NAMESPACE_SUNATAGGREGATE);
            UBL21NamespaceContext.getInstance().removeMapping("cec");
            UBL21NamespaceContext.getInstance().addMapping("ext", CUBL21.XML_SCHEMA_CEC_NAMESPACE_URL);
        }
        final InvoiceType invoice = new InvoiceType();
        invoice.setUBLVersionID("2.1");
        final var customizationID = new CustomizationIDType();
        customizationID.setSchemeAgencyName(Utils.AGENCYNAME);
        customizationID.setValue("2.0");
        invoice.setCustomizationID(customizationID);
        invoice.setID(getNumber());
        invoice.setIssueDate(new IssueDateType(getDate()));
        invoice.setInvoiceTypeCode(Utils.getInvoiceType(getDocType()));
        invoice.setDocumentCurrencyCode(Utils.getDocumentCurrencyCode(getCurrency()));
        invoice.getNote().add(Utils.getWordsForAmount(getCrossTotal()));
        invoice.addSignature(Utils.getSignature(getSupplier()));
        invoice.setAccountingSupplierParty(Utils.getSupplier(getSupplier()));
        invoice.setAccountingCustomerParty(Utils.getCustomer(getCustomer()));
        invoice.setInvoiceLine(Utils.getInvoiceLines(getLines()));
        invoice.setAllowanceCharge(AllowancesCharges.getAllowanceCharge(getAllowancesCharges()));
        invoice.setTaxTotal(Taxes.getTaxTotal(getTaxes(), false));
        invoice.setLegalMonetaryTotal(getMonetaryTotal(invoice));
        invoice.setPaymentTerms(Utils.getPaymentTerms(getPaymentTerms()));
        return DocumentMarshaller.invoice()
                        .setCharset(getEncoding())
                        .setFormattedOutput(true)
                        .getAsString(invoice);
    }
}
