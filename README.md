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


---------------------------------------------
Where to update the common package version for a new release. 
pom.xml
README
Also update common package consumers
EventExtractor, XMLValidator and ndc if needed. 