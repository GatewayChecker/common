
package com.brandsure.common;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

import java.util.LinkedList;
import java.util.List;

public class SimpleErrorHandler implements ErrorHandler {
    // New stuff to catch all exceptions
    final List<SAXParseException> exceptions = new LinkedList<SAXParseException>();

    public void warning(SAXParseException e) throws SAXException {
        System.out.println(e.getMessage());
        exceptions.add(e);
    }

    public void error(SAXParseException e) throws SAXException {
        System.out.println(e.getMessage());
        exceptions.add(e);
    }

    public void fatalError(SAXParseException e) throws SAXException {
        System.out.println(e.getMessage());
        exceptions.add(e);
    }

    public List<SAXParseException> getExceptions() {
        return exceptions;
    }
}