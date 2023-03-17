package org.efaps.ubl;

import org.efaps.ubl.extension.SummaryDocumentsType;

import com.helger.jaxb.builder.JAXBDocumentType;
import com.helger.jaxb.builder.JAXBReaderBuilder;

public class SummaryReader extends JAXBReaderBuilder<SummaryDocumentsType, SummaryReader>
{

    public SummaryReader()
    {
        super(new JAXBDocumentType(SummaryDocumentsType.class, null, null));
    }
}
