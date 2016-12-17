package fr.k2i.adbeback.webapp.controller;

import com.google.common.collect.Lists;
import fr.k2i.adbeback.webapp.bean.CartBean;
import fr.k2i.adbeback.webapp.bean.search.MusicBean;
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
public class ProductController {


    @Autowired
    private MediaFacade mediaFacade;


    @RequestMapping("/product.html")
    public String product(@RequestParam("musicId") Long musicId, Map<String, Object> model,HttpServletRequest request) throws Exception {
        model.put("media",mediaFacade.getMediaById(musicId));
        return "product";
    }


    @RequestMapping("/albumPage.html")
    public String album(@RequestParam("albumId") Long albumId,@ModelAttribute("search")SearchCommand searchCommand, Pageable pageable, Map<String, Object> model,HttpServletRequest request) throws Exception {
        model.put("media",mediaFacade.getMediaById(albumId));
        model.put("musics",mediaFacade.findMusicsForAlbum(albumId,searchCommand.getReq(), pageable));
        model.put("showmore",false);
        return "album";
    }
}
