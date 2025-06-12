package com.dormitory.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/sinh-vien/**").hasAnyRole("ADMIN", "SINH_VIEN")
                .requestMatchers("/api/momo/**").permitAll()
                .requestMatchers("/sinh-vien/hoa-don/momo-return").hasRole("SINH_VIEN")
                .requestMatchers("/css/**", "/js/**", "/images/**", "/img/**", "/vendor/**").permitAll()
                .requestMatchers("/login", "/register", "/", "/logout").permitAll()
                .requestMatchers("/sinh-vien/dang-ky-thong-tin", "/sinh-vien/dang-ky-phong", "/sinh-vien/thong-tin-phong").hasRole("SINH_VIEN")
                .requestMatchers("/sinh-vien/**").hasRole("SINH_VIEN")
                .requestMatchers("/thong-bao/**").hasAnyRole("ADMIN", "SINH_VIEN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/momo/**")
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
