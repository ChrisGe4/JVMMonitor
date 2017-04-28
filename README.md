# JVM metrics

## Description
Provides an opinionated implementation of recording and reporting JVM metrics.

This module is base on dropwizard jvm metrics. 

## Usage

Config by property file **metric.properties**

```
metric.type=classloading gc memory thread_states //default
metric.output=statsd //default   or console 
convertRatesTo=MINUTES //default
convertDurationsTo=MINUTES //default
metrics.from=JVMMonitor  //default
statsd.port=8125 //default
statsd.host=10.63.71.199  //default localhost

```
or in the module


install module (java)

```
public class MyModule extends AbstractModule {
    @Override
    protected void configure() {
        MetricsModule metricsModule = new MetricsModule();
        metricsModule.configure((config) -> {
                      config.from = "test-jvm-metrics";//metric prefix
                  });
        });
        install(metricsModule);
    }
}
```

then start the reporter

```
In Ratpack, you may create a service like below:

          @Override
            public void onStart(StartEvent event) throws Exception {
                event.getRegistry().get(JVMMonitor.class).getReporter().get()
                    .start(10, TimeUnit.SECONDS);
            }


```

## Features

* Available Gauges:
   * ClassLoadingGaugeSet as "classloading"
   * GarbageCollectorMetricSet as "gc"
   * MemoryUsageGaugeSet as "memory"
   * ThreadDeadlockDetector as "thread_deadlock"
   * ThreadStatesGaugeSet as "thread_states"
 