package io.fonimus.retry;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.annotation.EnableRetry;

@Slf4j
@Configuration
@EnableRetry
public class RetryConfiguration {

    @Bean
    public ServiceByConstr serviceByConstr() {
        return new ServiceByConstr();
    }

    @Bean
    public RetryListener retryListener() {
        return new RetryListener() {
            @Override
            public <T, E extends Throwable> boolean open(RetryContext retryContext, RetryCallback<T, E> retryCallback) {
                LOGGER.info(">>> in open [ctx={}, callback={}]", retryContext, retryCallback);
                return true;
            }

            @Override
            public <T, E extends Throwable> void close(RetryContext retryContext, RetryCallback<T, E> retryCallback, Throwable throwable) {
                LOGGER.info(">>> in close [ex={}, ctx={}, callback={}]", (throwable.getClass().getName() + " - " + throwable.getMessage()), retryContext, retryCallback);
            }

            @Override
            public <T, E extends Throwable> void onError(RetryContext retryContext, RetryCallback<T, E> retryCallback, Throwable throwable) {
                LOGGER.info(">>> in onError [ex={}, ctx={}, callback={}]", (throwable.getClass().getName() + " - " + throwable.getMessage()), retryContext, retryCallback);
            }
        };
    }

}
