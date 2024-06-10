package com.java.recruitment.config;

import com.java.recruitment.service.properties.MinioProperties;
import com.java.recruitment.web.security.JwtTokenFilter;
import com.java.recruitment.web.security.JwtTokenProvider;
import io.minio.MinioClient;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.java.recruitment.service.model.user.Role.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class SecurityConfiguration {

    private final JwtTokenProvider tokenProvider;

    private final MinioProperties minioProperties;

    /**
     * Создает и возвращает экземпляр кодировщика паролей BCrypt.
     *
     * @return Кодировщик паролей.
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Получает и возвращает менеджер аутентификации из конфигурации аутентификации.
     *
     * @param configuration Конфигурация аутентификации.
     * @return Менеджер аутентификации.
     */

    @Bean
    @SneakyThrows
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }

    /**
     * Настраивает цепочку фильтров безопасности для обработки HTTP-запросов.
     *
     * @param httpSecurity Объект HttpSecurity для настройки безопасности.
     * @return Цепочка фильтров безопасности.
     */

    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(final HttpSecurity httpSecurity) {

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS
                                )
                )
                .exceptionHandling(configurer ->
                        configurer.authenticationEntryPoint(
                                        (request, response, exception) -> {
                                            response.setStatus(
                                                    HttpStatus.UNAUTHORIZED
                                                            .value()
                                            );
                                            response.getWriter()
                                                    .write("Unauthorized.");
                                        })
                                .accessDeniedHandler(
                                        (request, response, exception) -> {
                                            response.setStatus(
                                                    HttpStatus.FORBIDDEN
                                                            .value()
                                            );
                                            response.getWriter()
                                                    .write("Unauthorized.");
                                        }))
                .authorizeHttpRequests(configurer ->
                        configurer.requestMatchers("/api/v1/auth/**")
                                .permitAll()
                                .requestMatchers("/swagger-ui/**")
                                .permitAll()
                                .requestMatchers("/v3/api-docs/**")
                                .permitAll()
                                .requestMatchers("/api/v1/admin/**").hasAnyAuthority(
                                        String.valueOf(ADMIN)
                                )
                                .requestMatchers("/api/v1/interviewer/**").hasAnyAuthority(
                                        String.valueOf(ADMIN),
                                        String.valueOf(INTERVIEW_SPECIALIST)
                                )
                                .requestMatchers("/api/v1/hr/**").hasAnyAuthority(
                                        String.valueOf(ADMIN),
                                        String.valueOf(HR)
                                )
                                .requestMatchers("/emails/**").hasAnyAuthority(
                                        String.valueOf(INTERVIEW_SPECIALIST),
                                        String.valueOf(HR)
                                )
                                .requestMatchers("/api/v1/users/**").hasAnyAuthority(
                                        String.valueOf(ADMIN),
                                        String.valueOf(HR),
                                        String.valueOf(INTERVIEW_SPECIALIST),
                                        String.valueOf(USER)
                                )
                                .anyRequest().authenticated())
                .anonymous(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtTokenFilter(tokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioProperties.getUrl())
                .credentials(minioProperties.getAccessKey(),
                        minioProperties.getSecretKey())
                .build();
    }

    /**
     * Создает и возвращает объект OpenAPI для документации API.
     *
     * @return Объект OpenAPI с настройками безопасности и информацией о API.
     */

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearerAuth"))
                .components(
                        new Components()
                                .addSecuritySchemes("bearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .info(new Info()
                        .title("Recruitment for job API")
                        .description("Spring Boot Application")
                        .version("1.0")
                );
    }
}
