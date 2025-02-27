package com.kai.Vasara;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

    @Configuration
    public class SecurityConfig {
        // todo: better security

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .cors(withDefaults())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/chapters/**").permitAll()
                            .requestMatchers("/api/stories/**").permitAll()
                            .requestMatchers("/api/authors/**").permitAll()
                            .requestMatchers("/api/authorsfollows/**").permitAll()
                            .requestMatchers("/api/favorites/**").permitAll()
                            .requestMatchers("/api/follows/**").permitAll()
                            .requestMatchers("/api/reads/**").permitAll()
                            .requestMatchers("/actuator/**").permitAll()
                            .requestMatchers("/h2-console/**").permitAll() // temporary config for h2db
                            .anyRequest().authenticated())

                    // temporary config for h2 db console access in the browser
                    .headers(headers -> headers
                            .addHeaderWriter((request, response) ->
                                    response.setHeader("X-Frame-Options", "ALLOW-FROM http://localhost:8080")));

            return http.build();
        }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
