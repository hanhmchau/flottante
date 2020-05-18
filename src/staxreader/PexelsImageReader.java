package staxreader;

import utils.XMLUtils;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PexelsImageReader {
    public List<String> getImages(InputStream inputStream) throws XMLStreamException {
        XMLStreamReader reader = XMLUtils.getXMLStreamReader(inputStream);
        List<String> images = new ArrayList<>();
        boolean isInImageElement = false;

        while (reader.hasNext()) {
            int eventType = reader.getEventType();
            switch (eventType) {
                case XMLStreamConstants.START_ELEMENT:
                    if (reader.getLocalName().equals("image")) {
                        isInImageElement = true;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (isInImageElement) {
                        images.add(reader.getText().trim());
                    }
                break;
                case XMLStreamConstants.END_ELEMENT:
                    isInImageElement = false;
                    break;
            }
            if (reader.getEventType() != XMLStreamConstants.END_DOCUMENT) {
                reader.next();
            }
        }

        reader.close();
        return images;
    }
}
