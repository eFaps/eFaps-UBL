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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Shipment
{

    private String handlingCode;
    private String handlingInstructions;
    private BigDecimal crossWeight;
    private String crossWeightUoM;
    private List<String> instructions = new ArrayList<>();
    private List<Stage> stages = new ArrayList<>();
    private Delivery delivery = new Delivery();
    private List<Transport> transportUnits = new ArrayList<>();

    public List<Transport> getTransportUnits()
    {
        return transportUnits;
    }

    public Shipment addTransportUnit(final Transport transportUnit)
    {
        this.transportUnits.add(transportUnit);
        return this;
    }

    public Shipment withTransportUnits(final List<Transport> transportUnits)
    {
        this.transportUnits = transportUnits;
        return this;
    }

    public Shipment addStage(final Stage stage)
    {
        this.stages.add(stage);
        return this;
    }

    public Shipment setStages(List<Stage> stages)
    {
        this.stages = stages;
        return this;
    }

    public List<Stage> getStages()
    {
        return stages;
    }

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

    public Delivery getDelivery()
    {
        return delivery;
    }

    public Shipment withDelivery(final Delivery delivery)
    {
        this.delivery = delivery;
        return this;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
