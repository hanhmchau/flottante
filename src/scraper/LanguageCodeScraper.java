package scraper;

import constants.PageConstants;
import models.City;
import staxreader.LanguageCodeReader;
import staxreader.WorldPopulationReader;
import wellformer.HTMLWellformer;
import xsltapplier.XSLTApplier;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LanguageCodeScraper {
    private XSLTApplier xsltApplier = new XSLTApplier();
    Map<String, String> getLanguageCodeMapping() {
        Map<String, String> map = new HashMap<>();
        try {
            String textContent = Utils.getContent(PageConstants.LANGUAGE_CODE.SITE);
            if (textContent != null) {
                String wellformedHTML = HTMLWellformer.makeWellformed(textContent);
                ByteArrayOutputStream simplifiedXML = xsltApplier.applyStylesheet("src/resources/language/Language.xsl", wellformedHTML);
                return (new LanguageCodeReader()).getLanguageMapping(getInputStream(simplifiedXML));
            }
        } catch (IOException | TransformerException | XMLStreamException e) {
            e.printStackTrace();
        }
        return map;
    }

    private ByteArrayInputStream getInputStream(ByteArrayOutputStream simplifiedXML) {
        return new ByteArrayInputStream(simplifiedXML.toByteArray());
    }
}
