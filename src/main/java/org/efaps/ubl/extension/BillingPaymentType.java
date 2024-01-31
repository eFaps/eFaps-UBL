/*
 * Copyright © 2003 - 2024 The eFaps Team (-)
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
 */
package org.efaps.ubl.extension;


import com.helger.ubl21.CUBL21;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.InstructionIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.PaidAmountType;

@XmlAccessorType(XmlAccessType.NONE)
public class BillingPaymentType
{

    @XmlElement(name = "PaidAmount", namespace = CUBL21.XML_SCHEMA_CBC_NAMESPACE_URL)
    private PaidAmountType paidAmountType;
    @XmlElement(name = "InstructionID", namespace = CUBL21.XML_SCHEMA_CBC_NAMESPACE_URL)
    private InstructionIDType instructionID;

    public PaidAmountType getPaidAmountType()
    {
        return paidAmountType;
    }

    public void setPaidAmountType(PaidAmountType paidAmountType)
    {
        this.paidAmountType = paidAmountType;
    }

    public InstructionIDType getInstructionID()
    {
        return instructionID;
    }

    public void setInstructionID(InstructionIDType instructionID)
    {
        this.instructionID = instructionID;
    }
}
