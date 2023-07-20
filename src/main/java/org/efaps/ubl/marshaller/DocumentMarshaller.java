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
package org.efaps.ubl.marshaller;

import javax.xml.namespace.QName;

import org.efaps.ubl.extension.AdditionalInformation;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.ubl21.UBL21Marshaller;
import com.helger.ubl21.UBL21Marshaller.UBL21JAXBMarshaller;
import com.helger.xsds.xmldsig.SignatureType;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import oasis.names.specification.ubl.schema.xsd.creditnote_21.CreditNoteType;
import oasis.names.specification.ubl.schema.xsd.despatchadvice_21.DespatchAdviceType;
import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;

public class DocumentMarshaller<JAXBTYPE>
    extends UBL21JAXBMarshaller<JAXBTYPE>
{

    private DocumentMarshaller(Class<JAXBTYPE> aType,
                               ICommonsList<ClassPathResource> aXSDs,
                               QName aRootElementQName)
    {
        super(aType, aXSDs, aRootElementQName);
    }

    @Override
    protected JAXBContext getJAXBContext(ClassLoader classloader)
        throws JAXBException
    {
        final var clazzes = new CommonsArrayList<Class<?>>();
        clazzes.add(InvoiceType.class);
        clazzes.add(CreditNoteType.class);
        clazzes.add(DespatchAdviceType.class);
        clazzes.add(AdditionalInformation.class);
        clazzes.add(SignatureType.class);
        final var context = JAXBContext.newInstance(clazzes.toArray(new Class[clazzes.size()]));
        return context;
    }

    public static DocumentMarshaller<DespatchAdviceType> deliveryNote()
    {
        return new DocumentMarshaller<>(DespatchAdviceType.class,
                        UBL21Marshaller.getAllDespatchAdviceXSDs(),
                        oasis.names.specification.ubl.schema.xsd.despatchadvice_21.ObjectFactory._DespatchAdvice_QNAME);
    }

    public static DocumentMarshaller<InvoiceType> invoice()
    {
        return new DocumentMarshaller<>(InvoiceType.class,
                        UBL21Marshaller.getAllInvoiceXSDs(),
                        oasis.names.specification.ubl.schema.xsd.invoice_21.ObjectFactory._Invoice_QNAME);
    }

    public static DocumentMarshaller<CreditNoteType> creditNote()
    {
        return new DocumentMarshaller<>(CreditNoteType.class,
                        UBL21Marshaller.getAllCreditNoteXSDs(),
                        oasis.names.specification.ubl.schema.xsd.creditnote_21.ObjectFactory._CreditNote_QNAME);
    }
}
