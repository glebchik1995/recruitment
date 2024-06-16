package com.java.recruitment.config;

import com.java.recruitment.web.security.JwtTokenFilter;
import com.java.recruitment.web.security.JwtTokenProvider;
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
//@Lazy - это аннотация, которая указывает Spring на то, что бин должен быть создан только при первом обращении к нему.
//Это может быть полезно, если создание бина требует больших ресурсов или занимает много времени.
//@RequiredArgsConstructor(onConstructor = @__(@Lazy)) - это аннотация Lombok, которая генерирует конструктор,
//принимающий все final поля класса в качестве аргументов. Аннотация @Lazy указывает Spring на то, что бины,
//переданные в качестве аргументов конструктора, должны быть созданы только при первом обращении к ним.
//Таким образом, аннотация @Lazy используется здесь для того, чтобы отложить создание зависимостей до момента,
//когда они действительно понадобятся. Это может ускорить запуск приложения и уменьшить потребление ресурсов.
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
                                                    .write("UNAUTHORIZED.");
                                        })
                                .accessDeniedHandler(
                                        (request, response, exception) -> {
                                            response.setStatus(
                                                    HttpStatus.FORBIDDEN
                                                            .value()
                                            );
                                            response.getWriter()
                                                    .write("FORBIDDEN.");
                                        }))
                .authorizeHttpRequests(configurer ->
                        configurer.requestMatchers("/api/v1/auth/**")
                                .permitAll()
                                .requestMatchers("/swagger-ui/**")
                                .permitAll()
                                .requestMatchers("/v3/api-docs/**")
                                .permitAll()
                                .requestMatchers("/api/v1/**").hasAnyAuthority(
                                        String.valueOf(ADMIN)
                                )
                                .requestMatchers("/api/v1/recruiter/**").hasAnyAuthority(
                                        String.valueOf(RECRUITER)
                                )
                                .requestMatchers("/api/v1/hr/**").hasAnyAuthority(
                                        String.valueOf(HR)
                                )
                                .requestMatchers("/emails/**").hasAnyAuthority(
                                        String.valueOf(RECRUITER),
                                        String.valueOf(HR)
                                )
                                .requestMatchers("/api/v1/vacancy/**").hasAnyAuthority(
                                        String.valueOf(RECRUITER),
                                        String.valueOf(HR)
                                )
                                .requestMatchers("/api/v1/users/**").hasAnyAuthority(
                                        String.valueOf(HR),
                                        String.valueOf(RECRUITER)
                                )
                                .anyRequest().authenticated())
                .anonymous(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtTokenFilter(tokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
