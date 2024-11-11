package com.brandsure.common;

// Report object used to generate the report
// writes an HTML report.

import java.util.ArrayList;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

public class Report {
    String schemaVersion;
    String date;
    String softwareVersion = "";
    String xmlFilename;    // Name of the output xml report
    String customMessageFilename = null; // filename for the custom error messages
    String outputDir;

    String applicationName = null; // prefix to prepend to -report.xml so report can be differentiated.
    MessageMapper customMessages = new MessageMapper();
    ArrayList<String> xsdErrorMessages = new ArrayList<String>(); // Only Errors from XSD processing
    ArrayList<String> errorMessages = new ArrayList<String>(); // Generic error messages not related to XSD Errors
    boolean isWellformed = false;
    Boolean xsdValidated = null;

    public static final String BRANDSURE = "brandsure";
    public static final String XML_FILE = "XMLFile";
    public static final String DATE = "date";
    public static final String SOFTWARE_VERSION = "SoftwareVersion";
    public static final String XML_WELLFORMED = "WellFormed";
    public static final String XSD_SCHEMA_VERSION = "SchemaVersion";
    public static final String XSD_VALIDATED = "XSDValidated";
    public static final String XSD_VALIDATION_ERRORS = "XSDValidationErrors";
    public static final String PROCESSING_ERRORS = "ProcessingErrors";
    public static final String ERROR = "error";
    public static final String RAW_ERROR = "RawError";
    public static final String CUSTOM_ERROR = "CustomError";


    static Logger logger = Logger.getLogger(Report.class);

    public void setSchemaVersion(String schema) {
        this.schemaVersion = schema;
    }

    /**
     * Constructor
     * Set the software version of the calling program. To be used in the report output
     * @param softwareVersion
     */
    public Report(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public Report(String softwareVersion, String applicationName) {
        this.softwareVersion = softwareVersion;
        this.applicationName = applicationName;
    }


    public void setDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        date = sdf.format(new Date());
    }

    public void setSoftwareVersion(String version) {
        softwareVersion = version;
    }

    public void setXmlFilename(String filename) {
        xmlFilename = filename;
    }

    public void setCustomMessageFilename(String filename) {
        customMessageFilename = filename;
    }

