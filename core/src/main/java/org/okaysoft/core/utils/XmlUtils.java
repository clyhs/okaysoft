package org.okaysoft.core.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.okaysoft.core.log.OkayLogger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



public class XmlUtils {
	
	protected static final OkayLogger log = new OkayLogger(XmlUtils.class);
	
	private XmlUtils() {
    }

	/**
     *验证类路径资源中的XML是否合法
     * @param xml 类路径资源
     * @return 是否验证通过
     */
    public static boolean validateXML(String xml) {
        if (!xml.startsWith("/")) {
            xml = "/" + xml;
        }
        String xmlPath = FileUtils.getAbsolutePath("/WEB-INF/classes" + xml);
        try {
            InputStream in = new FileInputStream(xmlPath);
            return validateXML(in);
        } catch (FileNotFoundException ex) {
            log.error("构造XML文件失败", ex);
        }
        return false;
    }
    /**
     *验证类路径资源中的XML是否合法
     * @param in XML文件输入流
     * @return 是否验证通过
     */
    public static boolean validateXML(InputStream in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(true);
            DocumentBuilder builder = dbf.newDocumentBuilder();
            builder.parse(new InputSource(in));
            return true;
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            log.error("验证XML失败",ex);
        }
        return false;
    }
}
