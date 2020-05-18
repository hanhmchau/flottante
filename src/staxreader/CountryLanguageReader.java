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
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountryLanguageReader {
    public Map<String, List<Language>> getCountryLanguageMapping(InputStream inputStream) throws XMLStreamException, JAXBException, SAXException {
        Map<String, List<Language>> countries = new HashMap<>();
        XMLStreamReader reader = XMLUtils.getXMLStreamReader(inputStream);
        Unmarshaller um = XMLUtils.getUnmarshaller(Countries.class);
        Schema schema = XMLUtils.getSchema("src/models/countries.xsd");
        um.setSchema(schema);

        while (reader.hasNext()) {
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                try {
                    if (reader.getLocalName().equals("countries")) {
                        JAXBElement<Countries> countryJAXBElement = um.unmarshal(reader, Countries.class);
                        Countries c = countryJAXBElement.getValue();
                        c.getCountries().forEach(country -> {
                            countries.put(country.getName(), country.getLanguages());
                        });
                        break;
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
        return countries;
    }
}
