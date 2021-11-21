package com.brandsure.common;


import org.apache.log4j.Logger;

/**
 * Class to parse an NDC with hyphens.
 * Provides method to  convert to  other objects like GTIN
 */
public class Ndc extends BaseGtin {

    String ndc;  // ndc code with hyphens
    String companyCode;
    String productFamily;
    String packagingCode;
    boolean ndcHasHyphens;

    static Logger logger = Logger.getLogger(Gtin.class);

    public Ndc(String ndc) {
        this.ndc = ndc;
    }


    // Convert the input ndc code to a GTIN with 14 digits.
    // See https://www.rxtrace.com/2012/01/depicting-an-ndc-within-a-gtin.html/
    // e.g indicator digit + 03 + 10-digit NDC + checksum
    public String convertToGtin14() throws IllegalArgumentException {
        // Remove hyphens if they are present
        String ndc10; // 10 digit ndc without hyphens

        if (ndc == null) {
            throw new IllegalArgumentException("NDC is null");
        }

        // remove the hyphens to get to 10 digits.
        if (ndc.contains("-")) {
            ndc10 = ndc.replace("-", "");
        } else {
            ndc10 = ndc;
        }
        // check that we have 10 digits
        if (ndc10.length() != 10) {
            throw new IllegalArgumentException("Not a valid NDC. length != 10" + ndc);
        }

        String indicatorDigit = "0";
        String GS1Prefix = "03";
        String GtinWoChecksum = indicatorDigit + GS1Prefix + ndc10;
        if (GtinWoChecksum.length() != 13) {
            throw new IllegalArgumentException("GTINwoChecksum should be 13 digits. Actual length is " + GtinWoChecksum.length());
        }

        String calculatedChecksum = calcChecksumOddInputDigits(GtinWoChecksum);
        String GTIN = GtinWoChecksum + calculatedChecksum;
        return GTIN;
    }

}
