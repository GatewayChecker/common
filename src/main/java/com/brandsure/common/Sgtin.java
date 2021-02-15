package com.brandsure.common;

import org.apache.log4j.Logger;

public class Sgtin {
	/**
	SGTIN=0371571.012128.03223,
			GTIN=00371571121280,
			LOT=KASM01, SN=03223,
			EXP=240731
			**/
	
	static Logger logger = Logger.getLogger(Sgtin.class);
	 
	private static final String NOT_FOUND = "";
	
	
	String sgtin; 
	String uniqueId; 
	String serialNumber;
	String[] sgtinParts;

	public Sgtin(String sgtin) {
		this.sgtin = sgtin;
	}
	
	public void parse() {
		logger.debug("sgtin:" + sgtin);
		uniqueId = getStringPartAfterToken(sgtin, "urn:epc:id:sgtin:");
		logger.debug("sgtin: " + uniqueId);
		// split on the .
		sgtinParts = uniqueId.split("\\.");
		serialNumber = sgtinParts[2];
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
		String gtinWithChecksum =  gtinWoChecksum + checksum; 
		logger.debug("gtinWithChecksum " + gtinWithChecksum);
		return gtinWithChecksum;
	}

	public String getCompanyCode() {
		String companyCode = sgtinParts[0].substring(2,7);
		return companyCode;
	}

	public String getProductFamily() {
		String companyCode = sgtinParts[1].substring(1,4);
		return companyCode;
	}

	public String getPackagingCode() {
		String companyCode = sgtinParts[1].substring(4,6);
		return companyCode;
	}





	public String getReleaseGLN() {
		String glnWoChecksum = sgtinParts[0] + "00000";
		String glnWithChecksum = glnWoChecksum + calcChecksumEvenInputDigits(glnWoChecksum);
		return glnWithChecksum; 
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


	
	//TODO - this is from ExtractEvents. Move to common location
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
}
