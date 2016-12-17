package fr.k2i.adbeback.webapp.metadata;


public interface IMetaDataController {


    interface HomeController {
        interface Views {
            interface Index {
                String PATH = "/";
                String VIEW = "index";
            }

            interface Home {
                String PATH = "/home";
                String VIEW = "home";
            }
        }
    }


    interface PermissionRestController {
        String PREFIX_PATH = "/admin/rest/permission";
        String FIND_PERMISSION_BY_ROLE_ID = "/byRole/{roleId}";
        String FIND_ALL_PERMISSION = "/all";


    }


    interface RoleRestController {
        String PREFIX_PATH = "/admin/rest/role";
        String DELETE_ROLE = "/delete/{id}";
        String EXIST_ROLE = "/exist/{id}/{newName}";

    }



    interface ProfileRestController {
        String PREFIX_PATH   = "/admin/rest/profile";
        String SAVE_PROFILE  = "/save";
        String EXIST_PROFILE = "/exist/{id}/{newName}";
        String DELETE_PROFILE = "/delete/{id}";

    }


    interface UserController {
        String PREFIX_PATH = "/admin/user";
        interface Views {
            interface List {
                String PATH = "";
                String VIEW = "admin/user/list";
            }
        }
    }


    interface PermissionController {
        String PREFIX_PATH = "/admin/profile";
        interface Views {

            interface ListProfiles {
                String PATH = "";
                String VIEW = "admin/permission/profiles";
            }


            interface EditProfile {
                String PATH = "/edit/{id}";
                String VIEW = "admin/permission/profile_edit_modal";
            }


            interface EditRole {
                String PATH = "/role/edit/{id}";
                String VIEW = "admin/permission/role_edit_modal";
                String SAVE = "/role/save";
                String SAVED = "admin/permission/role_saved";
            }


            interface CreateRole {
                String PATH = "/role/create";
                String VIEW = "admin/permission/role_edit_modal";
            }

            interface AddRoleToProfile {
                String PATH = "/addRole";
                String VIEW = "admin/permission/profile_add_role_modal";
            }

            interface CreateProfile {
                String PATH = "/create";
                String VIEW = "admin/permission/profile_create_modal";
                String CREATED = "admin/permission/profile_created";
            }
        }
    }



    interface  UserRestController{
        String PREFIX_PATH   = "/admin/rest/user";
        String SEARCH = "/search";
    }



}
