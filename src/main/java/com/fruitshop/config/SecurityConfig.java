package com.fruitshop.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fruitshop.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring Security配置
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
        // 返回一个不做任何操作的密码编码器，因为我们使用自定义验证
        return new AESPasswordEncoder();
    }

    // 可选：仍然保留UserDetailsService用于其他用途
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                // 静态资源允许所有访问
                .antMatchers("/static/**").permitAll()
                // HTML页面允许所有访问
                .antMatchers("/*.html").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/error").permitAll()
                // API认证端点
                .antMatchers("/api/auth/**").permitAll()
                // 其他请求需要认证
                .anyRequest().authenticated()
                .and()

                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/api/auth/login")
                // 自定义成功处理器，针对API请求返回JSON
                .successHandler((request, response, authentication) -> {
                    if (isApiRequest(request)) {
                        response.setStatus(HttpStatus.OK.value());
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        Map<String, Object> result = new HashMap<>();
                        result.put("code", 200);
                        result.put("message", "登录成功");
                        result.put("data", authentication.getPrincipal());
                        
                        ObjectMapper objectMapper = new ObjectMapper();
                        PrintWriter out = response.getWriter();
                        out.print(objectMapper.writeValueAsString(result));
                        out.flush();
                    } else {
                        // 对于非API请求，使用默认重定向
                        response.sendRedirect("/index.html");
                    }
                })
                // 自定义失败处理器，针对API请求返回JSON
                .failureHandler((request, response, exception) -> {
                    if (isApiRequest(request)) {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        Map<String, Object> result = new HashMap<>();
                        result.put("code", 401);
                        result.put("message", "用户名或密码错误");
                        
                        ObjectMapper objectMapper = new ObjectMapper();
                        PrintWriter out = response.getWriter();
                        out.print(objectMapper.writeValueAsString(result));
                        out.flush();
                    } else {
                        // 对于非API请求，重定向到错误页面
                        response.sendRedirect("/login.html?error=true");
                    }
                })
                .permitAll()
                .and()

                .httpBasic()
                .and()

                .logout()
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    if (isApiRequest(request)) {
                        response.setStatus(HttpStatus.OK.value());
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        Map<String, Object> result = new HashMap<>();
                        result.put("code", 200);
                        result.put("message", "注销成功");
                        
                        ObjectMapper objectMapper = new ObjectMapper();
                        PrintWriter out = response.getWriter();
                        out.print(objectMapper.writeValueAsString(result));
                        out.flush();
                    } else {
                        response.sendRedirect("/login.html");
                    }
                })
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
                .and()

                .sessionManagement()
                .maximumSessions(1)
                .expiredUrl("/login.html?expired=true");
    }
    
    /**
     * 判断是否为API请求
     */
    private boolean isApiRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.contains("/api/");
    }
}