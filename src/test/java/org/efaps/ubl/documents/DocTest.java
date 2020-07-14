package org.efaps.ubl.documents;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.commons.io.FileUtils;
import org.efaps.ubl.Signing;
import org.efaps.ubl.documents.Charges.ChargeEntry;
import org.testng.annotations.Test;

public class DocTest
{

    private Supplier getSupplier() {
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

    private Customer getCustomer() {
        final var ret = new Customer();
        ret.setCountry("PE");
        ret.setDOI("43289672");
        ret.setDoiType("1");
        ret.setName("ovar Lopez, Julio Odair");
        ret.setAddressLine("Av parque alto 291-A Lima - Lima - Santiago De Surco");
        return ret;
    }

    @Test
    public void createInvoice() throws DatatypeConfigurationException
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
            .withKeyStorePath(".keystore")
            .withKeyStorePwd("changeit")
            .withKeyAlias("mykey")
            .signInvoice(ubl);
    }

    private List<ILine> getLines()
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
    public void invoiceWithOneItemTaxAndCharge() throws IOException {

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
                        .withCharge(new ChargeEntry()
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
                        .withCharge(new ChargeEntry()
                                        .setReason("50")
                                        .setBaseAmount(new BigDecimal("35"))
                                        .setAmount(new BigDecimal("2.80"))
                                        .setFactor(new BigDecimal("0.08")))
                        .withLines(lines);
        final var ubl = invoice.getUBLXml();
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("Invoice1.xml").getFile());
        final var xml = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        assertEquals(ubl,  xml);
    }

    @Test
    public void invoiceWithPerUnitTax() throws IOException {

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
                        .withCharge(new ChargeEntry()
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
                                        .setTaxableAmount(new BigDecimal("0")))
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
                        .withCharge(new ChargeEntry()
                                        .setReason("50")
                                        .setBaseAmount(new BigDecimal("10.50"))
                                        .setAmount(new BigDecimal("0.84"))
                                        .setFactor(new BigDecimal("0.08")))
                        .withLines(lines);
        final var ubl = invoice.getUBLXml();
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("Invoice2.xml").getFile());
        final var xml = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        assertEquals(ubl,  xml);
    }
}
