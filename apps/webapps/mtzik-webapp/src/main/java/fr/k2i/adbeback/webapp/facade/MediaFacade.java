package fr.k2i.adbeback.webapp.facade;

import fr.k2i.adbeback.core.business.media.*;
import fr.k2i.adbeback.core.business.push.HomePush;
import fr.k2i.adbeback.core.business.transaction.Transaction;
import fr.k2i.adbeback.core.business.transaction.TransactionStatus;
import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.dao.IHomePushDao;
import fr.k2i.adbeback.dao.IMediaDao;
import fr.k2i.adbeback.dao.ITransactionDao;
import fr.k2i.adbeback.date.DateUtils;
import fr.k2i.adbeback.webapp.bean.PersonBean;
import fr.k2i.adbeback.webapp.bean.media.AlbumBean;
import fr.k2i.adbeback.webapp.bean.media.MediaBean;
import fr.k2i.adbeback.webapp.bean.media.MininalMusicBean;
import fr.k2i.adbeback.webapp.bean.search.*;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 05/01/14
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */
@Component
public class MediaFacade {

    @Autowired
    private IMediaDao mediaDao;

    @Autowired
    private IHomePushDao homePushDao;

    @Autowired
    private ITransactionDao transactionDao;


    @Value("${addonf.tmp.location:/tmp/}")
    private String tmpPath;


    @Value("${addonf.music.location:/musics/}")
    private String musicPath;

    @Value("${addonf.video.location:/movies/}")
    private String videoPath;


    @Transactional
    public List<MediaBean> getBestMusicDownload(Long idGenre,int max) throws Exception {
        return construstBeanList(mediaDao.searchBestMusicDownload(idGenre,max));
    }


    @Transactional
    public List<MediaBean> getHomePush() throws Exception {
        List<HomePush> pushes = homePushDao.findActualPushHome();

        if(pushes!=null && !pushes.isEmpty()){
            List<MediaBean> medias = new ArrayList<MediaBean>();
            for (HomePush push : pushes) {
                medias.add(contructMediaBean(push.getMedias()));
            }
            return medias;
        }else {
            return getBestMusicDownload(null,6);
        }
    }


    private List<MediaBean> construstBeanList(List<? extends Media> medias)throws Exception {
        List<MediaBean> res = new ArrayList<MediaBean>();
        for (Media media : medias) {
            MediaBean bean = contructMediaBean(media);
            res.add(bean);
        }

        return res;
    }

    private MediaBean contructMediaBean(Media media) {
        MediaBean bean = new MediaBean();
        bean.setDescription(media.getDescription());
        bean.setDuration(media.getDuration());
        bean.setCategories(media.getCategories());
        bean.setId(media.getId());
        if(StringUtils.isEmpty(media.getJacket())){
            bean.setJacket("no.png");
        }else {
            bean.setJacket(media.getJacket());
        }
        bean.setReleaseDate(DateUtils.asDate(media.getReleaseDate()));


        if(StringUtils.isEmpty(media.getThumbJacket())){
            bean.setThumbJacket("no.png");
        }else {
            bean.setThumbJacket(media.getThumbJacket());
        }



        bean.setTitle(media.getTitle());
        bean.setNbAdsNeeded(media.getNbAdsNeeded());
        if(media instanceof Music){
            List<Album> albums = ((Music) media).getAlbums();
            List<AlbumBean> albumBeans = new ArrayList<AlbumBean>();
            for (Album album : albums) {
                albumBeans.add(new AlbumBean(album.getId(),album.getTitle()));
            }
            bean.setAlbums(albumBeans);
        }

        if(media instanceof Album){
            List<PersonBean> artists = new ArrayList<PersonBean>();
            for (Artist a : ((Album) media).getArtists()) {
                artists.add(new PersonBean(a.getId(),a.getFirstName(), a.getLastName(),a.getPhoto()));
            }
            bean.setArtists(artists);
        }

        List<PersonBean> productors = new ArrayList<PersonBean>();
        for (Productor p : media.getProductors()) {
            productors.add(new PersonBean(p.getId(),p.getFirstName(), p.getLastName(),p.getPhoto()));
        }
        bean.setProductors(productors);

        if (media instanceof Music) {
            Music music = (Music) media;
            List<PersonBean> artists = new ArrayList<PersonBean>();
            for (Artist a : music.getArtists()) {
                artists.add(new PersonBean(a.getId(), a.getFirstName(), a.getLastName(),a.getPhoto()));
            }

            bean.setArtists(artists);
        }
        return bean;
    }

