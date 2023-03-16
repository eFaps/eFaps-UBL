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
package org.efaps.ubl.documents;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

public class SummaryTest
{

    @Test
    public void createSummary()
        throws IOException
    {
        final var summary = new Summary().setNumber("RC-20230228-1001")
                        .setReferenceDate(LocalDate.of(25023, 02, 28))
                        .setIssueDate(LocalDate.of(25023, 02, 28))
                        .setSupplier(DocTest.getSupplier())
                        .setLines(getLines());

        final var ubl = summary.getUBLXml();
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("Summary1.xml").getFile());
        final var xml = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        assertEquals(ubl, xml.trim());
    }

    public static List<ISummaryLine> getLines()
    {
        final var ret = new ArrayList<ISummaryLine>();
        ret.add(new ISummaryLine()
        {

            @Override
            public String getDocType()
            {
                return "03";
            }

            @Override
            public String getNumber()
            {
                return "B001-000116";
            }

            @Override
            public ICustomer getCustomer()
            {
                return DocTest.getCustomer();
            }
        });
        return ret;
    }
}

