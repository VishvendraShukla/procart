package com.vishvendra.procart.config;

import com.vishvendra.procart.filter.JwtAuthenticationFilter;
import com.vishvendra.procart.filter.JwtAuthenticationProvider;
import com.vishvendra.procart.filter.LoggingFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableAspectJAutoProxy
public class SecurityConfig {

  public static final String[] PUBLIC_URLS = {
      "/public/**",
      "/api/v1/authenticate",
      "/error"
  };

  public static final String[] USER_SPECIFIC_ROLE_URLS = {
      "/api/v1/user",
      "/api/v1/cart",
      "/api/v1/cart/complete",
      "/api/v1/orders"
  };

  public static final String[] ADMIN_SPECIFIC_ROLE_URLS = {
      "/api/v1/admin",
      "/api/v1/products",
      "/api/v1/dashboard",
      "/api/v1/auditlogs",
      "/api/v1/inventories",
      "/api/v1/charge",
      "/api/v1/currencies",
  };
  private final LoggingFilter loggingFilter;
  private final JwtAuthenticationProvider jwtAuthenticationProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(PUBLIC_URLS)
            .permitAll()
            .requestMatchers(HttpMethod.POST, "/api/v1/user")
            .permitAll()
            .requestMatchers(ADMIN_SPECIFIC_ROLE_URLS)
            .hasRole("ADMIN")
            .requestMatchers(USER_SPECIFIC_ROLE_URLS)
            .hasRole("USER")
            .anyRequest().authenticated()
        )
        .addFilterBefore(new JwtAuthenticationFilter(authenticationManager(http)),
            UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(loggingFilter, JwtAuthenticationFilter.class)
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .exceptionHandling(eh -> eh.accessDeniedHandler(new CustomAccessDeniedHandler()))
        .httpBasic(AbstractHttpConfigurer::disable)
        .anonymous(AbstractHttpConfigurer::disable);

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
        .authenticationProvider(jwtAuthenticationProvider)
        .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://127.0.0.1:5173"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
    configuration.setExposedHeaders(List.of("Authorization"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


}
