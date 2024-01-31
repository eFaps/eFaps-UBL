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
package org.efaps.ubl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.commons.io.FileUtils;
import org.efaps.ubl.documents.DocTest;
import org.testng.annotations.Test;

public class SummaryServiceTest
{

    @Test
    public void createSummary()
        throws DatatypeConfigurationException, IOException
    {
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file1 = new File(classLoader.getResource("Invoice1.xml").getFile());
        final var xml1 = FileUtils.readFileToString(file1, StandardCharsets.UTF_8);

        final File file2 = new File(classLoader.getResource("Invoice2.xml").getFile());
        final var xml2 = FileUtils.readFileToString(file2, StandardCharsets.UTF_8);
        final var summaryService = new SummaryService();
        final var summary = summaryService.createSummary(xml1, xml2);
        summary.setNumber("RC-20230228-1001")
            .setReferenceDate(LocalDate.of(2023, 02, 28))
            .setIssueDate(LocalDate.of(2023, 02, 28))
            .setSupplier(DocTest.getSupplier());
        final var ubl = summary.getUBLXml();
        System.out.println(ubl);
    }
}
