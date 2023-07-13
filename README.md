README for common package

----------------------------------------------------------------------------
Releases
2.0.1
Initial release

2.1.1
Add getGLN, getGS1 
Move checksum functions into BaseGtin class
Add Gtin class
Add support for getGLN to support a GS1 length of 3 through 7

2.2.1
Fixed bug where <ProcessingErrors></ProcessingErrors> tags were written out in report when no errors are actually present. 
Report was writing out <ProcessingErrors></ProcessingErrors> tags in the Event Extractor output. By default, the Event Extractor was writing out 
these outer tags even when there are no processing errors. The actual processing errors would have shown up inside these tags, if there were any.
This was a minor bug and will be fixed in the next release of Event Extractor. In the this version, these tags will only be written 
out when there are actual processing errors. 
If there were actual errors, they would show up inside these outer tags. 
These ProcessingErrors tags are not for XSDValidation. They are for any processing errors that show up while processing the EPCIS data.                        
XSD Validation Errors, if there were any, would come out with bracketing XMLElements
<XSDValidationErrors></XSDValidationErrors>

2.3.1
Fixed bug that doesn't correctly partition input SGTIN
code with a 4-4-2 digit pattern

2.4.1
11/13/21
Added Ndc class to support generating a GTIN from an NDC code.
Add a method getSGLN to get a SGLN from a GS1
Moved GTIN_DTO from ndc project to common. 

2.5.1 
12/19/21
Added method getGS1FromCompanyCode

2.6.1 
2/20/22
Added Sgln class to convert an SGLN to a GLN
Added method makeGTINFromDrugID in GTIN class. 

2.7.2 
3/26/22
Made UTF-8 the explicit coding when reading the xml document int the XMLDocumentReader class

2.8.2
4/30/22
Add Sscc class for extractor. fixed issue with serial number where the indicator digit was included. 
Added TestSscc class to test the indicator digit issue. 

2.9.1
5/14/22
Improved Error handling for SSCC. 
Added test code for SSCC
removed default UTF-8 encoding for XML document. Using default UTF-8 caused an error when
reading a file with a different encoding. 

2.10.1
7/13/23
add support for the following combinations of fdaLabelerCodeDigit and productCodeDigit in the Gtin class
4-4-2     9999-9999-99
5-3-2     99999-999-99
5-4-1     99999-9999-9

Modified the Sgtin class so we could support the  5-4-1 case in the ndc application. 
Before only 5-3-2 and 4-4-2 were supported. The sgtin was calculating the productCode from the 
second segment of the Sgtin, but there is not way to determine if it's a 5-4-1 or 5-3-2 only from
the input Sgtin. 

---------------------------------------------
Where to update the common package version for a new release. 
pom.xml
README
Also update common package consumers
EventExtractor, XMLValidator and ndc if needed. 