package metrics;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class MetricsSetup {
    @SuppressWarnings("resource")
    public void metrics() {
        MetricRegistry metricRegistry = new MetricRegistry();
        Slf4jReporter reporter = Slf4jReporter.forRegistry(metricRegistry)
                .outputTo(LoggerFactory.getLogger(SomeClass.class))
                .build();
        reporter.start(1, TimeUnit.SECONDS);

        Counter items = metricRegistry.counter("items");

    }

}
