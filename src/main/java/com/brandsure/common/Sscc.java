package com.brandsure.common;

/**
 * Class to extract components from the SSCC Identifier
 * e.g. urn:epc:id:sscc:0310144.0000000101
 */
public class Sscc extends BaseGtin {

    String sscc; // the raw SSCC label.

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
     * @param label
     * @return
     */
    public static String convertLastSectionToStar(String label) {
        int dotIndex = label.lastIndexOf('.');
        // get the digit after the dotIndex
        String newLabel = label.substring(0, dotIndex + 2) + '*';
        return newLabel;
    }

    /**
     * Extract the serial number from the SSCC.
     * The SSCC SN calculation shall not include the Extension digit.
     * @return the seriol number or empty string if not found
     */
    public String getSerialNumber() {
        String serialNumber = NOT_FOUND;
        String id = Sgtin.getStringPartAfterToken(sscc, "urn:epc:id:sscc:");
        // get the second part of id after the . the sscc as an
        int dotIndex = id.lastIndexOf(".");
        if(dotIndex >0)
        {
            serialNumber = id.substring(dotIndex + 2);
        } else
        {
            logger.error("sscc missing serial number " + sscc);
        }
        return serialNumber;
    }
}
