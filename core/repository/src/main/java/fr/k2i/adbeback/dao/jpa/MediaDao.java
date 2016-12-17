package fr.k2i.adbeback.dao.jpa;

import com.google.common.collect.Lists;
import com.mysema.query.Tuple;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;
import fr.k2i.adbeback.core.business.media.*;
import fr.k2i.adbeback.core.business.transaction.QTransaction;
import fr.k2i.adbeback.core.business.transaction.TransactionStatus;
import fr.k2i.adbeback.core.business.user.QUser;
import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.dao.IMediaDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.Query;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve User objects.
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a> Modified by
 *         <a href="mailto:dan@getrolling.com">Dan Kibler</a> Extended to
 *         implement Acegi UserDetailsService interface by David Carter
 *         david@carter.net Modified by <a href="mailto:bwnoll@gmail.com">Bryan
 *         Noll</a> to work with the new BaseDaoHibernate implementation that
 *         uses generics.
 */
@Repository("mediaDao")
public class MediaDao extends GenericDaoJpa<Media, Long> implements IMediaDao {


    @Transactional
    @Override
    public Page<String> findMusicAndAlbumTitleByTitle(String str,Pageable pageable) throws Exception {

        QMedia media = QMedia.media;
        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(media)
                .where(
                        media.title.containsIgnoreCase(str)
                );

        query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(media.title.asc());

        return new PageImpl<String>(query.list(media.title),pageable,query.count());
    }

    @Transactional
    @Override
    public List<Music> findMusicByTitle(String str,Integer maxResult) throws Exception {

        QMusic media = QMusic.music;
        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(media)
                .where(
                        media.title.containsIgnoreCase(str)
                );

        query
                .limit(maxResult)
                .orderBy(media.title.asc());

        return query.list(media);
    }


    @Transactional
    @Override
    public List<Album> findAlbumByTitle(String str,Integer maxResult) throws Exception {

        QAlbum media = QAlbum.album;
        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(media)
                .where(
                        media.title.containsIgnoreCase(str)
                );

        query
                .limit(maxResult)
                .orderBy(media.title.asc());

        return query.list(media);
    }


    @Transactional
    @Override
    public Page<String> findPersonFullNameByName(String req, Pageable pageable) {
        QPerson person = QPerson.person;
        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(person)
                .where(
                        person.lastName.containsIgnoreCase(req).or(person.firstName.containsIgnoreCase(req))
                );

        query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(person.lastName.asc())
                .orderBy(person.firstName.asc());

        List<Tuple> list = query.list(person.lastName, person.firstName);

        List<String> content = new ArrayList<String>();
        for (Tuple tuple : list) {
            String r = "";
            if(tuple.get(person.firstName).isEmpty()){
                r =tuple.get(person.lastName);
            }else{
                r = tuple.get(person.firstName)+" "+tuple.get(person.lastName);
            }
            content.add(r);
        }

        return new PageImpl<String>(content,pageable,query.count());
    }

    @Transactional
    @Override
    public Page<Artist> findArtistByFullName(String req, Pageable pageable) {
        QArtist artist = QArtist.artist;
        JPAQuery query = new JPAQuery(getEntityManager());
        BooleanExpression predicat = null;
        query.from(artist);

        if(!StringUtils.isEmpty(req)){
            String[] split = req.split(" ");

            for (String s : split) {
                BooleanExpression fullNamePredicat = artist.lastName.containsIgnoreCase(s).or(artist.firstName.containsIgnoreCase(s));
                if(predicat==null){
                    predicat = fullNamePredicat;
                }else{
                    predicat = predicat.or(fullNamePredicat);
                }
            }
            query.where(
                            predicat
                    );
        }


        query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(artist.lastName.asc())
                .orderBy(artist.firstName.asc());


        return new PageImpl<Artist>(query.list(artist),pageable,query.count());
    }

    @Transactional
    @Override
    public Page<Productor> findProductorByFullName(String req, Pageable pageable) {
        QProductor productor = QProductor.productor;
        JPAQuery query = new JPAQuery(getEntityManager());

        query.from(productor);

        if(!StringUtils.isEmpty(req)){
            query.where(
                            productor.lastName.containsIgnoreCase(req).or(productor.firstName.containsIgnoreCase(req))
                    );
        }

        query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(productor.lastName.asc())
                .orderBy(productor.firstName.asc());


        return new PageImpl<Productor>(query.list(productor),pageable,query.count());
    }


    @Transactional
    @Override
    public LocalDate getLastReleaseForProductor(Productor productor) {
        QMusic music = QMusic.music;
        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(music)
                .where(
                        music.productors.contains(productor)
                   );

        return query.uniqueResult(music.releaseDate.max());
    }


