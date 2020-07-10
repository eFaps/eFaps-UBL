package org.efaps.ubl.documents;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.time.LocalDate;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.efaps.ubl.Signing;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

public class DocTest
{

    @Test
    public void createInvoice()
        throws DatatypeConfigurationException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
        UnrecoverableEntryException, KeyStoreException, CertificateException, FileNotFoundException, IOException,
        SAXException, ParserConfigurationException, MarshalException, XMLSignatureException, XPathExpressionException, TransformerException
    {
        final var invoice = new Invoice()
                        .withCurrency("PEN")
                        .withName("F001-000156")
                        .withDate(LocalDate.of(2020, 8, 16))
                        .withNetTotal(new BigDecimal("100"))
                        .withCrossTotal(new BigDecimal("118"));

        final var ubl = invoice.getUBL();
        System.out.println(ubl);
        new Signing().sign(ubl);
    }
}
