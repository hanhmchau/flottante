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
import wellformer.HTMLWellformer;
import xsltapplier.XSLTApplier;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

class NumbeoCostOfLivingScraper {
    private XSLTApplier xsltApplier = new XSLTApplier();
    List<City> getCitiesWithCostOfLiving(List<Category> categoryWeights, Set<Category> unknownCategories, List<Country> countries) {
        try {
            String textContent = Utils.getContent(PageConstants.NUMBEO.COST_OF_LIVING_PAGE);
            String wellformedHTML = HTMLWellformer.makeWellformed(textContent);
            ByteArrayOutputStream simplifiedXML = xsltApplier.applyStylesheet("src/resources/numbeo/HomePage.xsl", wellformedHTML);
            Map<String, String> countrySites = (new NumbeoCountryListReader()).getCountries(getInputStream(simplifiedXML));
            Map<City, String> citySites = new HashMap<>();
            List<City> cities = new ArrayList<>();
            for (Map.Entry<String, String> e : countrySites.entrySet()) {
                String name = e.getKey();
                if (name.contains(",")) {
                    name = name.substring(0, e.getKey().indexOf(","));
                }
                String link = e.getValue();
//                if (name.startsWith("U"))
                processCountryPage(name, link, citySites, countries);
            }
            System.out.println("Done processing countries. Countries gotten: " + countrySites.size());
            for (Map.Entry<City, String> entry : citySites.entrySet()) {
                City key = entry.getKey();
                String value = entry.getValue();
                processCityPage(key, value, cities);
            }
            System.out.println("Done processing cities. Cities gotten: " + cities.size());
            return cities;
        } catch (IOException | TransformerException | XMLStreamException | JAXBException | SAXException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void processCityPage(City city, String cityPage, List<City> cities) throws IOException, TransformerException, XMLStreamException, JAXBException, SAXException {
        String cityContent = Utils.getContent(cityPage);
        String wellformedHTML = HTMLWellformer.makeWellformed(cityContent);
        ByteArrayOutputStream simplifiedXML = xsltApplier.applyStylesheet("src/resources/numbeo/CityPage.xsl", wellformedHTML);
        ByteArrayOutputStream structuredXML = xsltApplier.applyStylesheet("src/resources/numbeo/CostOfLiving.xsl", simplifiedXML.toByteArray());
        CostOfLiving costOfLiving = (new NumbeoCityCostOfLivingReader()).getCostOfLiving(getInputStream(structuredXML));
        city.setCostOfLiving(costOfLiving);
        cities.add(city);
    }

    private void processCountryPage(String countryName, String countryPage, Map<City, String> citySites, List<Country> countries) throws IOException, TransformerException, XMLStreamException {
        String countryContent = Utils.getContent(countryPage);
        String wellformedHTML = HTMLWellformer.makeWellformed(countryContent);
        ByteArrayOutputStream simplifiedXML = xsltApplier.applyStylesheet("src/resources/numbeo/CountryPage.xsl", wellformedHTML);
        Map<String, String> newSites = (new NumbeoCityListReader()).getCities(getInputStream(simplifiedXML));
        Country country = new Country();
        country.setName(countryName);
        countries.add(country);
        newSites.forEach((cityName, cityLink) -> citySites.put(new City(cityName, country), cityLink));
    }

    private ByteArrayInputStream getInputStream(ByteArrayOutputStream simplifiedXML) {
        return new ByteArrayInputStream(simplifiedXML.toByteArray());
    }
}
