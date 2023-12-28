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

import org.efaps.ubl.documents.elements.AllowancesCharges;
import org.efaps.ubl.documents.elements.Reference;
import org.efaps.ubl.documents.elements.Taxes;
import org.efaps.ubl.documents.elements.Utils;
import org.efaps.ubl.documents.values.CreditNoteTypeCode;
import org.efaps.ubl.extension.Definitions;
import org.efaps.ubl.marshaller.DocumentMarshaller;

import com.helger.ubl21.CUBL21;
import com.helger.ubl21.UBL21NamespaceContext;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.CustomizationIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.creditnote_21.CreditNoteType;

public class CreditNote
    extends AbstractDocument<CreditNote>
{

    private Reference reference;

    private CreditNoteTypeCode creditNoteTypeCode = CreditNoteTypeCode.C01;

    @Override
    protected CreditNote getThis()
    {
        return this;
    }

    @Override
    protected String getDocType()
    {
        return "07";
    }

    public Reference getReference()
    {
        return reference;
    }

    public void setReference(final Reference reference)
    {
        this.reference = reference;
    }

    public CreditNote withReference(final Reference reference)
    {
        this.reference = reference;
        return this;
    }

    public CreditNoteTypeCode getCreditNoteTypeCode()
    {
        return creditNoteTypeCode;
    }

    public void setCreditNoteTypeCode(final CreditNoteTypeCode creditNoteTypeCode)
    {
        this.creditNoteTypeCode = creditNoteTypeCode;
    }

    @Override
    public String getUBLXml()
    {
        if (!UBL21NamespaceContext.getInstance().getPrefixToNamespaceURIMap().containsKey("sac")) {
            UBL21NamespaceContext.getInstance().addMapping("sac", Definitions.NAMESPACE_SUNATAGGREGATE);
            UBL21NamespaceContext.getInstance().removeMapping("cec");
            UBL21NamespaceContext.getInstance().addMapping ("ext", CUBL21.XML_SCHEMA_CEC_NAMESPACE_URL);
        }
        final var creditNote = new CreditNoteType();
        creditNote.setUBLVersionID("2.1");
        final var customizationID = new CustomizationIDType();
        customizationID.setSchemeAgencyName(Utils.AGENCYNAME);
        customizationID.setValue("2.0");
        creditNote.setCustomizationID(customizationID);
        creditNote.setID(getNumber());
        creditNote.setIssueDate(new IssueDateType(getDate()));
        creditNote.setCreditNoteTypeCode(Utils.getCreditNoteType(getDocType()));
        creditNote.setDocumentCurrencyCode(Utils.getDocumentCurrencyCode(getCurrency()));
        creditNote.getNote().add(Utils.getWordsForAmount(getCrossTotal()));
        creditNote.addSignature(Utils.getSignature(getSupplier()));
        creditNote.setAccountingSupplierParty(Utils.getSupplier(getSupplier()));
        creditNote.setAccountingCustomerParty(Utils.getCustomer(getCustomer()));
        creditNote.setCreditNoteLine(Utils.getCreditNoteLines(getLines()));
        creditNote.setAllowanceCharge(AllowancesCharges.getAllowanceCharge(getAllowancesCharges()));
        creditNote.setTaxTotal(Taxes.getTaxTotal(getTaxes(), false));
        creditNote.setLegalMonetaryTotal(getMonetaryTotal(creditNote));
        creditNote.setPaymentTerms(Utils.getPaymentTerms(getPaymentTerms()));
        creditNote.setBillingReference(Utils.getBillingReferenceType(getReference()));
        creditNote.setDiscrepancyResponse(Utils.getDiscrepancyResponse(getCreditNoteTypeCode()));

        return DocumentMarshaller.creditNote()
                        .setCharset(getEncoding())
                        .setFormattedOutput(true)
                        .getAsString(creditNote);
    }
}
