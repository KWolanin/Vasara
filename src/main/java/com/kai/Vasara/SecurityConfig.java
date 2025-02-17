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

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .cors(withDefaults())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/chapters/**").permitAll()
                            .requestMatchers("/api/stories/**").permitAll()
                            .requestMatchers("/api/authors/**").permitAll()
                            .requestMatchers("/api/favorites/**").permitAll()
                            .requestMatchers("/api/follows/**").permitAll()
                            .requestMatchers("/api/reads/**").permitAll()
                            .requestMatchers("/actuator/**").permitAll()
                            .anyRequest().authenticated());

            return http.build();
        }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
