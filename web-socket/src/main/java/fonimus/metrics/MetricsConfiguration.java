package fonimus.metrics;

import fonimus.jpa.HistogramRepository;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.codahale.metrics.MetricRegistry;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;

/**
 * Created by francois on 05/03/2017.
 */
@Configuration
@EnableMetrics(proxyTargetClass = true)
public class MetricsConfiguration
		extends MetricsConfigurerAdapter {

	@Autowired
	private HistogramRepository histogramRepository;

	@Override
	public void configureReporters(MetricRegistry metricRegistry) {
		registerReporter(
				new MetricsDatabaseReporter(metricRegistry, "database-reporter", (name, metric) -> name.startsWith("fonimus."), TimeUnit.SECONDS,
						TimeUnit.MILLISECONDS, histogramRepository)).start(10, TimeUnit.SECONDS);
	}
}
