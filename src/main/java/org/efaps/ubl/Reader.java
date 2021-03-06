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
