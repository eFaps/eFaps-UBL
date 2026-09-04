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
package org.efaps.ubl.marshaller;

import static org.testng.Assert.assertEquals;

import java.io.File;

import org.testng.annotations.Test;

public class DocumentMarshallerTest
{
    @Test
    public void parseApplicationResponse() {
        final File responseFile = new File( getClass().getClassLoader().getResource("Response.xml").getFile());
        final var marshaller = DocumentMarshaller.applicationResponse();
        final var appResponse = marshaller.read(responseFile);
        final var response = appResponse.getDocumentResponseAtIndex(0).getResponse();
        final var responseCode = response.getResponseCodeValue();
        assertEquals(responseCode, "0");
    }
}
