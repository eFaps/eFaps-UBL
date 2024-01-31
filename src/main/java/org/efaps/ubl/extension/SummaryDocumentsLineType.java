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
package org.efaps.ubl.extension;

import java.util.List;

import com.helger.ubl21.CUBL21;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.CustomerPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.StatusType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.TaxTotalType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.DocumentTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.LineIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.TotalAmountType;

@XmlAccessorType(XmlAccessType.NONE)
public class SummaryDocumentsLineType
{

    @XmlElement(name = "LineID", namespace = CUBL21.XML_SCHEMA_CBC_NAMESPACE_URL)
    private LineIDType lineId;
    @XmlElement(name = "DocumentTypeCode", namespace = CUBL21.XML_SCHEMA_CBC_NAMESPACE_URL)
    private DocumentTypeCodeType documentTypeCode;
    @XmlElement(name = "ID", namespace = CUBL21.XML_SCHEMA_CBC_NAMESPACE_URL)
    private IDType id;
    @XmlElement(name = "AccountingCustomerParty", namespace = CUBL21.XML_SCHEMA_CAC_NAMESPACE_URL, required = true)
    private CustomerPartyType accountingCustomerParty;
    @XmlElement(name = "Status", namespace = CUBL21.XML_SCHEMA_CAC_NAMESPACE_URL, required = true)
    private StatusType status;
    @XmlElement(name = "TotalAmount", namespace = Definitions.NAMESPACE_SUNATAGGREGATE, required = true)
    private TotalAmountType totalAmount;
    @XmlElement(name = "BillingPayment", namespace = Definitions.NAMESPACE_SUNATAGGREGATE, required = true)
    private List<BillingPaymentType> billingPayments;
    @XmlElement(name = "TaxTotal", namespace = CUBL21.XML_SCHEMA_CAC_NAMESPACE_URL, required = true)
    private List<TaxTotalType> taxTotals;

    public List<TaxTotalType> getTaxTotals()
    {
        return taxTotals;
    }

    public void setTaxTotals(List<TaxTotalType> taxTotals)
    {
        this.taxTotals = taxTotals;
    }

    public List<BillingPaymentType> getBillingPayments()
    {
        return billingPayments;
    }

    public void setBillingPayments(List<BillingPaymentType> billingPayments)
    {
        this.billingPayments = billingPayments;
    }

    public TotalAmountType getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(TotalAmountType totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public StatusType getStatus()
    {
        return status;
    }

    public void setStatus(StatusType status)
    {
        this.status = status;
    }

    public CustomerPartyType getAccountingCustomerParty()
    {
        return accountingCustomerParty;
    }

    public void setAccountingCustomerParty(CustomerPartyType accountingCustomerParty)
    {
        this.accountingCustomerParty = accountingCustomerParty;
    }

    public LineIDType getLineId()
    {
        return lineId;
    }

    public void setLineId(LineIDType lineId)
    {
        this.lineId = lineId;
    }

    public DocumentTypeCodeType getDocumentTypeCode()
    {
        return documentTypeCode;
    }

    public void setDocumentTypeCode(DocumentTypeCodeType documentTypeCode)
    {
        this.documentTypeCode = documentTypeCode;
    }

    public IDType getId()
    {
        return id;
    }

    public void setId(IDType id)
    {
        this.id = id;
    }

}
