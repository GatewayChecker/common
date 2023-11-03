package com.brandsure.common;

import org.junit.Assert;
import org.junit.Test;

public class TestSgln {

    /**
     * expect no exception to be thrown
     */
    @Test
    public void testCheckStringAgainstRegexPasses() {
        Sgln sgln = new Sgln("086083301.0.0");
        sgln.checkStringAgainstRegex("sgln", "[0-9]{6,12}\\.[0-9]{0,6}\\..{1,20}", "086083301.0.1");
    }

    @Test
    public void testGetGLN() {
        Sgln sgln = new Sgln("08608330004.0.0");
        String gln = sgln.getGln();
        Assert.assertEquals( "0860833000406", gln);
    }

    /**
     * test that the regex fails when expected.
     */
    @Test
    public void testRegexMatchFail() {
        try {
            Sgln sgln = new Sgln("1234567808608330004.0.0");
            String gln = sgln.getGln();
        } catch (IllegalArgumentException e) {
            return;
        } // should not get here
        Assert.fail( "Expected exception to be thrown");
    }
}
