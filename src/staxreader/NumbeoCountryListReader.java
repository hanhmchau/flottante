package staxreader;

import constants.PageConstants;
import utils.XMLUtils;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class NumbeoCountryListReader {
    public Map<String, String> getCountries(InputStream inputStream) throws XMLStreamException {
        XMLStreamReader reader = XMLUtils.getXMLStreamReader(inputStream);
        Map<String, String> map = new LinkedHashMap<>();
        boolean isInCountryLink = false;
        String countryName;
        String countryLink = "";

        while (reader.hasNext()) {
            int eventType = reader.getEventType();
            switch (eventType) {
                case XMLStreamConstants.START_ELEMENT:
                    if (reader.getLocalName().equals("a")) {
                        isInCountryLink = true;
                        countryLink = reader.getAttributeValue(reader.getNamespaceURI(), "href");
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (isInCountryLink) {
                        countryName = reader.getText();
                        map.put(countryName, PageConstants.NUMBEO.getCountryCostOfLivingPage(countryLink));
                    }
                break;
                case XMLStreamConstants.END_ELEMENT:
                    isInCountryLink = false;
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
