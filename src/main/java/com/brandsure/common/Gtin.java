package com.brandsure.common;

import org.apache.log4j.Logger;

public class Gtin {

    static Logger logger = Logger.getLogger(Gtin.class);

    String gtin;
    String indicatorDigit;
    String GS1Prefix;
    String FDALablerCode;
    String mfgItemReference;
    String checkSum;

    public Gtin(String gtin) {
        this.gtin = gtin;
    }

    public void parse() throws IllegalArgumentException {
        logger.debug("gtin:" + gtin);
        // validate the length and chararcters.
        if ((gtin.length() != 14) || (gtin == null)) {
            throw new IllegalArgumentException("Not a valid GTIN is null or length != 14" + gtin);
        }
        if ( !gtin.chars().allMatch(Character::isDigit)) {
            throw new IllegalArgumentException("Not a valid GTIN. Must be only digits" + gtin);
        }

        indicatorDigit = gtin.substring(0,1);
        GS1Prefix = gtin.substring(1,3);
        FDALablerCode = gtin.substring(3,8);
        mfgItemReference = gtin.substring(8,13);
        checkSum = gtin.substring(13,14);
    }


    public String getIndicatorDigit() {
        return indicatorDigit;
    }

    public String getGS1Prefix() {
        return GS1Prefix;
    }

    public String getFDALablerCode() {
        return FDALablerCode;
    }

    public String getMfgItemReference() {
        return mfgItemReference;
    }

    public String getCheckSum() {
        return checkSum;
    }

    // Check the check sum
    public void validate() throws IllegalArgumentException {
        String gtinWoChecksum = gtin.substring(0,13);
        Sgtin sgtin = new Sgtin("00"); // just want to use it's function
        String calculatedChecksum = sgtin.calcChecksumOddInputDigits(gtinWoChecksum);
        if (!calculatedChecksum.equals(getCheckSum())) {
            throw new IllegalArgumentException("Actual checksum " + getCheckSum()
                    + " does not match calculated checksum " + calculatedChecksum);
        }
    }
}
