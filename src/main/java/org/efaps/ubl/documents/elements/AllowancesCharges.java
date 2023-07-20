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
package org.efaps.ubl.documents.elements;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.efaps.ubl.documents.interfaces.IAllowanceChargeEntry;
import org.efaps.ubl.documents.interfaces.IChargeEntry;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.AllowanceChargeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.AmountType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.BaseAmountType;

public class AllowancesCharges
{

    public static List<AllowanceChargeType> getAllowanceCharge(final List<IAllowanceChargeEntry> allowanceChargeEntries)
    {
        final var ret = new ArrayList<AllowanceChargeType>();
        for (final var allowanceChargeEntry : allowanceChargeEntries) {
            final var allowanceCharge = new AllowanceChargeType();
            ret.add(allowanceCharge);
            allowanceCharge.setChargeIndicator(allowanceChargeEntry.isCharge());
            allowanceCharge.setAllowanceChargeReasonCode(
                            Utils.getAllowanceChargeReasonCode(allowanceChargeEntry.getReason()));
            allowanceCharge.setMultiplierFactorNumeric(allowanceChargeEntry.getFactor());
            // /Invoice/cac:InvoiceLine/cac:Allowancecharge/cbc:Amount (Monto de
            // cargo/descuento) --> n(12,2)
            allowanceCharge.setAmount(Utils.getAmount(AmountType.class,
                            allowanceChargeEntry.getAmount().setScale(2, RoundingMode.HALF_UP)));
            // /Invoice/cac:InvoiceLine/cac:Allowancecharge/cbc:BaseAmount
            // (Monto base del cargo/descuento)--> n(12,2)
            allowanceCharge.setBaseAmount(Utils.getAmount(BaseAmountType.class,
                            allowanceChargeEntry.getBaseAmount().setScale(2, RoundingMode.HALF_UP)));
        }
        return ret;
    }

    public static class ChargeEntry
        implements IChargeEntry
    {

        private String reason;

        private BigDecimal factor;

        private BigDecimal amount;

        private BigDecimal baseAmount;

        @Override
        public String getReason()
        {
            return reason;
        }

        public ChargeEntry setReason(final String reason)
        {
            this.reason = reason;
            return this;
        }

        @Override
        public BigDecimal getFactor()
        {
            return factor;
        }

        public ChargeEntry setFactor(final BigDecimal factor)
        {
            this.factor = factor;
            return this;
        }

        @Override
        public BigDecimal getAmount()
        {
            return amount;
        }

        public ChargeEntry setAmount(final BigDecimal amount)
        {
            this.amount = amount;
            return this;
        }

        @Override
        public BigDecimal getBaseAmount()
        {
            return baseAmount;
        }

        public ChargeEntry setBaseAmount(final BigDecimal baseAmount)
        {
            this.baseAmount = baseAmount;
            return this;
        }
    }

}