    public MediaBean getMediaById(Long musicId) {
        Media media = mediaDao.get(musicId);
        return contructMediaBean(media);
    }





    private Page<ProductorBean> createPageProductors(Page<Productor> productors) {

        List<ProductorBean> content = new ArrayList<ProductorBean>();

        for (Productor a : productors) {
            content.add(new ProductorBean(a.getId(),a.getFirstName(),a.getLastName(),a.getPhoto(),mediaDao.getLastReleaseForProductor(a)));
        }

        return new PageImpl<ProductorBean>(content,new PageRequest(productors.getNumber(),productors.getSize()),productors.getTotalElements());
    }


    private Page<ArtistBean> createPageArtists(Page<Artist> artists) {

        List<ArtistBean> content = new ArrayList<ArtistBean>();

        for (Artist a : artists) {
            content.add(new ArtistBean(a.getId(),a.getFirstName(),a.getLastName(),a.getPhoto(),mediaDao.getLastReleaseForArtist(a)));
        }

        return new PageImpl<ArtistBean>(content,new PageRequest(artists.getNumber(),artists.getSize()),artists.getTotalElements());
    }


    private Page<MusicBean> createPageMusics(Page<Music> musics) {

        List<MusicBean> content = new ArrayList<MusicBean>();

        for (Music a : musics) {
            content.add(new MusicBean(a));
        }

        return new PageImpl<MusicBean>(content,new PageRequest(musics.getNumber(),musics.getSize()),musics.getTotalElements());
    }

    @Transactional
    public void search(String query, Map<String, Object> model) {
        Pageable pageableArtist = new PageRequest(0,12);
        Pageable pageableLabel = new PageRequest(0,9);
        Pageable pageableMusic = new PageRequest(0,25);

        Page<Artist> artist = mediaDao.findArtistByFullName(query, pageableArtist);
        Page<Productor> productors = mediaDao.findProductorByFullName(query, pageableLabel);
        Page<Music> musics = mediaDao.findMusicByTile(query, pageableMusic);


        model.put("artists",createPageArtists(artist));
        model.put("productors",createPageProductors(productors));
        model.put("musics",createPageMusics(musics));

    }

    @Transactional
    public Page<MusicBean> findMusics(Long genre, String query, Pageable pageable) {
        return createPageMusics(mediaDao.findMusicByTileAndGenre(query, genre, pageable));
    }

    @Transactional
    public Page<ArtistBean> findArtists(String req, Pageable pageable) {
        return createPageArtists( mediaDao.findArtistByFullName(req, pageable));
    }

    @Transactional
    public Page<ProductorBean> findProductors(String req, Pageable pageable) {
        return createPageProductors(mediaDao.findProductorByFullName(req, pageable));
    }

    @Transactional
    public Page<MusicBean> findNewMusics(Long genre, String query, Pageable pageable) {
        return createPageMusics(mediaDao.findMusicByTileAndGenreAndisNew(query, genre, pageable));
    }

    public  Page<MusicBean> findTopMusics(Long genre, String query, int top ,Pageable pageable) {
        return createPageMusics(mediaDao.findMusicByTileAndGenreAndTopDl(query, genre, top, pageable));
    }

    @Transactional
    public ArtistBean getArtistById(Long artistId) {
        Artist a = mediaDao.findArtistById(artistId);
        ArtistBean artistBean = new ArtistBean(a);
        artistBean.setLastRelease(DateUtils.asDate(mediaDao.getLastReleaseForArtist(a)));
        return artistBean;
    }

    public Long countMusicForArtist(Long artistId) {
        return mediaDao.countMediaForArtist(artistId);
    }

    @Transactional
    public List<MusicBean> last5MusicForArtist(Long artistId) {
        List<MusicBean> content= new ArrayList<MusicBean>();
        List<Music> musics  = mediaDao.last5MusicForArtist(artistId);

        for (Music a : musics) {
            content.add(new MusicBean(a));
        }

        return content;
    }

