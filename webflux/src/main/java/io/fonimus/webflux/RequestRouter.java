package io.fonimus.webflux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

@Configuration
public class RequestRouter {

	@Bean
	public RouterFunction<?> routes(RequestHandler requestHandler) {
		return RouterFunctions.route(RequestPredicates.GET("/weatherstream"), requestHandler::streamWeather);
	}
}
