package com.brandsure.common;

import org.apache.log4j.Logger;

/**
 * Contains methods useful for SGTIN, SSCC and SGLN processing
 */
public class BaseGtin {
    static Logger logger = Logger.getLogger(BaseGtin.class);
    public static final String NOT_FOUND = "";

    /**
     * Subtract the last digit of the sum from 10. This is the checksum digit
     * @param sum
     * @return
     */
    private String tenMinusLastDigit(int sum) {

        String sumAsString = Integer.toString(sum);
        char lastDigitAsChar =  sumAsString.charAt( sumAsString.length() - 1);
        // subtract last digit from 10
        int lastDigit = lastDigitAsChar - '0';
        // Deal with case of 10 - 0 = 10 which is 2 digits, not 1
        if (lastDigit == 0) {
            return "0";
        } else {
            return Integer.toString(10 - lastDigit);
        }
    }


    /* GS1 + 00000 + 1 digit checksum
     * GLN = 12 digits + checksum. LocRef digits adjust to match length
     * Match the location Ref digits length to get the correct final length
     * company code can be 4 or 5 digits
     */
    public String getGLN(String GS1) {
        String locationReference;
        String minLocationReference = "00000";
        if (GS1.length() == 7) {
            locationReference = minLocationReference;
        } else if (GS1.length() == 6) {
            locationReference = minLocationReference + "0";
        } else if (GS1.length() == 5) {
            locationReference = minLocationReference + "00";
        } else if (GS1.length() == 4) {
            locationReference = minLocationReference + "000";
        } else if (GS1.length() == 3) {
            locationReference = minLocationReference + "0000";
        } else {
            locationReference = "";  // set a default
            logger.error("Unsupported length for GS1 length:" + GS1.length());
        }

        String GLNwoCheckDigit = GS1 + locationReference;
        String checkDigit = calcChecksumEvenInputDigits(GLNwoCheckDigit);
        String GLN =  GLNwoCheckDigit + checkDigit;
        return GLN;
    }

    /**
     * Given the company code, return the GS1.
     * The Company code is the first section of the hyphenated ndc
     * e.g. 0002-1234-98. Company code is 0002
     * @param FDALabelerCode
     * @return GS1 String
     */
    public String getGS1FromFDALabelerCode(String FDALabelerCode) {
        String GS1 = "03" + FDALabelerCode;
        return GS1;
    }
    /**
     * SGLN is GS1 company prefix plus . + enough zeros to make it 12 digits + .0
     * @param GS1
     */
    public String getSGLN(String GS1) throws IllegalArgumentException {
        String SGLN;
        if (GS1 == null) throw new IllegalArgumentException("GS1 is null");

        int gs1Length = GS1.length();
        // find difference from 12 digits
        int digitsToAdd = 12 - gs1Length;
        StringBuffer extraZeros = new StringBuffer();
        for (int i=0; i<digitsToAdd; i++) {
            extraZeros.append("0");
        }

        SGLN = GS1 + "." + extraZeros.toString() + ".0";
        return SGLN;
    }

    /** apply checksum algorithm for 12 digit GTIN
     * multiply digit 0 by 1,
     * digit 1 by 3
     *digit 2 by 1, etc
     **/
    public String calcChecksumEvenInputDigits(String gtinWoChecksum) {
        int sum = 0;
        for (int i=0; i<gtinWoChecksum.length(); i++) {
            char digitAsChar = gtinWoChecksum.charAt(i);
            int digit = digitAsChar - '0';
            if (i%2 == 0) {
                sum += digit * 1;
            } else {
                sum += digit * 3;
            }
        }

        return tenMinusLastDigit(sum);
    }



    /** apply checksum algorithm for 13 digit GTIN
     * multiply digit 0 by 3,
     * digit 1 by 1
     *digit 2 by 3, etc
     **/
    public String calcChecksumOddInputDigits(String gtinWoChecksum) {
        int sum = 0;
        for (int i=0; i<gtinWoChecksum.length(); i++) {
            char digitAsChar = gtinWoChecksum.charAt(i);
            int digit = digitAsChar - '0';
            if (i%2 == 0) {
                sum += digit * 3;
            } else {
                sum += digit * 1;
            }
        }

        return tenMinusLastDigit(sum);
    }

    public static String getStringPartAfterToken(String fullString, String token) {
        String lastPart = NOT_FOUND;  // default
        if ((fullString == null) || fullString.isEmpty()) {
            logger.error("input string is null or empty");
            lastPart = NOT_FOUND;
        } else {
            String[] eventTypeParts = fullString.split(token);
            if (eventTypeParts.length > 1) {
                lastPart = eventTypeParts[1];
            } else {
                logger.info("Can't extract part after trying to split string " + fullString + " on token " + token);
                lastPart = fullString;
            }
        }
        return lastPart;
    }

}
