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

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.commons.io.FileUtils;
import org.efaps.ubl.Signing;
import org.efaps.ubl.documents.AllowancesCharges.ChargeEntry;
import org.testng.annotations.Test;

public class DocTest
{

    public static Supplier getSupplier()
    {
        final var ret = new Supplier();
        ret.setCountry("PE");
        ret.setAnexo("1000");
        ret.setDOI("20601327318");
        ret.setDoiType("6");
        ret.setName("Tiendas Mass");
        ret.setUbigeo("150101");
        ret.setDistrict("Lima");
        ret.setAddressLine("JR. CRESPO Y CASTILLO NRO. 2087");
        return ret;
    }

    public static Customer getCustomer()
    {
        final var ret = new Customer();
        ret.setCountry("PE");
        ret.setDOI("43289672");
        ret.setDoiType("1");
        ret.setName("ovar Lopez, Julio Odair");
        ret.setAddressLine("Av parque alto 291-A Lima - Lima - Santiago De Surco");
        return ret;
    }

    public static Carrier getCarrier()
    {
        final var ret = new Carrier();
        ret.setDOI("12345678901");
        ret.setDoiType("2");
        ret.setName("Transportes - El mas rapido");
        ret.withCompanyId("MTC-123465897");
        return ret;
    }

    public static Driver getDriver()
    {
        return new Driver()
                        .withDOI("87654321")
                        .withDoiType("1")
                        .withFamilyName("Quispe")
                        .withFirstName("Carlos")
                        .withLicense("12345666");

    }

    public static List<ILine> getLines()
    {
        final var ret = new ArrayList<ILine>();
        ret.add(Line.builder().withSku("123.456")
                        .withDescription("Lenovo Tablet 7\" TB-7305F 1GB / 16GB Cam post 2MP / Front 2MP")
                        .withQuantity(BigDecimal.ONE)
                        .withCrossUnitPrice(new BigDecimal("118"))
                        .withCrossPrice(new BigDecimal("118"))
                        .withNetUnitPrice(new BigDecimal("100"))
                        .withNetPrice(new BigDecimal("100"))
                        .withTax(new Taxes.IGV()
                                        .setAmount(new BigDecimal("18"))
                                        .setTaxableAmount(new BigDecimal("100")))
                        .build());
        return ret;
    }

    @Test
    public void createInvoice()
        throws DatatypeConfigurationException
    {
        final var invoice = new Invoice()
                        .withSupplier(getSupplier())
                        .withCustomer(getCustomer())
                        .withCurrency("PEN")
                        .withNumber("F001-000156")
                        .withDate(LocalDate.of(2020, 8, 16))
                        .withNetTotal(new BigDecimal("100"))
                        .withCrossTotal(new BigDecimal("118"))
                        .withTax(new Taxes.IGV()
                                        .setAmount(new BigDecimal("18"))
                                        .setTaxableAmount(new BigDecimal("100")))
                        .withLines(getLines());

        final var ubl = invoice.getUBLXml();
        new Signing()
                        .withKeyStorePath("keystore.jks")
                        .withKeyStorePwd("changeit")
                        .withKeyAlias("testkey")
                        .withKeyPwd("changeit")
                        .signDocument(ubl);
    }

