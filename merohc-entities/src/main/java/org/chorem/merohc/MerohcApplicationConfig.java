package org.chorem.merohc;

import com.google.common.collect.Maps;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuiton.config.ApplicationConfig;
import org.nuiton.config.ArgumentsParserException;

import java.util.Map;
import java.util.Properties;

public class MerohcApplicationConfig {

    private static final Log log = LogFactory.getLog(MerohcApplicationConfig.class);

    protected ApplicationConfig applicationConfig;

    public MerohcApplicationConfig() {

        applicationConfig = new ApplicationConfig();

        // to allow using wao.config.path environment variable
        applicationConfig.setAppName("merohc");

        applicationConfig.setEncoding("UTF-8");

        applicationConfig.setConfigFileName("merohc.properties");

        try {
            applicationConfig.parse();
        } catch (ArgumentsParserException e) {
            throw new MerohcTechnicalException(e);
        }

        if (log.isInfoEnabled()) {
            log.info(applicationConfig.getFlatOptions());
        }
    }

    public Map<String, String> getTopiaProperties() {
        Map<String, String> jpaParameters = Maps.newHashMap();
        Properties hibernateProperties = applicationConfig.getOptionStartsWith("hibernate");
        jpaParameters.putAll((Map) hibernateProperties);
        Properties topiaProperties = applicationConfig.getOptionStartsWith("topia");
        jpaParameters.putAll((Map) topiaProperties);
        return jpaParameters;
    }

}
