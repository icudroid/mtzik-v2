package fr.k2i.adbeback.dao.jpa;


import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.dao.IGenericDao;

import java.util.List;

/**
 * User: dimitri
 * Date: 07/01/15
 * Time: 11:11
 * Goal:
 */
public interface IUserRepository extends IGenericDao<User,Integer> {

    User findByLoginEnabled(String username);

    List<User> findUsersWithProfile(Integer idProfile);

    User findByLogin(String username);
}
