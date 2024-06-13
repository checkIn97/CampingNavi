package com.demo.campingnavi.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/resources/**", "/static/**", "/assets/**", "/templates/**","/css/**", "/js/**", "/images/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/resources/**", "/static/**", "/assets/**", "/templates/**","/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/member/join", "/index", "/member/joinProc", "/member/membershipAgree", "/member/membership", "/member/validateUser", "/member/validateNickname").permitAll()
                        .requestMatchers("/mailSend", "mailCheck", "/camp/search", "/camp/detail/go").permitAll()
                        .anyRequest().permitAll()
                );
        http
                .formLogin((auth) -> auth
                        .loginPage("/member/login")
                        .defaultSuccessUrl("/main")
                        .usernameParameter("username")
                        .passwordParameter("pw")
                        .loginProcessingUrl("/member/loginProc")
                        .successHandler(
                                (request, response, authentication) -> {
                                    System.out.println("authentication : " + authentication.getName());
                                    response.sendRedirect("/main");
                                }
                        )
                        .failureHandler(
                                (request, response, exception) -> {
                                    System.out.println("exception : " + exception.getMessage());
                                    response.sendRedirect("/member/login");
                                }
                        )
                        .permitAll()
                );
        http
                .csrf((auth) -> auth.disable()
                ); // 테스트하기 위해 잠시 disable
        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }


}
