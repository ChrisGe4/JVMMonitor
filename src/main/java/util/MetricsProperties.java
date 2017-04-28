package util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Properties;


public class MetricsProperties {
    public static final Duration DEFAULT_INTERVAL = Duration.ofSeconds(15);
    public static final String PROPERTY_FILE_NAME = "metric.properties";
    public static final String TYPE_NAME = "metric.type";
    public static final String OUTPUT_NAME = "metric.output";
    public static final String FROM_NAME = "metrics.from";
    public static final String STATSD_HOST_NAME = "statsd.host";
    public static final String STATSD_PORT_NAME = "statsd.port";
    public static final String CONVERT_RATES_TO_NAME = "convertRatesTo";
    public static final String OUT_PUT = "statsd";
    public static final String CONVERT_RATES_TO = "MINUTES";
    public static final String STATSD_PORT = "8125";
    public static final String STATSD_HOST = "10.63.71.10";
    public static final String FROM = "jvm_monitor";
    public static final String TYPE = "classloading gc memory thread_states";
    private final static Logger log = LoggerFactory.getLogger(MetricsProperties.class);

    private final Properties prop;
    //public static final String PRE_FIXED_WITH = "prefixedWith";

    public MetricsProperties(Properties prop) {
        this.prop = prop;
    }


    public String getPropertyValue(String propertyName) {
        return getPropertyValue(propertyName, "");
    }
    

    public String getPropertyValue(String propertyName, String defaultValue) {
        String value = prop.getProperty(propertyName);
        return StringUtils.isEmpty(value) ? defaultValue : value;
    }

    public void setPropertyValue(String name, String value) {

        prop.setProperty(name, value);
    }

    public String getOutput() {
        return prop.getProperty(MetricsProperties.OUTPUT_NAME);
    }

    public String getConvertRatesTo() {
        return prop.getProperty(MetricsProperties.CONVERT_RATES_TO_NAME);
    }

    public String getStatsdPort() {
        return prop.getProperty(MetricsProperties.STATSD_PORT_NAME);
    }

    public String getStatsdHost() {

        return prop.getProperty(MetricsProperties.STATSD_HOST_NAME);

    }

    public String getMetricFrom() {
        return prop.getProperty(MetricsProperties.FROM_NAME);

    }

    public String getMetricType() {
        return prop.getProperty(MetricsProperties.TYPE_NAME);

    }
}