    public void setOutputDir(String directory) {
        outputDir = directory;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void addXsdErrorMessage(String message) {
        xsdErrorMessages.add(message);
    }

    public void addErrorMessage(String message) { errorMessages.add(message); }

    public void setIsWellformed(boolean value) {
        isWellformed = value;
    }

    public void setIsXsdValidated(boolean value) {
        xsdValidated = value;
    }

    /**
     * Returns an unmodifiable list or ErrorMessages
     * @return
     */
    public List<String> getErrorMessages() {
        return Collections.unmodifiableList(errorMessages);
    }

    public void generateXML() throws IOException {
        PrintWriter pw = null;

        try {
            // make output dir if it doesn't exist
            File directory = new File(outputDir);
            if(!directory.exists()) {
                directory.mkdir();
            }

            // Load the custom error messages
            if (customMessageFilename != null) {
                customMessages.init(customMessageFilename);
            }

            String outputFilename = makeOutputFilename();
            File file = new File(outputFilename);
            FileWriter fw = new FileWriter(file, false);
            pw = new PrintWriter(fw);
            // Now build the output xml
            setDate();
            pw.println("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>");
            pw.println(beginTag(BRANDSURE));
            printElement(pw, XML_FILE, xmlFilename);
            printElement(pw, DATE, date);
            printElement(pw, SOFTWARE_VERSION, softwareVersion);
            printElement(pw, XML_WELLFORMED, toPassFail(isWellformed));
            printElement(pw, XSD_SCHEMA_VERSION, schemaVersion);
            if (xsdValidated != null) {
                printElement(pw, XSD_VALIDATED, toPassFail(xsdValidated));
            }
            // print XSD validation errors, if any
            if (!xsdErrorMessages.isEmpty()) {
                printXSDErrorMessages(pw);
            }
            // print other (non-XSD validation) processing errors, if any
            if (!errorMessages.isEmpty()) {
                printErrorMessages(pw);
            }
            pw.println(endTag(BRANDSURE));
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    // Convert a true to PASS and a false to FAIL
    private String toPassFail(boolean value) {
        if (value) {
            return "PASS";
        } else {
            return "FAIL";
        }
    }

    private void printXSDErrorMessages(PrintWriter pw) {
        pw.println(beginTag(XSD_VALIDATION_ERRORS));
        for (String errorMessage: xsdErrorMessages) {
            pw.println("  " + beginTag(ERROR));
            // sanitize the error message by escaping xml like tags
            pw.println("    " + beginTag(RAW_ERROR) + encode(errorMessage) + endTag(RAW_ERROR));
            String customMessage = customMessages.getCustomMessage(errorMessage);
            if (customMessage != null) { // don't want to print a "null"
                pw.println("    " + beginTag(CUSTOM_ERROR) + customMessage + endTag(CUSTOM_ERROR));
            }
            pw.println("  " + endTag(ERROR));
        }
        pw.println(endTag(XSD_VALIDATION_ERRORS));
    }

    private void printErrorMessages(PrintWriter pw) {
        pw.println(beginTag(PROCESSING_ERRORS));
        for (String errorMessage: errorMessages) {
            pw.println("  " + beginTag(ERROR));
            // sanitize the error message by escaping xml like tags
            pw.println("    " + beginTag(RAW_ERROR) + encode(errorMessage) + endTag(RAW_ERROR));
            String customMessage = customMessages.getCustomMessage(errorMessage);
            if (customMessage != null) { // don't want to print a "null"
                pw.println("    " + beginTag(CUSTOM_ERROR) + customMessage + endTag(CUSTOM_ERROR));
            }
            pw.println("  " + endTag(ERROR));
        }
        pw.println(endTag(PROCESSING_ERRORS));
    }


    /**
     * Escape any strings that don't belong inside xml tags
     * @param input
     * @return
     */
    public static String encode(String input) {
        if (input == null) {
            return null;
        } else {
            try {
                return URLEncoder.encode(input, StandardCharsets.UTF_8.toString());
            } catch (Exception e) {
                logger.error("error urlending string " + input, e);
                return null;
            }
        }
    }

    private void printElement(PrintWriter pw, String tag, String value) {
        pw.println(beginTag(tag) + value + endTag(tag));
    }


    private String beginTag(String name) {
        return "<" + name + ">";
    }

    private String endTag(String name) {
        return "</" + name + ">";
    }

    private String makeOutputFilename() {
        int index =xmlFilename.lastIndexOf(File.separator);
        // get everything after separator
        String filenameWithoutPath = "";
        if (index != -1) {
            filenameWithoutPath = xmlFilename.substring(index);
        } else {
            filenameWithoutPath = xmlFilename;
        }

        // default in case file doesn't end in .xml.
        // Avoids using same filename.
        String outputFilename  = "";

        // Make sure we append -report.xml for either .XML or .xml files. Add the application name if it's not null
        String postpend = "-report.xml";
        if (applicationName != null) {
            postpend = "-" + applicationName + postpend;
        }
        if (filenameWithoutPath.contains(".xml")) {
            outputFilename = outputDir + File.separator + filenameWithoutPath.replace(".xml", postpend);
        } else if (filenameWithoutPath.contains(".XML")) {
            outputFilename = outputDir + File.separator + filenameWithoutPath.replace(".XML", postpend);
        } else {
            outputFilename  = outputDir + File.separator + filenameWithoutPath + postpend;
            logger.error("filename does not end in .xml");
        }

        // to lowercase to make the report.xml filename consistent with extractor
        String outputFilenameLowerCase = outputFilename.toLowerCase();
        logger.info("Report outputFile " + outputFilenameLowerCase);
        return outputFilenameLowerCase;
    }

}
