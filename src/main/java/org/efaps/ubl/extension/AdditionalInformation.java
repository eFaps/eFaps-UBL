package org.efaps.ubl.extension;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "AdditionalInformation", namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1")
@XmlType(name = "AdditionalInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class AdditionalInformation
    implements Serializable
{

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "AdditionalProperty", namespace = "urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1")
    private AdditionalProperty additionalProperty;

    public AdditionalProperty getAdditionalProperty()
    {
        return additionalProperty;
    }

    public void setAdditionalProperty(final AdditionalProperty additionalProperty)
    {
        this.additionalProperty = additionalProperty;
    }
}
