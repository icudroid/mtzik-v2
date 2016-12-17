package fr.k2i.adbeback.webapp.controller;

import fr.k2i.adbeback.dao.ICategoryDao;
import fr.k2i.adbeback.dao.IMediaDao;
import fr.k2i.adbeback.webapp.bean.search.*;
import fr.k2i.adbeback.webapp.facade.MediaFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * User: dimitri
 * Date: 07/01/14
 * Time: 11:29
 * Goal:
 */
@Controller
public class SearchController{
    @Value(value ="${addonf.static.url}" )
    private String staticUrl;

    @Autowired
    private IMediaDao mediaDao;
    @Autowired
    private ICategoryDao categoryDao;

    @Autowired
    private MediaFacade mediaFacade;


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public @ResponseBody SearchResponseBean ajaxSearch(@RequestParam(value = "q") String req) throws Exception {
        SearchResponseBean res = new SearchResponseBean();

        res.setMusics(mediaFacade.findMusicByTitle(req, 5));
        res.setArtists(mediaFacade.findArtistByName(req, 5));
        res.setLabels(mediaFacade.findLabelByName(req, 5));

        return res;
    }

    @RequestMapping("/search.html")
    public String search(@RequestParam(value = "q",required = false) String req,Map<String, Object> model,HttpServletRequest request) throws Exception {

        if(StringUtils.isEmpty(req)){
            req="";
        }
        model.put("staticUrl",staticUrl);
        model.put("categories",categoryDao.getAll());
        model.put("query", req);
        model.put("showmore", true);


        mediaFacade.search(req,model);
        model.put("nosearch",false);

        return "search";
    }



    @RequestMapping("/search/{what}")
    public String search(@PathVariable String what, @ModelAttribute("search")SearchCommand searchCommand,Pageable pageable, Map<String, Object> model,HttpServletRequest request) throws Exception {

        model.put("staticUrl",staticUrl);
        model.put("categories",categoryDao.getAll());
        model.put("showmore", false);

        model.put("pushes", mediaFacade.getHomePush());


        String req = searchCommand.getReq();
        if("music".equals(what)){
            model.put("musics",mediaFacade.findMusics(searchCommand.getGenreId(), req,pageable));
            return "search/music";
        }else if("label".equals(what)){
             model.put("productors",mediaFacade.findProductors(req,pageable));
            return "search/label";
        }else if("artist".equals(what)){
            model.put("artists",mediaFacade.findArtists(req,pageable));
            return "search/artist";
        }else if("news".equals(what)){
            model.put("musics",mediaFacade.findNewMusics(searchCommand.getGenreId(), req,pageable));
            return "search/news";
        }else if("top50".equals(what)){
            model.put("musics",mediaFacade.findTopMusics(searchCommand.getGenreId(), req,searchCommand.getTop(),pageable));
            return "search/top50";
        }else if("all".equals(what)){

            if(StringUtils.isEmpty(req)){
                req="";
            }
            model.put("query", req);
            mediaFacade.search(req,model);
            model.put("showmore", true);

            return "search";
        }else{
            return "search";
        }

    }
}


