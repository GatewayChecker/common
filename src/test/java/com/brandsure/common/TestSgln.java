package com.brandsure.common;

import org.junit.Assert;
import org.junit.Test;

public class TestSgln {

    @Test
    public void testGetGLN() {
        Sgln sgln = new Sgln("08608330004.0.0");
        String gln = sgln.getGln();
        Assert.assertEquals( "0860833000406", gln);
    }
}
