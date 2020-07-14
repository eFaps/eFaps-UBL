package org.efaps.ubl.documents;

import java.math.BigDecimal;
import java.util.List;

public interface ILine
{

    List<ITaxEntry> getTaxEntries();

    List<IChargeEntry> getCharges();

    BigDecimal getNetPrice();

    BigDecimal getCrossPrice();

    BigDecimal getNetUnitPrice();

    BigDecimal getCrossUnitPrice();

    String getUoMCode();

    BigDecimal getQuantity();

    String getDescription();

    String getSku();
}
