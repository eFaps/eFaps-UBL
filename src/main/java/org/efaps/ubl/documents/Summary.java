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

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.efaps.ubl.builder.SummaryBuilder;
import org.efaps.ubl.extension.Definitions;
import org.efaps.ubl.extension.SummaryDocumentsType;

import com.helger.ubl21.CUBL21;
import com.helger.ubl21.UBL21NamespaceContext;

public class Summary
{

    private String number;
    private LocalDate referenceDate;
    private LocalDate issueDate;
    private ISupplier supplier;
    private List<ISummaryLine> lines;

    public List<ISummaryLine> getLines()
    {
        return lines;
    }

    public Summary setLines(List<ISummaryLine> lines)
    {
        this.lines = lines;
        return this;
    }

    public Summary addLine(ISummaryLine line)
    {
        if (this.lines == null) {
            this.lines = new ArrayList<>();
        }
        this.lines.add(line);
        return this;
    }

    public ISupplier getSupplier()
    {
        return supplier;
    }

    public Summary setSupplier(ISupplier supplier)
    {
        this.supplier = supplier;
        return this;
    }

    public String getNumber()
    {
        return number;
    }

    public LocalDate getReferenceDate()
    {
        return referenceDate;
    }

    public Summary setReferenceDate(LocalDate referenceDate)
    {
        this.referenceDate = referenceDate;
        return this;
    }

    public LocalDate getIssueDate()
    {
        return issueDate;
    }

    public Summary setIssueDate(LocalDate issueDate)
    {
        this.issueDate = issueDate;
        return this;
    }

    public Summary setNumber(String number)
    {
        this.number = number;
        return this;
    }

    public String getUBLXml()
    {
        UBL21NamespaceContext.getInstance().setDefaultNamespaceURI(Definitions.NAMESPACE_SUMMARY);
        if (!UBL21NamespaceContext.getInstance().getPrefixToNamespaceURIMap().containsKey("sac")) {
            UBL21NamespaceContext.getInstance().addMapping("sac", Definitions.NAMESPACE_SUNATAGGREGATE);
            UBL21NamespaceContext.getInstance().removeMapping("cec");
            UBL21NamespaceContext.getInstance().addMapping("ext", CUBL21.XML_SCHEMA_CEC_NAMESPACE_URL);
        }
        final var summary = new SummaryDocumentsType();
        summary.setUBLVersionID("2.0");
        summary.setID(getNumber());
        summary.setReferenceDate(getReferenceDate());
        summary.setIssueDate(getIssueDate());
        summary.addSignature(Utils.getSignature(getSupplier()));
        summary.setAccountingSupplierParty(Utils.getSupplier(getSupplier()));
        summary.setSummaryDocumentsLines(Utils.getSummaryLines(getLines()));
        final var ret = new SummaryBuilder().setCharset(StandardCharsets.UTF_8)
                        .setFormattedOutput(true)
                        .getAsString(summary);
        UBL21NamespaceContext.getInstance().removeMapping("");
        return ret;
    }
}
