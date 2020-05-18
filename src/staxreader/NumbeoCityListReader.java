package staxreader;

import constants.PageConstants;
import utils.XMLUtils;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class NumbeoCityListReader {
    public Map<String, String> getCities(InputStream inputStream) throws XMLStreamException {
        XMLStreamReader reader = XMLUtils.getXMLStreamReader(inputStream);
        Map<String, String> map = new HashMap<>();
        boolean isInCityLink = false;
        String cityName;
        String cityLink = "";

        while (reader.hasNext()) {
            int eventType = reader.getEventType();
            switch (eventType) {
                case XMLStreamConstants.START_ELEMENT:
                    if (reader.getLocalName().equals("a")) {
                        isInCityLink = true;
                        cityLink = reader.getAttributeValue(reader.getNamespaceURI(), "href");
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (isInCityLink) {
                        cityName = reader.getText().trim();
                        cityLink = isValidUrl(cityLink) ? PageConstants.NUMBEO.appendCurrency(cityLink, PageConstants.USD) :
                                PageConstants.NUMBEO.getCityCostOfLivingPage(cityLink, PageConstants.USD);
                        map.putIfAbsent(cityName, cityLink);
                    }
                break;
                case XMLStreamConstants.END_ELEMENT:
                    isInCityLink = false;
                    break;
            }
            if (reader.getEventType() != XMLStreamConstants.END_DOCUMENT) {
                reader.next();
            }
        }

        reader.close();
        return map;
    }

    private boolean isValidUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
