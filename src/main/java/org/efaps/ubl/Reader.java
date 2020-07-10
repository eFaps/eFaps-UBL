package org.efaps.ubl;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.efaps.ubl.extension.AdditionalInformation;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.jaxb.JAXBContextCache;
import com.helger.ubl21.UBL21ReaderBuilder;
import com.helger.xsds.xmldsig.SignatureType;

import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;

public class Reader
extends UBL21ReaderBuilder<InvoiceType>
{

    public Reader()
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
        clazzes.add(SignatureType.class);

        JAXBContext jaxbContext;
        if (isUseJAXBContextCache()) {
            jaxbContext = JAXBContextCache.getInstance().getFromCache(clazzes);
        } else {
            jaxbContext = JAXBContext.newInstance(clazzes.toArray(new Class[clazzes.size()]));
        }
        return jaxbContext;
    }

}
