package fr.k2i.adbeback.dao.jpa;

import com.mysema.query.jpa.impl.JPAQuery;
import fr.k2i.adbeback.core.business.media.*;
import fr.k2i.adbeback.dao.IArtistDao;
import fr.k2i.adbeback.dao.IMediaDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class ArtistDao extends GenericDaoJpa<Artist, Long> implements IArtistDao {



	@Override
	public boolean isMainProductor(Artist artist, Productor productor) {
		JPAQuery query = new JPAQuery(getEntityManager());

		QArtist qArtist = QArtist.artist;

		query.from(qArtist).where(qArtist.mainProductor.eq(productor).and(qArtist.eq(artist)));


		return query.exists();
	}

	@Override
	public List<Artist> findByLabel(Productor productor) {
		JPAQuery query = new JPAQuery(getEntityManager());
		QArtist qArtist = QArtist.artist;
		query.from(qArtist).where(qArtist.mainProductor.eq(productor));
		return query.list(qArtist);
	}

	@Override
	public List<Artist> findByLabel(Productor productor, String artistname) {
		JPAQuery query = new JPAQuery(getEntityManager());
		QArtist qArtist = QArtist.artist;
		query.from(qArtist).where(qArtist.mainProductor.eq(productor).and(qArtist.artistName.containsIgnoreCase(artistname)));
		return query.list(qArtist);
	}

	@Override
	public Page<Artist> findArtistByLabel(String value, Sort sort, Productor productor, Pageable pageable) {

		QArtist artist = QArtist.artist;
		JPAQuery query = createQuery(artist);
		query.where(
				artist.artistName.containsIgnoreCase(value)
				.and(artist.productors.contains(productor))
		);

		long count = query.count();

		applyPagination(pageable,query);
		applySorting(sort,query);
		List<Artist> list = query.list(artist);

		Page<Artist> res = new PageImpl<Artist>(list,pageable,count);

		return res;
	}

	@Override
	public Page<Artist> findArtist(String value, Sort sort, Pageable pageable) {
		QArtist artist = QArtist.artist;
		JPAQuery query = createQuery(artist);
		query.where(
				artist.artistName.containsIgnoreCase(value)
		);

		long count = query.count();

		applyPagination(pageable,query);
		applySorting(sort,query);
		List<Artist> list = query.list(artist);

		Page<Artist> res = new PageImpl<Artist>(list,pageable,count);

		return res;
	}
}
