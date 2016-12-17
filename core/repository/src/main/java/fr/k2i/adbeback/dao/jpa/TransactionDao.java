package fr.k2i.adbeback.dao.jpa;

import com.mysema.query.jpa.impl.JPAQuery;
import fr.k2i.adbeback.core.business.media.QMedia;
import fr.k2i.adbeback.core.business.transaction.QTransaction;
import fr.k2i.adbeback.core.business.transaction.Transaction;
import fr.k2i.adbeback.core.business.transaction.TransactionStatus;
import fr.k2i.adbeback.core.business.user.QUser;
import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.dao.ICityDao;
import fr.k2i.adbeback.dao.ITransactionDao;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class TransactionDao extends GenericDaoJpa<Transaction, Long> implements ITransactionDao {



    @Override
    public boolean musicIsWonByPlayer(User player, Long musicId) {
        QUser user = QUser.user;
        QTransaction qTransaction = QTransaction.transaction;
        QMedia media = QMedia.media;

        JPAQuery query = new JPAQuery(getEntityManager());

        query.from(qTransaction).join(qTransaction.medias,media).where(
                qTransaction.user.id.eq(player.getId())
                        .and(qTransaction.status.eq(TransactionStatus.DONE))
                        .and(media.id.eq(musicId))
        );

        return query.exists();
    }
}

