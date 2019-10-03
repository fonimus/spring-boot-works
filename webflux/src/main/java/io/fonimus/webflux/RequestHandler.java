package io.fonimus.webflux;

import reactor.core.publisher.Mono;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
public class RequestHandler {

	private final WeatherService weatherService;

	public RequestHandler(WeatherService weatherService) {
		this.weatherService = weatherService;
	}

	public Mono<ServerResponse> streamWeather(ServerRequest request) {
		return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(weatherService.streamWeather(), WeatherEvent.class);
	}
}
