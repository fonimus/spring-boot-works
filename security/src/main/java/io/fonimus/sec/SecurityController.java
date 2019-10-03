package io.fonimus.sec;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
