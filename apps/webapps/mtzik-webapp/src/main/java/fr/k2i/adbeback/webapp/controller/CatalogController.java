package fr.k2i.adbeback.webapp.controller;

import com.google.common.collect.Lists;
import fr.k2i.adbeback.dao.ICategoryDao;
import fr.k2i.adbeback.webapp.bean.CartBean;
import fr.k2i.adbeback.webapp.bean.search.ArtistBean;
import fr.k2i.adbeback.webapp.bean.search.MusicBean;
import fr.k2i.adbeback.webapp.bean.search.ProductorBean;
import fr.k2i.adbeback.webapp.bean.search.SearchCommand;
import fr.k2i.adbeback.webapp.facade.MediaFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dimitri
 * Date: 29/10/13
 * Time: 13:25
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class CatalogController{

    @Autowired
    private MediaFacade mediaFacade;

    @Autowired
    private ICategoryDao categoryDao;

    @Value("${addonf.facebook.api}")
    private String fbApiKey;


    @RequestMapping("/catalog.html")
    public String catalog(Map<String, Object> model,HttpServletRequest request) throws Exception {
          return "catalog";
    }



    @RequestMapping("/artistPage.html")
    public String artist(@RequestParam Long artistId, @ModelAttribute("search")SearchCommand searchCommand,Pageable pageable,Map<String, Object> model,HttpServletRequest request) throws Exception {

        ArtistBean artistBean = mediaFacade.getArtistById(artistId);

        model.put("nbMusic", mediaFacade.countMusicForArtist(artistId));
        model.put("last5", mediaFacade.last5MusicForArtist(artistId));

        model.put("top10", mediaFacade.top10MusicForArtist(artistId));

        model.put("showmore",false);

        model.put("musics",mediaFacade.findMusicsForArtist(artistId, searchCommand.getReq(), pageable));

        model.put("artist", artistBean);

        model.put("FBApiKey",fbApiKey);

        return "artist";
    }


    @RequestMapping("/majorPage.html")
    public String label(@RequestParam Long majorId, @ModelAttribute("search")SearchCommand searchCommand,Pageable pageable,Map<String, Object> model,HttpServletRequest request) throws Exception {

        ProductorBean productorBean = mediaFacade.getProductorById(majorId);

        model.put("nbArtist", mediaFacade.countArtistForLabel(majorId));
        model.put("last10", mediaFacade.last10MusicForLabel(majorId));
        model.put("showmore",false);
        model.put("categories",categoryDao.getAll());


        model.put("musics",mediaFacade.findMusicsForLabel(majorId,searchCommand.getGenreId(), searchCommand.getReq(), pageable));

        model.put("label", productorBean);

        return "label";
    }

}
