package fr.k2i.adbeback.dao;

import fr.k2i.adbeback.core.business.media.Artist;
import fr.k2i.adbeback.core.business.media.Music;
import fr.k2i.adbeback.core.business.media.Productor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * User: dimitri
 * Date: 30/12/14
 * Time: 15:46
 * Goal:
 */
public interface IMusicBoDao extends IGenericDao<Music, Long>{

    List<Music> findMusicForArtist(Artist userArtist);
    List<Music> findMusicForLabel(Productor userLabel);

    List<Music> findMusicForArtist(Artist userArtist,String title);
    List<Music> findMusicForLabel(Productor userLabel,String title);


    Long countDownloaded(Music music);

    void delete(Music music);

    List<Artist> findArtistByIds(List<Long> artists);

    Page<Music> findMusicForArtist(String value, Sort sort, Pageable pageable);
}
