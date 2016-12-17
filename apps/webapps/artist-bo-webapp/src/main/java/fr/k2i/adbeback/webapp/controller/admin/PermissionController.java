package fr.k2i.adbeback.webapp.controller.admin;


import fr.k2i.adbeback.dao.jpa.IRoleRepository;
import fr.k2i.adbeback.webapp.bean.security.ProfileBean;
import fr.k2i.adbeback.webapp.bean.security.RoleBean;
import fr.k2i.adbeback.webapp.facade.PermissionFacade;
import fr.k2i.adbeback.webapp.validator.ProfileValidator;
import fr.k2i.adbeback.webapp.validator.RoleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static fr.k2i.adbeback.webapp.metadata.IMetaDataController.PermissionController.*;
import static fr.k2i.adbeback.webapp.metadata.IMetaDataController.PermissionController.Views.*;

/**
 * User: dimitri
 * Date: 12/01/15
 * Time: 15:04
 * Goal:
 */
@Controller

@RequestMapping(PREFIX_PATH)
public class PermissionController {

    @Autowired
    private PermissionFacade permissionFacade;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private ProfileValidator profileValidator;

    @Autowired
    private RoleValidator roleValidator;


    @InitBinder("profile")
    protected void initBinderProfile(WebDataBinder binder) {
        binder.setValidator(profileValidator);
    }


    @InitBinder("role")
    protected void initBinderRole(WebDataBinder binder) {
        binder.setValidator(roleValidator);
    }



    @Secured("ROLE_PROFILE_L")
    @RequestMapping(ListProfiles.PATH)
    private String listProfiles(ModelMap map){
        map.addAttribute("profiles", permissionFacade.findAllProfiles());
        map.addAttribute("roles", permissionFacade.getAllRole());
        return ListProfiles.VIEW;
    }



    @Secured("ROLE_PROFILE_R")
    @RequestMapping(EditProfile.PATH)
    private String editProfile(@PathVariable Integer id ,ModelMap map){
        map.addAttribute("profile", permissionFacade.getProfile(id));
        map.addAttribute("roles", permissionFacade.getAllRole());
        return EditProfile.VIEW;
    }





    @Secured("ROLE_PROFILE_W")
    @RequestMapping(CreateProfile.PATH)
    private String createProfile(ModelMap map){
        map.addAttribute("profile", new ProfileBean());
        map.addAttribute("roles", permissionFacade.getAllRole());
        return CreateProfile.VIEW;
    }



    @Secured("ROLE_PROFILE_W")
    @RequestMapping(value = CreateProfile.PATH,method = RequestMethod.POST)
    private String createProfile(@Valid @ModelAttribute("profile") ProfileBean profile,BindingResult bindingResult,ModelMap map){
        map.addAttribute("roles", permissionFacade.getAllRole());

        if(bindingResult.hasErrors()){
            return CreateProfile.VIEW;
        }else{
            permissionFacade.createProfile(profile);
            return CreateProfile.CREATED;
        }

    }




    @Secured("ROLE_ROLE_R")
    @RequestMapping(EditRole.PATH)
    private String editRole(@PathVariable Integer id ,ModelMap map){
        map.addAttribute("role", permissionFacade.getRole(id));
        map.addAttribute("permissionsRights", permissionFacade.getAllPermissions());
        return EditRole.VIEW;
    }



    @Secured("ROLE_ROLE_W")
    @RequestMapping(CreateRole.PATH)
    private String createRole(@ModelAttribute("role")RoleBean role ,ModelMap map){
        map.addAttribute("permissionsRights", permissionFacade.getAllPermissions());
        return CreateRole.VIEW;
    }


    @Secured("ROLE_ROLE_W")
    @RequestMapping(value = EditRole.SAVE,method = RequestMethod.POST)
    private String saveRole(@Valid @ModelAttribute("role")RoleBean role,BindingResult bindingResult,ModelMap map){
        if(bindingResult.hasErrors()){
            map.addAttribute("permissionsRights", permissionFacade.getAllPermissions());

            return EditRole.VIEW;
        }else{
            permissionFacade.saveRole(role);
            return EditRole.SAVED;
        }

    }




    @Secured("ROLE_PROFILE_W")
    @RequestMapping(AddRoleToProfile.PATH)
    private String addRoleModal(){
        return AddRoleToProfile.VIEW;
    }
}
