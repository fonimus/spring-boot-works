/*
 * Copyright (c) Worldline 2018.
 */

package io.fonimus.sec;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class SecurityController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/hello")
    public String hello(Principal user) {
        String username = user != null ? user.getName() : "anonymous";
        return "Hello " + username;
    }

    @GetMapping("/auth")
    public Principal auth(Principal user) {
        return user;
    }

    @GetMapping("/admin")
    public Principal admin(Principal user) {
        return user;
    }

}