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
import java.util.ArrayList;
import java.util.List;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.TaxCategoryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.TaxSchemeType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.TaxSubtotalType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.TaxTotalType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.BaseUnitMeasureType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.PerUnitAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.TaxAmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.TaxableAmountType;

public class Taxes
{

    public static List<TaxTotalType> getTaxTotal(final List<ITaxEntry> taxEntries, final boolean isItem)
    {
        final var ret = new ArrayList<TaxTotalType>();
        final var taxTotal = new TaxTotalType();
        ret.add(taxTotal);
        final var subTotals = new ArrayList<TaxSubtotalType>();
        for (final var taxEntry : taxEntries) {
            subTotals.add(getTaxSubtotal(taxEntry, isItem));
        }
        taxTotal.setTaxSubtotal(subTotals);
        taxTotal.setTaxAmount(Utils.getAmount(TaxAmountType.class, taxEntries.stream()
                        .map(entry -> { return entry.getAmount(); })
                        .reduce(BigDecimal.ZERO, BigDecimal::add)));
        return ret;
    }

    public static TaxSubtotalType getTaxSubtotal(final ITaxEntry taxEntry, final boolean isItem)
    {
        final var ret = new TaxSubtotalType();
        if (TaxType.ADVALOREM.equals(taxEntry.getTaxType())) {
            ret.setTaxableAmount(Utils.getAmount(TaxableAmountType.class, taxEntry.getTaxableAmount()));
        }
        if (TaxType.PERUNIT.equals(taxEntry.getTaxType())) {
            final var baseUnitMeasure = new BaseUnitMeasureType();
            baseUnitMeasure.setUnitCode("NIU");
            baseUnitMeasure.setValue(BigDecimal.ONE);
            ret.setBaseUnitMeasure(baseUnitMeasure);
        }
        ret.setTaxAmount(Utils.getAmount(TaxAmountType.class, taxEntry.getAmount()));
        ret.setTaxCategory(getTaxCategory(taxEntry, isItem));
        return ret;
    }

    public static TaxCategoryType getTaxCategory(final ITaxEntry taxEntry, final boolean isItem)
    {
        final var ret = new TaxCategoryType();
        if (isItem) {
            ret.setTaxExemptionReasonCode(taxEntry.getTaxExemptionReasonCode());
        }

        if (TaxType.ADVALOREM.equals(taxEntry.getTaxType())) {
            ret.setPercent(taxEntry.getPercent());
        }

        if (TaxType.PERUNIT.equals(taxEntry.getTaxType())) {
            ret.setPercent(taxEntry.getAmount());
            ret.setPerUnitAmount(Utils.getAmount(PerUnitAmountType.class, taxEntry.getAmount()));
        }

        final var taxScheme = new TaxSchemeType();
        final var idType = new IDType();
        idType.setSchemeAgencyID("6");
        idType.setSchemeID("UN/ECE 5153");
        idType.setValue(taxEntry.getId());
        taxScheme.setID(idType);
        taxScheme.setName(taxEntry.getName());
        taxScheme.setTaxTypeCode(taxEntry.getCode());
        ret.setTaxScheme(taxScheme);
        return ret;
    }

    public static abstract class TaxEntry
        implements ITaxEntry
    {

        private BigDecimal amount;
        private BigDecimal taxableAmount;

        @Override
        public BigDecimal getAmount()
        {
            return amount;
        }

        public TaxEntry setAmount(final BigDecimal amount)
        {
            this.amount = amount;
            return this;
        }

        @Override
        public BigDecimal getTaxableAmount()
        {
            return taxableAmount;
        }

        public TaxEntry setTaxableAmount(final BigDecimal taxableAmount)
        {
            this.taxableAmount = taxableAmount;
            return this;
        }
    }

    public static class IGV
        extends TaxEntry
    {

        @Override
        public BigDecimal getPercent()
        {
            return new BigDecimal("18");
        }

        @Override
        public String getId()
        {
            return "1000";
        }

        @Override
        public String getName()
        {
            return "IGV";
        }

        @Override
        public String getCode()
        {
            return "VAT";
        }

        @Override
        public String getTaxExemptionReasonCode()
        {
            return "40";
        }
    }

    public static class ICB
        extends TaxEntry
    {

        @Override
        public BigDecimal getPercent()
        {
            return new BigDecimal("0");
        }

        @Override
        public String getId()
        {
            return "9999";
        }

        @Override
        public String getName()
        {
            return "OTROS";
        }

        @Override
        public String getCode()
        {
            return "OTH";
        }

        @Override
        public String getTaxExemptionReasonCode()
        {
            return null;
        }
    }

}
