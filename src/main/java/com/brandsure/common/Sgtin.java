package com.brandsure.common;

import org.apache.log4j.Logger;

import java.text.ParseException;


/**
 * Takes an SGTIN as input and breaks it into it's component parts.
 */
public class Sgtin extends BaseGtin {
	/**
	 * SGTIN=0371571.012128.03223,
	 * GTIN=00371571121280,
	 * LOT=KASM01, SN=03223,
	 * EXP=240731
	 **/

	static Logger logger = Logger.getLogger(Sgtin.class);


	String sgtin;
	String uniqueId;
	String serialNumber;
	String[] sgtinParts;
	// We expect the sgtin to have two parts divided by a .
	int part0Length;
	int part1Length;
	int companyCodeDigits;


	public Sgtin(String sgtin) {
		this.sgtin = sgtin;
	}

	public void parse() throws ParseException {
		logger.debug("sgtin:" + sgtin);
		uniqueId = getStringPartAfterToken(sgtin, Constants.URN_EPC_ID_SGTIN_TOKEN );
		logger.debug("sgtin: " + uniqueId);
		// split on the .
		sgtinParts = uniqueId.split("\\.");
		if (sgtinParts.length != 3) {
			String errMsg = "Unable to split sgtin " + uniqueId + " into 3 parts separated by a period";
			logger.error(errMsg);
			throw new ParseException(errMsg, 3);
		}
		part0Length = sgtinParts[0].length();
		part1Length = sgtinParts[1].length();
		if ((part0Length + part1Length) != 13) {
			logger.error("Parsing Sgtin " + sgtin + "part 1 and part 2 length do not equal 13");
		}

		if ((part0Length < 6) || (part0Length > 11)) {
			logger.error("Parsing Sgtin " + sgtin + "first segment length should be between 6 to 11 inclusive");
		}

		// CompanyCodeDigits plus productFamily digits should add to 10.
		// either 5 + 3 + 2   or 4 + 4 + 2
		companyCodeDigits = part0Length - 2;

		serialNumber = sgtinParts[2];
		if ((serialNumber.length() == 0) || (serialNumber.length() > 20)) {
			logger.error("Parsing Sgtin " + sgtin + " part 3 length is <1 || >20");
		}
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public String getGtin() {
		// Get the first char on part 1 and move to the front
		String part1 = sgtinParts[1];
		char part1FirstChar = part1.charAt(0);
		String part1Remainder = part1.substring(1);
		String gtinWoChecksum = part1FirstChar + sgtinParts[0] + part1Remainder;
		logger.debug("gtinWoChecksum " + gtinWoChecksum);
		String checksum = calcChecksumOddInputDigits(gtinWoChecksum);
		String gtinWithChecksum = gtinWoChecksum + checksum;
		logger.debug("gtinWithChecksum " + gtinWithChecksum);
		return gtinWithChecksum;
	}

	public String getCompanyCode() {
		String companyCode = sgtinParts[0].substring(2, (companyCodeDigits + 2));
		return companyCode;
	}

	// GS1 = 03 + company code
	public String getGS1() {
		String GS1 = getGS1FromFDALabelerCode(getCompanyCode());
		return GS1;
	}

	/**
	 * Take a SGTIN type ID and convert the last section to a *
	 * for example  urn:epc:id:sgtin:0371571.512128.0HP9D5DW -> urn:epc:id:sgtin:0371571.512128.*
	 * @param label
	 * @return
	 */
	public static String convertLastSectionToStar(String label) {
		int dotIndex = label.lastIndexOf('.');
		String newLabel = label.substring(0, dotIndex+1) + '*';
		return newLabel;
	}

	// Make a GTIN from the additionalTradeItemIdentification attribute (in EPCIS chema 1.2)
	// in the XML product identifier section.
	//The drugTradeID is an SGTIN without the "urn:epc:idpat:sgtin:" prefix
	public  String makeGTINFromDrugTradeID(String drugTradeID) {
		//use drugTradeId and move first digit after decimal
		//		indicatorDigit before 03 below.
		Sgtin sgtin = new Sgtin(drugTradeID);
		String gtin = sgtin.getGtin();
	  /**
		String gtinWoCheckSum = "03" + drugID;
		String checkSum = calcChecksumEvenInputDigits(gtinWoCheckSum);
		String gtin = gtinWoCheckSum + checkSum;
		return gtin;
	   **/
	   return gtin;
	}
}
