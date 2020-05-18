package staxreader;

import utils.XMLUtils;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class NumbeoSafetyListReader {
    public Map<String, Double> getCities(InputStream inputStream) throws XMLStreamException {
        XMLStreamReader reader = XMLUtils.getXMLStreamReader(inputStream);
        Map<String, Double> map = new HashMap<>();
        boolean isInItemLink = false;
        String startElement = "";
        String place = "";
        double safetyIndex = 0;

        while (reader.hasNext()) {
            int eventType = reader.getEventType();
            switch (eventType) {
                case XMLStreamConstants.START_ELEMENT:
                    startElement = reader.getLocalName();
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (startElement.equals("place")) {
                        place = reader.getText();
                    } else {
                        try {
                            safetyIndex = Double.parseDouble(reader.getText());
                        } catch (NumberFormatException e) {
                            safetyIndex = -1;
                        }
                    }
                break;
                case XMLStreamConstants.END_ELEMENT:
                    if (safetyIndex != -1) {
                        map.put(place, safetyIndex);
                    }
                    break;
            }
            if (reader.getEventType() != XMLStreamConstants.END_DOCUMENT) {
                reader.next();
            }
        }

        reader.close();
        return map;
    }
}
