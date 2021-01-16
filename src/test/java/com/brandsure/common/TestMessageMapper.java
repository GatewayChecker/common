package com.brandsure.common;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

//import junit.framework.*;


public class TestMessageMapper {
   protected MessageMapper msgMapper; 
   
   // Initialize the messageMapper
   @Before
   public void setUp(){
		String currentWorkingDir = System.getProperty("user.dir");
		String testFile =  currentWorkingDir + File.separatorChar + "src" + File.separatorChar + "test" + File.separatorChar +
				"resources" + File.separatorChar + "messageMapper" + File.separatorChar + "errorMessageTest.txt";
	   	msgMapper = new MessageMapper();
	   	msgMapper.init(testFile);
   }

   // Custom Message should be found
   @Test 
   public void testMatchedMessage() {

	// note no period after message
   	String firstMsg = "first error messsage line#: 12";
   	// test if we can retrieve the expected message
   	String expectedMsg  = "custom error message line#: 12";
   	String actualMsg = msgMapper.getCustomMessage(firstMsg);
   	// confirm we found the expected message
   	Assert.assertEquals(expectedMsg, actualMsg);
   }
   
   // what happens when message isn't found
   @Test
   public void testUnmatchedMessage() {
	   Assert.assertNull(msgMapper.getCustomMessage("foobar line#: 12"));
   }
}