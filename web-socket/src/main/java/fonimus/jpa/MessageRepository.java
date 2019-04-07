package fonimus.jpa;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by francois on 04/03/2017.
 */
public interface MessageRepository
		extends CrudRepository<Message, Long> {

}
