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
package org.efaps.ubl.extension;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.helger.ubl21.CUBL21;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.ValueType;

@XmlAccessorType(XmlAccessType.FIELD)
public class AdditionalProperty
{

    @XmlElement(name = "ID", namespace = CUBL21.XML_SCHEMA_CBC_NAMESPACE_URL)
    private IDType id;

    @XmlElement(name = "Value", namespace = CUBL21.XML_SCHEMA_CBC_NAMESPACE_URL)
    private ValueType value;

    public IDType getId()
    {
        return id;
    }

    public void setId(final IDType id)
    {
        this.id = id;
    }

    public void setId(final String id)
    {
        final var idType = new IDType();
        idType.setSchemeID(id);
        this.id = idType;
    }


    public ValueType getValue()
    {
        return value;
    }


    public void setValue(final ValueType value)
    {
        this.value = value;
    }

    public void setValue(final String value)
    {
        final var valueType = new ValueType();
        valueType.setValue(value);
        this.value = valueType;
    }
}
