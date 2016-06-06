package edu.yale.library.fileservice;


import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropsUtil {

    private static final Logger logger = LoggerFactory.getLogger(PropsUtil.class);

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
            general = new PropertiesConfiguration("application.properties");
        } catch (Exception e) {
            logger.error("Error setting property file", e);
        }
    }

    public static String getProperty(final String p) {
        return general.getProperty(p).toString();
    }


}