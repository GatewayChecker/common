package com.brandsure.common;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestGtin {

    @Test
    public void testParse() {
       String gtinStr = "00312345678913";
       Gtin gtin = new Gtin(gtinStr);
       gtin.parse();
       Assert.assertEquals("0", gtin.getIndicatorDigit());
       Assert.assertEquals("03", gtin.getGS1Prefix());
       Assert.assertEquals("12345", gtin.getFDALablerCode());
       Assert.assertEquals("67891", gtin.getMfgItemReference());
       // Not a legit checksum, jsut checking parsing
       Assert.assertEquals("3", gtin.getCheckSum());
       gtin.validate();
    }

}
