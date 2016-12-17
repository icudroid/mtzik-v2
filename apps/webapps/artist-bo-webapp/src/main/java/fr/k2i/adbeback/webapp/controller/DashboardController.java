package fr.k2i.adbeback.webapp.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by dev on 28/01/15.
 */
@Controller
public class DashboardController {

    @Secured("ROLE_DASHBOARD")
    @RequestMapping("/dashboard")
    public String dashboard(){
        return "dashboard/dashboard";
    }
}
