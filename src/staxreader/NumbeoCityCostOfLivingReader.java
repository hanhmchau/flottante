package staxreader;

import models.CostOfLiving;
import org.xml.sax.SAXException;
import utils.XMLUtils;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.validation.Schema;
import java.io.InputStream;

public class NumbeoCityCostOfLivingReader {
    public CostOfLiving getCostOfLiving(InputStream inputStream) throws XMLStreamException, JAXBException, SAXException {
        CostOfLiving costOfLiving = null;
        XMLStreamReader reader = XMLUtils.getXMLStreamReader(inputStream);
        Unmarshaller um = XMLUtils.getUnmarshaller(CostOfLiving.class);
        Schema schema = XMLUtils.getSchema("src/models/cost_of_living.xsd");
        um.setSchema(schema);

        while (reader.hasNext()) {
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                try {
                    if (reader.getLocalName().equals("cost-of-living")) {
                        JAXBElement<CostOfLiving> costOfLivingJAXBElement = um.unmarshal(reader, CostOfLiving.class);
                        costOfLiving = costOfLivingJAXBElement.getValue();
                    }
                } catch (UnmarshalException e) {
                    e.printStackTrace();
                }
            }
            if (reader.getEventType() != XMLStreamConstants.END_DOCUMENT) {
                reader.next();
            }
        }

        reader.close();
        return costOfLiving;
    }
}
