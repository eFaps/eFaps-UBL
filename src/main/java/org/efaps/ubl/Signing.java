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
package org.efaps.ubl;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBElement;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.efaps.ubl.builder.CreditNoteBuilder;
import org.efaps.ubl.builder.DeliveryNoteBuilder;
import org.efaps.ubl.builder.InvoiceBuilder;
import org.efaps.ubl.builder.SummaryBuilder;
import org.efaps.ubl.dto.SignResponseDto;
import org.efaps.ubl.extension.Definitions;
import org.efaps.ubl.reader.CreditNoteReader;
import org.efaps.ubl.reader.DeliveryNoteReader;
import org.efaps.ubl.reader.InvoiceReader;
import org.efaps.ubl.reader.SummaryReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.helger.ubl21.CUBL21;
import com.helger.ubl21.UBL21NamespaceContext;
import com.helger.xsds.xmldsig.CanonicalizationMethodType;
import com.helger.xsds.xmldsig.DigestMethodType;
import com.helger.xsds.xmldsig.KeyInfoType;
import com.helger.xsds.xmldsig.ReferenceType;
import com.helger.xsds.xmldsig.SignatureMethodType;
import com.helger.xsds.xmldsig.SignatureType;
import com.helger.xsds.xmldsig.SignatureValueType;
import com.helger.xsds.xmldsig.SignedInfoType;
import com.helger.xsds.xmldsig.TransformType;
import com.helger.xsds.xmldsig.TransformsType;
import com.helger.xsds.xmldsig.X509DataType;

import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_21.ExtensionContentType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_21.UBLExtensionType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_21.UBLExtensionsType;

public class Signing
{

    private static final Logger LOG = LoggerFactory.getLogger(Signing.class);
    private String keyStorePath;
    private String keyStorePwd;
    private String keyAlias;
    private String keyPwd;

    public Signing withKeyStorePath(final String keyStorePath)
    {
        this.keyStorePath = keyStorePath;
        return this;
    }

    public Signing withKeyStorePwd(final String keyStorePwd)
    {
        this.keyStorePwd = keyStorePwd;
        return this;
    }

    public Signing withKeyAlias(final String keyAlias)
    {
        this.keyAlias = keyAlias;
        return this;
    }

    public Signing withKeyPwd(final String keyPwd)
    {
        this.keyPwd = keyPwd;
        return this;
    }

    public boolean verify()
    {
        return getPrivateKey() != null && getCertificate() != null;
    }

