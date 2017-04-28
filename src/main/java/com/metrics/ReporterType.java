package com.metrics;

/**
 * @author Chris.Ge
 */
public enum ReporterType {


    CONSOLE("console"),
    STATSD("statsd");

    private String name;

    ReporterType(String name) {
        this.name = name;
    }

    public static String getReporterType(String name) {

        for (ReporterType reporterType : ReporterType.values()) {
            if (reporterType.getName().equals(name))
                return reporterType.name();
        }
        return name;
    }

    public static String getAllTypes() {

        return Arrays.stream(ReporterType.values()).map(m -> m.name()).collect(joining(","));

    }

    public static String getAllClassNames() {

        return Arrays.stream(ReporterType.values()).map(m -> m.getName()).collect(joining(","));

    }

    public String getName() {
        return name;
    }


}
