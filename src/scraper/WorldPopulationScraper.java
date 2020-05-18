package scraper;

import constants.PageConstants;
import models.City;
import staxreader.PexelsImageReader;
import staxreader.WorldPopulationReader;
import wellformer.HTMLWellformer;
import xsltapplier.XSLTApplier;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class WorldPopulationScraper {
    private XSLTApplier xsltApplier = new XSLTApplier();
    List<City> getCityPopulations(List<City> cities) {
        try {
            String textContent = Utils.getGzippedContent(PageConstants.WORLD_POPULATION.SITE);
            if (textContent != null) {
                String wellformedHTML = HTMLWellformer.makeWellformed(textContent);
                ByteArrayOutputStream simplifiedXML = xsltApplier.applyStylesheet("src/resources/worldpopulation/Population.xsl", wellformedHTML);
                Map<String, Integer> cityPopulation = (new WorldPopulationReader()).getCityPopulation(getInputStream(simplifiedXML));
                cities.forEach(city -> {
                    city.setPopulation(cityPopulation.getOrDefault(city.getName(), 0));
                });
            }
        } catch (IOException | TransformerException | XMLStreamException e) {
            e.printStackTrace();
        }
        return cities;
    }

    private ByteArrayInputStream getInputStream(ByteArrayOutputStream simplifiedXML) {
        return new ByteArrayInputStream(simplifiedXML.toByteArray());
    }
}
