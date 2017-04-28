package com.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.ScheduledReporter;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.readytalk.metrics.StatsDReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.codahale.metrics.MetricRegistry.name;
import static com.google.common.base.Preconditions.checkNotNull;



/**
 * @author Chris.Ge
 */
@Singleton
public class JVMMonitor {

    private final static Logger log = LoggerFactory.getLogger(JVMMonitor.class);
    private final JVMMetricsConfig properties;
    private final MetricRegistry registry;
    private final String GAUGE_PATH = "com.codahale.metrics.jvm.";


    @Inject
    public JVMMonitor(JVMMetricsConfig properties, MetricRegistry registry) {
        this.properties = properties;
        this.registry = registry;
        register();
    }


    public JVMMonitor() {
        this(new JVMMetricsConfig(), new MetricRegistry());

    }


    public Optional<ScheduledReporter> getReporter() {

        //        String output = properties.getPropertyValue(MetricsProperties.OUTPUT_NAME, DEFAULT_OUTPUT);

        Optional<ScheduledReporter> reporter;


        if (ReporterType.STATSD.getName().equals(properties.output)) {
            reporter = getStatsDReporter();
        } else {
            reporter = getConsoleReporter();
        }
        return reporter;

    }

    public Optional<ScheduledReporter> getConsoleReporter() {
        TimeUnit timeUnit = TimeUnit.valueOf(
            //  properties.getPropertyValue(MetricsProperties.CONVERT_RATES_TO_NAME, DEFAULT_TIMEUNIT)
            properties.convertRatesTo.toUpperCase());
        return Optional
            .of(ConsoleReporter.forRegistry(registry).outputTo(System.out).convertRatesTo(timeUnit)
                .build());
        //console.start(10, TimeUnit.SECONDS);


    }

    public Optional<ScheduledReporter> getStatsDReporter() {

        if (Strings.isNullOrEmpty(properties.statsdHost) || Strings
            .isNullOrEmpty(properties.statsdPort)) {
            log.error("StatsD host or port is empty");
            return Optional.empty();
        }

        return Optional.of(StatsDReporter.forRegistry(registry)
            .build(properties.statsdHost, Integer.valueOf(properties.statsdPort)));
    }

    private void register() {

        String[] types = properties.type.split(" ");
        Arrays.stream(types).forEach(type -> {
            try {
                registry.register(name(properties.from, type), getMetricSetInstance(type.trim()));
            } catch (Throwable e) {
                log.warn("A Dropwizard JVM metric class type " + type
                        + " cannot be instantiated. Valid types are " + MetricType.getAllClassNames(),
                    e);
            }
        });

    }


    private MetricSet getMetricSetInstance(String type)
        throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
        InvocationTargetException, InstantiationException {
        Class gaugeClass = Class.forName(GAUGE_PATH + MetricType.getMetricType(type));
        checkNotNull(gaugeClass);
        Constructor constructor = gaugeClass.getConstructor();
        constructor.setAccessible(true);
        return (MetricSet) constructor.newInstance();

    }

}
