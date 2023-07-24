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
package org.efaps.ubl.documents.values;

public enum DeliveryNoteInstruction
{

    SCHEDULED_SHIPMENT("SUNAT_Envio_IndicadorTransbordoProgramado"),
    VEHICLE_MINOR("SUNAT_Envio_IndicadorTrasladoVehiculoM1L"),
    RETURNING_VEHICLE_CONTAINER("SUNAT_Envio_IndicadorRetornoVehiculoEnvaseVacio"),
    RETURNING_VEHICLE_EMPTY("SUNAT_Envio_IndicadorRetornoVehiculoVacio"),
    TRANSFER_TOTAL("SUNAT_Envio_IndicadorTrasladoTotalDAMoDS"),
    VEHICLE_CONDUCTOR("SUNAT_Envio_IndicadorVehiculoConductoresTransp");

    private String name;

    DeliveryNoteInstruction(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
