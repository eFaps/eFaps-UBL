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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.helger.ubl21.CUBL21;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.SignatureType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.SupplierPartyType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.CustomizationIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.ReferenceDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.UBLVersionIDType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_21.UBLExtensionsType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(namespace = Definitions.NAMESPACE_SUMMARY)
@XmlRootElement(name = "SummaryDocuments", namespace = Definitions.NAMESPACE_SUMMARY)
public class SummaryDocumentsType
{

    @XmlElement(name = "UBLExtensions", namespace = CUBL21.XML_SCHEMA_CEC_NAMESPACE_URL)
    private UBLExtensionsType ublExtensions;
    @XmlElement(name = "UBLVersionID", namespace = CUBL21.XML_SCHEMA_CBC_NAMESPACE_URL)
    private UBLVersionIDType ublVersionID;
    @XmlElement(name = "CustomizationID", namespace = CUBL21.XML_SCHEMA_CBC_NAMESPACE_URL)
    private final CustomizationIDType customizationID = new CustomizationIDType("1.1");
    @XmlElement(name = "ID", namespace = CUBL21.XML_SCHEMA_CBC_NAMESPACE_URL, required = true)
    private IDType id;
    @XmlElement(name = "ReferenceDate", namespace = CUBL21.XML_SCHEMA_CBC_NAMESPACE_URL)
    private ReferenceDateType referenceDate;
    @XmlElement(name = "IssueDate", namespace = CUBL21.XML_SCHEMA_CBC_NAMESPACE_URL)
    private IssueDateType issueDate;
    @XmlElement(name = "Signature", namespace = CUBL21.XML_SCHEMA_CAC_NAMESPACE_URL)
    private List<SignatureType> signature;
    @XmlElement(name = "AccountingSupplierParty", namespace = CUBL21.XML_SCHEMA_CAC_NAMESPACE_URL, required = true)
    private SupplierPartyType accountingSupplierParty;
    @XmlElement(name = "SummaryDocumentsLine", namespace = Definitions.NAMESPACE_SUNATAGGREGATE)
    private List<SummaryDocumentsLineType> summaryDocumentsLines;

    public UBLVersionIDType getUBLVersionID()
    {
        return ublVersionID;
    }

    public UBLVersionIDType setUBLVersionID(final String valueParam)
    {
        UBLVersionIDType aObj = getUBLVersionID();
        if (aObj == null) {
            aObj = new UBLVersionIDType(valueParam);
            setUBLVersionID(aObj);
        } else {
            aObj.setValue(valueParam);
        }
        return aObj;
    }

    public void setUBLVersionID(UBLVersionIDType value)
    {
        this.ublVersionID = value;
    }

    public void setID(String id)
    {
        this.id = new IDType(id);
    }

    public void setReferenceDate(LocalDate date)
    {
        this.referenceDate = new ReferenceDateType(date);
    }

    public void setIssueDate(LocalDate date)
    {
        this.issueDate = new IssueDateType(date);
    }

    public void setAccountingSupplierParty(SupplierPartyType value)
    {
        this.accountingSupplierParty = value;
    }

    public void setSummaryDocumentsLines(List<SummaryDocumentsLineType> value)
    {
        this.summaryDocumentsLines = value;
    }

    public void addSignature(SignatureType signature)
    {
        if (this.signature == null) {
            this.signature = new ArrayList<>();
        }
        this.signature.add(signature);
    }

    public UBLExtensionsType getUBLExtensions()
    {
        return ublExtensions;
    }

    public void setUBLExtensions(UBLExtensionsType ublExtensions)
    {
        this.ublExtensions = ublExtensions;
    }
}
