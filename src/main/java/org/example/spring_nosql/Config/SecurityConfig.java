package org.example.spring_nosql.Config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.spring_nosql.filter.JwtAuthenticationFilter;
import org.example.spring_nosql.Handler.CustomAccessDeniedHandler;
import org.example.spring_nosql.Handler.CustomAuthenticationEntryPoint;
import org.example.spring_nosql.Service.AdmCustomDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AdmCustomDetailsService AdmCustomDetailsService;

    public SecurityConfig(AdmCustomDetailsService admCustomDetailsService){
        this.AdmCustomDetailsService = admCustomDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/adm/**").authenticated()
                    .requestMatchers("/recipes/insertComent/**").authenticated()
                    .requestMatchers("/persons/deleteUserById/**", "/persons/updatePerson/**", "/persons/checkIngredients/**", "/persons/insertPerson", "/persons/saveRecipesIngredients", "/persons/likeRecipes").authenticated()

                    .requestMatchers("/auth/login").permitAll()
                    .requestMatchers("swagger-ui/**").permitAll()
                    .requestMatchers("/v3/api-docs/**").permitAll()
                    .anyRequest().permitAll()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtAuthenticationFilter(AdmCustomDetailsService, secretKey()),
                        UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(AdmCustomDetailsService)
                .exceptionHandling(exceptions -> {
                    exceptions
                            .accessDeniedHandler(new CustomAccessDeniedHandler())  // Seu handler personalizado
                            .authenticationEntryPoint(new CustomAuthenticationEntryPoint());  // Para erros de autenticação
                });

        return http.build();
    }

    @Bean
    public SecretKey secretKey(){
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }
}