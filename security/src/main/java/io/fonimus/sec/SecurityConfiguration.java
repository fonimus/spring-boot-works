package io.fonimus.sec;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private Environment environment;

    public SecurityConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/ping", "/hello").permitAll()
                .antMatchers("/admin").hasRole("ADMIN");

        if (!environment.acceptsProfiles(Profiles.of("open"))) {
            http.authorizeRequests().requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR");
        } else {
            http.authorizeRequests().requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll();
        }

        http.authorizeRequests().anyRequest().authenticated();

    }
}
