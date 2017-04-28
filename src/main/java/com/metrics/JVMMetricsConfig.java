package com.metrics;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import javax.inject.Singleton;
import java.time.Duration;

/**
 * @author Chris.Ge
 */
@Singleton
public class JVMMetricsConfig {

    public static final Duration DEFAULT_INTERVAL = Duration.ofSeconds(15);

    @Inject(optional = true)
    @Named("metric.type")
    public String type = "classloading gc memory thread_states";
    @Inject(optional = true)
    @Named("metric.output")
    public String output = "statsd";
    @Inject(optional = true)
    @Named("convertRatesTo")
    public String convertRatesTo = "MINUTES";
    @Inject(optional = true)
    @Named("statsd.port")
    public String statsdPort = "8125";
    @Inject(optional = true)
    @Named("statsd.host")
    public String statsdHost = "localhost";
    @Inject(optional = true)
    @Named("metrics.from")
    public String from = "jvm_monitor";

    public JVMMetricsConfig() {
    }

    public JVMMetricsConfig(JVMMetricsConfig config) {

        this.type = config.type;
        this.output = config.output;
        this.convertRatesTo = config.convertRatesTo;
        this.statsdHost = config.statsdHost;
        this.statsdPort = config.statsdPort;
        this.from = config.from;

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JVMMetricsConfig{");
        sb.append("type='").append(type).append('\'');
        sb.append(", output='").append(output).append('\'');
        sb.append(", convertRatesTo='").append(convertRatesTo).append('\'');
        sb.append(", statsdPort='").append(statsdPort).append('\'');
        sb.append(", statsdHost='").append(statsdHost).append('\'');
        sb.append(", from='").append(from).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
