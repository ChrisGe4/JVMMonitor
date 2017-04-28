package com.metrics;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * @author Chris.Ge
 */

public class JVMMonitorTest {

    JVMMonitor monitor;

    private MetricRegistry registry;
    private JVMMetricsConfig metricsProperties;

    @Before
    public void setup() {

        //  System.setProperty("metric.properties", "src/test/resources/metric.properties");
        System.setProperty("log.level", "DEBUG");
        registry = new MetricRegistry();
        metricsProperties = new JVMMetricsConfig();


    }

    @Test
    public void testStatsD() throws InterruptedException {
        monitor = new JVMMonitor(metricsProperties, registry);
        ScheduledReporter reporter = monitor.getReporter().get();
        reporter.start(3, TimeUnit.SECONDS);
        Thread.sleep(15000L);

    }

    @Test
    public void testConsole() throws InterruptedException {
        metricsProperties.output = "console";
        registry.register(name("cache-evictions"), (Gauge<Integer>) () -> 55);
        JVMMonitor monitor1 = new JVMMonitor(metricsProperties, registry);
        ScheduledReporter reporter = monitor1.getReporter().get();
        reporter.start(3, TimeUnit.SECONDS);
        Thread.sleep(5000L);
        reporter.stop();

    }

    @Test
    public void testInvalidStatsDReporter() {
        metricsProperties.statsdPort = "";
        JVMMonitor monitor1 = new JVMMonitor(metricsProperties, registry);
        Optional<ScheduledReporter> reporter = monitor1.getReporter();
        Assert.assertTrue(!reporter.isPresent());
    }


    @Test
    public void testMetricModule() {
        MetricModule metricModule = new MetricModule();
        metricModule.configure(config -> {
            config.from = "test-jvm-metrics";
        });

        Injector injector = Guice.createInjector(metricModule);
        JVMMetricsConfig config = injector.getInstance(JVMMetricsConfig.class);
        System.out.println("config = " + config);

    }

    @Test
    public void testWithRatpack() throws Exception {

        //        EmbeddedApp.of(s -> s.serverConfig(ServerConfig.embedded())
        //            .registry(Guice.registry(b -> b.module(MetricModule.class, config -> {
        //                    config.setOutput("console");
        //
        //                }).bindInstance(new Service() {
        //
        //
        //
        //                    @Override
        //                    public void onStop(StopEvent event) throws Exception {
        //                        JVMMonitor monitor = event.getRegistry().get(JVMMonitor.class);
        //                        ScheduledReporter reporter = monitor.getReporter().get();
        //                        reporter.stop();
        //                    }
        //
        //                    @Override
        //                    public void onStart(StartEvent event) throws Exception {
        //                        System.out.println("event = " + event.getRegistry().toString());
        //                        JVMMonitor monitor = event.getRegistry().get(JVMMonitor.class);
        //                        System.out.println("monitor = " + monitor);
        //                        ScheduledReporter reporter = monitor.getReporter().get();
        //                        reporter.start(2, TimeUnit.SECONDS);
        //                        Thread.sleep(3000L);
        //
        //
        //                    }
        //                })
        //
        //            ))).test(testHttpClient -> System.out.print("tested"));

        //        RatpackServer server = RatpackServer.of(s -> s.serverConfig(ServerConfig.embedded())
        //            .registry(Guice.registry(b -> b.module(MetricModule.class, config -> {
        //                    //config.setOutput("console");
        //                    //config.setOutput("statsd");
        //                    config.setFrom("test");
        //                }).bindInstance(new Service() {
        //                    @Override
        //                    public void onStop(StopEvent event) throws Exception {
        //                        JVMMonitor monitor = event.getRegistry().get(JVMMonitor.class);
        //                        ScheduledReporter reporter = monitor.getReporter().get();
        //                        reporter.stop();
        //                    }
        //
        //                    @Override
        //                    public void onStart(StartEvent event) throws Exception {
        //                        JVMMonitor monitor = event.getRegistry().get(JVMMonitor.class);
        //                        ScheduledReporter reporter = monitor.getReporter().get();
        //                        reporter.start(2, TimeUnit.SECONDS);
        //                        Thread.sleep(3000L);
        //                    }
        //                })
        //
        //            ))
        //
        //            .handler(r -> ctx -> ctx.render("ok")));
        //        server.start();
        //        Thread.sleep(5000L);
        //        server.stop();
        //
    }



}
