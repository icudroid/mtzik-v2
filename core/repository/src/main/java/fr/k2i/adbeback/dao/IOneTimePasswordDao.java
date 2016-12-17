package fr.k2i.adbeback.dao;

import fr.k2i.adbeback.core.business.otp.OneTimePassword;
import fr.k2i.adbeback.core.business.otp.OtpAction;
import fr.k2i.adbeback.core.business.user.User;
import org.springframework.transaction.annotation.Transactional;

/**
 * User Data Access Object (IGenericDao) interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface IOneTimePasswordDao extends IGenericDao<OneTimePassword, Long> {

    @Transactional
    OneTimePassword findBy(User user, OtpAction action);

}


