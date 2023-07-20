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

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.efaps.ubl.Signing;
import org.efaps.ubl.documents.elements.Taxes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.helger.xmldsig.XMLDSigValidator;

public class SignTest
{
    private static final Logger LOG = LoggerFactory.getLogger(SignTest.class);

    @Test
    public void testSignature()
        throws Exception
    {
        final var invoice = new Invoice()
                        .withSupplier(DocTest.getSupplier())
                        .withCustomer(DocTest.getCustomer())
                        .withCurrency("PEN")
                        .withNumber("F001-000156")
                        .withDate(LocalDate.of(2020, 8, 16))
                        .withNetTotal(new BigDecimal("100"))
                        .withCrossTotal(new BigDecimal("118"))
                        .withTax(new Taxes.IGV()
                                        .setAmount(new BigDecimal("18"))
                                        .setTaxableAmount(new BigDecimal("100")))
                        .withLines(DocTest.getLines());

        final var ubl = invoice.getUBLXml();
        final var signResponseDto = new Signing()
                        .withKeyStorePath("keystore.jks")
                        .withKeyStorePwd("changeit")
                        .withKeyAlias("testkey")
                        .withKeyPwd("changeit")
                        .signInvoice(ubl);
        final var signed = signResponseDto.getUbl();
        LOG.info("Signed xml: {}", signed);

        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        final Document doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(signed.getBytes()));
        final var validation = XMLDSigValidator.validateSignature(doc);
        assertTrue(validation.isSignatureValid());
    }

    @Test
    public void verifySignature1()
        throws IOException, SAXException, ParserConfigurationException, XMLSignatureException
    {
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("InvoiceWithSignature1.xml").getFile());
        final var xml = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        final Document doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
        final var validation = XMLDSigValidator.validateSignature(doc);
        assertTrue(validation.isSignatureValid());
    }

    @Test
    public void signInvoiceFromFile()
        throws IOException, SAXException, ParserConfigurationException, XMLSignatureException
    {
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("Invoice3.xml").getFile());
        final var xml = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        final var signResponseDto = new Signing()
                        .withKeyStorePath("keystore.jks")
                        .withKeyStorePwd("changeit")
                        .withKeyAlias("testkey")
                        .withKeyPwd("changeit")
                        .signDocument(xml);
        final var signed = signResponseDto.getUbl();
        assertNotNull(signed);

        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        final Document doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(signed.getBytes()));
        final var validation = XMLDSigValidator.validateSignature(doc);
        assertTrue(validation.isSignatureValid());
    }

    @Test
    public void signDeliveryNoteFromFile()
        throws IOException, SAXException, ParserConfigurationException, XMLSignatureException
    {
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("DeliveryNote1.xml").getFile());
        final var xml = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        final var signResponseDto = new Signing()
                        .withKeyStorePath("keystore.jks")
                        .withKeyStorePwd("changeit")
                        .withKeyAlias("testkey")
                        .withKeyPwd("changeit")
                        .signDocument(xml);
        final var signed = signResponseDto.getUbl();
        assertNotNull(signed);

        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        final Document doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(signed.getBytes()));
        final var validation = XMLDSigValidator.validateSignature(doc);
        assertTrue(validation.isSignatureValid());
    }
}
