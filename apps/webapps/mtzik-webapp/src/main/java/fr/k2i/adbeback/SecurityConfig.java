package fr.k2i.adbeback;

import fr.k2i.adbeback.webapp.bean.CartBean;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE - 8)
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
    @Autowired
    private ApplicationContext context;


    @Value(value ="${addonf.static.url}" )
    private String staticUrl;


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new StandardPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService((UserDetailsService) context.getBean("webPlayerDao"));
        UserDetailsService userDetailsService = (UserDetailsService) context.getBean("authenticationUserService");
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);
        auth.authenticationProvider(authenticationProvider);
    }

    @Bean
    public AnonymousAuthenticationFilter anonymousAuthFilter() {
        return new AnonymousAuthenticationFilter("anonymous"){
            @Override
            public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
                    throws IOException, ServletException {

                if (applyAnonymousForThisRequest((HttpServletRequest) req)) {

                    addAttributesToSession((HttpServletRequest)req);

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(createAuthentication((HttpServletRequest) req));

                        if (logger.isDebugEnabled()) {
                            logger.debug("Populated SecurityContextHolder with anonymous token: '"
                                    + SecurityContextHolder.getContext().getAuthentication() + "'");
                        }
                    } else {
                        if (logger.isDebugEnabled()) {
                            logger.debug("SecurityContextHolder not populated with anonymous token, as it already contained: '"
                                    + SecurityContextHolder.getContext().getAuthentication() + "'");
                        }
                    }
                }

                chain.doFilter(req, res);
            }

            private void addAttributesToSession(HttpServletRequest req) {
                if(req.getSession().getAttribute("cart")==null)
                    req.getSession().setAttribute("cart", new CartBean());
                if(req.getSession().getAttribute("staticUrl")==null)
                    req.getSession().setAttribute("staticUrl", staticUrl);
            }

        };
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
                    super.onAuthenticationSuccess(request, response, authentication);
                }

            }

        };
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
                    .antMatchers("/").permitAll()

                    .antMatchers("/home.html").permitAll()
                    .antMatchers("/signup").permitAll()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/logout-success").permitAll()
                    .antMatchers("/back").permitAll()
                    .antMatchers("/ads").permitAll()


                    .antMatchers("/static/**").permitAll()

                /*    .antMatchers("/coral/**").permitAll()
                    .antMatchers("/css/**").permitAll()
                    .antMatchers("/js/**").permitAll()
                    .antMatchers("/img/**").permitAll()
                    .antMatchers("/template/**").permitAll()*/

                    .antMatchers("/checkout.html").permitAll()
                    .antMatchers("/cart.html").permitAll()
                    .antMatchers("/product.html").permitAll()
                    .antMatchers("/catalog.html").permitAll()
                    .antMatchers("/search.html").permitAll()
                    .antMatchers("/search/**").permitAll()
                    .antMatchers("/artistPage.html").permitAll()
                    .antMatchers("/majorPage.html").permitAll()
                    .antMatchers("/getForgottenPwd").permitAll()
                    .antMatchers("/pwdinit/**").permitAll()
                    .antMatchers("/contactForm").permitAll()
                    .antMatchers("/cgu.html").permitAll()
                    .antMatchers("/albumPage.html").permitAll()
                    .antMatchers("/rest/createTr").permitAll()

                    .antMatchers("/getTownsByName/**").permitAll()
                    .antMatchers("/rest/musics/news/*").permitAll()


                    //manage cart
                    .antMatchers("/rest/addToCart/*").permitAll()
                    .antMatchers("/rest/removeFromCart/*").permitAll()
                    .antMatchers("/rest/cart").permitAll()
                    .antMatchers("/rest/cart/empty").permitAll()
                    .antMatchers("/partial/*.html").permitAll()


                    .antMatchers("/rest/validate").permitAll()
                    .antMatchers("/downloadMusic/*").permitAll()
                    .antMatchers("/dln/*").permitAll()
                    .antMatchers("/rest/paymentReturn").permitAll()

                    .antMatchers("/artist_upload").permitAll()
                    .antMatchers("/sample/*").permitAll()

                    .antMatchers("/**").hasRole("USER");

        http.formLogin()
                    .loginPage("/login")
                    .successHandler(ajaxLoginSuccessHandler())
                    .failureHandler(ajaxLoginFailureHandler())
                    .permitAll();

        http.logout()
                    .deleteCookies("remove")
                    .invalidateHttpSession(false)
                    .logoutUrl("/custom-logout")
                    .logoutSuccessUrl("/");

        http.sessionManagement()
                    .maximumSessions(1)
                    .expiredUrl("/");

        http.anonymous()
                .authenticationFilter(anonymousAuthFilter());


        http.csrf().disable();
/*        http
                .headers()
                    .frameOptions().disable();*/
    }



}

