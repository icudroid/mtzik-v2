package fr.k2i.adbeback.webapp.controller.admin.rest;


import fr.k2i.adbeback.webapp.bean.WebResponseBean;
import fr.k2i.adbeback.webapp.facade.PermissionFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static fr.k2i.adbeback.webapp.metadata.IMetaDataController.RoleRestController.*;

/**
 * User: dimitri
 * Date: 12/01/15
 * Time: 15:04
 * Goal:
 */
@RestController
@RequestMapping(PREFIX_PATH)
public class RoleRestController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PermissionFacade permissionFacade;


    @Secured("ROLE_ROLE_W")
    @RequestMapping(DELETE_ROLE)
    private WebResponseBean<Void> deleteRoleById(@PathVariable Integer id){

        try {
            permissionFacade.deleteRoleById(id);
            return WebResponseBean.OK();
        } catch (Exception e) {
            logger.error("Une erreur lors de la suppression du role "+id,e);
            //Todo:
            return WebResponseBean.FAILED(1,e.getMessage());
        }
    }


    @Secured("ROLE_ROLE_R")
    @RequestMapping(value = EXIST_ROLE)
    private @ResponseBody
    WebResponseBean<Boolean> existProfile(@PathVariable Integer id,@PathVariable String newName){
        try {
            return WebResponseBean.OK_WITH_DATA(permissionFacade.existRole(id,newName));
        } catch (Exception e) {
            logger.error("Une erreur",e);
            //Todo:
            return WebResponseBean.FAILED(1,e.getMessage());
        }

    }








}
