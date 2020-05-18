package staxreader;

import utils.XMLUtils;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class LanguageCodeReader {
    public Map<String, String> getLanguageMapping(InputStream inputStream) throws XMLStreamException {
        XMLStreamReader reader = XMLUtils.getXMLStreamReader(inputStream);
        Map<String, String> languageMapping = new HashMap<>();
        String tagName = "";
        String languageName = "";
        String languageCode = "";

        while (reader.hasNext()) {
            int eventType = reader.getEventType();
            switch (eventType) {
                case XMLStreamConstants.START_ELEMENT:
                    tagName = reader.getLocalName();
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (tagName.equals("name")) {
                        languageName = reader.getText();
                    } else if (tagName.equals("code")) {
                        languageCode = reader.getText();
                    }
                break;
                case XMLStreamConstants.END_ELEMENT:
                    languageMapping.put(languageCode, languageName);
                    break;
            }
            if (reader.getEventType() != XMLStreamConstants.END_DOCUMENT) {
                reader.next();
            }
        }

        reader.close();
        return languageMapping;
    }
}
