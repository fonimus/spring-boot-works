package io.fonimus.prometheus;

import io.fonimus.prometheus.jpa.Customer;
import io.fonimus.prometheus.jpa.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PrometheusController {

    private static final Logger LOGGER = LoggerFactory.getLogger("test");

    private static final String STATUS = "status";
    private static final String NAME = "name";

    private static final String PATH = "/api/{" + STATUS + "}";
    private static final String PATH_NAME = "/customer/{" + NAME + "}";

    private CustomerRepository repository;

    public PrometheusController(CustomerRepository repository) {
        this.repository = repository;
    }

    @GetMapping(path = PATH)
    public ResponseEntity<Void> status(@PathVariable(name = STATUS) String status) {
        int code = Integer.parseInt(status);
        LOGGER.debug("Status: {}", status);
        if (code >= 200 && code < 300) {
            LOGGER.info("OK");
        } else {
            LOGGER.error("Error occurred !!");
        }
        if (code == 503) {
            throw new IllegalArgumentException("TEST");
        }
        return ResponseEntity.status(code).build();
    }

    @GetMapping(path = PATH_NAME)
    public List<Customer> byLastName(@PathVariable(name = NAME) String name) {
        return repository.findByLastName(name);
    }


}
