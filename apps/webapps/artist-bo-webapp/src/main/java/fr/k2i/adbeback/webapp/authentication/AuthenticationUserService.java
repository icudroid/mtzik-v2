package fr.k2i.adbeback.webapp.authentication;

import com.mysema.query.jpa.impl.JPAQuery;
import fr.k2i.adbeback.core.business.user.BOUser;
import fr.k2i.adbeback.core.business.user.QUser;
import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.dao.jpa.GenericDaoJpa;
import fr.k2i.adbeback.dao.jpa.IProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve User objects.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *   Modified by <a href="mailto:dan@getrolling.com">Dan Kibler</a>
 *   Extended to implement Acegi UserDetailsService interface by David Carter david@carter.net
 *   Modified by <a href="mailto:bwnoll@gmail.com">Bryan Noll</a> to work with 
 *   the new BaseDaoHibernate implementation that uses generics.
*/
@Repository
public class AuthenticationUserService extends GenericDaoJpa<User, Long> implements UserDetailsService {


    @Autowired
    private IProfileRepository profileRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QUser user = QUser.user;

        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(user)
                .where(
                        user.username.eq(username).or(user.email.eq(username))
                );

        List<User> users = query.list(user);

        if (users == null || users.isEmpty()) {
            throw new UsernameNotFoundException("user '" + username + "' not found...");
        } else {

            if(
                    users.get(0).getProfiles().contains(profileRepository.findByName(IProfileRepository.ADMIN))
                    ||
                    users.get(0).getProfiles().contains(profileRepository.findByName(IProfileRepository.ARTIST_LABEL))
                    ||
                    users.get(0).getProfiles().contains(profileRepository.findByName(IProfileRepository.LABEL_PROFILE))

            ){
                return new BOUser(users.get(0),profileRepository.loadGrantedAuthorities(users.get(0)));
            }else{
                throw new UsernameNotFoundException("user '" + username + "' not found...");
            }


        }
    }

}

