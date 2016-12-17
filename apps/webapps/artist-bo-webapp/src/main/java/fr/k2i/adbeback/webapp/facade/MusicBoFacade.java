package fr.k2i.adbeback.webapp.facade;

import fr.k2i.adbeback.audio.AudioTools;
import fr.k2i.adbeback.core.business.media.Artist;
import fr.k2i.adbeback.core.business.media.Music;
import fr.k2i.adbeback.core.business.media.Productor;
import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.dao.ICategoryDao;
import fr.k2i.adbeback.dao.IMediaDao;
import fr.k2i.adbeback.dao.jpa.MusicBoDao;
import fr.k2i.adbeback.date.DateUtils;
import fr.k2i.adbeback.webapp.bean.ArtistUploadBean;
import fr.k2i.adbeback.webapp.bean.MusicBean;
import fr.k2i.adbeback.webapp.bean.datatable.query.DataTableOrder;
import fr.k2i.adbeback.webapp.bean.datatable.query.DataTableQuery;
import fr.k2i.adbeback.webapp.bean.datatable.query.DataTableSearch;
import fr.k2i.adbeback.webapp.bean.datatable.response.DataTableResponse;
import fr.k2i.adbeback.webapp.dto.MusicDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.transaction.Transactional;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * User: dimitri
 * Date: 30/12/14
 * Time: 15:50
 * Goal:
 */
@Component
public class MusicBoFacade {

    @Autowired
    private MusicBoDao musicBoDao;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private IMediaDao mediaDao;


    @Autowired
    private ICategoryDao categoryDao;

    @Autowired
    private AudioTools audioTools;




    @Value("${addonf.tmp.location:/tmp/}")
    private String tmpPath;

    @Value("${addonf.music.location:/musics/}")
    private String musicPath;

    @Value("${addonf.video.location:/movies/}")
    private String videoPath;


    @Value("${addonf.music.sample.location}")
    private String samplePath;

    public List<MusicBean> getListArtist(String query) {
        User user = userFacade.getCurrentUser();
        return listMusicForUser(user,null);
    }


    @Transactional
    public List<MusicBean> list() {
        User user = userFacade.getCurrentUser();
        return listMusicForUser(user,null);
    }

    private List<MusicBean> listMusicForUser(User user, String query) {
        List<MusicBean> res = new ArrayList<>();
        List<Music> musics = null;
        if(user.getIdentity() instanceof Artist){
            musics = musicBoDao.findMusicForArtist((Artist) user.getIdentity(),query);
        }else if(user.getIdentity() instanceof Productor){
            musics = musicBoDao.findMusicForLabel((Productor) user.getIdentity(), query);
        }

        return getMusicBeans(res, musics);
    }


    private List<MusicBean> getMusicBeans(List<MusicBean> res, List<Music> musics) {
        for (Music music : musics) {
            MusicBean musicBean = new MusicBean(music);
            musicBean.setDownloaded(musicBoDao.countDownloaded(music));
            res.add(musicBean);
        }
        return res;
    }


   //Todo : it's ugly
   @Transactional
   public void deleteMusic(Long idMusic) throws Exception {
        User user = userFacade.getCurrentUser();
        Music music = musicBoDao.get(idMusic);
        if (user.getIdentity() instanceof Productor) {
            if(music.getProductors().contains(user.getIdentity())){
                musicBoDao.delete(music);
            }else{
                throw new Exception("Bad user");
            }
        }else if (user.getIdentity() instanceof Artist) {
            if(music.getArtists().contains(user.getIdentity())){
                musicBoDao.delete(music);
            }else{
                throw new Exception("Bad user");
            }
        }else{
            throw new Exception("Bad user");
        }
    }

