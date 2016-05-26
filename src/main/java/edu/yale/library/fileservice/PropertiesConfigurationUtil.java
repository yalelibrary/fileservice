package edu.yale.library.fileservice;


import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PropertiesConfigurationUtil {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesConfigurationUtil.class);

    /**
     * for general props
     */
    private static Configuration general;

    /**
     * For getting login details
     */
    private static Configuration login;


    static {
        try {
            general = new PropertiesConfiguration("paths.properties");
        } catch (Exception e) {
            logger.error("Error setting property file", e);
        }
    }

    public static String getProperty(final String p) {
        return general.getProperty(p).toString();
    }


}