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
package org.efaps.ubl.documents.elements;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.efaps.ubl.documents.interfaces.IAddress;

public class Delivery
{

    private IAddress deliveryAddress;

    private IAddress despatchAddress;

    public IAddress getDeliveryAddress()
    {
        return deliveryAddress;
    }

    public Delivery withDeliveryAddress(IAddress deliveryAddress)
    {
        this.deliveryAddress = deliveryAddress;
        return this;
    }

    public IAddress getDespatchAddress()
    {
        return despatchAddress;
    }

    public Delivery withDespatchAddress(IAddress despatchAddress)
    {
        this.despatchAddress = despatchAddress;
        return this;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
