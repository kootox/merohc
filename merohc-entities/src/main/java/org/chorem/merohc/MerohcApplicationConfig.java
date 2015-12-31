package org.chorem.merohc;

import com.google.common.collect.Maps;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuiton.config.ApplicationConfig;
import org.nuiton.config.ArgumentsParserException;
import org.nuiton.topia.persistence.TopiaConfiguration;
import org.nuiton.topia.persistence.TopiaConfigurationBuilder;

import java.util.Map;
import java.util.Properties;

public class MerohcApplicationConfig {

    private static final Log log = LogFactory.getLog(MerohcApplicationConfig.class);

    protected ApplicationConfig applicationConfig;

    protected TopiaConfiguration topiaConfiguration;

    public MerohcApplicationConfig(String configFileName) {

        applicationConfig = new ApplicationConfig();

        // to allow using wao.config.path environment variable
        applicationConfig.setAppName("merohc");

        applicationConfig.setEncoding("UTF-8");

        applicationConfig.setConfigFileName(configFileName);

        try {
            applicationConfig.parse();
        } catch (ArgumentsParserException e) {
            throw new MerohcTechnicalException(e);
        }

        if (log.isInfoEnabled()) {
            log.info(applicationConfig.getFlatOptions());
        }

        topiaConfiguration = new TopiaConfigurationBuilder().readProperties(applicationConfig.getFlatOptions());
    }

    public Map<String, String> getTopiaProperties() {
        Map<String, String> jpaParameters = Maps.newHashMap();
        Properties hibernateProperties = applicationConfig.getOptionStartsWith("hibernate");
        jpaParameters.putAll((Map) hibernateProperties);
        Properties topiaProperties = applicationConfig.getOptionStartsWith("topia");
        jpaParameters.putAll((Map) topiaProperties);
        return jpaParameters;
    }

    public TopiaConfiguration getTopiaConfiguration() {
        return topiaConfiguration;
    }
}
