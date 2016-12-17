package fr.k2i.adbeback.webapp.controller.admin.rest;

import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.webapp.bean.UserBean;
import fr.k2i.adbeback.webapp.bean.datatable.query.DataTableQuery;
import fr.k2i.adbeback.webapp.bean.datatable.response.DataTableResponse;
import fr.k2i.adbeback.webapp.command.UserCommand;
import fr.k2i.adbeback.webapp.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;


import java.util.Arrays;
import java.util.List;

import static fr.k2i.adbeback.webapp.metadata.IMetaDataController.UserRestController.*;

/**
 * Created by dev on 08/02/15.
 */
@RestController
@RequestMapping(PREFIX_PATH)
public class UserRestController {
    private final static List<String> USER_CONFIG = Arrays.asList("username", "email", "identity.firstName", "identity.lastName", "profiles", "identity.class", "id");

    @Autowired
    private UserFacade userFacade;


    @Secured("ROLE_USER_L")
    @RequestMapping(value = SEARCH,method = RequestMethod.POST)
    public @ResponseBody
    DataTableResponse<UserBean> list(@RequestBody DataTableQuery<UserCommand> query){
        UserCommand additionalForm = query.getAdditionalForm();
        return userFacade.findByQuery(query, USER_CONFIG);
    }

}
