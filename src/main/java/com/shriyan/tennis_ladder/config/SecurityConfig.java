package com.shriyan.tennis_ladder.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(
            PasswordEncoder passwordEncoder,
            @Value("${app.security.player-password}")
            String playerPassword,
            @Value("${app.security.coach-password}")
            String coachPassword) {

        UserDetails player = User.withUsername("player")
                .password(passwordEncoder.encode(playerPassword))
                .roles("PLAYER")
                .build();

        UserDetails coach = User.withUsername("coach")
                .password(passwordEncoder.encode(coachPassword))
                .roles("COACH")
                .build();

        return new InMemoryUserDetailsManager(player, coach);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                        "/css/**",
                        "/login",
                        "/error"
                ).permitAll()

                .requestMatchers(
                        "/coach/**",
                        "/players/**"
                ).hasRole("COACH")

                .requestMatchers(
                        HttpMethod.POST,
                        "/challenges/*/approve",
                        "/challenges/*/reject",
                        "/challenges/*/result"
                ).hasRole("COACH")

                .requestMatchers(
                        HttpMethod.GET,
                        "/challenges/*/result"
                ).hasRole("COACH")

                .requestMatchers(
                        "/",
                        "/challenges",
                        "/challenges/new"
                ).hasAnyRole("PLAYER", "COACH")

                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler((request, response, authentication) -> {
                    boolean isCoach = authentication
                            .getAuthorities()
                            .stream()
                            .anyMatch(authority ->
                                    authority.getAuthority()
                                            .equals("ROLE_COACH"));

                    response.sendRedirect(
                            isCoach ? "/coach" : "/"
                    );
                })
                .permitAll()
            )

            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}
