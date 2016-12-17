package fr.k2i.adbeback.dao.jpa;


import fr.k2i.adbeback.core.business.user.security.Permission;
import fr.k2i.adbeback.core.business.user.security.Profile;
import fr.k2i.adbeback.dao.IGenericDao;

import java.util.List;
import java.util.Map;

/**
 * User: dimitri
 * Date: 07/01/15
 * Time: 11:11
 * Goal:
 */
public interface IPermissionRepository extends IGenericDao<Permission,Integer> {

    Map<String, List<Permission>> findPermissionsByRoleId(Integer roleId);

    Map<String, List<Permission>> findAllPermissions();

}