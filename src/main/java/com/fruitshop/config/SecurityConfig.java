package com.fruitshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config. annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core. userdetails.User;
import org.springframework.security.core. userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.  security.crypto.bcrypt.BCryptPasswordEncoder;
import org. springframework.security.crypto.password. PasswordEncoder;
import org. springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Spring Security配置
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("user123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                . authorizeRequests()
                // 静态资源允许所有访问
                .antMatchers("/static/**").permitAll()
                // HTML页面允许所有访问
                .antMatchers("/*. html").permitAll()
                . antMatchers("/login").permitAll()
                .antMatchers("/error").permitAll()
                // API认证端点
                .antMatchers("/api/auth/**").permitAll()
                // 其他请求需要认证
                .anyRequest().authenticated()
                . and()

                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/api/auth/login")
                .defaultSuccessUrl("/index. html", true)
                .failureUrl("/login.html? error=true")
                . permitAll()
                .and()

                .httpBasic()
                .and()

                .logout()
                .logoutUrl("/api/auth/logout")
                .logoutSuccessUrl("/login.html")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
                .and()

                .sessionManagement()
                .maximumSessions(1)
                .expiredUrl("/login.html? expired=true");
    }
}