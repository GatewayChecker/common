package com.brandsure.common;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XMLDocumentReader {

    private Report report;
    static Logger logger = Logger.getLogger(XMLDocumentReader.class);
    public static final int EXIT_FAILURE = -1;

    public XMLDocumentReader(Report report) {
        this.report = report;
    }

    //https://stackoverflow.com/questions/6362926/xml-syntax-validation-in-java
    // This method checks if the xml file is well formed.
    // Returns the document is parsing is successful.
    // Returns null if parsing for well formed fails.
    public  Document loadDocument(String xmlFile) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(true);

            DocumentBuilder builder = factory.newDocumentBuilder();

            builder.setErrorHandler(new SimpleErrorHandler());
            // the "parse" method also validates XML, will throw an exception if misformatted
            logger.info("loading xml file " + xmlFile);

            // Check if the file exists
            File inputFile = new File(xmlFile);
            if (!inputFile.exists()) {
                logger.error("ERROR input file " + xmlFile + " was not found. Exiting");
                System.exit(EXIT_FAILURE);
            }

            Document document = builder.parse(new InputSource(xmlFile));
            logger.info(xmlFile + " wellformed PASS");
            return document;
        } catch (IOException ie) {
            logger.error("Caught IOxception loading xmlFile " + xmlFile + ". " + ie.getMessage());
            report.addXsdErrorMessage(ie.getMessage());
            //ie.printStackTrace();
            return null;
        } catch (ParserConfigurationException pce) {
            logger.error("Caught exception loading xmlFile " + xmlFile + ". " + pce.getMessage());
            report.addXsdErrorMessage(pce.getMessage());
            // pce.printStackTrace();
            return null;
        } catch (SAXException se) {
            logger.error("Caught exception loading xmlFile " + xmlFile + ". " + se.getMessage());
            report.addXsdErrorMessage(se.getMessage());
            // se.printStackTrace();
            return null;
        }
    }
}
