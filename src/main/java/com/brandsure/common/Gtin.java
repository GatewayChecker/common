package com.brandsure.common;

import org.apache.log4j.Logger;

/**
 * Takes a GTIN as input and breaks it into it's component parts.
 * example GTIN=00371571121280,
 */
public class Gtin extends BaseGtin {

    String gtin;
    String indicatorDigit;
    String GS1Prefix;
    String FDALablerCode;
    String mfgItemReference;
    String checkSum;

    int fdaLabelerCodeDigits;
    int productCodeDigits;  // same as mfgItemReference

    static Logger logger = Logger.getLogger(Gtin.class);

    /** Supported digits for Gtin
     *  fdaLabelerCode - productCode - packagingCode
     *  4-4-2     9999-9999-99
     *  5-3-2     99999-999-99
     *  5-4-1     99999-9999-9
     */
    public Gtin(String gtin, int fdaLabelerCodeDigits, int productCodeDigits) {
        this.gtin = gtin;
        this.fdaLabelerCodeDigits = fdaLabelerCodeDigits;
        this.productCodeDigits = productCodeDigits;
   }

    public void parse() throws IllegalArgumentException {
        logger.debug("gtin:" + gtin);
        // validate the length and chararcters.
        if ((gtin.length() != 14) || (gtin == null)) {
            throw new IllegalArgumentException("Not a valid GTIN is null or length != 14 " + gtin);
        }
        if ( !gtin.chars().allMatch(Character::isDigit)) {
            throw new IllegalArgumentException("Not a valid GTIN. Must be only digits" + gtin);
        }

        indicatorDigit = gtin.substring(0,1);
        GS1Prefix = gtin.substring(1,3);
        // mfgItemReference is the productCode
        if ((fdaLabelerCodeDigits == 4) && (productCodeDigits == 4)) {
            FDALablerCode = gtin.substring(3, 7);
            mfgItemReference = gtin.substring(7, 11);
        } else if ((fdaLabelerCodeDigits == 5) && (productCodeDigits == 3)) {
            FDALablerCode = gtin.substring(3, 8);
            mfgItemReference = gtin.substring(8, 11);
        } else if ((fdaLabelerCodeDigits == 5) && (productCodeDigits == 4)) {
           FDALablerCode = gtin.substring(3, 8);
            mfgItemReference = gtin.substring(8, 12);
        } else {
           String errMsg = "fdaLabelerCodeDigits=" + fdaLabelerCodeDigits +
                " productCodeDigits=" + productCodeDigits + " are not a supported" +
                " combination";
            logger.error(errMsg);
            throw new IllegalArgumentException(errMsg);
        }
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

    public String getGtin() {return gtin;}


    // 03 plus FDALabelerCode
    public String getGS1() {
        String GS1 = "03" + getFDALablerCode();
        return GS1;
    }

    // convert a GTIN to an SGTIN
    public String getSgtin() {
        char lastFDALabelerCodeDigit = FDALablerCode.charAt(4);
        String truncatedFDALabelerCode = FDALablerCode.substring(0,4);
        return GS1Prefix + truncatedFDALabelerCode + "." + getIndicatorDigit()  + lastFDALabelerCodeDigit + mfgItemReference + ".*";
    }

    // Check the check sum
    public void validate() throws IllegalArgumentException {
        String gtinWoChecksum = gtin.substring(0,13);
        String calculatedChecksum = calcChecksumOddInputDigits(gtinWoChecksum);
        if (!calculatedChecksum.equals(getCheckSum())) {
            throw new IllegalArgumentException("Actual checksum " + getCheckSum()
                    + " does not match calculated checksum " + calculatedChecksum);
        }
    }
}
