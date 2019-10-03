package io.fonimus.camel;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MyCamelRouter
        extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:hello?period={{timer.period}}").routeId("hello").routeGroup("hello-group")
                .transform().method("myBean", "saySomething")
                .filter(simple("${body} contains 'foo'"))
                .to("log:foo")
                .end()
                .to("stream:out");
    }

}
