/*
 * Copyright (c) Worldline 2018.
 */

package io.fonimus.swagger.rs;

public class HelloServiceImpl implements HelloService {

    public String sayHello(String a) {
        return "Hello " + a + ", Welcome to CXF RS Spring Boot World!!!";
    }

}