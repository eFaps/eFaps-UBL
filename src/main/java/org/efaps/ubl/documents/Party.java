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
package org.efaps.ubl.documents;

public class Party
    implements IParty
{

    private String doiType;
    private String doi;
    private String name;
    private String addressLine;
    private String country;

    @Override
    public String getDoiType()
    {
        return doiType;
    }

    public void setDoiType(final String doiType)
    {
        this.doiType = doiType;
    }

    @Override
    public String getDOI()
    {
        return doi;
    }

    public void setDOI(final String dOI)
    {
        doi = dOI;
    }

    @Override
    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    @Override
    public String getAddressLine()
    {
        return addressLine;
    }

    public void setAddressLine(final String addressLine)
    {
        this.addressLine = addressLine;
    }

    @Override
    public String getCountry()
    {
        return country;
    }

    public void setCountry(final String country)
    {
        this.country = country;
    }
}
