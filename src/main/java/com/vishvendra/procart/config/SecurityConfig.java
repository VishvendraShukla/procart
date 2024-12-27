package com.vishvendra.procart.config;

import com.vishvendra.procart.filter.LoggingFilter;
import com.vishvendra.procart.service.CustomUserDetailsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
      "/error"
  };

  public static final String[] USER_SPECIFIC_ROLE_URLS = {
      "/api/v1/user",
  };

  public static final String[] ADMIN_SPECIFIC_ROLE_URLS = {
      "/api/v1/admin",
      "/api/v1/product",
      "/api/v1/inventory",
      "/api/v1/productcurrency",
  };
  private final CustomUserDetailsService userDetailsService;
  private final LoggingFilter loggingFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
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
        .addFilterBefore(loggingFilter, UsernamePasswordAuthenticationFilter.class)
        .authenticationProvider(authenticationProvider())
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .exceptionHandling(eh -> eh.accessDeniedHandler(new CustomAccessDeniedHandler()))
        .httpBasic(Customizer.withDefaults());

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
        .authenticationProvider(authenticationProvider())
        .build();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
    return authProvider;
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:4200"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
    configuration.setExposedHeaders(List.of("Authorization"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

}
