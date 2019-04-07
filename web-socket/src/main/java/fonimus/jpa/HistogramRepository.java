package fonimus.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by francois on 04/03/2017.
 */
public interface HistogramRepository
		extends CrudRepository<Histogram, Long> {

	List<Histogram> findByTimeBetweenOrderByTimeAsc(Date start, Date end, Pageable page);

	List<Histogram> findByNameAndTimeBetweenOrderByTimeAsc(String name, Date start, Date end, Pageable page);

}
