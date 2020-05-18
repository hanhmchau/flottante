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
import staxreader.NumbeoSafetyListReader;
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

class NumbeoSafetyScraper {
    private XSLTApplier xsltApplier = new XSLTApplier();
    Map<String, Double> getCitiesWithSafetyRanking() {
        List<City> cities = new ArrayList<>();
        try {
            String textContent = Utils.getContent(PageConstants.NUMBEO.SAFETY_PAGE);
            String wellformedHTML = HTMLWellformer.makeWellformed(textContent);
            ByteArrayOutputStream simplifiedXML = xsltApplier.applyStylesheet("src/resources/numbeo/SafetyPage.xsl", wellformedHTML);
            return (new NumbeoSafetyListReader()).getCities(getInputStream(simplifiedXML));
        } catch (IOException | TransformerException | XMLStreamException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    Map<String, Double> getCountriesWithSafetyIndex() {
        try {
            String textContent = Utils.getContent(PageConstants.NUMBEO.SAFETY_PAGE_BY_COUNTRIES);
            String wellformedHTML = HTMLWellformer.makeWellformed(textContent);
            ByteArrayOutputStream simplifiedXML = xsltApplier.applyStylesheet("src/resources/numbeo/SafetyPage.xsl", wellformedHTML);
            return (new NumbeoSafetyListReader()).getCities(getInputStream(simplifiedXML));
        } catch (IOException | TransformerException | XMLStreamException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private ByteArrayInputStream getInputStream(ByteArrayOutputStream simplifiedXML) {
        return new ByteArrayInputStream(simplifiedXML.toByteArray());
    }
}
