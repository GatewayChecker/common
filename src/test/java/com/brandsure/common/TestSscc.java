package com.brandsure.common;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestSscc {

    /**
     *  SSCC SN calculation shall not include the Extension digit.
     *  Not include the first digit to the right of the decimal point.
     */
    @Test
    public void testGetSerialNumber() {
        String ssccLabel = "urn:epc:id:sscc:0370727.1230002312";

        Sscc sscc = new Sscc(ssccLabel);
        // We should drop the 1 at the beginning of 123. 1 is the indicator digit
        Assert.assertEquals("checkSerialNumber for SSCC","230002312", sscc.getSerialNumber());
    }

}