    public SignResponseDto signInvoiceV1(final String xml)
    {
        SignResponseDto ret = null;
        try {
            final var xmlSignatureFactory = XMLSignatureFactory.getInstance();
            final var ref = xmlSignatureFactory.newReference("",
                            xmlSignatureFactory.newDigestMethod(DigestMethod.SHA1, null),
                            Collections.singletonList(
                                            xmlSignatureFactory.newTransform(Transform.ENVELOPED,
                                                            (TransformParameterSpec) null)),
                            null, null);
            final var info = xmlSignatureFactory.newSignedInfo(
                            xmlSignatureFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,
                                            (C14NMethodParameterSpec) null),
                            xmlSignatureFactory.newSignatureMethod("http://www.w3.org/2000/09/xmldsig#rsa-sha1", null),
                            Collections.singletonList(ref));

            final var invoice = new InvoiceReader().read(xml);
            final var extension = new UBLExtensionType();
            final var extensionContent = new ExtensionContentType();
            extension.setExtensionContent(extensionContent);
            if (invoice.getUBLExtensions() == null) {
                invoice.setUBLExtensions(new UBLExtensionsType());
            }
            invoice.getUBLExtensions().addUBLExtension(extension);

            final var doc = new InvoiceBuilder().setCharset(StandardCharsets.UTF_8)
                            .setUseSchema(false)
                            .setFormattedOutput(false).getAsDocument(invoice);

            final var certificate = getCertificate();
            final KeyInfoFactory kif = xmlSignatureFactory.getKeyInfoFactory();
            final var x509Content = new ArrayList<>();
            x509Content.add(certificate.getSubjectX500Principal().getName());
            x509Content.add(certificate);
            final X509Data xd = kif.newX509Data(x509Content);
            final KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));

            final DOMSignContext dsc = new DOMSignContext(getPrivateKey(), doc.getDocumentElement().getFirstChild());

            final XMLSignature signature = xmlSignatureFactory.newXMLSignature(info, ki);
            signature.sign(dsc);

            final var signatureT = new SignatureType();

            final var ele = new JAXBElement<>(new QName("http://www.w3.org/2000/09/xmldsig#", "Signature"),
                            SignatureType.class, signatureT);
            extensionContent.setAny(ele);
            signatureT.setId("SB001-000095");
            final var signatureValue = new SignatureValueType();
            signatureValue.setValue(signature.getSignatureValue().getValue());
            signatureT.setSignatureValue(signatureValue);
            final var signedInfo = new SignedInfoType();
            signatureT.setSignedInfo(signedInfo);

            final var canonicalizationMethod = new CanonicalizationMethodType();
            canonicalizationMethod.setAlgorithm(signature.getSignedInfo().getCanonicalizationMethod().getAlgorithm());
            signedInfo.setCanonicalizationMethod(canonicalizationMethod);

            final var signatureMethod = new SignatureMethodType();
            signatureMethod.setAlgorithm(signature.getSignedInfo().getSignatureMethod().getAlgorithm());
            signedInfo.setSignatureMethod(signatureMethod);

            final var reference = new ReferenceType();
            final var digestMethod = new DigestMethodType();
            digestMethod.setAlgorithm(
                            signature.getSignedInfo().getReferences().get(0).getDigestMethod().getAlgorithm());
            reference.setDigestMethod(digestMethod);
            final var transforms = new TransformsType();
            final var transform = new TransformType();
            transform.setAlgorithm(
                            signature.getSignedInfo().getReferences().get(0).getTransforms().get(0).getAlgorithm());
            transforms.setTransform(Collections.singletonList(transform));
            reference.setTransforms(transforms);
            final var hash = signature.getSignedInfo().getReferences().get(0).getDigestValue();
            reference.setDigestValue(signature.getSignedInfo().getReferences().get(0).getDigestValue());

            signedInfo.setReference(Collections.singletonList(reference));

            final var keyInfo = new KeyInfoType();

            final var x509Data = new X509DataType();
            final var x509DataEle = new JAXBElement<>(
                            new QName("http://www.w3.org/2000/09/xmldsig#", "X509Data"), X509DataType.class, x509Data);
            keyInfo.setContent(Collections.singletonList(x509DataEle));

            final var X509SubjectNameEle = new JAXBElement<>(
                            new QName("http://www.w3.org/2000/09/xmldsig#", "X509SubjectName"), String.class,
                            certificate.getSubjectX500Principal().getName());
            x509Data.addX509IssuerSerialOrX509SKIOrX509SubjectName(X509SubjectNameEle);

            final var X509CertificateEle = new JAXBElement<>(
                            new QName("http://www.w3.org/2000/09/xmldsig#", "X509Certificate"), byte[].class,
                            certificate.getPublicKey().getEncoded());
            x509Data.addX509IssuerSerialOrX509SKIOrX509SubjectName(X509CertificateEle);

            signatureT.setKeyInfo(keyInfo);

            final var ubl = new InvoiceBuilder().setCharset(StandardCharsets.UTF_8)
                            .setFormattedOutput(false).getAsString(invoice);

            ret = SignResponseDto.builder()
                            .withUbl(ubl)
                            .withHash(Base64.getEncoder().encodeToString(hash))
                            .build();

        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | MarshalException
                        | XMLSignatureException e) {
            LOG.error("Catched", e);
        }
        return ret;
    }

    public SignResponseDto signDocument(final String xml)
    {
        SignResponseDto ret = null;
        var xml2 = "";
        try {
            if (xml.contains("<CreditNote ")) {
                final var creditNote = new CreditNoteReader().read(xml);
                final var extension = new UBLExtensionType();
                final var extensionContent = new ExtensionContentType();
                extension.setExtensionContent(extensionContent);
                if (creditNote.getUBLExtensions() == null) {
                    creditNote.setUBLExtensions(new UBLExtensionsType());
                }
                creditNote.getUBLExtensions().addUBLExtension(extension);
                if (!UBL21NamespaceContext.getInstance().getPrefixToNamespaceURIMap().containsKey("sac")) {
                    UBL21NamespaceContext.getInstance().addMapping("sac", Definitions.NAMESPACE_SUNATAGGREGATE);
                    UBL21NamespaceContext.getInstance().removeMapping("cec");
                    UBL21NamespaceContext.getInstance().addMapping("ext", CUBL21.XML_SCHEMA_CEC_NAMESPACE_URL);
                }

                xml2 = new CreditNoteBuilder().setCharset(StandardCharsets.UTF_8)
                                .setUseSchema(false)
                                .setFormattedOutput(true).getAsString(creditNote);
            } else if (xml.contains("<DespatchAdvice ")) {
                final var deliveryNote = new DeliveryNoteReader().read(xml);
                final var extension = new UBLExtensionType();
                final var extensionContent = new ExtensionContentType();
                extension.setExtensionContent(extensionContent);
                if (deliveryNote.getUBLExtensions() == null) {
                    deliveryNote.setUBLExtensions(new UBLExtensionsType());
                }
                deliveryNote.getUBLExtensions().addUBLExtension(extension);
                if (!UBL21NamespaceContext.getInstance().getPrefixToNamespaceURIMap().containsKey("sac")) {
                    UBL21NamespaceContext.getInstance().addMapping("sac", Definitions.NAMESPACE_SUNATAGGREGATE);
                    UBL21NamespaceContext.getInstance().removeMapping("cec");
                    UBL21NamespaceContext.getInstance().addMapping("ext", CUBL21.XML_SCHEMA_CEC_NAMESPACE_URL);
                }
                xml2 = new DeliveryNoteBuilder().setCharset(StandardCharsets.UTF_8)
                                .setUseSchema(false)
                                .setFormattedOutput(true).getAsString(deliveryNote);
            } else if (xml.contains("<SummaryDocuments ")) {
                final var summary = new SummaryReader().read(xml);
                final var extension = new UBLExtensionType();
                final var extensionContent = new ExtensionContentType();
                extension.setExtensionContent(extensionContent);
                if (summary.getUBLExtensions() == null) {
                    summary.setUBLExtensions(new UBLExtensionsType());
                }
                summary.getUBLExtensions().addUBLExtension(extension);
                xml2 = new SummaryBuilder().setCharset(StandardCharsets.UTF_8)
                                .setUseSchema(false)
                                .setFormattedOutput(true).getAsString(summary);
            } else {
                final var invoice = new InvoiceReader().read(xml);
                final var extension = new UBLExtensionType();
                final var extensionContent = new ExtensionContentType();
                extension.setExtensionContent(extensionContent);
                if (invoice.getUBLExtensions() == null) {
                    invoice.setUBLExtensions(new UBLExtensionsType());
                }
                invoice.getUBLExtensions().addUBLExtension(extension);
                if (!UBL21NamespaceContext.getInstance().getPrefixToNamespaceURIMap().containsKey("sac")) {
                    UBL21NamespaceContext.getInstance().addMapping("sac", Definitions.NAMESPACE_SUNATAGGREGATE);
                    UBL21NamespaceContext.getInstance().removeMapping("cec");
                    UBL21NamespaceContext.getInstance().addMapping("ext", CUBL21.XML_SCHEMA_CEC_NAMESPACE_URL);
                }

                xml2 = new InvoiceBuilder().setCharset(StandardCharsets.UTF_8)
                                .setUseSchema(false)
                                .setFormattedOutput(true).getAsString(invoice);
            }
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            final DocumentBuilder builder = dbf.newDocumentBuilder();
            final var doc = builder.parse(new ByteArrayInputStream(xml2.getBytes(StandardCharsets.UTF_8)));
            final XPathFactory factory = XPathFactory.newInstance();
            final XPath xPath = factory.newXPath();
            xPath.setNamespaceContext(new UniversalNamespaceResolver(doc));

            if (doc != null) {
                final NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']", doc,
                                XPathConstants.NODESET);
                for (int i = 0; i < nodeList.getLength(); i++) {
                    final Node nodeO = nodeList.item(i);
                    final Node nodeN = nodeO.cloneNode(true);
                    nodeN.setNodeValue(nodeO.getNodeValue().replaceAll("(\\t|\\ )", ""));
                    nodeO.getParentNode().replaceChild(nodeN, nodeO);
                }
            }

            final var signatureFactory = XMLSignatureFactory.getInstance("DOM");
            final var ref = signatureFactory.newReference("",
                            signatureFactory.newDigestMethod("http://www.w3.org/2000/09/xmldsig#sha1", null),
                            Collections.singletonList(
                                            signatureFactory.newTransform(
                                                            "http://www.w3.org/2000/09/xmldsig#enveloped-signature",
                                                            (TransformParameterSpec) null)),
                            null, null);
            final var signedInfo = signatureFactory.newSignedInfo(
                            signatureFactory.newCanonicalizationMethod(
                                            "http://www.w3.org/TR/2001/REC-xml-c14n-20010315",
                                            (C14NMethodParameterSpec) null),
                            signatureFactory.newSignatureMethod("http://www.w3.org/2000/09/xmldsig#rsa-sha1", null),
                            Collections.singletonList(ref));

            final var x509Content = new ArrayList<>();
            final var cert = getCertificate();
            x509Content.add(cert.getSubjectX500Principal().getName());
            x509Content.add(cert);

            final var keyInfoFactory = signatureFactory.getKeyInfoFactory();
            final X509Data xd = keyInfoFactory.newX509Data(x509Content);
            final KeyInfo ki = keyInfoFactory.newKeyInfo(Collections.singletonList(xd));

            final DOMSignContext dsc = new DOMSignContext(getPrivateKey(), getNodeToSign(doc, xPath));
            final XMLSignature signature = signatureFactory.newXMLSignature(signedInfo, ki);
            dsc.setDefaultNamespacePrefix("ds");
            signature.sign(dsc);

            final var hash = signature.getSignedInfo().getReferences().get(0).getDigestValue();

            final var idReference = getSignReference(doc, xPath);
            final Element elementParent = (Element) dsc.getParent();
            if (idReference != null && elementParent.getElementsByTagName("ds:Signature") != null) {
                final Element elementSignature = (Element) elementParent.getElementsByTagName("ds:Signature").item(0);
                elementSignature.setAttribute("Id", idReference);
            }

            final DOMSource source = new DOMSource(doc);
            final StringWriter out = new StringWriter();
            final StreamResult result = new StreamResult(out);
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            final Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty("encoding", "UTF-8");
            transformer.transform(source, result);

            ret = SignResponseDto.builder()
                            .withUbl(out.toString())
                            .withHash(Base64.getEncoder().encodeToString(hash))
                            .build();
        } catch (final NoSuchAlgorithmException | ParserConfigurationException | SAXException | IOException
                        | InvalidAlgorithmParameterException | MarshalException | XMLSignatureException
                        | XPathExpressionException | TransformerException e) {
            LOG.error("Catched", e);
        }
        return ret;
    }

    public SignResponseDto signInvoice(final String xml)
    {
        SignResponseDto ret = null;
        try {
            final var invoice = new InvoiceReader().read(xml);
            final var extension = new UBLExtensionType();
            final var extensionContent = new ExtensionContentType();
            extension.setExtensionContent(extensionContent);
            if (invoice.getUBLExtensions() == null) {
                invoice.setUBLExtensions(new UBLExtensionsType());
            }
            invoice.getUBLExtensions().addUBLExtension(extension);

            if (!UBL21NamespaceContext.getInstance().getPrefixToNamespaceURIMap().containsKey("sac")) {
                UBL21NamespaceContext.getInstance().addMapping("sac", Definitions.NAMESPACE_SUNATAGGREGATE);
                UBL21NamespaceContext.getInstance().removeMapping("cec");
                UBL21NamespaceContext.getInstance().addMapping("ext", CUBL21.XML_SCHEMA_CEC_NAMESPACE_URL);
            }

            final var xml2 = new InvoiceBuilder().setCharset(StandardCharsets.UTF_8)
                            .setUseSchema(false)
                            .setFormattedOutput(true).getAsString(invoice);

            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            final DocumentBuilder builder = dbf.newDocumentBuilder();
            final var doc = builder.parse(new ByteArrayInputStream(xml2.getBytes(StandardCharsets.UTF_8)));
            final XPathFactory factory = XPathFactory.newInstance();
            final XPath xPath = factory.newXPath();
            xPath.setNamespaceContext(new UniversalNamespaceResolver(doc));

            if (doc != null) {
                final NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']", doc,
                                XPathConstants.NODESET);
                for (int i = 0; i < nodeList.getLength(); i++) {
                    final Node nodeO = nodeList.item(i);
                    final Node nodeN = nodeO.cloneNode(true);
                    nodeN.setNodeValue(nodeO.getNodeValue().replaceAll("(\\t|\\ )", ""));
                    nodeO.getParentNode().replaceChild(nodeN, nodeO);
                }
            }

            final var signatureFactory = XMLSignatureFactory.getInstance("DOM");
            final var ref = signatureFactory.newReference("",
                            signatureFactory.newDigestMethod("http://www.w3.org/2000/09/xmldsig#sha1", null),
                            Collections.singletonList(
                                            signatureFactory.newTransform(
                                                            "http://www.w3.org/2000/09/xmldsig#enveloped-signature",
                                                            (TransformParameterSpec) null)),
                            null, null);
            final var signedInfo = signatureFactory.newSignedInfo(
                            signatureFactory.newCanonicalizationMethod(
                                            "http://www.w3.org/TR/2001/REC-xml-c14n-20010315",
                                            (C14NMethodParameterSpec) null),
                            signatureFactory.newSignatureMethod("http://www.w3.org/2000/09/xmldsig#rsa-sha1", null),
                            Collections.singletonList(ref));

            final var x509Content = new ArrayList<>();
            final var cert = getCertificate();
            x509Content.add(cert.getSubjectX500Principal().getName());
            x509Content.add(cert);

            final var keyInfoFactory = signatureFactory.getKeyInfoFactory();
            final X509Data xd = keyInfoFactory.newX509Data(x509Content);
            final KeyInfo ki = keyInfoFactory.newKeyInfo(Collections.singletonList(xd));

            final DOMSignContext dsc = new DOMSignContext(getPrivateKey(), getNodeToSign(doc, xPath));
            final XMLSignature signature = signatureFactory.newXMLSignature(signedInfo, ki);
            dsc.setDefaultNamespacePrefix("ds");
            signature.sign(dsc);

            final var hash = signature.getSignedInfo().getReferences().get(0).getDigestValue();

            final var idReference = getSignReference(doc, xPath);
            final Element elementParent = (Element) dsc.getParent();
            if (idReference != null && elementParent.getElementsByTagName("ds:Signature") != null) {
                final Element elementSignature = (Element) elementParent.getElementsByTagName("ds:Signature").item(0);
                elementSignature.setAttribute("Id", idReference);
            }

            final DOMSource source = new DOMSource(doc);
            final StringWriter out = new StringWriter();
            final StreamResult result = new StreamResult(out);
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            final Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty("encoding", "UTF-8");
            transformer.transform(source, result);

            ret = SignResponseDto.builder()
                            .withUbl(out.toString())
                            .withHash(Base64.getEncoder().encodeToString(hash))
                            .build();
        } catch (final NoSuchAlgorithmException | ParserConfigurationException | SAXException | IOException
                        | InvalidAlgorithmParameterException | MarshalException | XMLSignatureException
                        | XPathExpressionException | TransformerException e) {
            LOG.error("Catched", e);
        }
        return ret;
    }

    protected Node getNodeToSign(final Document doc, final XPath xpath)
        throws XPathExpressionException
    {
        final var nodeToSign = (Node) xpath.evaluate(
                        "//ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent", doc,
                        XPathConstants.NODE);
        if (nodeToSign == null) {

        }
        return nodeToSign;
    }

    protected String getSignReference(final Document doc, final XPath xpath)
        throws XPathExpressionException
    {
        final String value = (String) xpath.evaluate(
                        "//cac:Signature/cac:DigitalSignatureAttachment/cac:ExternalReference/cbc:URI", doc,
                        XPathConstants.STRING);
        return value;
    }

    protected X509Certificate getCertificate()
    {
        return (X509Certificate) getKeyEntry().getCertificate();
    }

    protected PrivateKey getPrivateKey()
    {
        return getKeyEntry().getPrivateKey();
    }

    protected PrivateKeyEntry getKeyEntry()
    {
        PrivateKeyEntry ret = null;
        try {
            final KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(getKeyStorePath()), getKeyStorePwd().toCharArray());
            ret = (KeyStore.PrivateKeyEntry) ks.getEntry(getKeyAlias(),
                            new KeyStore.PasswordProtection(getKeyPwd().toCharArray()));
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | UnrecoverableEntryException
                        | IOException e) {
            LOG.error("Catched", e);
        }
        return ret;
    }

    protected String getKeyStorePwd()
    {
        return keyStorePwd;
    }

    protected String getKeyAlias()
    {
        return keyAlias;
    }

    protected String getKeyStorePath()
    {
        return keyStorePath;
    }

    protected String getKeyPwd()
    {
        return keyPwd;
    }

    public static class UniversalNamespaceResolver
        implements NamespaceContext
    {

        private final Document sourceDocument;

        public UniversalNamespaceResolver(final Document document)
        {
            sourceDocument = document;
        }

        @Override
        public String getNamespaceURI(final String prefix)
        {
            if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
                return sourceDocument.lookupNamespaceURI(null);
            } else {
                return sourceDocument.lookupNamespaceURI(prefix);
            }
        }

        @Override
        public String getPrefix(final String namespaceURI)
        {
            return sourceDocument.lookupPrefix(namespaceURI);
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public Iterator getPrefixes(final String namespaceURI)
        {
            return null;
        }
    }
}
