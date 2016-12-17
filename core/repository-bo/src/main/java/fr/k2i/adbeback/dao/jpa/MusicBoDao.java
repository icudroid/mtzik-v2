package fr.k2i.adbeback.dao.jpa;


import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;
import fr.k2i.adbeback.core.business.media.*;
import fr.k2i.adbeback.core.business.transaction.QTransaction;
import fr.k2i.adbeback.core.business.transaction.TransactionStatus;
import fr.k2i.adbeback.dao.IMusicBoDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User: dimitri
 * Date: 29/01/14
 * Time: 14:03
 * Goal:
 */
@Repository
public class MusicBoDao extends GenericDaoJpa<Music, Long> implements IMusicBoDao {

    @Override
    public List<Music> findMusicForArtist(Artist userArtist) {
        JPAQuery query = new JPAQuery(getEntityManager());
        QMusic qMusic = QMusic.music;
        query.from(qMusic).where(qMusic.artists.contains(userArtist));
        return query.list(qMusic);
    }

    @Override
    public List<Music> findMusicForLabel(Productor userLabel) {
        JPAQuery query = new JPAQuery(getEntityManager());
        QMusic qMusic = QMusic.music;
        query.from(qMusic).where(qMusic.productors.contains(userLabel));
        return query.list(qMusic);
    }

    @Override
    public List<Music> findMusicForArtist(Artist userArtist, String title) {
        JPAQuery query = new JPAQuery(getEntityManager());
        QMusic qMusic = QMusic.music;
        BooleanExpression exp = qMusic.artists.contains(userArtist);
        if(title!=null){
            exp =exp.and(qMusic.title.containsIgnoreCase(title));
        }
        query.from(qMusic).where(exp);
        return query.list(qMusic);
    }

    @Override
    public List<Music> findMusicForLabel(Productor userLabel, String title) {
        JPAQuery query = new JPAQuery(getEntityManager());
        QMusic qMusic = QMusic.music;
        BooleanExpression exp = qMusic.productors.contains(userLabel);
        if(title!=null){
            exp  = exp.and(qMusic.title.containsIgnoreCase(title));
        }
        query.from(qMusic).where(exp);
        return query.list(qMusic);
    }

    @Override
    public Long countDownloaded(Music music) {
        JPAQuery query = new JPAQuery(getEntityManager());
        QTransaction qTransaction = QTransaction.transaction;
        query.from(qTransaction).where(qTransaction.medias.contains(music).and(qTransaction.status.eq(TransactionStatus.DONE)));
        return query.count();
    }

    @Override
    public void delete(Music music) {
        music.setDeletedDate(LocalDateTime.now());
    }

    @Override
    public List<Artist> findArtistByIds(List<Long> artists) {
        JPAQuery query = new JPAQuery(getEntityManager());
        QArtist qArtist = QArtist.artist;
        query.from(qArtist).where(qArtist.id.in(artists));
        return query.list(qArtist);
    }

    @Override
    public Page<Music> findMusicForArtist(String value, Sort sort, Pageable pageable) {
        QMusic qMusic = QMusic.music;
        JPAQuery query = createQuery(qMusic);
        query.where(qMusic.title.containsIgnoreCase(value));

        long count = query.count();

        applyPagination(pageable,query);
        applySorting(sort,query);
        List<Music> list = query.list(qMusic);

        Page<Music> res = new PageImpl<Music>(list,pageable,count);

        return res;
    }
}
