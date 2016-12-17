package fr.k2i.adbeback.webapp.controller.admin.rest;


import fr.k2i.adbeback.webapp.bean.security.PermissionRightDto;
import fr.k2i.adbeback.webapp.facade.PermissionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static fr.k2i.adbeback.webapp.metadata.IMetaDataController.PermissionRestController.*;

/**
 * User: dimitri
 * Date: 12/01/15
 * Time: 15:04
 * Goal:
 */
@RestController
@RequestMapping(PREFIX_PATH)
public class PermissionRestController {

    @Autowired
    private PermissionFacade permissionFacade;


    @Secured("ROLE_PERMISSION_R")
    @RequestMapping(FIND_PERMISSION_BY_ROLE_ID)
    private List<PermissionRightDto> findPermissionsByRoleId(@PathVariable Integer roleId){
        return permissionFacade.findPermissionsByRoleId(roleId);
    }




    @Secured("ROLE_PERMISSION_R")
    @RequestMapping(FIND_ALL_PERMISSION)
    private List<PermissionRightDto> findAllPermissions(){
        return permissionFacade.findAllPermissions();
    }




}
