package fr.k2i.adbeback.dao.jpa;


import fr.k2i.adbeback.core.business.user.security.Role;
import fr.k2i.adbeback.dao.IGenericDao;

import java.util.List;

/**
 * User: dimitri
 * Date: 07/01/15
 * Time: 11:11
 * Goal:
 */
public interface IRoleRepository extends IGenericDao<Role,Integer> {

    List<Role> findAllInId(List<Integer> rolesId);

    boolean existsByName(Integer id, String name);
}
