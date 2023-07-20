package org.efaps.ubl.documents.values;

/*
 * Anexo N.° 8: Catálogo de códigos
 * No. 09 - Catálogo Códigos de tipo de nota de crédito electrónica
 */
public enum CreditNoteTypeCode
{

    C01("01", "Anulación de la operación"),
    C02("02", "Anulación por error en el RUC"),
    C03("03", "Corrección por error en la descripción."),
    C04("04", "Descuento global"),
    C05("05", "Descuento por ítem"),
    C06("06", "Devolución total"),
    C07("07", "Devolución por ítem"),
    C08("08", "Bonificación"),
    C09("09", "Disminución en el valor"),
    C10("10", "Otros conceptos"),
    C11("11", "Ajustes de operaciones de exportación"),
    C12("12", "Ajustes afectos al IVAP"),
    C13("13", "Corrección del monto neto pendiente de pago y/o la(s) fechas(s) de vencimiento del pago único "
                    + "o de las cuotas y/o los montos correspondientes a cada cuota, de ser el caso");

    private final String code;

    private final String description;

    CreditNoteTypeCode(final String code,
                       final String description)
    {
        this.code = code;
        this.description = description;
    }

    public String getCode()
    {
        return code;
    }

    public String getDescription()
    {
        return description;
    }

}
