package com.rekreation.learning.vaadin;

import com.rekreation.learning.vaadin.ui.view.login.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@SpringBootApplication
public class VadinExampleApplication extends VaadinWebSecurity {

    private static final Logger log = LoggerFactory.getLogger(VadinExampleApplication.class);

    public static void main(String[] args) {
        log.info("Starting Vaadin Example Application...");
        SpringApplication.run(VadinExampleApplication.class, args);
        log.info("Vaadin Example Application started successfully");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Bean
    public UserDetailsService getUserDetailsService() {
        return new InMemoryUserDetailsManager(
                User.withUsername("tushar").password("{noop}tushar").roles("ADMIN").build(),
                User.withUsername("anna").password("{noop}anna").roles("ADMIN").build()
        );
    }
}
