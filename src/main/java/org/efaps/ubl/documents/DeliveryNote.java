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

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Collections;

import org.efaps.ubl.builder.DeliveryNoteBuilder;
import org.efaps.ubl.extension.Definitions;

import com.helger.ubl21.CUBL21;
import com.helger.ubl21.UBL21NamespaceContext;

import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PeriodType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.ShipmentStageType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.ShipmentType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.CustomizationIDType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.GrossWeightMeasureType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.HandlingCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.HandlingInstructionsType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IssueDateType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.IssueTimeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.SpecialInstructionsType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.TransportModeCodeType;
import oasis.names.specification.ubl.schema.xsd.despatchadvice_21.DespatchAdviceType;

public class DeliveryNote
    extends AbstractDocument<DeliveryNote>
{

    private LocalTime time;
    private Shipment shipment;

    public Shipment getShipment()
    {
        return shipment;
    }

    public DeliveryNote withShipment(final Shipment shipment)
    {
        this.shipment = shipment;
        return this;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public DeliveryNote withTime(final LocalTime time)
    {
        this.time = time;
        return this;
    }

    @Override
    protected DeliveryNote getThis()
    {
        return this;
    }

    @Override
    protected String getDocType()
    {
        return "09";
    }

    @Override
    public String getUBLXml()
    {
        if (!UBL21NamespaceContext.getInstance().getPrefixToNamespaceURIMap().containsKey("sac")) {
            UBL21NamespaceContext.getInstance().addMapping("sac", Definitions.NAMESPACE_SUNATAGGREGATE);
            UBL21NamespaceContext.getInstance().removeMapping("cec");
            UBL21NamespaceContext.getInstance().addMapping("ext", CUBL21.XML_SCHEMA_CEC_NAMESPACE_URL);
        }
        final var despatchAdvice = new DespatchAdviceType();
        despatchAdvice.setUBLVersionID("2.1");
        final var customizationID = new CustomizationIDType();
        customizationID.setSchemeAgencyName(Utils.AGENCYNAME);
        customizationID.setValue("2.0");
        despatchAdvice.setCustomizationID(customizationID);
        despatchAdvice.setID(getNumber());
        despatchAdvice.setIssueDate(new IssueDateType(getDate()));
        despatchAdvice.setIssueTime(new IssueTimeType(getTime()));
        despatchAdvice.addSignature(Utils.getSignature(getSupplier()));
        despatchAdvice.setDespatchSupplierParty(Utils.getSupplier(getSupplier()));
        despatchAdvice.setDeliveryCustomerParty(Utils.getCustomer(getCustomer()));
        // despatchAdvice.setBuyerCustomerParty(Utils.getCustomer(getCustomer()));
        despatchAdvice.setDespatchLine(Utils.getDeliveryNoteLines(getLines()));

        final var shipment = new ShipmentType();
        shipment.setID("1");
        if (getShipment().getHandlingCode() != null) {
            final var handlingCodeType = new HandlingCodeType();
            handlingCodeType.setListAgencyName(Utils.AGENCYNAME);
            handlingCodeType.setListName(Catalogs.MOTTRASL.getName());
            handlingCodeType.setListURI(Catalogs.MOTTRASL.getURI());
            handlingCodeType.setValue(getShipment().getHandlingCode());
            shipment.setHandlingCode(handlingCodeType);
        }
        if (getShipment().getHandlingInstructions() != null) {
            shipment.setHandlingInstructions(Collections
                            .singletonList(new HandlingInstructionsType(getShipment().getHandlingInstructions())));
        }

        if (getShipment().getCrossWeight() != null && getShipment().getCrossWeightUoM() != null) {
            final var grossWeightMeasureType = new GrossWeightMeasureType();
            grossWeightMeasureType.setValue(getShipment().getCrossWeight());
            grossWeightMeasureType.setUnitCode(getShipment().getCrossWeightUoM());
            shipment.setGrossWeightMeasure(grossWeightMeasureType);
        }
        for (final var instruction: getShipment().getInstructions()) {
            final var specialInstructionsType = new SpecialInstructionsType();
            specialInstructionsType.setValue(instruction);
            shipment.addSpecialInstructions(specialInstructionsType);
        }
        for (final var stage: getShipment().getStages()) {

            final var shipmentStageType = new ShipmentStageType();
            final var transportModeCodeType = new TransportModeCodeType();
            transportModeCodeType.setListAgencyName(Utils.AGENCYNAME);
            transportModeCodeType.setListName(Catalogs.MODTRASL.getName());
            transportModeCodeType.setListURI(Catalogs.MODTRASL.getURI());
            transportModeCodeType.setValue(stage.getMode());
            shipmentStageType.setTransportModeCode(transportModeCodeType);

            final var periodType = new PeriodType();
            periodType.setStartDate(stage.getStartDate());
            shipmentStageType.setTransitPeriod(periodType);

            if (stage.getCarrier() != null) {
                shipmentStageType.addCarrierParty(Utils.getCarrier(stage.getCarrier()));
            }

            shipment.addShipmentStage(shipmentStageType);
        }
        despatchAdvice.setShipment(shipment);

        /**
         * creditNote.setCreditNoteTypeCode(Utils.getCreditNoteType(getDocType()));
         * creditNote.setDocumentCurrencyCode(Utils.getDocumentCurrencyCode(getCurrency()));
         * creditNote.getNote().add(Utils.getWordsForAmount(getCrossTotal()));
         *
         * creditNote.setCreditNoteLine(Utils.getCreditNoteLines(getLines()));
         * creditNote.setAllowanceCharge(AllowancesCharges.getAllowanceCharge(getAllowancesCharges()));
         * creditNote.setTaxTotal(Taxes.getTaxTotal(getTaxes(), false));
         * creditNote.setLegalMonetaryTotal(getMonetaryTotal(creditNote));
         * creditNote.setPaymentTerms(Utils.getPaymentTerms(getPaymentTerms()));
         * creditNote.setBillingReference(Utils.getBillingReferenceType(getReference()));
         * creditNote.setDiscrepancyResponse(Utils.getDiscrepancyResponse(getCreditNoteTypeCode()));
         **/
        return new DeliveryNoteBuilder().setCharset(StandardCharsets.UTF_8)
                        .setFormattedOutput(true)
                        .getAsString(despatchAdvice);
    }
}
