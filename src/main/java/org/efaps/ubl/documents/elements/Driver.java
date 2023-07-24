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
package org.efaps.ubl.documents.elements;

import org.efaps.ubl.documents.interfaces.IDriver;

public class Driver
    implements IDriver
{

    private String doiType;
    private String doi;
    private String firstName;
    private String familyName;
    private String license;
    private String jobTitle;

    @Override
    public String getJobTitle()
    {
        return jobTitle;
    }


    @Override
    public String getDoiType()
    {
        return doiType;
    }

    @Override
    public String getDOI()
    {
        return doi;
    }

    @Override
    public String getFirstName()
    {
        return firstName;
    }

    @Override
    public String getFamilyName()
    {
        return familyName;
    }

    @Override
    public String getLicense()
    {
        return license;
    }

    public String getDoi()
    {
        return doi;
    }

    public Driver withDOI(String doi)
    {
        this.doi = doi;
        return this;
    }

    public Driver withDoiType(String doiType)
    {
        this.doiType = doiType;
        return this;
    }

    public Driver withFirstName(String firstName)
    {
        this.firstName = firstName;
        return this;
    }

    public Driver withFamilyName(String familyName)
    {
        this.familyName = familyName;
        return this;
    }

    public Driver withLicense(String license)
    {
        this.license = license;
        return this;
    }

    public Driver withJobTitle(String jobTitle)
    {
        this.jobTitle = jobTitle;
        return this;
    }

}
