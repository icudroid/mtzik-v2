package fr.k2i.adbeback.dao;

import fr.k2i.adbeback.core.business.push.HomePush;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User Data Access Object (IGenericDao) interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface IHomePushDao extends IGenericDao<HomePush, Long> {


    @Transactional
    List<HomePush> findActualPushHome() throws Exception;
}

