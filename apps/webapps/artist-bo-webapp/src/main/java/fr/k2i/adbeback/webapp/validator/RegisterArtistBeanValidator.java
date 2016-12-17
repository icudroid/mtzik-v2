package fr.k2i.adbeback.webapp.validator;

import edu.vt.middleware.password.*;
import fr.k2i.adbeback.dao.jpa.IUserBoDao;
import fr.k2i.adbeback.logger.LogHelper;
import fr.k2i.adbeback.webapp.bean.RegisterBoBean;
import fr.k2i.adbeback.webapp.util.PhoneNumberUtils;
import fr.k2i.adbeback.webapp.util.ValidatorHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.ConstraintViolation;
import javax.validation.groups.Default;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * User: dimitri
 * Date: 09/12/14
 * Time: 13:39
 * Goal:
 */
@Component
public class RegisterArtistBeanValidator implements Validator {
    private Logger logger = LogHelper.getLogger(this.getClass());

    @Autowired
    private javax.validation.Validator beanValidator;

    @Autowired
    private ValidatorHelper validatorHelper;

    @Autowired
    private IUserBoDao userBoDao;

    @Override
    public boolean supports(Class<?> aClass) {
        return RegisterBoBean.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RegisterBoBean register = (RegisterBoBean) o;

        Set<ConstraintViolation<RegisterBoBean>> validate = beanValidator.validate(register, Default.class);
        validatorHelper.importBeanValidationErrors(validate,errors);

        if(userBoDao.existUserBoName(register.getName())){
            errors.rejectValue("name","artistname.exist");
        }

        if(userBoDao.existEmail(register.getEmail())){
            errors.rejectValue("email","email.exist");
        }
        if(userBoDao.existUserName(register.getUsername())){
            errors.rejectValue("username","username.exist");
        }

        if(StringUtils.isEmpty(register.getPassword())){
            errors.rejectValue("password","password.required");
        }

        if(StringUtils.isEmpty(register.getRpassword())){
            errors.rejectValue("rpassword","password.not.match");
        }else if(!StringUtils.isEmpty(register.getPassword()) && !StringUtils.isEmpty(register.getRpassword()) &&
                !register.getPassword().equals(register.getRpassword())){
            errors.rejectValue("rpassword","password.not.match");
        }else{



            // password must be between 8 and 16 chars long
            LengthRule lengthRule = new LengthRule(8, 16);

            // don't allow whitespace
            WhitespaceRule whitespaceRule = new WhitespaceRule();

            // control allowed characters
            CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule();
            // require at least 1 digit in passwords
            charRule.getRules().add(new DigitCharacterRule(1));
            // require at least 1 non-alphanumeric char
            charRule.getRules().add(new NonAlphanumericCharacterRule(1));
            // require at least 1 upper case char
            charRule.getRules().add(new UppercaseCharacterRule(1));
            // require at least 1 lower case char
            charRule.getRules().add(new LowercaseCharacterRule(1));
            // require at least 3 of the previous rules be met
            charRule.setNumberOfCharacteristics(3);

            // don't allow alphabetical sequences
            AlphabeticalSequenceRule alphaSeqRule = new AlphabeticalSequenceRule();

            // don't allow 4 repeat characters
            RepeatCharacterRegexRule repeatRule = new RepeatCharacterRegexRule(4);

            // group all rules together in a List
            List<Rule> ruleList = new ArrayList<Rule>();
            ruleList.add(lengthRule);
            ruleList.add(whitespaceRule);
            ruleList.add(charRule);
            ruleList.add(alphaSeqRule);
            ruleList.add(repeatRule);


            PasswordValidator validator = new PasswordValidator(ruleList);
            PasswordData passwordData = new PasswordData(new Password(register.getPassword()));

            RuleResult result = validator.validate(passwordData);
            if (!result.isValid()) {
                errors.rejectValue("password","not.enough.strong");
            }

        }

        if(!org.springframework.util.StringUtils.isEmpty(register.getPhone()) && !PhoneNumberUtils.isValidPhoneNumber(register.getPhone(), register.getCountry())){
            errors.rejectValue("phone","wrong");
        }

/*        if (register.getCityId()==null) {
            errors.rejectValue("address", "errors.required", new Object[] { getText("user.address.required", request.getLocale()) },
                    "Address is a required field.");
        }*/

        if (register.getBirthDate()==null) {
            Locale locale = LocaleContextHolder.getLocale();
            DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
            builder .appendValue(ChronoField.DAY_OF_MONTH)
                    .appendLiteral("/")
                    .appendValue(ChronoField.MONTH_OF_YEAR)
                    .appendLiteral("/")
                    .appendValue(ChronoField.YEAR);
            DateTimeFormatter formatter = builder.toFormatter();
            LocalDate date = LocalDate.parse(register.getBirthDate(), formatter);
            String dateToCheck = date.format(formatter);
            if(!register.getBirthDate().equals(dateToCheck)){
                errors.rejectValue("birthDate", "wrong.dateFormat");
            }else{
                if(date.isAfter(LocalDate.now())){
                    errors.rejectValue("birthDate", "wrong.age");
                }
            }



        }


    }
}
