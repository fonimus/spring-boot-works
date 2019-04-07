package fonimus.metrics;

import fonimus.jpa.HistogramRepository;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.codahale.metrics.Timer;

/**
 * Created by francois on 05/03/2017.
 */
public class MetricsDatabaseReporter extends ScheduledReporter {

	private HistogramRepository histogramRepository;

	protected MetricsDatabaseReporter(MetricRegistry registry, String name, MetricFilter filter,
			TimeUnit rateUnit, TimeUnit durationUnit, HistogramRepository histogramRepository) {
		super(registry, name, filter, rateUnit, durationUnit);
		this.histogramRepository = histogramRepository;
	}

	@Override
	public void report(SortedMap<String, Gauge> gauges, SortedMap<String, Counter> counters, SortedMap<String, Histogram> histograms,
			SortedMap<String, Meter> meters, SortedMap<String, Timer> timers) {
		Date date = new Date();
		for (Map.Entry<String, Timer> e : timers.entrySet()) {
			fonimus.jpa.Histogram h = new fonimus.jpa.Histogram();
			h.setName(e.getKey());
			h.setTime(date);
			h.setCount(e.getValue().getCount());
			h.setMin(convertDuration(e.getValue().getSnapshot().getMin()));
			h.setMax(convertDuration(e.getValue().getSnapshot().getMax()));
			h.setMean(convertDuration(e.getValue().getSnapshot().getMean()));
			h.setMedian(convertDuration(e.getValue().getSnapshot().getMedian()));
			h.setP75(convertDuration(e.getValue().getSnapshot().get999thPercentile()));
			h.setP95(convertDuration(e.getValue().getSnapshot().get95thPercentile()));
			h.setP99(convertDuration(e.getValue().getSnapshot().get99thPercentile()));
			h.setP999(convertDuration(e.getValue().getSnapshot().get999thPercentile()));
			histogramRepository.save(h);
		}
	}
}
