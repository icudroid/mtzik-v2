package fr.k2i.adbeback.dao.jpa.jpa;

import com.mysema.query.jpa.impl.JPAQuery;
import fr.k2i.adbeback.core.business.user.QUser;
import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.dao.jpa.GenericDaoJpa;
import fr.k2i.adbeback.dao.jpa.IUserDao;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;
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
@Repository("playerDao")
public class UserDao extends GenericDaoJpa<User, Long> implements IUserDao {


    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<User> getUser() {

        JPAQuery query = new JPAQuery(getEntityManager());
        QUser qUser = QUser.user;

        query.from(qUser);
        query.orderBy(qUser.username.toLowerCase().asc());
        return query.list(qUser);

/*        CriteriaBuilderHelper<User> helper = new CriteriaBuilderHelper(getEntityManager(),User.class);
        helper.criteriaHelper.asc(helper.criteriaHelper.upper(helper.rootHelper.get(User_.username)));
        return helper.getResultList();*/
    }


    @Transactional
    public User saveUser(User user) {
        User u = super.save(user);
        getEntityManager().flush();
        return u;
    }



	public User loadUserByEmail(String email) {

        JPAQuery query = new JPAQuery(getEntityManager());
        QUser qUser = QUser.user;

        query.from(qUser);
        query.where(qUser.email.eq(email));

/*
        CriteriaBuilderHelper<User> helper = new CriteriaBuilderHelper(getEntityManager(),User.class);
        helper.criteriaHelper.and(
                helper.criteriaHelper.equal(helper.rootHelper.get(User_.email), email)
        );
*/
        List users = query.list(qUser);
        if (users == null || users.isEmpty()) {
            return null;
        } else {
            return (User) users.get(0);
        }
	}


    @Override
    public User findByEmailorUserName(String username) {
        QUser player = QUser.user;
        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(player)
                .where(
                        player.username.eq(username).or(player.email.eq(username))
                );

        return query.uniqueResult(player);
    }



    @Override
    public String getUserPassword(String username) {
        JdbcTemplate jdbcTemplate =
                new JdbcTemplate(org.springframework.orm.hibernate4.SessionFactoryUtils.getDataSource(getSessionFactory()));
        Table table = AnnotationUtils.findAnnotation(User.class, Table.class);
        return jdbcTemplate.queryForObject(
                "select password from " + table.name() + " where username=?", String.class, username);
    }

}

