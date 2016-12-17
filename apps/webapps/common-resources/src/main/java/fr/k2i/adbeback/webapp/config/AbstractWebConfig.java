package fr.k2i.adbeback.webapp.config;

import fr.k2i.adbeback.application.services.mail.config.EmailConfig;
import fr.k2i.adbeback.webapp.util.ReloadableResourceYamlMessageSource;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

import javax.validation.Validator;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 14/12/13
 * Time: 11:14
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractWebConfig extends SpringBootServletInitializer {

    private static String I18N_BASE = "classpath:i18n/";

    @Bean
    public EmailConfig emailConfig(){
        return new EmailConfig();
    }


    @Bean
    public MessageSource messageSource(){
        ReloadableResourceYamlMessageSource messageSource = new ReloadableResourceYamlMessageSource();
        messageSource.setBasenames(I18N_BASE+"resources",I18N_BASE+"errors",I18N_BASE+"references");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }


    @Bean
    public Validator validator(){
        return new LocalValidatorFactoryBean();
    }


    @Bean
    public IDialect java8TimeDialect(){
        return new Java8TimeDialect();
    }
}
