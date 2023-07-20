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
package org.efaps.ubl.documents.interfaces;

import java.math.BigDecimal;
import java.util.List;

public interface ISummaryLine
{
    String getDocType();
    String getNumber();
    ICustomer getCustomer();
    default int getStatusCode() {
        // 1 = Adicionar
        // 2 = Modificar
        // 3 = Anulado
        return 1;
    }
    BigDecimal getCrossTotal();
    // 01: Valor de venta de las operaciones gravadas con el IGV
    BigDecimal getNetTotal();
    List<ITaxEntry> getTaxEntries();
}
