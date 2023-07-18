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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Shipment
{

    private String handlingCode;
    private String handlingInstructions;
    private BigDecimal crossWeight;
    private String crossWeightUoM;
    private List<String> instructions = new ArrayList<>();


    public List<String> getInstructions()
    {
        return instructions;
    }


    public Shipment withInstructions(List<String> instructions)
    {
        this.instructions = instructions;
        return this;
    }

    public Shipment addInstruction(String instruction)
    {
        this.instructions.add(instruction);
        return this;
    }

    public String getHandlingInstructions()
    {
        return handlingInstructions;
    }

    public Shipment withHandlingInstructions(String handlingInstructions)
    {
        this.handlingInstructions = handlingInstructions;
        return this;
    }

    public String getHandlingCode()
    {
        return handlingCode;
    }

    public Shipment withHandlingCode(final String handlingCode)
    {
        this.handlingCode = handlingCode;
        return this;
    }

    public BigDecimal getCrossWeight()
    {
        return crossWeight;
    }

    public Shipment withCrossWeight(final BigDecimal crossWeight)
    {
        this.crossWeight = crossWeight;
        return this;
    }

    public String getCrossWeightUoM()
    {
        return crossWeightUoM;
    }

    public Shipment withCrossWeightUoM(final String crossWeightUoM)
    {
        this.crossWeightUoM = crossWeightUoM;
        return this;
    }
}