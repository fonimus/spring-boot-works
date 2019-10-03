package io.fonimus.neo4j;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@Disabled
public class Neo4jApplicationTests {

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private RoleRepository roleRepository;

	@BeforeEach
	public void setUp() throws Exception {
		movieRepository.deleteAll();
		personRepository.deleteAll();
	}

	@AfterEach
	public void tearDown() throws Exception {
		//		movieRepository.deleteAll();
		//		personRepository.deleteAll();
	}

	@Test
	public void contextLoads() throws Exception {

		Movie takeShelter = new Movie();
		takeShelter.setTitle("Take Shelter");
		takeShelter.setReleased(2011);

		Movie mud = new Movie();
		mud.setTitle("Mud");
		mud.setReleased(2013);

		Movie midnight = new Movie();
		midnight.setTitle("Midnight Special");
		midnight.setReleased(2016);

		Person ms = new Person();
		ms.setName("Michael Shannon");
		ms.setBorn(1969);

		Person ts = new Person();
		ts.setName("Tye Sheridan");
		ts.setBorn(1996);

		Person jc = new Person();
		jc.setName("Jessica Chastain");
		jc.setBorn(1977);

		Person jf = new Person();
		jf.setName("Jeff Nichols");
		jf.setBorn(1978);

		takeShelter.setRoles(Arrays.asList(
				new Role(null, Collections.singletonList("Samantha LaForche"), jc, takeShelter),
				new Role(null, Collections.singletonList("Curtis LaForche"), ms, takeShelter))
		);
		takeShelter.setDirectors(Collections.singletonList(jf));
		movieRepository.save(takeShelter);

		midnight.setRoles(Collections.singletonList(
				new Role(null, Collections.singletonList("Roy Tomlin"), ms, midnight))
		);
		midnight.setDirectors(Collections.singletonList(jf));
		movieRepository.save(midnight);

		mud.setRoles(Arrays.asList(
				new Role(null, Collections.singletonList("Ellis"), ts, mud),
				new Role(null, Collections.singletonList("Galen"), ms, mud))
		);
		mud.setDirectors(Collections.singletonList(jf));
		movieRepository.save(mud);

		Movie result = movieRepository.findByTitle("Mud");

		Collection<Movie> result2 = movieRepository.findByTitleContaining("d");

		Collection<Movie> result3 = (Collection<Movie>) movieRepository.findAll();
		print(result3);
		Collection<Person> result4 = (Collection<Person>) personRepository.findAll();
		print(result4);
		Collection<Role> result5 = (Collection<Role>) roleRepository.findAll();
		print(result5);

		long movieCount = movieRepository.count();
	}

	private void print(Object object) throws Exception {
		System.err.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object));
	}

}
