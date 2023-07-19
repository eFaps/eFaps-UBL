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

import java.time.LocalDate;

public class Stage
{

    private String mode;
    private LocalDate startDate;
    private ICarrier carrier;

    public ICarrier getCarrier()
    {
        return carrier;
    }

    public Stage withCarrier(ICarrier carrier)
    {
        this.carrier = carrier;
        return this;
    }

    public LocalDate getStartDate()
    {
        return startDate;
    }

    public Stage withStartDate(LocalDate startDate)
    {
        this.startDate = startDate;
        return this;
    }

    public String getMode()
    {
        return mode;
    }

    public Stage withMode(final String mode)
    {
        this.mode = mode;
        return this;
    }
}
