package fr.k2i.adbeback.webapp.controller.ajax;

import fr.k2i.adbeback.core.business.media.Music;
import fr.k2i.adbeback.webapp.bean.media.MediaBean;
import fr.k2i.adbeback.webapp.bean.media.MininalMusicBean;
import fr.k2i.adbeback.webapp.facade.MediaFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * User: dimitri
 * Date: 26/02/14
 * Time: 15:09
 * Goal:
 */
@Controller
public class MediaWebservice {
    @Autowired
    private MediaFacade mediaFacade;

    @Value(value ="${addonf.bestdl.home.max:12}" )
    private Integer maxHomeBestDl;


    @RequestMapping(value="/rest/musics/news/{genreId}",method = RequestMethod.GET)
    public  @ResponseBody
    List<MininalMusicBean> addToCart(@PathVariable Long genreId , HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<MininalMusicBean> newMusics = mediaFacade.searchNewMusics(genreId, maxHomeBestDl);

        return newMusics;
    }
}
