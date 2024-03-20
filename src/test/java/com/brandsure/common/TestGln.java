package com.brandsure.common;

import org.junit.Assert;
import org.junit.Test;

public class TestGln {

    @Test
    public void testPostiveCase() {
        Gln gln = new Gln("0860004646401");
        String companyPrefix = "08600046464";
        String sglnCalc = gln.getSgln(companyPrefix);
        Assert.assertEquals( "08600046464.0.0", sglnCalc);
    }

}