    @Test
    public void invoiceWithOneItemTaxAndCharge()
        throws IOException
    {

        final var lines = new ArrayList<ILine>();
        lines.add(Line.builder().withSku("123.456")
                        .withDescription("Pasta larga salsa tradicional")
                        .withQuantity(BigDecimal.ONE)
                        .withCrossUnitPrice(new BigDecimal("42.70"))
                        .withCrossPrice(new BigDecimal("42.70"))
                        .withNetUnitPrice(new BigDecimal("35"))
                        .withNetPrice(new BigDecimal("35"))
                        .withTax(new Taxes.IGV()
                                        .setAmount(new BigDecimal("6.30"))
                                        .setTaxableAmount(new BigDecimal("35")))
                        .withAllowanceCharge(new ChargeEntry()
                                        .setReason("50")
                                        .setBaseAmount(new BigDecimal("35"))
                                        .setAmount(new BigDecimal("2.80"))
                                        .setFactor(new BigDecimal("0.08")))
                        .build());

        final var invoice = new Invoice()
                        .withSupplier(getSupplier())
                        .withCustomer(getCustomer())
                        .withCurrency("PEN")
                        .withNumber("F001-000156")
                        .withDate(LocalDate.of(2020, 6, 13))
                        .withNetTotal(new BigDecimal("100"))
                        .withCrossTotal(new BigDecimal("118"))
                        .withTax(new Taxes.IGV()
                                        .setAmount(new BigDecimal("6.30"))
                                        .setTaxableAmount(new BigDecimal("35")))
                        .withAllowanceCharge(new ChargeEntry()
                                        .setReason("50")
                                        .setBaseAmount(new BigDecimal("35"))
                                        .setAmount(new BigDecimal("2.80"))
                                        .setFactor(new BigDecimal("0.08")))
                        .withLines(lines);
        final var ubl = invoice.getUBLXml();
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("Invoice1.xml").getFile());
        final var xml = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        assertEquals(ubl, xml.trim());
    }

    @Test
    public void invoiceWithPerUnitTax()
        throws IOException
    {

        final var lines = new ArrayList<ILine>();
        lines.add(Line.builder().withSku("123.456")
                        .withDescription("Agua mineral")
                        .withQuantity(BigDecimal.ONE)
                        .withCrossUnitPrice(new BigDecimal("13.23"))
                        .withCrossPrice(new BigDecimal("13.23"))
                        .withNetUnitPrice(new BigDecimal("10.50"))
                        .withNetPrice(new BigDecimal("10.50"))
                        .withTax(new Taxes.IGV()
                                        .setAmount(new BigDecimal("1.89"))
                                        .setTaxableAmount(new BigDecimal("10.50")))
                        .withAllowanceCharge(new ChargeEntry()
                                        .setReason("50")
                                        .setBaseAmount(new BigDecimal("10.50"))
                                        .setAmount(new BigDecimal("0.84"))
                                        .setFactor(new BigDecimal("0.08")))
                        .build());

        lines.add(Line.builder().withSku("333.4567")
                        .withDescription("Bolsa Plástica")
                        .withQuantity(BigDecimal.ONE)
                        .withCrossUnitPrice(new BigDecimal("0.30"))
                        .withCrossPrice(new BigDecimal("0.30"))
                        .withNetUnitPrice(new BigDecimal("0.10"))
                        .withNetPrice(new BigDecimal("0.10"))
                        .withTax(new Taxes.ICB()
                                        .setAmount(new BigDecimal("0.20"))
                                        .setTaxableAmount(new BigDecimal("1")))
                        .build());

        final var invoice = new Invoice()
                        .withSupplier(getSupplier())
                        .withCustomer(getCustomer())
                        .withCurrency("PEN")
                        .withNumber("F001-000156")
                        .withDate(LocalDate.of(2020, 6, 13))
                        .withNetTotal(new BigDecimal("10.10"))
                        .withCrossTotal(new BigDecimal("13.53"))
                        .withTax(new Taxes.IGV()
                                        .setAmount(new BigDecimal("1.89"))
                                        .setTaxableAmount(new BigDecimal("10.50")))
                        .withTax(new Taxes.ICB()
                                        .setAmount(new BigDecimal("0.20"))
                                        .setTaxableAmount(new BigDecimal("0")))
                        .withAllowanceCharge(new ChargeEntry()
                                        .setReason("50")
                                        .setBaseAmount(new BigDecimal("10.50"))
                                        .setAmount(new BigDecimal("0.84"))
                                        .setFactor(new BigDecimal("0.08")))
                        .withLines(lines);
        final var ubl = invoice.getUBLXml();
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("Invoice2.xml").getFile());
        final var xml = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        assertEquals(ubl, xml.trim());
    }

    @Test
    public void invoiceWithCashPayment()
        throws IOException
    {
        final var invoice = new Invoice()
                        .withSupplier(getSupplier())
                        .withCustomer(getCustomer())
                        .withCurrency("PEN")
                        .withNumber("F001-000156")
                        .withDate(LocalDate.of(2020, 8, 16))
                        .withNetTotal(new BigDecimal("100"))
                        .withCrossTotal(new BigDecimal("118"))
                        .withTax(new Taxes.IGV()
                                        .setAmount(new BigDecimal("18"))
                                        .setTaxableAmount(new BigDecimal("100")))
                        .withLines(getLines())
                        .withPaymentTerms(new IPaymentTerms()
                        {

                            @Override
                            public boolean isCredit()
                            {
                                return false;
                            }

                            @Override
                            public BigDecimal getTotal()
                            {
                                return new BigDecimal("512.26");
                            }

                            @Override
                            public List<IInstallment> getInstallments()
                            {
                                return null;
                            }
                        });

        final var ubl = invoice.getUBLXml();
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("Invoice4.xml").getFile());
        final var xml = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        assertEquals(ubl, xml.trim());
    }

    @Test
    public void invoiceWithInstallments()
        throws IOException
    {
        final var invoice = new Invoice()
                        .withSupplier(getSupplier())
                        .withCustomer(getCustomer())
                        .withCurrency("PEN")
                        .withNumber("F001-000156")
                        .withDate(LocalDate.of(2020, 8, 16))
                        .withNetTotal(new BigDecimal("100"))
                        .withCrossTotal(new BigDecimal("118"))
                        .withTax(new Taxes.IGV()
                                        .setAmount(new BigDecimal("18"))
                                        .setTaxableAmount(new BigDecimal("100")))
                        .withLines(getLines())
                        .withPaymentTerms(new IPaymentTerms()
                        {

                            @Override
                            public boolean isCredit()
                            {
                                return true;
                            }

                            @Override
                            public BigDecimal getTotal()
                            {
                                return new BigDecimal("512.26");
                            }

                            @Override
                            public List<IInstallment> getInstallments()
                            {
                                final List<IInstallment> ret = new ArrayList<>();
                                ret.add(new IInstallment() {

                                    @Override
                                    public BigDecimal getAmount()
                                    {
                                        return new BigDecimal("150");
                                    }

                                    @Override
                                    public LocalDate getDueDate()
                                    {
                                        return LocalDate.of(2022, 4, 1);
                                    }
                                });
                                ret.add(new IInstallment() {

                                    @Override
                                    public BigDecimal getAmount()
                                    {
                                        return new BigDecimal("250");
                                    }

                                    @Override
                                    public LocalDate getDueDate()
                                    {
                                        return LocalDate.of(2022, 5, 1);
                                    }
                                });
                                return ret;
                            }
                        });

        final var ubl = invoice.getUBLXml();
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("Invoice5.xml").getFile());
        final var xml = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        assertEquals(ubl, xml.trim());
    }

    @Test
    public void createCreditNote()
        throws DatatypeConfigurationException
    {
        final var creditNOte = new CreditNote()
                        .withReference(new Reference()
                                        .setNumber("F001-000156")
                                        .setDate(LocalDate.of(2020, 6, 12))
                                        .setDocType("01"))
                        .withSupplier(getSupplier())
                        .withCustomer(getCustomer())
                        .withCurrency("PEN")
                        .withNumber("F001-000156")
                        .withDate(LocalDate.of(2020, 8, 16))
                        .withNetTotal(new BigDecimal("100"))
                        .withCrossTotal(new BigDecimal("118"))
                        .withTax(new Taxes.IGV()
                                        .setAmount(new BigDecimal("18"))
                                        .setTaxableAmount(new BigDecimal("100")))
                        .withLines(getLines());

        final var ubl = creditNOte.getUBLXml();
        new Signing()
                        .withKeyStorePath("keystore.jks")
                        .withKeyStorePwd("changeit")
                        .withKeyAlias("testkey")
                        .withKeyPwd("changeit")
                        .signDocument(ubl);
    }

    @Test
    public void creditNoteWithOneItemTaxAndCharge()
        throws IOException
    {

        final var lines = new ArrayList<ILine>();
        lines.add(Line.builder().withSku("123.456")
                        .withDescription("Pasta larga salsa tradicional")
                        .withQuantity(BigDecimal.ONE)
                        .withCrossUnitPrice(new BigDecimal("42.70"))
                        .withCrossPrice(new BigDecimal("42.70"))
                        .withNetUnitPrice(new BigDecimal("35"))
                        .withNetPrice(new BigDecimal("35"))
                        .withTax(new Taxes.IGV()
                                        .setAmount(new BigDecimal("6.30"))
                                        .setTaxableAmount(new BigDecimal("35")))
                        .withAllowanceCharge(new ChargeEntry()
                                        .setReason("50")
                                        .setBaseAmount(new BigDecimal("35"))
                                        .setAmount(new BigDecimal("2.80"))
                                        .setFactor(new BigDecimal("0.08")))
                        .build());

        final var invoice = new CreditNote()
                        .withReference(new Reference()
                                        .setNumber("F001-000156")
                                        .setDate(LocalDate.of(2020, 6, 12))
                                        .setDocType("01"))
                        .withSupplier(getSupplier())
                        .withCustomer(getCustomer())
                        .withCurrency("PEN")
                        .withNumber("NF01-000156")
                        .withDate(LocalDate.of(2020, 6, 13))
                        .withNetTotal(new BigDecimal("100"))
                        .withCrossTotal(new BigDecimal("118"))
                        .withTax(new Taxes.IGV()
                                        .setAmount(new BigDecimal("6.30"))
                                        .setTaxableAmount(new BigDecimal("35")))
                        .withAllowanceCharge(new ChargeEntry()
                                        .setReason("50")
                                        .setBaseAmount(new BigDecimal("35"))
                                        .setAmount(new BigDecimal("2.80"))
                                        .setFactor(new BigDecimal("0.08")))
                        .withLines(lines);
        final var ubl = invoice.getUBLXml();
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("CreditNote1.xml").getFile());
        final var xml = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        assertEquals(ubl, xml.trim());
    }

    @Test
    public void createDeliveryNote()
        throws IOException
    {
        final var lines = new ArrayList<ILine>();
        lines.add(Line.builder().withSku("123.456")
                        .withDescription("Pasta larga salsa tradicional")
                        .withQuantity(BigDecimal.ONE)
                        .withAdditionalItemProperties(Collections.singletonList(() -> ItemPropertyType.NORMALIZED))
                        .build());

        final var shipment = new Shipment()
                        .withHandlingCode("01")
                        .withHandlingInstructions("Handle with care")
                        .withCrossWeight(new BigDecimal("13.5"))
                        .withCrossWeightUoM("KGM")
                        .addInstruction("SUNAT_Envio_IndicadorVehiculoConductoresTransp")
                        .addStage(new Stage()
                                        .withMode("01")
                                        .withStartDate(LocalDate.of(2023, 6, 13))
                                        .withCarrier(getCarrier())
                                        .withDriver(getDriver()))
                        .withDelivery(new Delivery()
                                        .withDeliveryAddress(getCustomer())
                                        .withDespatchAddress(new IAddress()
                                        {

                                            @Override
                                            public String getCountry()
                                            {
                                                return null;
                                            }

                                            @Override
                                            public String getAddressLine()
                                            {
                                                return getCustomer().getAddressLine();
                                            }
                                        }));

        final var deliveryNote = new DeliveryNote()
                        .withNumber("T001-000156")
                        .withDate(LocalDate.of(2023, 6, 13))
                        .withTime(LocalTime.of(15, 11))
                        .withSupplier(getSupplier())
                        .withCustomer(getCustomer())
                        .withLines(lines)
                        .withShipment(shipment);

        final var ubl = deliveryNote.getUBLXml();
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("DeliveryNote1.xml").getFile());
        final var xml = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        assertEquals(ubl, xml.trim());
    }

}
