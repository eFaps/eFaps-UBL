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


public enum Catalogs
{

    TDOC("Tipo de Documento", "01"),
    TAX("Codigo de Tributos", "05"),
    DOI("Documento de Identidad", "06"),
    AIGV("Afectacion de IGV", "07"),
    MODTRASL("Modalidad de traslado", "18"),
    MOTTRASL("Motivo de traslado", "20"),
    CADE("Cargo/descuento", "53"),
    ITEMPROP("Propiedad del item", "55");

    private String name;
    private String number;

    Catalogs(final String name, final String number) {
        this.name = name;
        this.number = number;
    }

    public String getURI() {
         return "urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo" + number;
    }

    public String getName() {
        return name;
    }
}
