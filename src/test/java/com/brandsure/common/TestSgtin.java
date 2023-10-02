
/**
 * Copyright Brandsure 2020
 *
 */
package com.brandsure.common;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class TestSgtin {
	 // Initialize the messageMapper
	   @Before
	   public void setUp(){
			String currentWorkingDir = System.getProperty("user.dir");
			String testFile =  currentWorkingDir + File.separatorChar + "src" + File.separatorChar + "test" + File.separatorChar +
					"resources" + File.separatorChar + "messageMapper" + File.separatorChar + "errorMessageTest.txt";
	   }

	
	// Test for a 12 digit input
	@Test
	public void testChecksumEvenInputDigitsGLN() throws Exception {
		Sgtin sgtin = new Sgtin("0371571.012128.03223"); 
		// test for 12 digit GLN
		String checksum = sgtin.calcChecksumEvenInputDigits("037157100000");
		String expectedChecksum = "4"; 
		Assert.assertEquals(expectedChecksum, checksum);

		 
String sgtinGtinPairs[][] = {
		{"0123456.098765.0", "00123456987654"},
		{"0143456.098764.0",  "00143456987645"},
		{"0123456.098764.0",  "00123456987647"},
		{"0123456.092224.0",  "00123456922242"},
		{"0123456.033764.0",  "00123456337640"},
		{"0361571.512128.0",  "50361571121286"},
		{"0183456.098764.0",  "00183456987641"},
		{"0123433.098760.0",  "00123433987608"},
		{"0183452.021131.0",  "00183452211313"},
		{"0371571.712128.0",  "70371571121289"}};

		for (String[] pair: sgtinGtinPairs) {
			
			Sgtin sgtin2 = new Sgtin("urn:epc:id:sgtin:" + pair[0]);
			sgtin2.parse(); 
			String gtin2= sgtin2.getGtin(); 
			System.out.println("testing sgtin to gtin inSgtin:" + pair[0]  + " expectedGtin:" + pair[1] + "  calculatedGtin:" + gtin2); 
			Assert.assertEquals(pair[1], gtin2);
		}
		
	}
	
	// test for a 13 digit input
	@Test
	public void testChecksumOddInputDigitsGTIN() {
		Sgtin sgtin = new Sgtin("0371571.012128.03223"); 
		String checksum = sgtin.calcChecksumOddInputDigits("8123456798765");
		String expectedChecksum = "7";
		Assert.assertEquals(expectedChecksum, checksum);
	}

	//@Test
	public void testGetCodes_5_3_2_pattern() throws Exception{
		Sgtin sgtin = new Sgtin("urn:epc:id:sgtin:0371571.012128.03223");
		sgtin.parse();

		String expectedCompanyCode = "71571";
		Assert.assertEquals(expectedCompanyCode, sgtin.getCompanyCode());
	}

	//@Test
	public void testGetCodes_4_4_2_pattern() throws Exception {
		Sgtin sgtin = new Sgtin("urn:epc:id:sgtin:030093.0720198.1");
		sgtin.parse();

		String expectedCompanyCode = "0093";
		Assert.assertEquals(expectedCompanyCode, sgtin.getCompanyCode());
	}

	// SGLN is GS1 company prefix plus . + enough zeros to make it 12 digits + .0
	@Test
	public  void testGetSGLN() {
	   	Sgtin sgtin = new Sgtin("urn:epc:id:sgtin:030093.0720198.1"); // We don't use this number
		String sgln = sgtin.getSGLN("0300937");

		Assert.assertEquals("0300937.00000.0", sgln);
	}
}
