/*
 * Copyright 2003 - 2022 The eFaps Team
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

import java.time.LocalDate;

public class Reference
{

    private LocalDate date;
    private String number;
    private String docType;

    public LocalDate getDate()
    {
        return date;
    }

    public Reference setDate(final LocalDate date)
    {
        this.date = date;
        return this;
    }

    public String getNumber()
    {
        return number;
    }

    public Reference setNumber(final String number)
    {
        this.number = number;
        return this;
    }

    public String getDocType()
    {
        return docType;
    }

    public Reference setDocType(final String docType)
    {
        this.docType = docType;
        return this;
    }
}