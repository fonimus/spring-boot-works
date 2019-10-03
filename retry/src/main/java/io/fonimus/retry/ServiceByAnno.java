package io.fonimus.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class ServiceByAnno {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceByAnno.class);

    private int count = 0;

    // try the method 9 times with 2 seconds delay.
    @Retryable(label = "retry", maxAttemptsExpression = "${tracer.retry.max-attempts}", value = MySubEx.class, backoff = @Backoff(delayExpression = "${tracer.retry.delay}"))
    public String springReTryTest() throws Exception {
        count++;
        LOGGER.warn("try!");

        if (count < 4)
            throw new MySubEx("WWWWWWWWWWWWWWWW");
        else if (count < 5)
            throw new Exception("xx");
        else
            return "bla";
    }

}
