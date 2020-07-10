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
package org.efaps.ubl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignedInfo;
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
import javax.xml.namespace.QName;
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

import org.efaps.ubl.extension.AdditionalInformation;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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

public class Signing
{

    public void sign(final String xml)
        throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, UnrecoverableEntryException,
        KeyStoreException, CertificateException, FileNotFoundException, IOException, SAXException,
        ParserConfigurationException, MarshalException, XMLSignatureException, XPathExpressionException, TransformerException
    {
        final XMLSignatureFactory fac = XMLSignatureFactory.getInstance();
        final Reference ref = fac.newReference("", fac.newDigestMethod(DigestMethod.SHA1, null),
                        Collections.singletonList(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)),
                        null, null);

        final SignedInfo si = fac.newSignedInfo(
                        fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
                        fac.newSignatureMethod("http://www.w3.org/2000/09/xmldsig#rsa-sha1", null),
                        Collections.singletonList(ref));
        System.out.println(si);




        final var invoice = new Reader().read(xml);
        final var extension = new UBLExtensionType();
        final var extensionContent = new ExtensionContentType();
        extension.setExtensionContent(extensionContent);
        invoice.getUBLExtensions().addUBLExtension(extension);

        extensionContent.setAny(new AdditionalInformation());

        final var doc = new Builder().setCharset(StandardCharsets.UTF_8)
                        .setFormattedOutput(false).getAsDocument(invoice);

        final TransformerFactory tf = TransformerFactory.newInstance();
        final Transformer transformer = tf.newTransformer();
        final StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        final String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
        System.out.println(output);

        final XPath xPath = XPathFactory.newInstance().newXPath();
        final var tmm=  (NodeList) xPath.compile("//sac:AdditionalInformation").evaluate(doc.getFirstChild(), XPathConstants.NODESET);

        System.out.println(invoice);
        System.out.println(tmm.getLength());
        Node signParentNode = null;
        final var root = doc.getFirstChild();
        final NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            final Node childNode = children.item(i);
            if (childNode.getLocalName().equals("UBLExtensions")) {
                final var lastChild = childNode.getLastChild();
                signParentNode = lastChild.getFirstChild();
                break;
            }
        }
System.out.println(signParentNode);
signParentNode.removeChild(signParentNode.getFirstChild());


        final KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(".keystore"), "changeit".toCharArray());
        final KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry("mykey",
                        new KeyStore.PasswordProtection("changeit".toCharArray()));
        final X509Certificate cert = (X509Certificate) keyEntry.getCertificate();
        // Create the KeyInfo containing the X509Data.
        final KeyInfoFactory kif = fac.getKeyInfoFactory();
        final List x509Content = new ArrayList();
        x509Content.add(cert.getSubjectX500Principal().getName());
        x509Content.add(cert);
        final X509Data xd = kif.newX509Data(x509Content);
        final KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));

        final DOMSignContext dsc = new DOMSignContext(keyEntry.getPrivateKey(), doc.getDocumentElement());

        final XMLSignature signature = fac.newXMLSignature(si, ki);
        signature.sign(dsc);

        final var signatureT = new SignatureType();

        final var ele = new JAXBElement<SignatureType>(new QName("http://www.w3.org/2000/09/xmldsig#", "Signature" ), SignatureType.class, signatureT);
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
        digestMethod.setAlgorithm(signature.getSignedInfo().getReferences().get(0).getDigestMethod().getAlgorithm());
        reference.setDigestMethod(digestMethod);
        final var transforms = new TransformsType();
        final var transform = new TransformType();
        transform.setAlgorithm(signature.getSignedInfo().getReferences().get(0).getTransforms().get(0).getAlgorithm());
        transforms.setTransform(Collections.singletonList(transform));
        reference.setTransforms(transforms);
        reference.setDigestValue(signature.getSignedInfo().getReferences().get(0).getDigestValue());

        signedInfo.setReference(Collections.singletonList(reference));

        final var keyInfo = new KeyInfoType();

        final var x509Data = new X509DataType();
        final var x509DataEle = new JAXBElement<X509DataType>(new QName("http://www.w3.org/2000/09/xmldsig#", "X509Data" ), X509DataType.class, x509Data);
        keyInfo.setContent(Collections.singletonList(x509DataEle));

        final var X509SubjectNameEle = new JAXBElement<String>(new QName("http://www.w3.org/2000/09/xmldsig#", "X509SubjectName" ), String.class, cert.getSubjectX500Principal().getName());
        x509Data.addX509IssuerSerialOrX509SKIOrX509SubjectName(X509SubjectNameEle);


        final var X509CertificateEle = new JAXBElement<byte[]>(new QName("http://www.w3.org/2000/09/xmldsig#", "X509Certificate" ), byte[].class, cert.getPublicKey().getEncoded());
        x509Data.addX509IssuerSerialOrX509SKIOrX509SubjectName(X509CertificateEle);


        signatureT.setKeyInfo(keyInfo);



        new Builder().setCharset(StandardCharsets.UTF_8)
        .setFormattedOutput(false).write(invoice, new File("target/dummy-invoice1.xml"));

    }
}
