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

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.efaps.ubl.Builder;
import org.efaps.ubl.extension.Definitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.ubl21.UBL21NamespaceContext;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.MonetaryTotalType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.CustomizationIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.PayableAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.TaxExclusiveAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.TaxInclusiveAmountType;
import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;

public abstract class AbstractDocument<T extends AbstractDocument<T>>
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractDocument.class);

    private String currency;
    private String number;
    private LocalDate date;
    private BigDecimal netTotal;
    private BigDecimal crossTotal;
    private ISupplier supplier;
    private ICustomer customer;
    private List<ITaxEntry> taxes = new ArrayList<>();
    private List<IChargeEntry> charges = new ArrayList<>();
    private List<ILine> lines = new ArrayList<>();

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

    public List<IChargeEntry> getCharges()
    {
        return charges;
    }

    public void setCharges(final List<IChargeEntry> charges)
    {
        this.charges = charges;
    }

    public T withCharges(final List<IChargeEntry> charges)
    {
        this.charges = charges;
        return getThis();
    }

    public T withCharge(final IChargeEntry charge)
    {
        this.charges.add(charge);
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

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }

    protected abstract T getThis();

    protected abstract String getDocType();

    protected MonetaryTotalType getMonetaryTotal()
    {
        final var ret = new MonetaryTotalType();
        // ret.setLineExtensionAmount(Utils.getAmount(LineExtensionAmountType.class,
        // new BigDecimal("2133")));
        ret.setTaxExclusiveAmount(Utils.getAmount(TaxExclusiveAmountType.class, getNetTotal()));
        ret.setTaxInclusiveAmount(Utils.getAmount(TaxInclusiveAmountType.class, getCrossTotal()));
        // ret.setAllowanceTotalAmount(Utils.getAmount(AllowanceTotalAmountType.class,
        // new BigDecimal("0")));
        // ret.setChargeTotalAmount(Utils.getAmount(ChargeTotalAmountType.class,
        // new BigDecimal("0")));
        ret.setPayableAmount(Utils.getAmount(PayableAmountType.class, getCrossTotal()));
        return ret;
    }

    public String getUBLXml()
    {
        if (!UBL21NamespaceContext.getInstance().getPrefixToNamespaceURIMap().containsKey("sac")) {
            UBL21NamespaceContext.getInstance().addMapping("sac", Definitions.NAMESPACE);
        }
        final InvoiceType invoice = new InvoiceType();
        invoice.setUBLVersionID("2.1");
        final var customizationID = new CustomizationIDType();
        customizationID.setSchemeAgencyName(Utils.AGENCYNAME);
        customizationID.setValue("2.0");
        invoice.setCustomizationID(customizationID);
        invoice.setID(getNumber());
        try {
            invoice.setIssueDate(
                            new IssueDateType(DatatypeFactory.newInstance().newXMLGregorianCalendar(getDate().toString())));
        } catch (final DatatypeConfigurationException e) {
            LOG.error("Catched", e);
        }
        invoice.setInvoiceTypeCode(Utils.getInvoiceType(getDocType()));
        invoice.setDocumentCurrencyCode(Utils.getDocumentCurrencyCode(getCurrency()));
        invoice.setUBLExtensions(Utils.getWordsForAmount(getCrossTotal()));
        invoice.setLegalMonetaryTotal(getMonetaryTotal());
        invoice.setTaxTotal(Taxes.getTaxTotal(getTaxes()));
        invoice.addSignature(Utils.getSignature(getSupplier()));
        invoice.setAccountingSupplierParty(Utils.getSupplier(getSupplier()));
        invoice.setAccountingCustomerParty(Utils.getCustomer(getCustomer()));
        invoice.setInvoiceLine(Utils.getInvoiceLines(getLines()));
        invoice.setAllowanceCharge(Charges.getAllowanceCharge(getCharges()));
        return new Builder().setCharset(StandardCharsets.UTF_8)
                        .setFormattedOutput(true)
                        .getAsString(invoice);
    }

}
