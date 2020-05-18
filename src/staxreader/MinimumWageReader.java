package staxreader;

import models.Countries;
import models.Language;
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
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinimumWageReader {
    public Map<String, Double> getMinimumWage(ByteArrayInputStream inputStream) throws XMLStreamException {
        XMLStreamReader reader = XMLUtils.getXMLStreamReader(inputStream);
        Map<String, Double> minWageMapping = new HashMap<>();
        String tagName = "";
        String name = "";
        double wage = 0;

        while (reader.hasNext()) {
            int eventType = reader.getEventType();
            switch (eventType) {
                case XMLStreamConstants.START_ELEMENT:
                    tagName = reader.getLocalName();
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (tagName.equals("name")) {
                        name = reader.getText();
                    } else if (tagName.equals("wage")) {
                        try {
                            wage = Double.parseDouble(reader.getText());
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    minWageMapping.put(name, wage);
                    break;
            }
            if (reader.getEventType() != XMLStreamConstants.END_DOCUMENT) {
                reader.next();
            }
        }

        reader.close();
        return minWageMapping;
    }
}
