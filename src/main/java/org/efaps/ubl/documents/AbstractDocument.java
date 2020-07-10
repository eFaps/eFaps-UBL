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

import org.efaps.ubl.Builder;
import org.efaps.ubl.Sim;
import org.efaps.ubl.extension.Definitions;

import com.helger.ubl21.UBL21NamespaceContext;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.MonetaryTotalType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.PayableAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.TaxExclusiveAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.TaxInclusiveAmountType;
import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;

public abstract class AbstractDocument<T extends AbstractDocument<T>>
{

    private String currency;
    private String name;
    private LocalDate date;
    private BigDecimal netTotal;
    private BigDecimal crossTotal;
    private List<ITaxEntry> taxes = new ArrayList<ITaxEntry>();

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

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public T withName(final String name)
    {
        setName(name);
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

    public T withTaxes(final List<ITaxEntry> taxes) {
        this.taxes  = taxes;
        return getThis();
    }

    public T withTax(final ITaxEntry tax) {
        this.taxes.add(tax);
        return getThis();
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
        //ret.setAllowanceTotalAmount(Utils.getAmount(AllowanceTotalAmountType.class, new BigDecimal("0")));
        //ret.setChargeTotalAmount(Utils.getAmount(ChargeTotalAmountType.class, new BigDecimal("0")));
        ret.setPayableAmount(Utils.getAmount(PayableAmountType.class, getCrossTotal()));
        return ret;
    }

    public String getUBL()
        throws DatatypeConfigurationException
    {
        if (!UBL21NamespaceContext.getInstance().getPrefixToNamespaceURIMap().containsKey("sac")) {
            UBL21NamespaceContext.getInstance().addMapping("sac", Definitions.NAMESPACE);
        }
        final InvoiceType invoice = new InvoiceType();
        invoice.setUBLVersionID("2.1");
        invoice.setID(getName());
        invoice.setIssueDate(
                        new IssueDateType(DatatypeFactory.newInstance().newXMLGregorianCalendar(getDate().toString())));
        invoice.setInvoiceTypeCode(Utils.getInvoiceType(getDocType()));
        invoice.setDocumentCurrencyCode(Utils.getDocumentCurrencyCode(getCurrency()));
        invoice.setUBLExtensions(Utils.getWordsForAmount(getCrossTotal()));
        invoice.setLegalMonetaryTotal(getMonetaryTotal());


        invoice.setAccountingSupplierParty(Sim.getSupplier("Tiendas Mass", "6", "20601327318", "150101", "1000", "Lima",
                        "JR. CRESPO Y CASTILLO NRO. 2087"));
        invoice.setAccountingCustomerParty(Sim.getCustomer("Tovar Lopez, Julio Odair", "1", "43289672",
                        "Av parque alto 291-A Lima - Lima - Santiago De Surco"));

        invoice.setTaxTotal(Taxes.getTaxTotal(getTaxes()));

        invoice.setInvoiceLine(Sim.getInvoiceLines());

        invoice.addSignature(Utils.getSignature());

        return new Builder().setCharset(StandardCharsets.UTF_8)
                        .setFormattedOutput(true)
                        .getAsString(invoice);
    }

}
