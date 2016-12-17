package fr.k2i.adbeback.webapp.util;

import com.google.common.collect.Lists;
import fr.k2i.adbeback.logger.LogHelper;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 22/12/13
 * Time: 15:11
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ValidatorHelper {


    private Logger logger = LogHelper.getLogger(ValidatorHelper.class);

    public <T> void importBeanValidationErrors(Set<ConstraintViolation<T>> violations, Errors errors) {
        importBeanValidationErrors(violations, errors, "");
    }

    public <T> Set<ConstraintViolation<T>> removeConstraintViolation(Set<ConstraintViolation<T>> violations,String ...propertiesToRemove){
        Set<ConstraintViolation<T>> res = new HashSet<ConstraintViolation<T>>();

        ArrayList<String> toRemove = Lists.newArrayList(propertiesToRemove);


        for (ConstraintViolation<T> violation : violations) {
            if(!toRemove.contains(violation.getPropertyPath().toString())){
                res.add(violation);
            }
        }

        return res;
    }

    public <T> void importBeanValidationErrors(Set<ConstraintViolation<T>> violations, Errors errors, String prefix) {
        for (ConstraintViolation<T> violation : violations) {
            if (!errors.hasFieldErrors(violation.getPropertyPath().toString())) {
                String messageCode = violation.getMessageTemplate();
                if (messageCode.endsWith("}")) {
                    // the error code may be surrounded by curly braces (eg :
                    // "{javax.validation.constraints.Size.message}")
                    messageCode = messageCode.substring(1, messageCode.length() - 1);
                }

                Map<String, Object> attributes = violation.getConstraintDescriptor().getAttributes();
                Object[] errorArgs = attributes.values().toArray();
                logger.debug("Rejecting field {}, with code {}, args : {}, default message , {}",
                        new Object[] { prefix + violation.getPropertyPath().toString(), messageCode, errorArgs,
                                violation.getMessage() });
                errors.rejectValue(prefix + violation.getPropertyPath().toString(), messageCode, errorArgs,
                        violation.getMessage());
            }
        }

    }

}