    @Transactional
    public Page<MusicBean> findMusicsForArtist(Long artistId, String req, Pageable pageable) {
        return createPageMusics(mediaDao.findMusicsForArtist(artistId, req, pageable));
    }

    @Transactional
    public ProductorBean getProductorById(Long majorId) {
        Productor a = mediaDao.getProductor(majorId);
        return new ProductorBean(a.getId(),a.getFirstName(),a.getLastName(),a.getPhoto(),mediaDao.getLastReleaseForProductor(a));  //To change body of created methods use File | Settings | File Templates.
    }

    @Transactional
    public Long countArtistForLabel(Long majorId) {
        return mediaDao.countArtistForLabel(majorId);
    }

    public List<MusicBean> last10MusicForLabel(Long majorId) {
        List<MusicBean> content= new ArrayList<MusicBean>();
        List<Music> musics  = mediaDao.last10MusicForLabel(majorId);

        for (Music a : musics) {
            content.add(new MusicBean(a));
        }

        return content;
    }

    public  Page<MusicBean> findMusicsForLabel(Long majorId,Long genre, String req, Pageable pageable) {
        return createPageMusics(mediaDao.findMusicsForLabel(majorId, genre, req, pageable));
    }


    @Transactional
    public List<MediaBean> getNewMusics(Integer max) throws Exception {
        return construstBeanList(mediaDao.getNewMusics(max));
    }

    @Transactional
    public Page<MediaBean> findMusicsForAlbum(Long albumId, String req,Pageable pageable) {
        Page<MusicBean> res = null;

        Page<TrackNumberMusic> musicsForAlbum = mediaDao.findMusicsForAlbum(albumId, req, pageable);
        List<MediaBean> content = new ArrayList<MediaBean>();

        for ( TrackNumberMusic track : musicsForAlbum.getContent()) {
            MediaBean mediaBean = contructMediaBean(track.getMusic());
            mediaBean.setTitle(track.getTrackNumber()+" - "+mediaBean.getTitle());
            content.add(mediaBean);
        }

        return new PageImpl<MediaBean>(content,pageable,musicsForAlbum.getTotalElements());
    }


    @Transactional
    public List<MusicBean> top10MusicForArtist(Long artistId) {
        List<MusicBean> content= new ArrayList<MusicBean>();
        List<Music> musics  = mediaDao.top10MusicForArtist(artistId);

        for (Music a : musics) {
            content.add(new MusicBean(a));
        }

        return content;
    }


    public List<MininalMusicBean> searchNewMusics(Long genreId, Integer max) throws Exception {
        return construstMininalMusicBeanList(mediaDao.searchNewMusics(genreId, max));
    }

    private List<MininalMusicBean> construstMininalMusicBeanList(List<? extends Media> medias) {
        List<MininalMusicBean> res = new ArrayList<MininalMusicBean>();
        for (Media media : medias) {
            MininalMusicBean bean = contructMininalMusicBean(media);
            res.add(bean);
        }

        return res;
    }

    private MininalMusicBean contructMininalMusicBean(Media media) {
        MininalMusicBean bean = new MininalMusicBean();
        bean.setId(media.getId());

        if(StringUtils.isEmpty(media.getJacket())){
            bean.setJacket("no.png");
        }else {
            bean.setJacket(media.getJacket());
        }

        bean.setTitle(media.getTitle());
        bean.setNbAdsNeeded(media.getNbAdsNeeded());
        return bean;
    }


    public List<SmallMediaBean> findMusicByTitle(String req, int max) throws Exception {
        List<Music> medias = mediaDao.findMusicByTitle(req, max);
        List<SmallMediaBean> res = new ArrayList<SmallMediaBean>(max);

        for (Music media : medias) {
            res.add(new SmallMediaBean(media));
        }

        return res;
    }

    public List<SmallPersonBean> findArtistByName(String req, int max) {
        Page<Artist> artists = mediaDao.findArtistByFullName(req, new PageRequest(0, max));
        List<SmallPersonBean> res = new ArrayList<SmallPersonBean>(max);

        for (Artist artist : artists) {
            res.add(new SmallPersonBean(artist));
        }

        return res;
    }

    public List<SmallPersonBean> findLabelByName(String req, int max) {
        Page<Productor> productors = mediaDao.findProductorByFullName(req, new PageRequest(0, max));
        List<SmallPersonBean> res = new ArrayList<SmallPersonBean>(max);

        for (Productor productor : productors) {
            res.add(new SmallPersonBean(productor));
        }

        return res;
    }


