package io.fonimus.jupiter;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(CustomExtension.class)
class TestWithExtension {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestWithExtension.class);

	@Test
	void test(List<String> errors) {
		LOGGER.info(">>>>>>>>>>> IN THE TEST <<<<<<<<<<<<<<<");
		errors.add("An Error");
	}

	@Test
	void test2(List<String> errors) {
		LOGGER.info(">>>>>>>>>>> IN THE TEST 2 <<<<<<<<<<<<<<<");
		errors.add("Another Error");
		fail("XXX");
	}
}
