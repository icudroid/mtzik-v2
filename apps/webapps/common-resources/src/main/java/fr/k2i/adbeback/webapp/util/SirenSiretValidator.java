package fr.k2i.adbeback.webapp.util;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

/**
 * User: dimitri
 * Date: 01/02/13
 * Time: 11:53
 * Goal: Validate Siren and siret code
 */
@Component
public class SirenSiretValidator {

    private static final String REGEX_SIRET_SIREN = "(\\d{9}|\\d{14})";

    private static final String REQUIRED_MSG = "required";
    private static final String PATERN_EXCEPTION_MSG = "wrong";

    public void validate(String siretOrSiren,Errors errors,String path){
        if(StringUtils.isEmpty(siretOrSiren)){
            errors.rejectValue(path, REQUIRED_MSG);
        }else if(!siretOrSiren.matches(REGEX_SIRET_SIREN)){
            errors.rejectValue(path, PATERN_EXCEPTION_MSG);
        }else{
            if(siretOrSiren.length() == 9){
                validateSiren(siretOrSiren, errors, path);
            }else if(siretOrSiren.length() == 14){
                validateSiret(siretOrSiren, errors, path);
            }
        }
    }

    public void validateSiret(String siret, Errors errors, String path) {
        //index paire * 1
        //index impaire * 2
        //sum multiple of 10
        int somme = 0;
        for(int index=0;index<siret.length();index++){
            if(index % 2 == 0){
                int value = Character.getNumericValue(siret.charAt(index)) * 2;
                somme+= (value >9)?((value-10)+1):value;
            }else{
                somme+=Character.getNumericValue(siret.charAt(index));
            }
        }

        if(somme%10 != 0){
            errors.rejectValue(path, PATERN_EXCEPTION_MSG);
        }

    }

    public void validateSiren(String siren, Errors errors, String path) {
        //index paire * 1
        //index impaire * 2
        //sum multiple of 10
        int somme = 0;
        for(int index=0;index<siren.length();index++){
            if(index % 2 == 0){
                somme+=Character.getNumericValue(siren.charAt(index));
            }else{
                int value = Character.getNumericValue(siren.charAt(index)) * 2;
                somme+= (value >9)?((value-10)+1):value;
            }
        }

        if(somme%10 != 0){
            errors.rejectValue(path, PATERN_EXCEPTION_MSG);
        }
    }


}
