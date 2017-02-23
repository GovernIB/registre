package es.caib.regweb3.sir.utils;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by earrivi on 15/06/2016.
 */
public class XPathReaderUtil {

    private InputStream xmlInputStream;
    private Document xmlDocument;
    private XPath xPath;

    public XPathReaderUtil(InputStream xmlInputStream) {
        this.xmlInputStream = xmlInputStream;
        initObjects();
    }

    private void initObjects() {
        try {
            xmlDocument = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(xmlInputStream);
            xPath = XPathFactory.newInstance().newXPath();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }
    }

    public Object read(String expression, QName returnType) {
        try {
            XPathExpression xPathExpression = xPath.compile(expression);
            return xPathExpression.evaluate(xmlDocument, returnType);
        } catch (XPathExpressionException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
