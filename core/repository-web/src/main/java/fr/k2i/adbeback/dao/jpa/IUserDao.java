package fr.k2i.adbeback.dao.jpa;

import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.dao.IGenericDao;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User Data Access Object (IGenericDao) interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface IUserDao extends IGenericDao<User, Long> {

    /**
     * Gets a list of users ordered by the uppercase version of their username.
     *
     * @return List populated list of users
     */
    List<User> getUser();

    /**
     * Saves a user's information.
     * @param user the object to be saved
     * @return the persisted User object
     */
    User saveUser(User user);

    /**
     * Retrieves the password in DB for a user
     * @param username the user's username
     * @return the password in DB, if the user is already persisted
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    String getUserPassword(String username);

    /**
     * 
     * @param email
     * @return
     */
	User loadUserByEmail(String email);


    User findByEmailorUserName(String username);
}

