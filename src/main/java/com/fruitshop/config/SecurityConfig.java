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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

/**
 * Spring Security配置（最终修复版）
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 使用自定义认证提供者
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 注意：确保AESPasswordEncoder正确实现PasswordEncoder接口的encode和matches方法
        return new AESPasswordEncoder();
    }

    // 可选：仍然保留UserDetailsService用于其他用途
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    // 关键补充：会话注册表，让maximumSessions生效
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    // 关键补充：监听会话事件，支持会话并发控制
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                // 静态资源路径匹配前端实际引用
                .antMatchers("/resources/static/**").permitAll()
                // 只放行登录页面，其他HTML页面需要认证
                .antMatchers("/login.html").permitAll()
                .antMatchers("/error").permitAll()
                // API认证端点放行
                .antMatchers("/api/auth/**").permitAll()
                // 其他请求需要认证
                .anyRequest().authenticated()
                .and()

                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/api/auth/login")
                // 参数名改为uId，与前端传参保持一致
                .usernameParameter("uId")
                .passwordParameter("password")
                .defaultSuccessUrl("/index.html", true)
                .failureUrl("/login.html?error=true")
                .permitAll()
                .and()

                // ========== 关键修复：禁用Basic认证 ==========
                .httpBasic().disable()

                .logout()
                .logoutUrl("/api/auth/logout")
                .logoutSuccessUrl("/login.html")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
                .and()

                .sessionManagement()
                // 关联SessionRegistry，让单会话限制生效
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry())
                .expiredUrl("/login.html?expired=true");
    }
}