    @Transactional
    @Override
    public LocalDate getLastReleaseForArtist(Artist artist) {
        QMusic music = QMusic.music;
        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(music)
                .where(
                        music.artists.contains(artist)
                );

        return query.uniqueResult(music.releaseDate.max());
    }

    @Override
    public Page<Music> findMusicByTileAndGenre(String req, Long genre, Pageable pageable) {
        QCategory qGenre = QCategory.category;

        QMusic music = QMusic.music;
        JPAQuery query = new JPAQuery(getEntityManager());

        BooleanExpression predicat = null;

        query.from(music);

        if(genre!=null && genre>0){
            predicat = music.categories.any().id.eq(genre);
        }


        if(!StringUtils.isEmpty(req)){
            BooleanExpression musicTitlePredicat = music.title.containsIgnoreCase(req);
            if(predicat!=null){
                predicat.and(musicTitlePredicat);
            }else{
                predicat = musicTitlePredicat;
            }
        }

        if(predicat!=null){
            query.where(predicat);
        }

        query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(music.title.asc());


        return new PageImpl<Music>(query.list(music),pageable,query.count());
    }

    @Transactional
    @Override
    public Page<Music> findMusicByTileAndGenreAndisNew(String req, Long genre, Pageable pageable) {
        QCategory qGenre = QCategory.category;

        QMusic music = QMusic.music;
        JPAQuery query = new JPAQuery(getEntityManager());

        BooleanExpression predicat = music.releaseDate.gt( LocalDate.now().minusMonths(1));

        query.from(music);


        if(genre!=null && genre>0){
            predicat = predicat.and(music.categories.any().id.eq(genre));
        }

        if(!StringUtils.isEmpty(req)){
            predicat = predicat.and(music.title.containsIgnoreCase(req));
        }


        query   .where(predicat)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(music.title.asc());


        return new PageImpl<Music>(query.list(music),pageable,query.count());
    }

    @Transactional
    @Override
    public Page<Music> findMusicByTileAndGenreAndTopDl(String req, Long genre, int limit ,Pageable pageable) {

        QCategory qGenre = QCategory.category;
        QMusic music = QMusic.music;

        JPAQuery queryTopDl = new JPAQuery(getEntityManager());
        queryTopDl.from(music).groupBy(music.id).orderBy(music.id.count().desc()).limit(limit);


        JPAQuery query = new JPAQuery(getEntityManager());
        List<Long> musicCount = queryTopDl.list(music.id);
        if(musicCount.isEmpty()){
            return new PageImpl<Music>(Lists.newArrayList(),pageable,0);
        }

        BooleanExpression predicat = music.id.in(musicCount);

        if(!StringUtils.isEmpty(req)){
            predicat = predicat.and(music.title.containsIgnoreCase(req));
        }

        query.from(music);

        if(genre!=null && genre>0){
            predicat = predicat.and(music.categories.any().id.eq(genre));
        }


        query   .where(predicat)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(music.title.asc());


        return new PageImpl<Music>(query.list(music),pageable,query.count());
    }

    @Override
    public Artist findArtistById(Long artistId) {
        QArtist qArtist = QArtist.artist;
        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(qArtist).where(qArtist.id.eq(artistId));
        return query.uniqueResult(qArtist);
    }

    @Override
    public Long countMediaForArtist(Long artistId) {
        QMusic music =QMusic.music;
        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(music).where(music.artists.any().id.eq(artistId));

        return query.count();
    }

    @Transactional
    @Override
    public List<Music> last5MusicForArtist(Long artistId) {
        QMusic music =QMusic.music;
        JPAQuery query = new JPAQuery(getEntityManager());

        query
                .from(music)
                .where(
                        music.artists.any().id.eq(artistId)
                );

        query.orderBy(music.releaseDate.desc());
        query.limit(5);
        return query.list(music);
    }

    @Transactional
    @Override
    public Page<Music> findMusicsForArtist(Long artistId, String req, Pageable pageable) {
        QMusic music =QMusic.music;
        JPAQuery query = new JPAQuery(getEntityManager());

        BooleanExpression predicat = music.artists.any().id.eq(artistId);

        if(!StringUtils.isEmpty(req)){
            predicat = predicat.and(music.title.containsIgnoreCase(req));
        }

        query
                .from(music)
                .where(
                        predicat
                );


        query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(music.title.asc());


        return new PageImpl<Music>(query.list(music),pageable,query.count());
    }

    @Override
    public Productor getProductor(Long majorId) {
        QProductor qProductor = QProductor.productor;
        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(qProductor).where(qProductor.id.eq(majorId));

        return query.uniqueResult(qProductor);
    }

