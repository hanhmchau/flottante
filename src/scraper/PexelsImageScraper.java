package scraper;

import analyzer.NumbeoCostOfLivingProcessor;
import constants.PageConstants;
import models.Category;
import models.City;
import models.CostOfLiving;
import models.Country;
import org.xml.sax.SAXException;
import staxreader.NumbeoCityCostOfLivingReader;
import staxreader.NumbeoCityListReader;
import staxreader.NumbeoCountryListReader;
import staxreader.PexelsImageReader;
import wellformer.HTMLWellformer;
import xsltapplier.XSLTApplier;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PexelsImageScraper {
    private XSLTApplier xsltApplier = new XSLTApplier();
    List<String> getCityImages(String cityName, int size) {
        List<String> images = new ArrayList<>();
        try {
            String textContent = Utils.getContent(PageConstants.PEXELS.getCityImagesPage(cityName));
            if (textContent != null) {
                String wellformedHTML = HTMLWellformer.makeWellformed(textContent);
                ByteArrayOutputStream simplifiedXML = xsltApplier.applyStylesheet("src/resources/pexels/Search.xsl", wellformedHTML);
                images = (new PexelsImageReader()).getImages(getInputStream(simplifiedXML));
            }
        } catch (IOException | TransformerException | XMLStreamException e) {
//            e.printStackTrace();
        }
        return images.subList(0, Math.min(size, images.size()));
    }

    private ByteArrayInputStream getInputStream(ByteArrayOutputStream simplifiedXML) {
        return new ByteArrayInputStream(simplifiedXML.toByteArray());
    }
}
