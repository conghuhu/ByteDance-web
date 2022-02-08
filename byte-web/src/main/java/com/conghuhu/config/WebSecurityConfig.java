package com.conghuhu.config;

import com.conghuhu.auth.*;
import com.conghuhu.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * @author conghuhu
 * @create 2021-09-28 11:21
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final MyUserDetailsService myUserDetailsService;
    /**
     * 匿名用户访问无权限资源时的异常
     */
    private final CustomizeAuthenticationEntryPoint authenticationEntryPoint;

    private final MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    private final MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    private final UserLogoutSuccessHandler userLogoutSuccessHandler;

    public WebSecurityConfig(MyUserDetailsService myUserDetailsService, CustomizeAuthenticationEntryPoint authenticationEntryPoint, MyAuthenticationSuccessHandler myAuthenticationSuccessHandler, MyAuthenticationFailureHandler myAuthenticationFailureHandler, UserLogoutSuccessHandler userLogoutSuccessHandler) {
        this.myUserDetailsService = myUserDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.myAuthenticationSuccessHandler = myAuthenticationSuccessHandler;
        this.myAuthenticationFailureHandler = myAuthenticationFailureHandler;
        this.userLogoutSuccessHandler = userLogoutSuccessHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailureHandler)
                .and()
                .logout()
                .logoutSuccessHandler(userLogoutSuccessHandler)
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
        ;
        http
                .addFilter(new TokenAuthenticationFilter(authenticationManager())).httpBasic();

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.rememberMe().rememberMeParameter("remember-me")
                .userDetailsService(myUserDetailsService).tokenValiditySeconds(1000);

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 配置认证方式和加密器
        auth.userDetailsService(myUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

//    @Bean
//    public AuthenticationProvider daoAuthenticationProvider() {
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setUserDetailsService(myUserDetailsService);
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
//        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
//        return daoAuthenticationProvider;
//    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        // 设置默认的加密方式（强hash方式加密）
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置哪些请求不拦截
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/swagger**/**",
                "/webjars/**",
                "/v2/**",
                "/register",
                "/mail/sendVerifyCodeToMail**",
                "/users/modifyUserPassWord",
                "/product/getInviteInfo**",
                "/websocket/**"
        );
    }


}