    @Override
    public Long countArtistForLabel(Long majorId) {
        QArtist qArtist = QArtist.artist;

        JPAQuery query = new JPAQuery(getEntityManager());
        QMusic music = QMusic.music;

        QArtist artist = QArtist.artist;
        QProductor productor = new QProductor("productor");

        query.from(artist).join(artist.productors,productor).where(productor.id.eq(majorId));
        query.distinct();

        return query.count();
    }

    @Override
    public List<Music> last10MusicForLabel(Long majorId) {
        QMusic music = QMusic.music;
        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(music)
                .where(
                        music.productors.any().id.eq(majorId)
                );


        query.orderBy(music.releaseDate.desc());
        query.limit(10);
        return query.list(music);
    }

    @Override
    public Page<Music> findMusicsForLabel(Long majorId, Long genre, String req, Pageable pageable) {
        QMusic music = QMusic.music;
        JPAQuery query = new JPAQuery(getEntityManager());

        if(req==null){
            req ="";
        }

        BooleanExpression predicat = music.title.containsIgnoreCase(req)
                .and(music.productors.any().id.eq(majorId))
                .and(music.title.containsIgnoreCase(req));

        query.from(music);

        if(genre!=null && genre>0){
            predicat = predicat.and(music.categories.any().id.eq(genre));
        }


        query.where(
                        predicat
                );


        query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(music.title.asc());

        return new PageImpl<Music>(query.list(music),pageable,query.count());
    }

    @Override
    public List<? extends Media> getNewMusics(Integer max) {


        QMusic music = QMusic.music;
        JPAQuery query = new JPAQuery(getEntityManager());


        query.from(music).where(music.releaseDate.gt( LocalDate.now().minusMonths(1)));

        query.orderBy(music.releaseDate.desc());

        query.limit(max);

        return query.list(music);
    }

    @Override
    public Page<TrackNumberMusic> findMusicsForAlbum(Long albumId, String req, Pageable pageable) {

        QAlbum qAlbum = QAlbum.album;

        JPAQuery query = new JPAQuery(getEntityManager());

        QTrackNumberMusic track = new QTrackNumberMusic("track");
        query.from(qAlbum).join(qAlbum.tracks,track);
        if(StringUtils.isEmpty(req)){
            query.where(
                    qAlbum.id.eq(albumId)
            );
        }else{
            query.where(
                    qAlbum.id.eq(albumId).and(track.music.title.containsIgnoreCase(req))
            );
        }

        long count = query.count();

        query
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qAlbum.tracks.any().trackNumber.asc());



