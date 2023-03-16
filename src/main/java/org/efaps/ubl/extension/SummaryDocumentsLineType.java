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
package org.efaps.ubl.extension;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.helger.ubl21.CUBL21;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.CustomerPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.StatusType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.DocumentTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.LineIDType;

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
