package com.sebcode.msproducts.security.config;

import com.sebcode.msproducts.security.config.security.JwtAuthenticationEntryPoint;
import com.sebcode.msproducts.security.config.security.JwtAuthenticationFilter;
import com.sebcode.msproducts.security.filter.RateLimitFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
//@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RateLimitFilter rateLimitFilter;

    public SecurityConfig(
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
//            @Lazy JwtAuthenticationFilter jwtAuthenticationFilter,
            RateLimitFilter rateLimitFilter) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
//        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.rateLimitFilter = rateLimitFilter;
    }

    @Value("${cors.allowed-origins}")
    private String allowedOriginsRaw;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, @Lazy JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/actuator/**").hasRole("ADMIN")
                        .requestMatchers("/swagger-ui/**", "/api-docs/**", "/v3/api-docs/**").hasRole("ADMIN")
                        // Catálogo interno (marcas, tipos/valores de atributo): no lo consume
                        // el storefront, solo el panel admin, así que basta con estar
                        // autenticado; el filtro ya no exige ROLE_ADMIN global para que un
                        // usuario que solo tenga CompanyRole (MANAGER/SELLER) pueda leerlo y
                        // llegar al chequeo fino de @catalogAccess en cada @PreAuthorize.
                        .requestMatchers("/api/v1/attribute-types/**").authenticated()
                        .requestMatchers("/api/v1/attribute-values/**").authenticated()
                        .requestMatchers("/api/v1/brands/**").authenticated()
                        .requestMatchers("/api/v1/products/**").permitAll()
                        .requestMatchers("/api/v1/product-images/**").permitAll()
                        .requestMatchers("/api/v1/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/subcategories/**").permitAll()
                        .requestMatchers("/api/v1/subcategories/**").authenticated()
                        .requestMatchers("/api/v1/variant-attributes/**").authenticated()
                        .requestMatchers("/api/v1/variant-products/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/complaints").permitAll()
                        .requestMatchers("/api/v1/complaints/**").hasRole("ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/seller/**").hasRole("SELLER")
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        List<String> origins = Arrays.asList(allowedOriginsRaw.split(","));

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(origins);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}