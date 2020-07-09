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