    @Transactional
    public void updateMusic(Long idMusic, MusicDTO musicBean) throws IOException, InterruptedException {
        Music music = musicBoDao.get(idMusic);


        List<Artist> artists = musicBoDao.findArtistByIds(musicBean.getArtists());
        music.setArtists(artists);
        music.setDescription("No description");
        music.setDuration(musicBean.getDuration());
        music.setNbAdsNeeded(3);
        music.setTitle(musicBean.getTitle());


        String uuid = UUID.randomUUID().toString();

        String originalName = musicBean.getMp3().getOriginalFilename();

        int dot = originalName.lastIndexOf(".");
        String ext = originalName.substring(dot + 1);

        String localFile = uuid + "." + ext;

        String filename = musicPath + localFile;

        FileOutputStream fos = new FileOutputStream(filename);
        fos.write(musicBean.getMp3().getBytes());
        fos.close();

        String filenameSample = samplePath + localFile;
        audioTools.copySampleAudio(filename, filenameSample);


        music.setFile(localFile);
        music.setMp3Sample(localFile);

        music.setReleaseDate(musicBean.getReleaseDate());
        music.setCategories(musicBean.getCategories());

        mediaDao.save(music);

    }


    @Transactional
    public void uploadMusic(ArtistUploadBean artistUploadBean) throws IOException, UnsupportedAudioFileException, InterruptedException {
        Artist artist = mediaDao.findArtistById(artistUploadBean.getId());


        Music music = new Music();
        music.addArtists(artist);

        music.setDescription("No description");
        music.setDuration(artistUploadBean.getDuration());
        music.setNbAdsNeeded(3);
        music.setTitle(artistUploadBean.getTitle());


        String uuid = UUID.randomUUID().toString();
        String originalName = artistUploadBean.getMp3().getOriginalFilename();

        int dot = originalName.lastIndexOf(".");
        String ext = originalName.substring(dot + 1);

        String localFile = uuid + "." + ext;

        String filename = musicPath + localFile;

        FileOutputStream fos = new FileOutputStream(filename);
        fos.write(artistUploadBean.getMp3().getBytes());
        fos.close();

        String filenameSample = samplePath + localFile;
        audioTools.copySampleAudio(filename, filenameSample);


        music.setFile(localFile);
        music.setMp3Sample(localFile);

        music.setReleaseDate(DateUtils.asLocalDate(artistUploadBean.getReleaseDate()));
        music.setCategories(artistUploadBean.getCategories());

        mediaDao.save(music);
    }


    public MusicBean findMusicById(Long id) {
        Music music = (Music) mediaDao.get(id);
        MusicBean musicBean = new MusicBean(music);
        musicBean.setDownloaded(musicBoDao.countDownloaded(music));
        return musicBean;
    }

    @Transactional
    public DataTableResponse<MusicBean> findMusicByQuery(DataTableQuery query,List<String> relationObject) {
        DataTableResponse res = new DataTableResponse();
        res.setDraw(query.getDraw());

        DataTableSearch searchObj = query.getSearch();
        String value = searchObj.getValue();


        Integer length = query.getLength();
        Integer start = query.getStart();
        List<Sort.Order> orders = new ArrayList<>();


        List<DataTableOrder> orderObj = query.getOrder();
        orders.addAll(orderObj.stream().map(dataTableOrder -> new Sort.Order(dataTableOrder.toDirection(), relationObject.get(dataTableOrder.getColumn()))).collect(Collectors.toList()));
        Sort sort = new Sort(orders);


        Pageable pageable = new PageRequest(start/length,length,sort);
        Page<Music> musicPage = musicBoDao.findMusicForArtist(value,sort,pageable);

        List<MusicBean> content = new ArrayList<>();

        for (Music music : musicPage) {
            MusicBean musicBean = new MusicBean(music);
            musicBean.setDownloaded(musicBoDao.countDownloaded(music));
            content.add(musicBean);
        }



        res.setData(content);
        res.setRecordsFiltered(musicPage.getTotalElements());
        res.setRecordsTotal(musicPage.getTotalElements());

        return res;
    }
}
