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

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

import org.efaps.ubl.extension.AdditionalInformation;
import org.efaps.ubl.extension.AdditionalProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.DocumentCurrencyCodeType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.InvoiceTypeCodeType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_21.ExtensionContentType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_21.UBLExtensionType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_21.UBLExtensionsType;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.AmountType;

public class Utils
{
    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

    public static InvoiceTypeCodeType getInvoiceType(final String type)
    {
        final var ret = new InvoiceTypeCodeType();
        ret.setListAgencyName("PE:SUNAT");
        ret.setListID("0101");
        ret.setListName("Tipo de Documento");
        ret.setListURI("urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo01");
        ret.setValue("03");
        return ret;
    }

    public static DocumentCurrencyCodeType getDocumentCurrencyCode(final String isoCode)
    {
        final var ret = new DocumentCurrencyCodeType();
        ret.setListAgencyName("United Nations Economic Commission for Europe");
        ret.setListID("ISO 4217 Alpha");
        ret.setListName("Currency");
        ret.setValue(isoCode);
        return ret;
    }

    public static UBLExtensionsType getWordsForAmount(final BigDecimal amount) {
        final var ret = new UBLExtensionsType();
        final var ublExtension = new UBLExtensionType();

        final var extension = new ExtensionContentType();
        ublExtension.setExtensionContent(extension);
        final var additionalInformation = new AdditionalInformation();
        extension.setAny(additionalInformation);
        final var additionalProperty = new AdditionalProperty();
        additionalInformation.setAdditionalProperty(additionalProperty);
        additionalProperty.setId("1000");
        additionalProperty.setValue(getWords4Number(amount));
        ret.addUBLExtension(ublExtension);
        return ret;
    }

    public static String getWords4Number(final BigDecimal _amount)
    {
        return new StringBuilder().append(org.efaps.number2words.Converter.getMaleConverter(
                        new Locale("es")).convert(_amount.longValue())).append(" y ")
                        .append(_amount.setScale(2, RoundingMode.HALF_UP).toPlainString().replaceAll("^.*\\.", ""))
                        .append("/100 ").toString().toUpperCase();
    }

    public static <T extends AmountType> T getAmount(final Class<T> type, final BigDecimal amount) {
        T ret = null;
        try {
            ret = type.getConstructor().newInstance();
            ret.setCurrencyID("PEN");
            ret.setValue(amount);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                        | NoSuchMethodException | SecurityException e) {
            LOG.error("Catched", e);
        }
        return ret;
    }

}
