package fr.k2i.adbeback.dao;

import fr.k2i.adbeback.core.business.media.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * User Data Access Object (GenericDao) interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface IArtistDao extends IGenericDao<Artist, Long> {

    boolean isMainProductor(Artist artist,Productor productor);

    List<Artist> findByLabel(Productor productor);

    List<Artist> findByLabel(Productor productor,String artistname);


    Page<Artist> findArtistByLabel(String artistName, Sort sort, Productor productor, Pageable pageable);

    Page<Artist> findArtist(String value, Sort sort, Pageable pageable);
}

