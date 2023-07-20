/*
 * Copyright 2003 - 2022 The eFaps Team
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
package org.efaps.ubl.documents.interfaces;

import java.math.BigDecimal;

import org.efaps.ubl.documents.values.TaxType;

public interface ITaxEntry
{

    BigDecimal getAmount();

    BigDecimal getTaxableAmount();

    BigDecimal getPercent();

    String getId();

    String getName();

    String getCode();

    String getTaxExemptionReasonCode();

    default TaxType getTaxType()
    {
        return TaxType.ADVALOREM;
    }

    default boolean isFreeOfCharge()
    {
        return false;
    }
}
