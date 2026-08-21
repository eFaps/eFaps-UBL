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
package org.efaps.ubl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.efaps.ubl.documents.elements.Summary;
import org.efaps.ubl.documents.interfaces.ICustomer;
import org.efaps.ubl.documents.interfaces.ISummaryLine;
import org.efaps.ubl.documents.interfaces.ITaxEntry;
import org.efaps.ubl.marshaller.DocumentMarshaller;

public class SummaryService
{

    public Summary createSummary(String... xmls)
    {
        final var summary = new Summary();
        for (final var xml : xmls) {
            final var invoice = DocumentMarshaller.invoice().read(xml);
            summary.addLine(new ISummaryLine()
            {

                @Override
                public String getDocType()
                {
                    return invoice.getInvoiceTypeCodeValue();
                }

                @Override
                public String getNumber()
                {
                    return invoice.getIDValue();
                }

                @Override
                public ICustomer getCustomer()
                {
                    return new ICustomer()
                    {

                        @Override
                        public String getDoiType()
                        {
                            final var doiType = invoice.getAccountingCustomerParty().getParty()
                                            .getPartyIdentificationAtIndex(0)
                                            .getID().getSchemeID();
                            return StringUtils.isNumeric(doiType) ? doiType : "0";
                        }

                        @Override
                        public String getDOI()
                        {
                            return invoice.getAccountingCustomerParty().getParty().getPartyIdentificationAtIndex(0)
                                            .getID().getValue();
                        }

                        @Override
                        public String getName()
                        {
                            return invoice.getAccountingCustomerParty().getParty().getPartyLegalEntityAtIndex(0)
                                            .getRegistrationNameValue();
                        }

                        @Override
                        public String getAddressLine()
                        {
                            return null;
                        }

                        @Override
                        public String getCountry()
                        {
                            return null;
                        }
                    };
                }

                @Override
                public BigDecimal getCrossTotal()
                {
                    return invoice.getLegalMonetaryTotal().getPayableAmountValue().setScale(2, RoundingMode.HALF_UP);
                }

                @Override
                public BigDecimal getNetTotal()
                {
                    return invoice.getLegalMonetaryTotal().getTaxExclusiveAmountValue().setScale(2,
                                    RoundingMode.HALF_UP);
                }

                @Override
                public List<ITaxEntry> getTaxEntries()
                {
                    final var entries = new ArrayList<ITaxEntry>();
                    for (final var taxtotal : invoice.getTaxTotal()) {
                        entries.add(new ITaxEntry()
                        {

                            @Override
                            public BigDecimal getTaxableAmount()
                            {
                                return taxtotal.getTaxSubtotalAtIndex(0).getTaxableAmountValue().setScale(2,
                                                RoundingMode.HALF_UP);
                            }

                            @Override
                            public String getTaxExemptionReasonCode()
                            {
                                return taxtotal.getTaxSubtotalAtIndex(0).getTaxCategory()
                                                .getTaxExemptionReasonCodeValue();
                            }

                            @Override
                            public BigDecimal getPercent()
                            {
                                return null;// taxtotal.getTaxSubtotalAtIndex(0).getTaxCategory().getPercentValue();
                            }

                            @Override
                            public String getName()
                            {
                                return taxtotal.getTaxSubtotalAtIndex(0).getTaxCategory().getTaxScheme().getNameValue();
                            }

                            @Override
                            public String getId()
                            {
                                return taxtotal.getTaxSubtotalAtIndex(0).getTaxCategory().getTaxScheme().getIDValue();
                            }

                            @Override
                            public String getCode()
                            {
                                return taxtotal.getTaxSubtotalAtIndex(0).getTaxCategory().getTaxScheme()
                                                .getTaxTypeCodeValue();
                            }

                            @Override
                            public BigDecimal getAmount()
                            {
                                return taxtotal.getTaxAmountValue().setScale(2, RoundingMode.HALF_UP);
                            }
                        });
                    }
                    return entries;
                }
            });
        }
        return summary;
    }
}
