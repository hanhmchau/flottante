package scraper;

import constants.PageConstants;
import staxreader.LanguageCodeReader;
import staxreader.MinimumWageReader;
import wellformer.HTMLWellformer;
import xsltapplier.XSLTApplier;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class MinimumWageScraper {
    private XSLTApplier xsltApplier = new XSLTApplier();
    Map<String, Double> getMinimumWage() {
        Map<String, Double> map = new HashMap<>();
        try {
            String textContent = Utils.getContent(PageConstants.MINIMUM_WAGE.SITE);
            if (textContent != null) {
                String wellformedHTML = HTMLWellformer.makeWellformed(textContent);
                ByteArrayOutputStream simplifiedXML = xsltApplier.applyStylesheet("src/resources/minwage/MinWage.xsl", wellformedHTML);
                return (new MinimumWageReader()).getMinimumWage(getInputStream(simplifiedXML));
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
