/* Copyright (c) 2013 OpenPlans - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.gwc;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.platform.GeoServerExtensions;
import org.geotools.util.logging.Logging;
import org.geowebcache.config.ConfigurationException;
import org.geowebcache.diskquota.DiskQuotaConfig;
import org.geowebcache.diskquota.QuotaStore;
import org.geowebcache.diskquota.jdbc.JDBCConfiguration;
import org.geowebcache.diskquota.jdbc.JDBCQuotaStoreFactory;
import org.geowebcache.storage.DefaultStorageFinder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Loads/save and tests the JDBC configuration in the GeoServer environment, adding support for the
 * GUI and password encryption
 * 
 * @author Andrea Aime - GeoSolutions
 * 
 */
class JDBCConfigurationStorage implements ApplicationContextAware {

    static final Logger LOGGER = Logging.getLogger(JDBCConfigurationStorage.class);

    private JDBCPasswordEncryptionHelper passwordHelper;

    private DefaultStorageFinder storageFinder;

    private ApplicationContext applicationContext;

    public JDBCConfigurationStorage(DefaultStorageFinder storageFinder) {
        this.storageFinder = storageFinder;
    }

    public synchronized void saveDiskQuotaConfig(DiskQuotaConfig config,
            JDBCConfiguration jdbcConfig) throws ConfigurationException, IOException,
            InterruptedException {
        File configFile = new File(storageFinder.getDefaultPath(), "geowebcache-diskquota-jdbc.xml");
        if ("JDBC".equals(config.getQuotaStore())) {
            JDBCConfiguration encrypted = passwordHelper.encryptPassword(jdbcConfig);
            JDBCConfiguration.store(encrypted, configFile);
        } else {
            if (configFile.exists() && !configFile.delete()) {
                LOGGER.log(Level.SEVERE, "Failed to delete " + configFile
                        + ", this might cause misbehavior on GeoServer restart");
            }
        }
    }

    public synchronized JDBCConfiguration getJDBCDiskQuotaConfig() throws IOException,
            org.geowebcache.config.ConfigurationException {
        File configFile = new File(storageFinder.getDefaultPath(), "geowebcache-diskquota-jdbc.xml");
        if (!configFile.exists()) {
            return null;
        }
        try {
            JDBCConfiguration configuration = JDBCConfiguration.load(configFile);
            return passwordHelper.unencryptPassword(configuration);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load geowebcache-diskquota-jdbc.xml", e);
            return null;
        }
    }

    /**
     * Checks the JDBC quota store can be instantiated
     * 
     * @param config
     * @param jdbcConfiguration
     * @throws ConfigurationException
     */
    public void testQuotaConfiguration(JDBCConfiguration jdbcConfiguration)
            throws ConfigurationException, IOException {
        JDBCQuotaStoreFactory factory = GeoServerExtensions.bean(JDBCQuotaStoreFactory.class);
        QuotaStore qs = null;
        try {
            qs = factory.getJDBCStore(applicationContext, jdbcConfiguration);
        } finally {
            if (qs != null) {
                try {
                    qs.close();
                } catch (Exception e) {
                    LOGGER.log(Level.FINE, "Failed to dispose test quota store", e);
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    

}
