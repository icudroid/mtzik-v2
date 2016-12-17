package fr.k2i.adbeback.webapp.controller;

import fr.k2i.adbeback.crypto.DESCryptoService;
import fr.k2i.adbeback.dao.jpa.IUserBoDao;
import fr.k2i.adbeback.webapp.bean.RegisterBoBean;
import fr.k2i.adbeback.webapp.bean.TypeRegistration;
import fr.k2i.adbeback.webapp.facade.OtpServiceFacade;
import fr.k2i.adbeback.webapp.facade.UserFacade;
import fr.k2i.adbeback.webapp.validator.RegisterArtistBeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dimitri
 * Date: 29/10/13
 * Time: 13:25
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class RegisterController {

    @Autowired
    private RegisterArtistBeanValidator registerArtistBeanValidator;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private DESCryptoService desCryptoService;

    @Autowired
    private OtpServiceFacade otpServiceFacade;

    @Autowired
    private IUserBoDao userBoDao;


    @InitBinder("registerBoBean")
    protected void initBinderId(WebDataBinder binder) {
        binder.setValidator(registerArtistBeanValidator);
    }

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



    @RequestMapping(value = "/signup",method = RequestMethod.POST)
    public String signup(@Valid @ModelAttribute("registerBoBean")RegisterBoBean registerArtistBean, BindingResult bindingResult,ModelMap map){

        if(bindingResult.hasErrors()){
            map.addAttribute("signup",true);
        }else{
            userFacade.createAccount(registerArtistBean);
            map.addAttribute("signedUp",true);
        }
        return "login";

    }


    @RequestMapping(value = "account/confirmCreateAccount/{crypt}/{code}")
    public String confirmAdminRegistration(@PathVariable String crypt,@PathVariable String code,ModelMap modelMap, HttpServletRequest req) throws Exception {
        String[] strings = desCryptoService.decrypt(crypt).split("\\|");

        String name = strings[0];
        String email = strings[1];

        OtpServiceFacade.ConfirmationRegistration res = otpServiceFacade.confirmRegistration(email,name,code);
        HttpSession session = req.getSession();
        modelMap.addAttribute("result", res.name());
        modelMap.addAttribute("email", email);

        switch (res){
            case OK:
                otpServiceFacade.removeOtpAndValidateUser(email, name, code);
                modelMap.addAttribute("createdAccount",true);
                modelMap.addAttribute("username",name);
                return "login";
            default:
                modelMap.addAttribute("createdAccountError",true);
                return "login";
        }


    }






}

