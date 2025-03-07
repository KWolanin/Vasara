package com.kai.Vasara;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/authors/login").permitAll()
                        .requestMatchers("/api/authors/register").permitAll()
                        .requestMatchers("/api/authors/{id}/**").permitAll()
                        .requestMatchers("/api/stories/all/**").permitAll()
                        .requestMatchers("/api/stories/{id}/**").permitAll()
                        .requestMatchers("/api/stories/count/**").permitAll()
                        .requestMatchers("/api/chapters/read/**").permitAll()
                        .requestMatchers("/api/chapters/navigable/**").permitAll()
                        .requestMatchers("/api/comments/add").permitAll()
                        .requestMatchers("/api/comments/{id}").permitAll()
                        .requestMatchers("/api/comments/perms/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/authors/update/**").authenticated()
                        .requestMatchers("/api/stories/my/**").authenticated()
                        .requestMatchers("/api/stories/add/**").authenticated()
                        .requestMatchers("/api/stories/edit/**").authenticated()
                        .requestMatchers("/api/stories/delete/**").authenticated()
                        .requestMatchers("/api/stories/mine/**").authenticated()
                        .requestMatchers("/api/chapters/add/**").authenticated()
                        .requestMatchers("/api/chapters/order/**").authenticated()
                        .requestMatchers("/api/chapters/all/**").authenticated()
                        .requestMatchers("/api/chapters/delete/**").authenticated()
                        .requestMatchers("/api/comments/delete/**").authenticated()
                        .requestMatchers("/api/authorfollows/**").authenticated()
                        .requestMatchers("/api/favorites/**").authenticated()
                        .requestMatchers("/api/follows/**").authenticated()
                        .requestMatchers("/api/reads/**").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:9000", "https://vasaraf-production.up.railway.app")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
