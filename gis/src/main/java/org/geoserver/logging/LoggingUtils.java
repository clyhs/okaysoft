/* Copyright (c) 2001 - 2013 OpenPlans - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.log4j.Appender;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.platform.GeoServerResourceLoader;
import org.vfny.geoserver.global.ConfigurationException;

public class LoggingUtils {
    
    public static final String RELINQUISH_LOG4J_CONTROL = "RELINQUISH_LOG4J_CONTROL";  
    public static final String GT2_LOGGING_REDIRECTION = "GT2_LOGGING_REDIRECTION";
    public static final String GEOSERVER_LOG_LOCATION = "GEOSERVER_LOG_LOCATION";
    public static enum GeoToolsLoggingRedirection {
        JavaLogging, CommonsLogging, Log4J;
        
     
        public static GeoToolsLoggingRedirection findValue(String name) {
            for (GeoToolsLoggingRedirection value : values()) {
                if(value.name().equalsIgnoreCase(name))
                    return value;
            }
            return Log4J;
        }
    }

    public static void configureGeoServerLogging(GeoServerResourceLoader loader, InputStream loggingConfigStream, boolean suppressStdOutLogging, boolean suppressFileLogging, String logFileName) throws FileNotFoundException, IOException,
                            ConfigurationException {
            
            List<Appender> appenders = new ArrayList();
            Enumeration a = LogManager.getRootLogger().getAllAppenders();
            while( a.hasMoreElements() ) {
                
            }
    
            Properties lprops = new Properties();
            lprops.load(loggingConfigStream);
            LogManager.resetConfiguration();
    //        LogLog.setQuietMode(true);
            PropertyConfigurator.configure(lprops);
    //        LogLog.setQuietMode(false);
            
            // configuring the log4j file logger
            if(!suppressFileLogging) {
                   
            }
            
            // ... and the std output logging too
            if (suppressStdOutLogging) {
                
            } 
            
            //add the appenders we saved above
            
            LoggingInitializer.LOGGER.fine("FINISHED CONFIGURING GEOSERVER LOGGING -------------------------");
        }

    public static void initLogging(GeoServerResourceLoader resourceLoader, String configFileName, boolean suppressStdOutLogging, String logFileName) throws Exception {
        
        LoggingInitializer.LOGGER.fine("CONFIGURING GEOSERVER LOGGING -------------------------");
        
        if (configFileName == null) {
            configFileName = "DEFAULT_LOGGING.properties";
            LoggingInitializer.LOGGER.warning("No log4jConfigFile defined in services.xml:  using 'DEFAULT_LOGGING.properties'");
        }
        
        File log4jConfigFile = resourceLoader.find( "logs", configFileName );
        if (log4jConfigFile == null) {
            
            LoggingInitializer.LOGGER.warning("log4jConfigFile '" + configFileName + "' couldn't be found in the data dir, so GeoServer will " +
            "install the various logging config file into the data dir, and then try to find it again.");
            
            File lcdir = resourceLoader.findOrCreateDirectory( "logs" );
            
            //now we copy in the various logging config files from the base repo location on the classpath
            final String[] lcfiles = new String[] { "DEFAULT_LOGGING.properties",
                    "VERBOSE_LOGGING.properties",
                    "PRODUCTION_LOGGING.properties",
                    "GEOTOOLS_DEVELOPER_LOGGING.properties",
                    "GEOSERVER_DEVELOPER_LOGGING.properties" };
            
            for (int i = 0; i < lcfiles.length; i++) {
                File target = new File(lcdir.getAbsolutePath(), lcfiles[i]);
                if (!target.exists()) {
                    resourceLoader.copyFromClassPath(lcfiles[i], target);
                }
            }
       
            log4jConfigFile = resourceLoader.find( "logs", configFileName );
            if (log4jConfigFile == null) {
                LoggingInitializer.LOGGER.warning("Still couldn't find log4jConfigFile '" + configFileName + "'.  Using DEFAULT_LOGGING.properties instead.");
            }
            
            log4jConfigFile = resourceLoader.find( "logs", "DEFAULT_LOGGING.properties" );
        }
    
        if (log4jConfigFile == null || !log4jConfigFile.exists()) {
            throw new ConfigurationException("Unable to load logging configuration '" + configFileName + "'.  In addition, an attempt " +
                    "was made to create the 'logs' directory in your data dir, and to use the DEFAULT_LOGGING configuration, but" +
                    "this failed as well.  Is your data dir writeable?");
        }
        
                // reconfiguring log4j logger levels by resetting and loading a new set of configuration properties
        InputStream loggingConfigStream = new FileInputStream(log4jConfigFile);
        if (loggingConfigStream == null) {
            LoggingInitializer.LOGGER.warning("Couldn't open Log4J configuration file '" + log4jConfigFile.getAbsolutePath());
            return;
        } else {
            LoggingInitializer.LOGGER.fine("GeoServer logging profile '" + log4jConfigFile.getName() + "' enabled.");
        }
    
        configureGeoServerLogging(resourceLoader, loggingConfigStream, suppressStdOutLogging, false, 
                                logFileName);
        
        
    }

    
    public static String getLogFileLocation(String baseLocation) {
        return getLogFileLocation(baseLocation, null);
    }

    
    public static String getLogFileLocation(String baseLocation, ServletContext context) {
        
        String location = context != null ?
            GeoServerExtensions.getProperty(LoggingUtils.GEOSERVER_LOG_LOCATION, context) :
            GeoServerExtensions.getProperty(LoggingUtils.GEOSERVER_LOG_LOCATION);
        if(location == null) {
            return baseLocation;
        } else {
            return location;
        }
    }

}
