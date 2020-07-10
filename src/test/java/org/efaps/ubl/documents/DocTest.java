package org.efaps.ubl.documents;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.xml.datatype.DatatypeConfigurationException;

import org.efaps.ubl.Signing;
import org.testng.annotations.Test;

public class DocTest
{

    @Test
    public void createInvoice() throws DatatypeConfigurationException
    {
        final var invoice = new Invoice()
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
