package com.fruitshop.config;

import com.fruitshop.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 * Spring Security配置（适配上下文路径+静态资源）
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new AESPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                // ========== 核心修复：放行静态资源（适配context-path） ==========
                .antMatchers("/css/**", "/js/**", "/img/**").permitAll()
                // 放行登录页、错误页、认证接口
                .antMatchers("/login.html", "/error", "/api/auth/**").permitAll()
                // 其他请求需要认证
                .anyRequest().authenticated()
                .and()

                .formLogin()
                // 登录页路径（包含context-path：/fruitshop/login.html）
                .loginPage("/login.html")
                // 登录处理接口
                .loginProcessingUrl("/api/auth/login")
                // 前端传参名：uId和password
                .usernameParameter("uId")
                .passwordParameter("password")
                // 登录成功后跳转首页（包含context-path）
                .defaultSuccessUrl("/index.html", true)
                // 登录失败跳转
                .failureUrl("/login.html?error=true")
                .permitAll()
                .and()

                .httpBasic().disable()

                .logout()
                // 退出接口
                .logoutUrl("/api/auth/logout")
                // 退出成功跳转登录页（包含context-path）
                .logoutSuccessUrl("/login.html")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
                .and()

                .sessionManagement()
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry())
                .expiredUrl("/login.html?expired=true");
    }
}