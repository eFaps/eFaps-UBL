package org.efaps.ubl;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.efaps.ubl.extension.AdditionalInformation;

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
        JAXBContext jaxbContext;
       // if (isUseJAXBContextCache()) {
        //jaxbContext = JAXBContextCache.getInstance().getFromCache(m_aDocType.getImplementationClass(),
        //                  getClassLoader());
            // } else {
             //jaxbContext = JAXBContext.newInstance(m_aDocType.getImplementationClass().getPackage().getName(),
              //              getClassLoader());
                            // }



        jaxbContext = JAXBContext.newInstance(InvoiceType.class, AdditionalInformation.class);

        return jaxbContext;
    }
}
