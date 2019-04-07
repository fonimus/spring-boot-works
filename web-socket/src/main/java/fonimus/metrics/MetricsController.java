package fonimus.metrics;

import fonimus.jpa.Histogram;
import fonimus.jpa.HistogramRepository;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by francois on 05/03/2017.
 */
@RestController
@RequestMapping(path = "/api")
public class MetricsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MetricsController.class);

	@Autowired
	private HistogramRepository histogramRepository;

	@RequestMapping(path = "/metrics")
	public ResponseEntity<List<Histogram>> metrics(
			@RequestParam(name = "start", required = false) Date start,
			@RequestParam(name = "end", required = false) Date end,
			@RequestParam(name = "limit", required = false) Integer limit
	) {
		LOGGER.info("Request to get all metrics [start={}, end={}, limit={}]", start, end, limit);
		return ResponseEntity.ok(histogramRepository
				.findByTimeBetweenOrderByTimeAsc(start != null ? start : new Date(0), end != null ? end : new Date(),
						new PageRequest(0, limit != null ? limit : 100)));
	}

	@RequestMapping(path = "/metrics/{name}")
	public ResponseEntity<List<Histogram>> metrics(
			@PathVariable(name = "name") String name,
			@RequestParam(name = "start", required = false) Date start,
			@RequestParam(name = "end", required = false) Date end,
			@RequestParam(name = "limit", required = false) Integer limit
	) {
		LOGGER.info("Request to get metrics [name={}, start={}, end={}, limit={}]", name, start, end, limit);
		return ResponseEntity.ok(histogramRepository
				.findByNameAndTimeBetweenOrderByTimeAsc(name, start != null ? start : new Date(0), end != null ? end : new Date(),
						new PageRequest(0, limit != null ? limit : 100)));
	}

}