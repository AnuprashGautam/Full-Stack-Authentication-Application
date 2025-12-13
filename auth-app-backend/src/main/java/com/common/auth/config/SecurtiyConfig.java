package com.common.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurtiyConfig {

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(authorizeHttpRequests ->
                    authorizeHttpRequests
                            .requestMatchers("/api/v1/auth/register").permitAll()
                            .requestMatchers("/api/v1/auth/login").permitAll()
                            .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

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
