package org.okaysoft.core.service;

import java.io.IOException;
import java.io.StringWriter;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.okaysoft.core.log.OkayLogger;

public abstract class ChartService {
	
	protected final OkayLogger log = new OkayLogger(getClass());
	
	protected String formatXML(Element rootElement) {
        StringWriter writer = new StringWriter();
        try {
            Document chartDocument = new Document(rootElement);
            XMLOutputter outputter = new XMLOutputter();
            outputter.setFormat(Format.getPrettyFormat());
            outputter.output(chartDocument, writer);
        } catch (IOException e) {
            log.error("保生成XML出错",e);
        }
        return writer.toString();
    }

    protected Element createRootElement(String caption, String subCaption) {
        Element rootElement = new Element("chart");
        rootElement.setAttribute(new Attribute("caption", caption));
        rootElement.setAttribute(new Attribute("subCaption", subCaption));
        return rootElement;
    }

}
