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
package org.efaps.ubl.documents.elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.efaps.ubl.documents.interfaces.IAdditionalItemProperty;
import org.efaps.ubl.documents.interfaces.IAllowanceChargeEntry;
import org.efaps.ubl.documents.interfaces.ILine;
import org.efaps.ubl.documents.interfaces.ITaxEntry;

public class Line
    implements ILine
{

    private List<ITaxEntry> taxEntries;

    private List<IAllowanceChargeEntry> allowancesCharges = new ArrayList<>();

    private BigDecimal netPrice;

    private BigDecimal crossPrice;

    private BigDecimal netUnitPrice;

    private BigDecimal crossUnitPrice;

    private String uoMCode;

    private BigDecimal quantity;

    private String description;

    private String sku;

    private String priceType;

    private List<IAdditionalItemProperty> additionalItemProperties;

    private Line(final Builder builder)
    {
        taxEntries = builder.taxEntries;
        netPrice = builder.netPrice;
        crossPrice = builder.crossPrice;
        netUnitPrice = builder.netUnitPrice;
        crossUnitPrice = builder.crossUnitPrice;
        uoMCode = builder.uoMCode;
        quantity = builder.quantity;
        description = builder.description;
        sku = builder.sku;
        priceType = builder.priceType;
        additionalItemProperties = builder.additionalItemProperties == null ? Collections.emptyList()
                        : builder.additionalItemProperties;
    }

    public Line()
    {
    }

    @Override
    public List<ITaxEntry> getTaxEntries()
    {
        return taxEntries;
    }

    public void setTaxEntries(final List<ITaxEntry> taxEntries)
    {
        this.taxEntries = taxEntries;
    }

    @Override
    public List<IAllowanceChargeEntry> getAllowancesCharges()
    {
        return allowancesCharges;
    }

    public void setAllowancesCharges(final List<IAllowanceChargeEntry> allowancesCharges)
    {
        this.allowancesCharges = allowancesCharges;
    }

    @Override
    public BigDecimal getNetPrice()
    {
        return netPrice;
    }

    public void setNetPrice(final BigDecimal netPrice)
    {
        this.netPrice = netPrice;
    }

    @Override
    public BigDecimal getCrossPrice()
    {
        return crossPrice;
    }

    public void setCrossPrice(final BigDecimal crossPrice)
    {
        this.crossPrice = crossPrice;
    }

    @Override
    public BigDecimal getNetUnitPrice()
    {
        return netUnitPrice;
    }

    public void setNetUnitPrice(final BigDecimal netUnitPrice)
    {
        this.netUnitPrice = netUnitPrice;
    }

    @Override
    public BigDecimal getCrossUnitPrice()
    {
        return crossUnitPrice;
    }

    public void setCrossUnitPrice(final BigDecimal crossUnitPrice)
    {
        this.crossUnitPrice = crossUnitPrice;
    }

    @Override
    public String getUoMCode()
    {
        return uoMCode;
    }

    public void setUoMCode(final String uoMCode)
    {
        this.uoMCode = uoMCode;
    }

    @Override
    public BigDecimal getQuantity()
    {
        return quantity;
    }

    public void setQuantity(final BigDecimal quantity)
    {
        this.quantity = quantity;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    @Override
    public String getSku()
    {
        return sku;
    }

    public void setSku(final String sku)
    {
        this.sku = sku;
    }

    @Override
    public String getPriceType()
    {
        return priceType == null ? "01" : priceType;
    }

    public void setPriceType(final String priceType)
    {
        this.priceType = priceType;
    }

    /**
     * Creates builder to build {@link Line}.
     *
     * @return created builder
     */
    public static Builder builder()
    {
        return new Builder();
    }

    @Override
    public List<IAdditionalItemProperty> getAdditionalItemProperties()
    {
        return additionalItemProperties;
    }

    /**
     * Builder to build {@link Line}.
     */
    public static final class Builder
    {

        private List<ITaxEntry> taxEntries = new ArrayList<>();
        private List<IAllowanceChargeEntry> allowancesCharges = new ArrayList<>();
        private BigDecimal netPrice;
        private BigDecimal crossPrice;
        private BigDecimal netUnitPrice;
        private BigDecimal crossUnitPrice;
        private String uoMCode;
        private BigDecimal quantity;
        private String description;
        private String sku;
        private String priceType;
        private List<IAdditionalItemProperty> additionalItemProperties;

        private Builder()
        {
        }

        public Builder withTax(final ITaxEntry tax) {
            taxEntries.add(tax);
            return this;
        }

        public Builder withTaxEntries(final List<ITaxEntry> taxEntries)
        {
            this.taxEntries = taxEntries;
            return this;
        }

        public Builder withAllowanceCharge(final IAllowanceChargeEntry allowanceCharge) {
            allowancesCharges.add(allowanceCharge);
            return this;
        }

        public Builder withAllowancesCharges(final List<IAllowanceChargeEntry> allowancesCharges)
        {
            this.allowancesCharges = allowancesCharges;
            return this;
        }

        public Builder withNetPrice(final BigDecimal netPrice)
        {
            this.netPrice = netPrice;
            return this;
        }

        public Builder withCrossPrice(final BigDecimal crossPrice)
        {
            this.crossPrice = crossPrice;
            return this;
        }

        public Builder withNetUnitPrice(final BigDecimal netUnitPrice)
        {
            this.netUnitPrice = netUnitPrice;
            return this;
        }

        public Builder withCrossUnitPrice(final BigDecimal crossUnitPrice)
        {
            this.crossUnitPrice = crossUnitPrice;
            return this;
        }

        public Builder withUoMCode(final String uoMCode)
        {
            this.uoMCode = uoMCode;
            return this;
        }

        public Builder withQuantity(final BigDecimal quantity)
        {
            this.quantity = quantity;
            return this;
        }

        public Builder withDescription(final String description)
        {
            this.description = description;
            return this;
        }

        public Builder withSku(final String sku)
        {
            this.sku = sku;
            return this;
        }

        public Builder withPriceType(final String priceType)
        {
            this.priceType = priceType;
            return this;
        }

        public Builder withAdditionalItemProperties(final List<IAdditionalItemProperty> additionalItemProperties)
        {
            this.additionalItemProperties = additionalItemProperties;
            return this;
        }

        public Line build()
        {
            return new Line(this);
        }
    }
}
