package fr.k2i.adbeback.dao;

import fr.k2i.adbeback.core.business.transaction.Transaction;
import fr.k2i.adbeback.core.business.user.User;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: dimitri
 * Date: 25/03/14
 * Time: 17:08
 * Goal:
 */
public interface ITransactionDao extends IGenericDao<Transaction, Long>  {
    @Transactional
    boolean musicIsWonByPlayer(User player, Long musicId);
}