    public String getFilename(Transaction transaction) {
        List<Media> medias = transaction.getMedias();
        if(medias.size()>1){
            return "musics.zip";
        }else if(medias.size()==1){
            Media media = medias.get(0);
            return media.getTitle()+"."+new Filename(media.getFile()).extension();
        }else{
            return "";
        }
    }

    @Transactional
    public byte[] getMedias(Long idTransaction, HttpServletResponse response) throws IOException {
        byte[] res = null;
        Transaction transaction = transactionDao.get(idTransaction);
        if(TransactionStatus.OK.equals(transaction.getStatus())){

                List<Media> medias = transaction.getMedias();

                if(medias.size()>1){
                    // mettre dans un zip
                    String zipFileName = tmpPath+idTransaction+".zip";
                    ZipFile zipFile;
                    try {
                        zipFile = new ZipFile(zipFileName);
                        ArrayList<File> filesToAdd = new ArrayList<File> ();

                        for (Media media : medias) {
                            if (media instanceof Music) {
                                filesToAdd.add( new File(musicPath+media.getFile()));
                            }else{
                                filesToAdd.add( new File(videoPath+media.getFile()));
                            }
                        }

                        ZipParameters parameters = new ZipParameters();
/*
                    parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // set compression method to store compression
                    parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
                    parameters.setEncryptFiles(true);
                    parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
                    parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
                    parameters.setPassword(password);
*/
                        zipFile.createZipFile(filesToAdd, parameters);
                    } catch (ZipException e) {
                        e.printStackTrace();
                    }

                    java.io.File file = new java.io.File(zipFileName);

                    response.setContentType("application/force-download");
                    response.setHeader("Content-Transfer-Encoding", "binary");
                    response.setHeader("Content-Disposition","inline; filename=\"musics.zip\"");
                    response.setContentLength((int) file.length());
                    res = FileCopyUtils.copyToByteArray(file);
                    file.delete();
                }else if(medias.size()==1){

                    Media media = medias.get(0);

                    java.io.File file = null;

                    if (media instanceof Music) {
                        file =  new File(musicPath+media.getFile());
                    }else{
                        file =  new File(videoPath+media.getFile());
                    }


                    response.setContentType("application/force-download");
                    response.setHeader("Content-Transfer-Encoding", "binary");
                    response.setHeader("Content-Disposition","inline; filename=\""+media.getTitle()+"."+new Filename(media.getFile()).extension()+"\"");
                    response.setContentLength((int) file.length());
                    res = FileCopyUtils.copyToByteArray(file);
                }
                transaction.setStatus(TransactionStatus.DONE);
            }

            return res;

        }



    @Transactional
    public byte[] getMedia(Long musicId, User player, HttpServletResponse response) throws IOException {
        byte[] res = null;
        if (transactionDao.musicIsWonByPlayer(player,musicId)) {
            Media media = mediaDao.get(musicId);

            java.io.File file = null;

            if (media instanceof Music) {
                file =  new File(musicPath+media.getFile());
            }else{
                file =  new File(videoPath+media.getFile());
            }


            response.setContentType("audio/mpeg");
            response.setHeader("Content-Disposition","inline; filename=\""+media.getTitle()+"."+new Filename(media.getFile()).extension()+"\"");
            response.setContentLength((int) file.length());
            res = FileCopyUtils.copyToByteArray(file);
        }
        return res;
    }
}



class Filename {
    private String fullPath;
    private char pathSeparator, extensionSeparator;

    public Filename(String str) {
        fullPath = str;
        pathSeparator = File.pathSeparatorChar;
        extensionSeparator = '.';
    }

    public String extension() {
        int dot = fullPath.lastIndexOf(extensionSeparator);
        return fullPath.substring(dot + 1);
    }

    public String filename() { // gets filename without extension
        int dot = fullPath.lastIndexOf(extensionSeparator);
        int sep = fullPath.lastIndexOf(pathSeparator);
        return fullPath.substring(sep + 1, dot);
    }

    public String path() {
        int sep = fullPath.lastIndexOf(pathSeparator);
        return fullPath.substring(0, sep);
    }
}