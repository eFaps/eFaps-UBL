package org.efaps.ubl;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.efaps.ubl.extension.AdditionalInformation;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.jaxb.JAXBContextCache;
import com.helger.ubl21.UBL21WriterBuilder;

import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;

public class Builder
    extends UBL21WriterBuilder<InvoiceType>
{

    public Builder()
    {
        super(InvoiceType.class);
    }

    @Override
    protected JAXBContext getJAXBContext()
        throws JAXBException
    {
        final var clazzes = new CommonsArrayList<Class<?>>();
        clazzes.add(InvoiceType.class);
        clazzes.add(AdditionalInformation.class);

        JAXBContext jaxbContext;
        if (isUseJAXBContextCache()) {
            jaxbContext = JAXBContextCache.getInstance().getFromCache(clazzes);
        } else {
            jaxbContext = JAXBContext.newInstance(clazzes.toArray(new Class[clazzes.size()]));
        }
        return jaxbContext;
    }
}
