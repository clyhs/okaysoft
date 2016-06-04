package org.okaysoft.core.service;

import java.util.Locale;
import java.util.Properties;

import org.okaysoft.core.log.OkayLogger;
import org.springframework.core.io.ClassPathResource;

public class PropertyHolder {
	
	protected static final OkayLogger log =new OkayLogger(PropertyHolder.class);
	
	private static Properties props = new Properties();
	
	public static Properties getProperties() {
        return props;
    }
	
	static{
		reload();
	}
	
	public static void reload(){
		String dbConfig = "jdbc.properties";
		String sysConfig = "config.properties";
		
		ClassPathResource cr = null;
		try{
            cr = new ClassPathResource(sysConfig);
            props.load(cr.getInputStream());
            log.info("装入数主配置文件:"+sysConfig);
            log.info("main profile is loaded: "+sysConfig, Locale.ENGLISH);
        }catch(Exception e){
            log.info("装入主配置文件"+dbConfig+"失败!", e);
            log.info("Failed to load main profile "+sysConfig+"!", e, Locale.ENGLISH);
        }
		try{
            cr = new ClassPathResource(dbConfig);
            props.load(cr.getInputStream());
            log.info("装入数据库配置文件:"+dbConfig);
            log.info("jdbc profile is loaded: "+dbConfig, Locale.ENGLISH);
        }catch(Exception e){
            log.info("装入数据库文件"+dbConfig+"失败!", e);
            log.info("Failed to load jdbc profile "+dbConfig+"!", e, Locale.ENGLISH);
        }
		
		log.info("******************属性列表***************************");
        log.info("******************Properties List********************", Locale.ENGLISH);
        for(String propertyName : props.stringPropertyNames()){
            //log.info("  "+propertyName+" = "+props.getProperty(propertyName));
        }
        log.info("***********************************************************");
        
        //指定日志输出语言
        OkayLogger.setConfigLanguage(getLogLanguage());
	}
	
	/**
     * 日志使用什么语言输出
     * @return 
     */
    public static Locale getLogLanguage(){
       String language = getProperty("log.locale.language");
       return Locale.forLanguageTag(language);
    }

    public static boolean getBooleanProperty(String name) {
        String value = props.getProperty(name);

        return "true".equals(value);
    }

    public static int getIntProperty(String name) {
        String value = props.getProperty(name);

        return Integer.parseInt(value);
    }

    public static String getProperty(String name) {
        String value = props.getProperty(name);

        return value;
    }

    public static void setProperty(String name, String value) {
        props.setProperty(name, value);
    }

}
