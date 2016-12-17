package fr.k2i.adbeback.dao.jpa.jpa;


import com.mysema.query.jpa.JPAExpressions;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.types.expr.BooleanExpression;
import fr.k2i.adbeback.core.business.filter.UserFilter;
import fr.k2i.adbeback.core.business.media.*;
import fr.k2i.adbeback.core.business.user.QUser;
import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.dao.jpa.GenericDaoJpa;
import fr.k2i.adbeback.dao.jpa.IUserBoDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
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
@Repository("userBoDao")
public class UserBoDao extends GenericDaoJpa<User, Long> implements IUserBoDao {


    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<User> getUser() {

        JPAQuery query = new JPAQuery(getEntityManager());
        QUser qUser = QUser.user;

        query.from(qUser).where(qUser.identity.instanceOfAny(Artist.class, Productor.class));
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
        query.where(qUser.identity.instanceOfAny(Artist.class, Productor.class).and(qUser.email.eq(email)));

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
        QUser user = QUser.user;
        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(user)
                .where(
                        user.identity.instanceOfAny(Artist.class, Productor.class).and(
                                user.username.eq(username).or(user.email.eq(username))
                        )
                        );

        return query.uniqueResult(user);
    }



    @Override
    public String getUserPassword(String username) {
        QUser user = QUser.user;
        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(user).where(
                user.identity.instanceOfAny(Artist.class, Productor.class)
                        .and(
                            user.username.eq(username).or(user.email.eq(username)
                        )
        ));

        return query.uniqueResult(user.password);
    }

    @Override
    public boolean existUserBoName(String name) {
        return existArtistName(name) || existLabelName(name);
    }


    private boolean existArtistName(String artistName) {
        QArtist user = QArtist.artist;
        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(user)
                .where(
                        user.artistName.eq(artistName)
                );

        return query.exists();
    }


    private boolean existLabelName(String labelName) {
        QProductor user = QProductor.productor;
        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(user)
                .where(
                        user.labelName.eq(labelName)
                );

        return query.exists();
    }

    @Override
    public boolean existEmail(String email) {
        QUser user = QUser.user;
        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(user)
                .where(
                        user.identity.instanceOfAny(Artist.class, Productor.class).and(
                                user.email.eq(email)
                        )
                );

        return query.exists();
    }

    @Override
    public boolean existUserName(String username) {
        QUser user = QUser.user;
        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(user)
                .where(
                        user.identity.instanceOfAny(Artist.class, Productor.class).and(
                                user.username.eq(username)
                        )
                    );

        return query.exists();
    }

    @Override
    public User getUserByEmail(String email) {

        QUser user = QUser.user;

        JPAQuery query = new JPAQuery(getEntityManager());
            query.from(user)
                    .where(
                            user.identity.instanceOfAny(Artist.class, Productor.class).and(
                                    user.email.eq(email).or(user.username.eq(email))
                            )
                        );

            return query.uniqueResult(user);

    }

    @Override
    public void enable(Long userId) {
        log.info("User id"+userId+ "is enabled.");
        QUser user = QUser.user;
        new JPAUpdateClause(getEntityManager(), user).where(user.id.eq(userId))
                .set(user.enabled, true)
                .execute();
    }

    @Override
    public void setPassword(Long userId, String password) {
        log.info("Change password for user id:"+userId);
        QUser user = QUser.user;
        new JPAUpdateClause(getEntityManager(), user).where(user.id.eq(userId))
                .set(user.password, password)
                .execute();
    }

    @Override
    public Page<User> findUsers(UserFilter filter, Sort sort, Pageable pageable) {
        QUser user = QUser.user;
        JPAQuery query = createQuery();
        query.from(user);

        QIdentity identity = QIdentity.identity;
        query.join(user.identity, identity);


        BooleanExpression filterExp = null;
        if(!StringUtils.isEmpty(filter.getEmail())){
            filterExp = user.email.containsIgnoreCase(filter.getEmail());
        }

        if (!StringUtils.isEmpty(filter.getFirstname())){
            BooleanExpression exp = identity.firstName.containsIgnoreCase(filter.getFirstname());
            if(filterExp==null){
                filterExp = exp;
            }else {
                filterExp = filterExp.and(exp);
            }
        }


        if (!StringUtils.isEmpty(filter.getLastname())){
            BooleanExpression exp = identity.lastName.containsIgnoreCase(filter.getLastname());
            if(filterExp==null){
                filterExp = exp;
            }else {
                filterExp = filterExp.and(exp);
            }
        }



        if (!StringUtils.isEmpty(filter.getEmail())){
            BooleanExpression exp = user.email.containsIgnoreCase(filter.getEmail());
            if(filterExp==null){
                filterExp = exp;
            }else {
                filterExp = filterExp.and(exp);
            }
        }


        if (filter.getIdentity()!=null){
            BooleanExpression exp = identity.instanceOf(filter.getIdentity());
            if(filterExp==null){
                filterExp = exp;
            }else {
                filterExp = filterExp.and(exp);
            }
        }


        if (filterExp != null){
            query.where(filterExp);
        }

        //query.orderBy(JPAExpressions.type(identity).asc());

        long count = query.count();

        applyPagination(pageable,query);
        applySorting(sort,query);

        List<User> list = query.list(user);

        Page<User> res = new PageImpl<User>(list,pageable,count);

        return res;
    }
}

