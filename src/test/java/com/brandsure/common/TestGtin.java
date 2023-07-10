package com.brandsure.common;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TestGtin {
    /**
     * Test the parse with 5-3-2  fdaLabelerDigits pattern
     * 99999-999-99
     */
    @Test
    public void testParse_5_3_2() {
       String gtinStr = "00331722700900";
       Gtin gtin = new Gtin(gtinStr, 5, 3);
       gtin.parse();
       Assert.assertEquals("0", gtin.getIndicatorDigit());
       Assert.assertEquals("03", gtin.getGS1Prefix());
       Assert.assertEquals("31722", gtin.getFDALablerCode());
       Assert.assertEquals("700", gtin.getMfgItemReference());
       // Not a legit checksum, just checking parsing
       Assert.assertEquals("0", gtin.getCheckSum());
       gtin.validate();
    }

    /**
     * Test the parse with 4-4-2 fdaLabelerDigits pattern
     * 9999-9999-99
     */
    @Test
    public void testParse_4_4_2() {
        String gtinStr = "00300937201984";
        Gtin gtin = new Gtin(gtinStr, 4, 4);
        gtin.parse();
        Assert.assertEquals("0", gtin.getIndicatorDigit());
        Assert.assertEquals("03", gtin.getGS1Prefix());
        Assert.assertEquals("0093", gtin.getFDALablerCode());
        Assert.assertEquals("7201", gtin.getMfgItemReference());
        // Not a legit checksum, juat checking parsing
        Assert.assertEquals("4", gtin.getCheckSum());
        gtin.validate();
    }

    /**
     * Test the parse with 5-4-1 fdaLabelerDigits pattern
     * 9999-9999-99
     */
    @Test
    public void testParse_5_4_1() {
        String gtinStr = "00369238115499";
        Gtin gtin = new Gtin(gtinStr, 5,4);
        gtin.parse();
        Assert.assertEquals("0", gtin.getIndicatorDigit());
        Assert.assertEquals("03", gtin.getGS1Prefix());
        Assert.assertEquals("69238", gtin.getFDALablerCode());
        Assert.assertEquals("1154", gtin.getMfgItemReference());
        Assert.assertEquals("9", gtin.getCheckSum());
        gtin.validate();
    }
    /**
     * Test cases from Gary below to validate GLN, GCP and SGLN given an input GTIN14
     * Should be combined with test cases above at some point.
     * 4-4-2 refers to the number of digits in each section of the NDC below.
     *
     * NDC           GTIN            GLN            GCP      SGLN
     * 0093-7201-98  00300937201984  0300930000003  030093   030093.000000.0
     * 31722-700-90  00331722700900  0331722000000  0331722  0331722.00000.0
     * 69238-1154-9  00369238115499  0369238000009  0369238  0369238.00000.0
     **/
    @Test
    public void testGtinGary_4_4_2() {
        Gtin gtin = new Gtin("00300937201984", 4,4 );
        gtin.parse();
        gtin.validate();
        Assert.assertTrue("GLN", "0300930000003".equals(gtin.getGLN(gtin.getGS1())));
        Assert.assertTrue("GCP", "030093".equals(gtin.getGS1()));
        Assert.assertTrue("SGLN", "030093.000000.0".equals(gtin.getSGLN(gtin.getGS1())));
    }

    @Test
    public void testGtinGary_5_3_2() {
        Gtin gtin = new Gtin("00331722700900", 5,3 );
        gtin.parse();
        gtin.validate();
        Assert.assertTrue("GLN", "0331722000000".equals(gtin.getGLN(gtin.getGS1())));
        Assert.assertTrue("GCP", "0331722".equals(gtin.getGS1()));
        Assert.assertTrue("SGLN", "0331722.00000.0".equals(gtin.getSGLN(gtin.getGS1())));
    }

    @Test
    public void testGtinGary_5_4_1() {
        Gtin gtin = new Gtin("00369238115499", 5,3 );
        gtin.parse();
        gtin.validate();
        Assert.assertTrue("GLN", "0369238000009".equals(gtin.getGLN(gtin.getGS1())));
        Assert.assertTrue("GCP", "0369238".equals(gtin.getGS1()));
        Assert.assertTrue("SGLN", "0369238.00000.0".equals(gtin.getSGLN(gtin.getGS1())));
    }

}
