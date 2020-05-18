package xsltapplier;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class XSLTApplier {
    private TransformerFactory tf = TransformerFactory.newInstance();

    private Transformer getTransformer(String stylesheet) throws TransformerConfigurationException {
        StreamSource xsltFile = new StreamSource(stylesheet);
        Templates templates = tf.newTemplates(xsltFile);
        return templates.newTransformer();
    }

    private ByteArrayOutputStream applyStylesheet(String stylesheet, InputStream xmlStream) throws TransformerException {
        Transformer transformer = getTransformer(stylesheet);
        StreamSource xmlFile = new StreamSource(xmlStream);
        StreamResult resultStream = new StreamResult(new ByteArrayOutputStream());
        transformer.transform(xmlFile, resultStream);
        return (ByteArrayOutputStream) resultStream.getOutputStream();
    }

    public ByteArrayOutputStream applyStylesheet(String stylesheet, String xmlContent) throws TransformerException {
        return applyStylesheet(stylesheet, new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));
    }

    public ByteArrayOutputStream applyStylesheet(String stylesheet, byte[] xmlContent) throws TransformerException {
        return applyStylesheet(stylesheet, new ByteArrayInputStream(xmlContent));
    }
}
