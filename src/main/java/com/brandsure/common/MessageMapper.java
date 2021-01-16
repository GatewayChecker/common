package com.brandsure.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;

// Maps a standard error message to a custom error message
public class MessageMapper {
	private HashMap<String, String> messageMap = new HashMap<String, String>(); 
	public final static String SPLIT_TOKEN = "#BS#";
	public final static String LINE_NUMBER_TOKEN = "line#:";
	static Logger logger = Logger.getLogger(MessageMapper.class);
	
	// load the 
	public void init(String messageFilename) {
		// No errorMessages file, so no init
		if ((messageFilename == null) || messageFilename.isEmpty()) {
			return; 
		} else {
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(messageFilename));
				String line = reader.readLine();
				while (line != null) {
					logger.debug(line);
					// Ignore comment lines that begin with #
					if (line.startsWith("#")) {
						// ignore it
					} else if (line.contains(SPLIT_TOKEN)) {
						// Split the string on the token
						String[] pair = line.split(SPLIT_TOKEN, 2); 
						logger.debug("key: " + pair[0].trim());
						logger.debug("value: " + pair[1].trim());
						messageMap.put(pair[0].trim(), pair[1].trim());					
					} else {
						// do nothing, no SPLIT_TOKEN in this line
					}
					// read next line
					line = reader.readLine();
				}
				reader.close();
			} catch (IOException e) {
				logger.error("Caught exception reading errorMessages file " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	// Extract the lineNumber from incoming message
	private void trimLineNumber(String trimmedMessageIn, StringBuilder lineNumberOut, StringBuilder msgWoLineNumberOut) {
		// Token
		String[] pair = trimmedMessageIn.split(LINE_NUMBER_TOKEN, 2);
		msgWoLineNumberOut.append(pair[0]);
		if (pair.length > 1) {
			lineNumberOut.append(pair[1]);
		}
	}
	
	// If no message is found, return null
    public String getCustomMessage(String standardMessage) {
    	String customMessage = null; 
    	// check inputs
    	if (standardMessage == null) {
    		return null; 
    	}
    	String trimmedMessage =  standardMessage.trim();
    	StringBuilder lineNumber = new StringBuilder(); 
    	StringBuilder msgWoLineNumber = new StringBuilder();
    	trimLineNumber(trimmedMessage, lineNumber, msgWoLineNumber);
    	
    	logger.debug("getCustomMessage: rawMessage=" + trimmedMessage);
    	if (messageMap.containsKey(msgWoLineNumber.toString().trim())) {
    		customMessage = messageMap.get(msgWoLineNumber.toString().trim());
    	} else {
    		customMessage = null; 
    	}
    	logger.debug("retrieved customMessage: " + customMessage);
    	if (customMessage != null) {
    		return customMessage + " " + LINE_NUMBER_TOKEN + lineNumber; 
    	} else {
    		return null;
    	}
    }
    
    // For testing
    public static void main(String[] args) {
    	String testFile = "./src/test/messageMapper/errorMessageTest.txt";
    	MessageMapper msgMapper = new MessageMapper();
    	msgMapper.init(testFile);
    	String firstMsg = "first error messsage";
    	// test if we can retrieve the expected message
    	String expectedMsg = "custom error message";
    	String customMsg = msgMapper.getCustomMessage(firstMsg);
    	System.out.println("retrievedMsg: " + customMsg);
    	if (expectedMsg.equals(customMsg)) {
    		System.out.println("messages matched");
    	}
    	
    	// what happens when message isn't found
    	String noCustomMsg = msgMapper.getCustomMessage("foobar");
    	if (noCustomMsg == null) {
    	   System.out.println("noCustomMsg - null expected: " + noCustomMsg);
    	}
    }
    
}
