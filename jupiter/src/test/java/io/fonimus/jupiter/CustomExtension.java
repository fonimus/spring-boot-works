package io.fonimus.jupiter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomExtension
		implements BeforeAllCallback, BeforeEachCallback, BeforeTestExecutionCallback, AfterTestExecutionCallback, AfterEachCallback, AfterAllCallback,
		ParameterResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomExtension.class);

	private Map<String, TestResult> assertionErrors = new ConcurrentHashMap<>();

	@Override
	public void beforeAll(ExtensionContext extensionContext) throws Exception {
		LOGGER.info(">>> IN: before all {}", extensionContext);
	}

	@Override
	public void beforeEach(ExtensionContext extensionContext) throws Exception {
		LOGGER.info(">>> IN: before each {}", extensionContext);
		LOGGER.info("assertions: {}", assertionErrors);
		assertionErrors.put(extensionContext.getUniqueId(), new TestResult());
	}

	@Override
	public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {
		LOGGER.info(">>> IN: before test {}", extensionContext);
	}

	@Override
	public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
		LOGGER.info(">>> IN: after test {}", extensionContext);
		Optional<Throwable> ex = extensionContext.getExecutionException();
		ex.ifPresent(throwable -> assertionErrors.get(extensionContext.getUniqueId()).setThrowable(throwable));
	}

	@Override
	public void afterEach(ExtensionContext extensionContext) throws Exception {
		LOGGER.info(">>> IN: after each {}", extensionContext);
		LOGGER.info("assertions: {}", assertionErrors.get(extensionContext.getUniqueId()));
	}

	@Override
	public void afterAll(ExtensionContext extensionContext) throws Exception {
		LOGGER.info(">>> IN: after all {}", extensionContext);
		LOGGER.info("assertions: {}", assertionErrors);
	}

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return parameterContext.getParameter().getType().equals(List.class);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		if (parameterContext.getParameter().getType().equals(List.class)) {
			return assertionErrors.get(extensionContext.getUniqueId()).getAssertionErrors();
		}
		return null;
	}

	public static class TestResult {

		private List<String> assertionErrors = new ArrayList<>();

		private Throwable throwable;

		public List<String> getAssertionErrors() {
			return assertionErrors;
		}

		public void setAssertionErrors(List<String> assertionErrors) {
			this.assertionErrors = assertionErrors;
		}

		public Throwable getThrowable() {
			return throwable;
		}

		public void setThrowable(Throwable throwable) {
			this.throwable = throwable;
		}

		@Override
		public String toString() {
			return "TestResult{" +
					"assertionErrors=" + assertionErrors +
					", throwable=" + throwable +
					'}';
		}
	}
}
