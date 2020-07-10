package org.efaps.ubl.documents;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.xml.datatype.DatatypeConfigurationException;

import org.efaps.ubl.Signing;
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
                        .withName("F001-000156")
                        .withDate(LocalDate.of(2020, 8, 16))
                        .withNetTotal(new BigDecimal("100"))
                        .withCrossTotal(new BigDecimal("118"))
                        .withTax(new Taxes.IGV()
                                        .setAmount(new BigDecimal("18"))
                                        .setTaxableAmount(new BigDecimal("100")));

        final var ubl = invoice.getUBL();
        new Signing()
            .withKeyStorePath(".keystore")
            .withKeyStorePwd("changeit")
            .withKeyAlias("mykey")
            .signInvoice(ubl);
    }
}
