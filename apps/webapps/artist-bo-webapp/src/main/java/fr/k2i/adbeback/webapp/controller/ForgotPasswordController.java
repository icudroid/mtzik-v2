package fr.k2i.adbeback.webapp.controller;


import fr.k2i.adbeback.application.services.mail.IMailEngine;
import fr.k2i.adbeback.application.services.mail.exception.SendException;
import fr.k2i.adbeback.crypto.DESCryptoService;
import fr.k2i.adbeback.webapp.bean.ChangePasswordBean;
import fr.k2i.adbeback.webapp.bean.RegisterBoBean;
import fr.k2i.adbeback.webapp.bean.TypeRegistration;
import fr.k2i.adbeback.webapp.facade.UserFacade;
import fr.k2i.adbeback.webapp.validator.ChangePasswordBeanValidator;
import org.hibernate.validator.internal.constraintvalidators.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 14/01/14
 * Time: 12:47
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class ForgotPasswordController {

    @Autowired
    private DESCryptoService desCryptoService;

    @Autowired
    private IMailEngine mailEngine;

    @Autowired
    private ChangePasswordBeanValidator changePasswordBeanValidator;


    @InitBinder(value = "changePasswordBean")
    protected void initBinderChangePasswordBean(WebDataBinder binder) {
        binder.setValidator(changePasswordBeanValidator);
    }


    @Autowired
    private UserFacade userFacade;

    @ModelAttribute("registerBoBean")
    public RegisterBoBean registerArtistBean(){
        return new RegisterBoBean();
    }


    @ModelAttribute("typeRegistrations")
    public TypeRegistration[] typeRegistrations(){
        return TypeRegistration.values();
    }

    @ModelAttribute
    public List<String> allowCountries(){
        List<String> countryCodes = new ArrayList<String>();
        countryCodes.add("FR");
        return countryCodes;
    }


    @RequestMapping(value = "/getForgottenPwd",method = RequestMethod.POST)
    public String getForgottenPwd(@RequestParam("email") String email,ModelMap modelMap,HttpServletRequest request) throws SendException {
        EmailValidator validator = new EmailValidator();

        userFacade.sendForgottenPasswd(email,request);

        modelMap.addAttribute("forgottenPwdSent",true);
        return "login";
    }

    @RequestMapping(value = "/pwdinit/{encryt}/{key}" ,method = RequestMethod.GET)
    public String changePwd(@PathVariable String encryt,@PathVariable String key,@ModelAttribute("changePasswordBean")ChangePasswordBean changePasswordBean,Map<String, Object> model) throws Exception {
        //validate otp
        String username = desCryptoService.decrypt(encryt);
        if(!userFacade.isValidateOtp(username,key)){
            throw new Exception("Bad security key");
        }
        model.put("changed",false);
        return "changePwd";
    }


    @RequestMapping(value = "/pwdinit/{encryt}/{key}" ,method = RequestMethod.POST)
    public String changePwdSubmit(@PathVariable String encryt,@PathVariable String key,@Valid @ModelAttribute("changePasswordBean")ChangePasswordBean changePasswordBean,BindingResult bindingResult,Map<String, Object> model) throws Exception {
        String username = desCryptoService.decrypt(encryt);
        //validate otp
        if(!userFacade.isValidateOtp(username,key)){
            throw new Exception("Bad security key");
        }

        if(!bindingResult.hasErrors()){
            userFacade.changePasswd(username, changePasswordBean.getPassword());

            model.put("passwordChanged",true);
            return "login";
        }else{
            return "changePwd";
        }
    }


}
