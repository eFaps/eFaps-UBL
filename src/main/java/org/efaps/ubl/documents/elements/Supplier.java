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
package org.efaps.ubl.documents.elements;

import org.efaps.ubl.documents.interfaces.ISupplier;

public class Supplier
    extends Party
    implements ISupplier
{

    private String geoLocationId;

    private String anexo;

    private String district;

    private String streetName;

    @Override
    public String getGeoLocationId()
    {
        return geoLocationId;
    }

    public Supplier withGeoLocationId(final String geoLocationId)
    {
        this.geoLocationId = geoLocationId;
        return this;
    }

    @Override
    public String getAnexo()
    {
        return anexo;
    }

    public Supplier setAnexo(final String anexo)
    {
        this.anexo = anexo;
        return this;
    }

    @Override
    public String getDistrict()
    {
        return district;
    }

    public Supplier setDistrict(final String district)
    {
        this.district = district;
        return this;
    }

    @Override
    public String getStreetName()
    {
        return streetName;
    }

    public Supplier setStreetName(final String streetName)
    {
        this.streetName = streetName;
        return this;
    }
}
