package com.brandsure.common;

/**
 * Class to extract components from the SSCC Identifier
 * e.g. urn:epc:id:sscc:0310144.0000000101
 * = sscc:companyPrefix . extensionDigit + serialNumber
 */
public class Sscc extends BaseGtin {

    String sscc; // the raw SSCC label.
    boolean parsed = false; // flag set to true once we parse the SSCC into parts.
    String serialNumber = NOT_FOUND;
    char extensionDigit;
    String companyPrefix;

    public Sscc(String sscc) {
        this.sscc = sscc;
    }

    /**
     * Take a SSCC type ID and add a star for the last section
     * Some examples to highlight the method for counting, aggregating SSCCs with different extension digits are shown below.
     * The extension digit (ED) is defined by the entity establishing the SSCC.
     * <p>
     * Typically the ED is “0”.  However it can be any digit.
     * Some drug manufacturers use a different ED (e.g. “5”) to define a partial shipper case, as opposed to a pallet.
     * The ED appears to the right of the 1st decimal and functions the same as the Indicator Digit used in the GTIN.
     * Instead of "urn:epc:id:sscc:0352440.*    >> search and count as follows:   “urn:epc:id:sscc:0352440.0*”
     * <p>
     * If there does happen to be an SCC with an ED other than “0”; it would display as follows:
     * :   “urn:epc:id:sscc:0352440.5*”
     * <p>
     * If the system finds only “0” in the ED spot, that will be all that displays.
     * <p>
     * Results for one SSCC with ED= “5” and ED = “0”
     * "Shipment Contents"
     * "Number of Pack Levels","4"
     * "Pack Level","Type","Identifier","Quantity"
     * "1","sgtin","urn:epc:id:sgtin:0352440.010014.*","150"
     * "2","sgtin","urn:epc:id:sgtin:0352440.510014.*","7"
     * "2","sscc","urn:epc:id:sscc:0352440.5*","1"
     * "3","sscc","urn:epc:id:sscc:0352440.0*","1"
     *
     * returns ERROR is no . delimter is found.
     * @param label
     * @return
     */
    public static String convertLastSectionToStar(String label) {
        int dotIndex = label.lastIndexOf('.');
        // Check that the dot is present.
        if (dotIndex == 0) {
            logger.error("SSCC is missing the . delimiter " + label);
            return Constants.ERROR;
        } else {
            // get the digit after the dotIndex
            String newLabel = label.substring(0, dotIndex + 2) + '*';
            return newLabel;
        }
    }

    /**
     * Extract the serial number from the SSCC.
     * The SSCC SN calculation shall not include the Extension digit.
     * @return the seriol number or empty string if not found
     */
    public String getSerialNumber() {
        try {
            parse();
            return serialNumber;
        } catch (IllegalArgumentException e) {
            return Constants.ERROR;
        }
    }

    // Extract a GTIN from the SSCC
    public String getGtin() {
        String gtin;
        try {
            parse(); // done only once
            String gtinWoChecksum = extensionDigit + companyPrefix + serialNumber;
            // length should be 17 digits without checksum
            String checksum = calcChecksumOddInputDigits(gtinWoChecksum);
            gtin = gtinWoChecksum + checksum;
            if (gtin.length() != 18) {
                String errMsg = "calculated gtin  length is not 18 characters for SSCC " + sscc;
                logger.error(errMsg);
                throw new IllegalArgumentException(errMsg);
            }
        } catch (IllegalArgumentException e) {
            gtin = Constants.ERROR;
        }
        return gtin;
    }

    // companyPrefix . extensionDigit + serialNumber.
    private void parse() throws IllegalArgumentException {
        if (parsed == false) {
            String id = Sgtin.getStringPartAfterToken(sscc, "urn:epc:id:sscc:");
            if (id.length() != 18) {
                String errMsg = "Input sscc length is not 18 characters " + sscc;
                logger.error(errMsg);
                throw new IllegalArgumentException((errMsg));
            }
            // get the second part of id after the .
            int dotIndex = id.lastIndexOf(".");
            if (dotIndex == 0) {
                String errMsg = "Input sscc is missing the . delimiter " + sscc;
                logger.error(errMsg);
                throw new IllegalArgumentException((errMsg));
            }

            if (dotIndex > 0) {
                companyPrefix = id.substring(0, dotIndex);
                serialNumber = id.substring(dotIndex + 2);
                extensionDigit = id.charAt(dotIndex + 1);
            }
            parsed = true;
        }
    }
}
