package com.brandsure.common;


import org.apache.log4j.Logger;

// Provide conversions of an SGLN to other types
// example SGLN 08608330004.0.0
public class Sgln {

    String sgln;

    static Logger logger = Logger.getLogger(Ndc.class);

    public Sgln(String sgln) {
        this.sgln = sgln;
    }

    // convert the SGLN to a GLN
    // example GLN 086083300040
    // definition of GLN
    // https://www.gs1us.org/DesktopModules/Bring2mind/DMX/Download.aspx?Command=Core_Download&EntryId=158&language=en-US&PortalId=0&TabId=134
    public String getGln() {

        // need to escape . below since it's a regex field
        String[] sglnParts = sgln.split("\\.");
        if (sglnParts.length != 3) {
            String errMsg = "SGLN " + sgln + " is not properly formatted";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }
        String glnWoChecksum = sglnParts[0] + sglnParts[1];
        // Do length check. sgln first plus second part = 12 digits.
        if (glnWoChecksum.trim().length() != 12) {
            throw new IllegalArgumentException("SGLN first plus second part should have 12 digits sgln=" + sgln);
        }


        BaseGtin baseGtin = new BaseGtin();
        String checkSum = baseGtin.calcChecksumEvenInputDigits(glnWoChecksum);
        String gln = glnWoChecksum + checkSum;
        return gln;
    }
}
