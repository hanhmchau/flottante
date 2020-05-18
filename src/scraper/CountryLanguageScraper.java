package scraper;

import constants.PageConstants;
import models.Country;
import models.Language;
import org.xml.sax.SAXException;
import staxreader.CountryLanguageReader;
import staxreader.LanguageCodeReader;
import wellformer.HTMLWellformer;
import xsltapplier.XSLTApplier;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CountryLanguageScraper {
    private XSLTApplier xsltApplier = new XSLTApplier();

    List<Country> getCountryLanguageMapping(List<Country> countries) {
        try {
            String textContent = Utils.getContent(PageConstants.COUNTRY_LANGUAGE.SITE);
            if (textContent != null) {
                String wellformedHTML = HTMLWellformer.makeWellformed(textContent);
                ByteArrayOutputStream simplifiedXML = xsltApplier.applyStylesheet("src/resources/language/Countries.xsl", wellformedHTML);
                Map<String, List<Language>> countryLanguageMapping = (new CountryLanguageReader()).getCountryLanguageMapping(getInputStream(simplifiedXML));
                countries.forEach(c -> {
                    c.setLanguages(countryLanguageMapping.getOrDefault(c.getName(), new ArrayList<>()));
                });
            }
        } catch (IOException | TransformerException | XMLStreamException | JAXBException | SAXException e) {
            e.printStackTrace();
        }
        return countries;
    }

    private ByteArrayInputStream getInputStream(ByteArrayOutputStream simplifiedXML) {
        return new ByteArrayInputStream(simplifiedXML.toByteArray());
    }
}
