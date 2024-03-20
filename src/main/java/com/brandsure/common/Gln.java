package com.brandsure.common;

import org.apache.log4j.Logger;

public class Gln {
    String gln;

    static Logger logger = Logger.getLogger(Gln.class);

    public Gln(String gln) {
        this.gln = gln;
    }

    /**
     * Calculate the SGLN using the company prefix as input
     * @param companyPrefix
     */
    public String getSgln(String companyPrefix) {
        String sgln = "";

        if (gln.length() != 13) {
            String errMsg = "GLN " + gln + " is not 13 digits";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }

        int gcpLength = companyPrefix.length();
        if ((gcpLength + 1)  > gln.length())  {
            String errMsg = "GLN " + gln + " length too short for GCP " + companyPrefix;
            logger.error(errMsg);
        }

        // get the location Reference
        int locationReferenceBeginIndex = gcpLength;
        int locationReferenceEndIndex = gln.length() - 1;
        String locationReference = gln.substring(locationReferenceBeginIndex, locationReferenceEndIndex);
        String extension = "0";
        sgln = companyPrefix + "." + locationReference + "." + extension;
        return sgln;
    }
}
