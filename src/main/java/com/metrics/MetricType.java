package com.metrics;

/**
 * @author Chris.Ge
 */
public enum MetricType {

    ClassLoadingGaugeSet("classloading"),
    GarbageCollectorMetricSet("gc"),
    MemoryUsageGaugeSet("memory"),
    ThreadDeadlockDetector("thread_deadlock"),
    ThreadStatesGaugeSet("thread_states");

    private String name;

    MetricType(String name) {
        this.name = name;
    }

    public static String getMetricType(String name) {

        for (MetricType metricType : MetricType.values()) {
            if (metricType.getName().equals(name))
                return metricType.name();
        }
        return name;
    }

    public static String getAllTypes() {

        return Arrays.stream(MetricType.values()).map(m -> m.name()).collect(joining(","));

    }

    public static String getAllClassNames() {

        return Arrays.stream(MetricType.values()).map(m -> m.getName()).collect(joining(","));

    }

    public String getName() {
        return name;
    }


}
