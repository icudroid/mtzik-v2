package fr.k2i.adbeback.webapp.controller;


import fr.k2i.adbeback.application.services.mail.IMailEngine;
import fr.k2i.adbeback.application.services.mail.exception.SendException;
import fr.k2i.adbeback.crypto.DESCryptoService;
import fr.k2i.adbeback.webapp.bean.ChangePasswordBean;
import fr.k2i.adbeback.webapp.bean.ForgottenPasswordBean;
import fr.k2i.adbeback.webapp.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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

    @Autowired(required = false)
    Validator validator;

    @Autowired
    private DESCryptoService desCryptoService;

    @Autowired
    private IMailEngine mailEngine;


    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @Autowired
    private UserFacade userFacade;

    @RequestMapping(value = "/getForgottenPwd" ,method = RequestMethod.GET)
    public String showGetForgottenPwd(@ModelAttribute("passwordBean")ForgottenPasswordBean passwordBean,Map<String, Object> model){
        model.put("sended",false);
        return "getForgottenPwd";
    }


    @RequestMapping(value = "/getForgottenPwd",method = RequestMethod.POST)
    public String sendEmail(@Valid  @ModelAttribute("passwordBean")ForgottenPasswordBean username,BindingResult bindingResult,Map<String, Object> model,HttpServletRequest request) throws SendException {

        model.put("sended",false);

        if(!bindingResult.hasErrors()){
            userFacade.sendForgottenPasswd(username.getUsername(),request);
            model.put("sended",true);
        }

        return "getForgottenPwd";
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
            model.put("changed",true);
            return "changePwd";
        }else{
            model.put("changed",false);
            return "changePwd";
        }
    }


}
