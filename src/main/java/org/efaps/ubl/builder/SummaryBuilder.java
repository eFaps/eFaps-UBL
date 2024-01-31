/*
 * Copyright © 2003 - 2024 The eFaps Team (-)
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
 */
package org.efaps.ubl.builder;

import org.efaps.ubl.extension.SummaryDocumentsType;

import com.helger.jaxb.builder.JAXBDocumentType;
import com.helger.jaxb.builder.JAXBWriterBuilder;
import com.helger.ubl21.UBL21NamespaceContext;

public class SummaryBuilder extends JAXBWriterBuilder<SummaryDocumentsType, SummaryBuilder>
{

    public SummaryBuilder()
    {
        super(new JAXBDocumentType(SummaryDocumentsType.class, null, null));
        setNamespaceContext(UBL21NamespaceContext.getInstance());
    }
}
