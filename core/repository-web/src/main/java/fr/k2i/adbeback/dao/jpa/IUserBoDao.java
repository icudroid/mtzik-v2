package fr.k2i.adbeback.dao.jpa;

import fr.k2i.adbeback.core.business.filter.UserFilter;
import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.dao.IGenericDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * User Data Access Object (IGenericDao) interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface IUserBoDao extends IGenericDao<User, Long> {


    User findByEmailorUserName(String username);

    User loadUserByEmail(String email);

    User saveUser(User user);

    String getUserPassword(String username);

    boolean existUserBoName(String artistName);

    boolean existEmail(String email);

    boolean existUserName(String username);

    User getUserByEmail(String email);

    void enable(Long userId);

    void setPassword(Long userId, String password);

    Page<User> findUsers(UserFilter filter, Sort sort, Pageable pageable);
}

