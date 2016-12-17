package fr.k2i.adbeback;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE - 8)
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
    @Autowired
    private ApplicationContext context;


    @Autowired
    private DataSource dataSource;


    @Value(value ="${addonf.static.url}" )
    private String staticUrl;


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new StandardPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService((UserDetailsService) context.getBean("webPlayerDao"));
        UserDetailsService userDetailsService =  getUserDetailsService();
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);
        auth.authenticationProvider(authenticationProvider);
    }

    private UserDetailsService getUserDetailsService() {
        return context.getBean("authenticationUserService",UserDetailsService.class);
    }



    public SimpleUrlAuthenticationFailureHandler ajaxLoginFailureHandler(){
        return new SimpleUrlAuthenticationFailureHandler(){
            protected final Log logger = LogFactory.getLog(this.getClass());

            @Override
            public void onAuthenticationFailure(HttpServletRequest request,
                                                HttpServletResponse response, AuthenticationException exception)
                    throws IOException, ServletException {
                if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                    response.setContentType("application/json");
                    response.getWriter().print("{\"success\":false}");
                    response.getWriter().flush();
                } else {
                    super.onAuthenticationFailure(request, response, exception);
                }
            }

        };
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    //.antMatchers("/").permitAll()
                    .antMatchers("/signup").permitAll()
                    .antMatchers("/login").permitAll()
                    //.antMatchers("/logout-success").permitAll()
                    .antMatchers("/pwdinit/**").permitAll()
                    .antMatchers("/getForgottenPwd").permitAll()
                    .antMatchers("/static/**").permitAll()
                    .antMatchers("/account/confirmCreateAccount/**").permitAll()
                    .antMatchers("/**").authenticated();

        http.formLogin()
                .successHandler(ajaxLoginSuccessHandler())
                .failureHandler(ajaxLoginFailureHandler())
                .loginPage("/login")
                .failureUrl("/login?error")
                .permitAll();

        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(false)
                .logoutSuccessUrl("/");

        http.sessionManagement()
                .maximumSessions(1)
                .expiredUrl("/login?expired");

        http.exceptionHandling().accessDeniedPage("/access.error");


        http.csrf().disable();

        /*http.rememberMe()
                //.authenticationSuccessHandler(authenticationSuccessHandler())
                .key("key")
                .tokenRepository(tokenRepository())
                .userDetailsService(getUserDetailsService())
                .tokenValiditySeconds(60*60*24*5)//5 days
                //.useSecureCookie(true)
        ;*/


/*        http
                .headers()
                    .frameOptions().disable();*/
    }


    public SimpleUrlAuthenticationSuccessHandler ajaxLoginSuccessHandler(){
        return new SimpleUrlAuthenticationSuccessHandler(){
            protected final Log logger = LogFactory.getLog(this.getClass());

            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws ServletException, IOException {

                if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                    response.setContentType("application/json");
                    response.getWriter().print("{\"success\":true}");
                    response.getWriter().flush();
                } else {
                    SavedRequestAwareAuthenticationSuccessHandler auth
                            = new SavedRequestAwareAuthenticationSuccessHandler();
                    auth.setTargetUrlParameter("targetUrl");
                    auth.onAuthenticationSuccess(request, response, authentication);
                }

            }

        };
    }


    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl persistentTokenRepository = new JdbcTokenRepositoryImpl();
        persistentTokenRepository.setDataSource(dataSource);
        return persistentTokenRepository;
    }


}

