package fr.k2i.adbeback.dao.jpa;

import com.mysema.query.jpa.impl.JPAQuery;
import fr.k2i.adbeback.core.business.media.QMedia;
import fr.k2i.adbeback.core.business.push.HomePush;
import fr.k2i.adbeback.core.business.push.QHomePush;
import fr.k2i.adbeback.dao.IHomePushDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
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
@Repository("homePushDao")
public class HomePushDao extends GenericDaoJpa<HomePush, Long> implements IHomePushDao {



    @Transactional
    @Override
    public List<HomePush> findActualPushHome() throws Exception {
        LocalDate now = LocalDate.now();

        QHomePush homePush= QHomePush.homePush;
        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(homePush)
                .where(
                        homePush.startDate.lt(now).and(homePush.endDate.gt(now))
                );
        return query.list(homePush);
    }


}

