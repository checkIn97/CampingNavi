package com.demo.campingnavi.config;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.repository.jpa.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberRepository memberRepository;
    private final HttpSession session;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/resources/**", "/static/**", "/assets/**", "/templates/**","/css/**", "/js/**", "/images/**")
                .requestMatchers("/camp/**");
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
                        .loginPage("/oauth-login/member/login")
                        .defaultSuccessUrl("/main")
                        .usernameParameter("username")
                        .passwordParameter("pw")
                        .loginProcessingUrl("/oauth-login/member/loginProc")
                        .successHandler(
                                (request, response, authentication) -> {
                                    System.out.println("authentication : " + authentication.getName());
                                    Member member = memberRepository.findByUsername(authentication.getName());
                                    session.setAttribute("loginUser", member);
                                    response.sendRedirect("/main");
                                }
                        )
                        .failureHandler(
                                (request, response, exception) -> {
                                    System.out.println("exception : " + exception.getMessage());
                                    response.sendRedirect("/oauth-login/member/login");
                                }
                        )
                        .permitAll()
                );
        http
                .csrf((auth) -> auth.disable()
                ); // 테스트하기 위해 잠시 disable
        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );
        http
                .headers((headerConfig) -> headerConfig.frameOptions(
                        frameOptionsConfig -> frameOptionsConfig.disable()
                ))
                .logout((logoutConfig) -> logoutConfig.logoutSuccessUrl("/"))
                .oauth2Login((oauth) -> oauth.loginPage("/oauth-login/member/login")
                        .defaultSuccessUrl("/main")
                        .failureUrl("/oauth-login/member/login")
                        .successHandler(
                                (request, response, authentication) -> {
                                    System.out.println("authentication : " + authentication.getName());
                                    Member member = memberRepository.findByUsername(authentication.getName());
                                    session.setAttribute("loginUser", member);
                                    response.sendRedirect("/main");
                                }
                        )
                        .failureHandler(
                                (request, response, exception) -> {
                                    System.out.println("exception : " + exception.getMessage());
                                    response.sendRedirect("/oauth-login/member/login");
                                }
                        )
                        .permitAll());
        return http.build();
    }


}
