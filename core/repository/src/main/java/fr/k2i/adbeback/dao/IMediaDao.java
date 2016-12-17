package fr.k2i.adbeback.dao;

import fr.k2i.adbeback.core.business.media.*;
import fr.k2i.adbeback.core.business.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * User Data Access Object (GenericDao) interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface IMediaDao extends IGenericDao<Media, Long> {


    @Transactional
    List<Music> findMusicBy(String str, Long idGenre) throws Exception;


    @Transactional
    List<Music> searchNewMusic(Long idGenre, String str, int max)
			throws Exception;

    @Transactional
    Page<String> findMusicAndAlbumTitleByTitle(String str, Pageable pageable) throws Exception;

    @Transactional
    Page<String> findPersonFullNameByName(String req, Pageable pageable);

    @Transactional
    Page<Artist> findArtistByFullName(String req, Pageable pageable);

    @Transactional
    Page<Productor> findProductorByFullName(String req, Pageable pageable);

    @Transactional
    Page<Music> findMusicByTile(String req, Pageable pageable);

    @Transactional
    List<Music> searchBestMusicDownload(Long idGenre, int max) throws Exception;

    @Transactional
    List<Media> getByIds(List<Long> mediaIds);

    @Transactional
    LocalDate getLastReleaseForProductor(Productor productor);

    @Transactional
    LocalDate getLastReleaseForArtist(Artist artist);

    @Transactional
    Page<Music> findMusicByTileAndGenre(String query, Long genre, Pageable pageable);

    @Transactional
    Page<Music> findMusicByTileAndGenreAndisNew(String query, Long genre, Pageable pageable);

    @Transactional
    Page<Music> findMusicByTileAndGenreAndTopDl(String req, Long genre, int limit, Pageable pageable);

    @Transactional
    Artist findArtistById(Long artistId);

    @Transactional
    Long countMediaForArtist(Long artistId);

    @Transactional
    List<Music> last5MusicForArtist(Long artistId);

    @Transactional
    Page<Music> findMusicsForArtist(Long artistId, String req, Pageable pageable);

    @Transactional
    Productor getProductor(Long majorId);

    @Transactional
    Long countArtistForLabel(Long majorId);

    @Transactional
    List<Music> last10MusicForLabel(Long majorId);

    @Transactional
    Page<Music> findMusicsForLabel(Long majorId, Long genre, String req, Pageable pageable);

    @Transactional
    List<? extends Media> getNewMusics(Integer max);

    @Transactional
    Page<TrackNumberMusic> findMusicsForAlbum(Long albumId, String req, Pageable pageable);

    @Transactional
    List<Music> top10MusicForArtist(Long artistId);

    @Transactional
    List<? extends Media> searchNewMusics(Long genreId, Integer max);

    @Transactional
    Page<String> findLabelNameByName(String req, Pageable pageable);

    @Transactional
    List<Music> findMusicByTitle(String req, Integer maxResult) throws Exception;

    @Transactional
    List<Album> findAlbumByTitle(String str, Integer maxResult) throws Exception;


    @Transactional
    Page<Media> getDownloadedMusic(User player, long genreId, String req, Pageable pageable);
}

