package fr.k2i.adbeback.webapp.controller;

import fr.k2i.adbeback.webapp.bean.RegisterBoBean;
import fr.k2i.adbeback.webapp.bean.TypeRegistration;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dimitri
 * Date: 29/10/13
 * Time: 13:25
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class HomeController{
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(@ModelAttribute("registerBoBean")RegisterBoBean registerBoBean, ModelMap map){
        List<String> countryCodes = new ArrayList<String>();
        countryCodes.add("FR");
        map.addAttribute("allowCountries",countryCodes);
        map.addAttribute("typeRegistrations", TypeRegistration.values());
        return "login";
    }

    @Secured("ROLE_HOME")
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String home(){
        return "home";
    }

}

