package com.metrics;

import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Strings;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import error.ConfigFilePathNullException;
import util.MetricsProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * @author chris.ge
 */
public class MetricModule extends AbstractModule {
    public static final String PROPERTY_FILE_NAME = "metric.properties";

    JVMMetricsConfig config = new JVMMetricsConfig();
    Consumer<JVMMetricsConfig> configConsumer = jvmMetricsConfig -> {
    };


    @Override
    public void configure() {
        bind(MetricRegistry.class).in(Singleton.class);
        bind(JVMMonitor.class);
    }


    public void configure(Consumer<JVMMetricsConfig> configConsumer) {
        this.configConsumer = configConsumer;
    }


    @Provides
    @Singleton
    public JVMMetricsConfig metricsProperties() {
        MetricsProperties properties = new MetricsProperties(getConfigProperties());

        if (!Strings.isNullOrEmpty(properties.getMetricType()))
            config.type = properties.getMetricType();
        if (!Strings.isNullOrEmpty(properties.getMetricFrom()))
            config.from = properties.getMetricFrom();
        if (!Strings.isNullOrEmpty(properties.getConvertRatesTo()))
            config.convertRatesTo = properties.getConvertRatesTo();
        if (!Strings.isNullOrEmpty(properties.getStatsdHost()))
            config.statsdHost = properties.getStatsdHost();
        if (!Strings.isNullOrEmpty(properties.getStatsdPort()))
            config.statsdPort = properties.getStatsdPort();
        if (!Strings.isNullOrEmpty(properties.getOutput()))
            config.output = properties.getOutput();
        configConsumer.accept(config);
        return config;

    }

    Properties getConfigProperties() {

        String propertyFile = System.getProperty(PROPERTY_FILE_NAME, PROPERTY_FILE_NAME);
        final File propFile = new File(propertyFile);
        Properties prop = new Properties();
        try (final InputStream is = (propFile.isFile() && propFile.canRead()) ?
            new FileInputStream(propFile) :
            this.getClass().getClassLoader().getResourceAsStream(propertyFile)) {

            if (is != null) {
                prop.load(is);
            }
        } catch (Throwable t) {
            throw ConfigFilePathNullException.getException(ConfigFilePathNullException.class,
                String.format("Unable to read %s. ", propertyFile), t.getMessage());
        }
        return prop;
    }

}
