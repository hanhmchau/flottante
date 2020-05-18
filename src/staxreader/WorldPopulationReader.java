package staxreader;

import utils.XMLUtils;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class WorldPopulationReader {
    public Map<String, Integer> getCityPopulation(InputStream inputStream) throws XMLStreamException {
        XMLStreamReader reader = XMLUtils.getXMLStreamReader(inputStream);
        Map<String, Integer> map = new HashMap<>();
        String tagName = "";
        String cityName = "";
        int population = 0;

        while (reader.hasNext()) {
            int eventType = reader.getEventType();
            switch (eventType) {
                case XMLStreamConstants.START_ELEMENT:
                    tagName = reader.getLocalName();
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (tagName.equals("city")) {
                        cityName = reader.getText().trim();
                    } else if (tagName.equals("population")) {
                        population = Integer.parseInt(reader.getText().trim());
                    }
                break;
                case XMLStreamConstants.END_ELEMENT:
                    map.put(cityName, population);
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
