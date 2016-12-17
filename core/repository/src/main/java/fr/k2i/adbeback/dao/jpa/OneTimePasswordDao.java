package fr.k2i.adbeback.dao.jpa;

import com.mysema.query.jpa.impl.JPAQuery;
import fr.k2i.adbeback.core.business.otp.OneTimePassword;
import fr.k2i.adbeback.core.business.otp.OtpAction;
import fr.k2i.adbeback.core.business.otp.QOneTimePassword;
import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.dao.IOneTimePasswordDao;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 14/01/14
 * Time: 21:07
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class OneTimePasswordDao extends GenericDaoJpa<OneTimePassword, Long> implements IOneTimePasswordDao {


    @Override
    public OneTimePassword findBy(User user, OtpAction action) {
        QOneTimePassword oneTimePassword = QOneTimePassword.oneTimePassword;
        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(oneTimePassword).where(oneTimePassword.user.eq(user).and(oneTimePassword.action.eq(action)));
        return query.uniqueResult(oneTimePassword);
    }


}
