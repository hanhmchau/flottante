package utils;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;

public class XMLUtils {
    public static Unmarshaller getUnmarshaller(Class clazz) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        return jaxbContext.createUnmarshaller();
    }

    public static Object unmarshal(String str, Class clazz) throws JAXBException {
        Unmarshaller unmarshaller = getUnmarshaller(clazz);
        StringReader stringReader = new StringReader(str);
        return unmarshaller.unmarshal(stringReader);
    }

    public static String marshal(Object object) throws JAXBException {
        Marshaller marshaller = getMarshaller(object.getClass());
        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(object, stringWriter);
        return stringWriter.toString();
    }

    public static Marshaller getMarshaller(Class clazz) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        return jaxbContext.createMarshaller();
    }

    public static XMLStreamReader getXMLStreamReader(InputStream inputStream) throws XMLStreamException {
        XMLInputFactory xif = XMLInputFactory.newFactory();
        return xif.createXMLStreamReader(inputStream);
    }

    public static Schema getSchema(String file) throws SAXException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        return schemaFactory.newSchema(new File(file));
    }

    public static Document parseFileToDOM(String filePath) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(filePath);
    }
}
