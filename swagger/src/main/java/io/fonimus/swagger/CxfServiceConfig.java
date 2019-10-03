package io.fonimus.swagger;

import io.fonimus.swagger.rs.HelloServiceImpl;

import java.util.Collections;

import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CxfServiceConfig {

    @Autowired
    private Bus bus;

    @Bean
    public Server rsServer() {
        JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
        endpoint.setBus(bus);
        endpoint.setAddress("/api");
        endpoint.setServiceBeans(Collections.singletonList(new HelloServiceImpl()));
        Swagger2Feature swagger = new Swagger2Feature();
        swagger.setTitle("Cxf Api Documentation");
        swagger.setDescription("Cxf Api Documentation");
        endpoint.setFeatures(Collections.singletonList(swagger));
        return endpoint.create();
    }
}
