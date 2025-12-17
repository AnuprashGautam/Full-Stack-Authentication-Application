package com.common.auth.config;

import com.common.auth.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class SecurtiyConfig {

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                        .cors(Customizer.withDefaults())
                        .sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .authorizeHttpRequests(authorizeHttpRequests ->
                            authorizeHttpRequests
                                    .requestMatchers("/api/v1/auth/register").permitAll()
                                    .requestMatchers("/api/v1/auth/login").permitAll()
                                    .anyRequest().authenticated()
                        )
                        .exceptionHandling(ex -> ex.authenticationEntryPoint(
                                (request, response, e) -> {
                                    e.printStackTrace();
                                    response.setStatus(401);
                                    response.setContentType("application/json");
                                    String message ="Unauthorized access: "+e.getMessage();
                                    Map<String,String> errorMap = Map.of(
                                            "message", message,
                                            "status", String.valueOf(401)
                                    );
                                    var objectMapper = new ObjectMapper();
                                    response.getWriter().write(objectMapper.writeValueAsString(errorMap));
                                }
                        ))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//    @Bean
//    public UserDetailsService users() {
//        // The builder will ensure the passwords are encoded before saving in memory
////        User.UserBuilder users = User.withDefaultPasswordEncoder();
////        UserDetails user = users
////                .username("user")
////                .password("password")
////                .roles("USER")
////                .build();
////        UserDetails admin = users
////                .username("admin")
////                .password("password")
////                .roles("USER", "ADMIN")
////                .build();
////
//        return new InMemoryUserDetailsManager(user, admin);
//    }
}
