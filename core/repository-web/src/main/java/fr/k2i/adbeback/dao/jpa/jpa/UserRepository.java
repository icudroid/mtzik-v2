package fr.k2i.adbeback.dao.jpa.jpa;


import com.mysema.query.jpa.impl.JPAQuery;
import fr.k2i.adbeback.core.business.user.QUser;
import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.core.business.user.security.QProfile;
import fr.k2i.adbeback.dao.jpa.GenericDaoJpa;
import fr.k2i.adbeback.dao.jpa.IUserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: dimitri
 * Date: 07/01/15
 * Time: 11:12
 * Goal:
 */
@Repository
public class UserRepository extends GenericDaoJpa<User,Integer> implements IUserRepository {


    @Override
    public User findByLoginEnabled(String username) {
        JPAQuery query = createQuery();

        QUser user = QUser.user;

        query.from(user).where(user.username.eq(username).and(user.enabled.eq(true)));

        return query.uniqueResult(user);
    }

    @Override
    public List<User> findUsersWithProfile(Integer idProfile) {
        JPAQuery query = createQuery();

        QUser user = QUser.user;
        QProfile profile = QProfile.profile;

        query.from(user).join(user.profiles,profile).where(profile.id.eq(idProfile));

        return query.list(user);

    }

    @Override
    public User findByLogin(String username) {
        JPAQuery query = createQuery();

        QUser user = QUser.user;

        query.from(user).where(user.username.eq(username));

        return query.uniqueResult(user);
    }





}