        return new PageImpl<TrackNumberMusic>(query.list(track),pageable, count);
    }

    @Transactional
    @Override
    public List<Music> top10MusicForArtist(Long artistId) {
        Query q = getEntityManager().createQuery(
                "select count(media.id), media from Transaction tr inner join tr.medias as media with media.class = Music inner join media.artists as artist with artist.id=:artistId where tr.status= :status group by media.id order by count(media.id) desc");
        q.setParameter("artistId",artistId);
        q.setParameter("status", TransactionStatus.DONE);
        q.setMaxResults(10);

        List<Music> res = new ArrayList<Music>();
        for (Object[] objects : (List<Object[]>)q.getResultList()) {
            res.add((Music) objects[1]);
        }
        return res;
    }

    @Override
    public List<? extends Media> searchNewMusics(Long genreId, Integer max) {

        QMusic music = QMusic.music;
        JPAQuery query = new JPAQuery(getEntityManager());

        BooleanExpression predicat = music.releaseDate.gt( LocalDate.now().minusMonths(1));

        QCategory category = new QCategory("category");

        if(genreId!=null && genreId>0){
            predicat = predicat.and(category.id.eq(genreId));
        }

        query.from(music).join(music.categories,category).where(predicat);

        query.orderBy(music.releaseDate.desc());

        query.limit(max);

        return query.list(music);
    }

    @Override
    public Page<String> findLabelNameByName(String req, Pageable pageable) {
        QProductor productor = QProductor.productor;

        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(productor)
                .where(
                        productor.lastName.containsIgnoreCase(req)
                );

        query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(productor.lastName.asc());

        return new PageImpl<String>(query.list(productor.lastName),pageable,query.count());
    }


    @Transactional
    @Override
    public Page<Music> findMusicByTile(String req, Pageable pageable) {
        QMusic music = QMusic.music;
        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(music);

        if(!StringUtils.isEmpty(req)){
            query.where(
                            music.title.containsIgnoreCase(req)
                    );

        }

        query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(music.title.asc());


        return new PageImpl<Music>(query.list(music),pageable,query.count());
    }


    @Transactional
    @Override
	public List<Music> findMusicBy(String str, Long idGenre) throws Exception {
        JPAQuery query = new JPAQuery(getEntityManager());
        QMusic qMusic = QMusic.music;
        QArtist qArtist = QArtist.artist;
        QCategory qCategory = QCategory.category;

        query.from(qMusic).join(qMusic.artists,qArtist);
        BooleanExpression exp1 = null;

		if(str!=null){

			String[] split = str.split(" ");
			for (String search : split) {
                BooleanExpression or = qArtist.firstName.containsIgnoreCase(search).or(qArtist.lastName.containsIgnoreCase(search)).or(qMusic.title.containsIgnoreCase(search));
                if(exp1 == null){
                    exp1 = or;
                }else {
                    exp1.or(or);
                }


			}
            query.orderBy(qMusic.title.desc(),qArtist.firstName.desc(),qArtist.lastName.desc());

		}

		if (idGenre != null && idGenre != -1) {
            query.join(qMusic.categories,qCategory);
            BooleanExpression eq = qCategory.id.eq(idGenre);
            if(exp1==null){
                exp1 = eq;
            }else{
                exp1.and(eq);
            }

            query.where(exp1);
		}
		
		return query.list(qMusic);
	}

    @Transactional
    @Override
	public List<Music> searchBestMusicDownload(Long genre,int max) throws Exception {

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select m, count(m.id) from Transaction tr join tr.medias as m ");
        if(genre !=null && genre !=-1){
            queryBuilder.append("join m.categories as genre where genre.id =:genre and tr.status=:status and m.class=Music ");
        }else{
            queryBuilder.append("where tr.status=:status and m.class=Music ");
        }
        queryBuilder.append("group by m.id order by count(m.id) desc ");

        Query q = getEntityManager().createQuery(queryBuilder.toString());
        q.setParameter("status", TransactionStatus.DONE);
        if(genre !=null && genre !=-1){
            q.setParameter("genre", genre);
        }
        q.setMaxResults(max);

        List resultList = q.getResultList();

        List<Music> res = new ArrayList<Music>();

        for (Object o : resultList) {
            Object[] tmp = (Object[])o;
            res.add((Music) tmp[0]);
        }

        return res;
    }

    @Override
    public List<Media> getByIds(List<Long> mediaIds) {
        QMedia media = QMedia.media;
        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(media)
                .where(
                        media.id.in(mediaIds)
                );

        return query.list(media);

    }


    @Transactional
	@Override
	public List<Music> searchNewMusic(Long idGenre, String str, int max)
			throws Exception {



        JPAQuery query = new JPAQuery(getEntityManager());
        QMusic qMusic = QMusic.music;
        QArtist qArtist = QArtist.artist;
        QCategory qCategory = QCategory.category;

        query.from(qMusic).join(qMusic.artists,qArtist);

        BooleanExpression exp1 = qMusic.releaseDate.goe(LocalDate.now().minusDays(1));

        if(str!=null){

            String[] split = str.split(" ");
            for (String search : split) {
                BooleanExpression or = qArtist.firstName.containsIgnoreCase(search).or(qArtist.lastName.containsIgnoreCase(search)).or(qMusic.title.containsIgnoreCase(search));
                if(exp1 == null){
                    exp1 = or;
                }else {
                    exp1.or(or);
                }


            }
            query.orderBy(qMusic.title.desc(),qArtist.firstName.desc(),qArtist.lastName.desc(),qMusic.releaseDate.desc());

        }

        if (idGenre != null && idGenre != -1) {
            query.join(qMusic.categories,qCategory);
            BooleanExpression eq = qCategory.id.eq(idGenre);
            if(exp1==null){
                exp1 = eq;
            }else{
                exp1.and(eq);
            }

            query.where(exp1);
        }

        query.limit(max);

        return query.list(qMusic);

	}



    @Override
    public Page<Media> getDownloadedMusic(User player, long genreId, String req, Pageable pageable) {

        QTransaction qTransaction = QTransaction.transaction;
        QUser qUser = QUser.user;
        QMedia media = QMedia.media;
        JPAQuery query = new JPAQuery(getEntityManager());
        BooleanExpression predicat = qUser.eq(player).and(qTransaction.status.eq(TransactionStatus.DONE));

        if(genreId>0){
            predicat = predicat.and(qTransaction.medias.any().categories.any().id.eq(genreId));
        }

        if(!StringUtils.isEmpty(req)){
            predicat = predicat.and(qTransaction.medias.any().title.containsIgnoreCase(req));
        }

        query.from(qTransaction).join(qTransaction.medias,media).where(
                predicat.and(qTransaction.user.eq(player)).and(qTransaction.medias.any().instanceOf(Music.class))
        );


        query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(media.title.asc());

        return new PageImpl<Media>(query.list(media),pageable,query.count());
    }
}
