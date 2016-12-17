package fr.k2i.adbeback.webapp.controller.admin;

import com.google.common.collect.Lists;
import fr.k2i.adbeback.webapp.command.UserCommand;
import fr.k2i.adbeback.webapp.command.UserType;
import fr.k2i.adbeback.webapp.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


import static fr.k2i.adbeback.webapp.metadata.IMetaDataController.UserController.*;
import static fr.k2i.adbeback.webapp.metadata.IMetaDataController.UserController.Views.*;

/**
 * Created by dev on 08/02/15.
 */
@Controller
@RequestMapping(PREFIX_PATH)
public class UserController {

    @Autowired
    private UserFacade userFacade;

    @Secured("ROLE_USER_L")
    @RequestMapping(List.PATH)
    private String listUsers(ModelMap map){
        map.addAttribute("users", userFacade.findAllUsers());
        map.addAttribute("command",new UserCommand());
        map.addAttribute("userTypes", Lists.asList(null, UserType.values()));
        return List.VIEW;
    }

}
