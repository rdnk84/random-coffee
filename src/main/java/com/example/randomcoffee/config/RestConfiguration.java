package com.example.randomcoffee.config;


import com.example.randomcoffee.config.auth.AuthFilter;
import com.example.randomcoffee.model.auth.filters.CustomAuthenticationFilter;
import com.example.randomcoffee.model.auth.filters.CustomAuthorizationFilter;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class RestConfiguration {

    private static final String[] SWAGGER_ENDPOINT = {
            "/**swagger**/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };


    private static final String[] WHITELIST = {
            "/h2-console/**/**",
            "/docs/**",
            "/csrf/**",
            "/webjars/**"
    };

    @Qualifier("delegatedAuthenticationEntryPoint")
    private final AuthenticationEntryPoint authEntryPoint;


    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
//        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
//
//        http
//                .cors()
//                .and()
//                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .antMatchers(HttpMethod.GET, "/users/**").permitAll()
//                .antMatchers("/api/login/**", "/api/token/refresh/**").permitAll()
//                .antMatchers(SWAGGER_ENDPOINT).permitAll()
//                .antMatchers(POST, "/users/**").hasAnyAuthority("ROLE_ADMIN")
//                .anyRequest().authenticated().and()
//                .exceptionHandling().authenticationEntryPoint(authEntryPoint)
//                .and()
//                .addFilter(customAuthenticationFilter)
//                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
//    }

    @Configuration
    public class SecurityConfiguration {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
//                    .cors()
//                    .and()
//                    .csrf().disable()
//                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                    .and()
                    .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/users/**").permitAll()
                    .antMatchers("/api/login/**", "/api/token/refresh/**").permitAll()
                    .antMatchers(SWAGGER_ENDPOINT).permitAll()
                    .antMatchers(POST, "/users/**").hasAnyAuthority("ROLE_ADMIN")
                    .anyRequest().authenticated().and()
                    .exceptionHandling().authenticationEntryPoint(authEntryPoint)
                    .and()
                    .addFilter(customAuthenticationFilter)
                    .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);


            return http.build();
        }

    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers(WHITELIST)
                .antMatchers();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
