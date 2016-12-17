package fr.k2i.adbeback.webapp.controller;

import fr.k2i.adbeback.webapp.bean.ArtistBean;
import fr.k2i.adbeback.webapp.bean.ResponseBean;
import fr.k2i.adbeback.webapp.dto.ArtistDTO;
import fr.k2i.adbeback.webapp.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by dev on 27/01/15.
 */
@Controller
@RequestMapping(value = "/artist")
public class ArtistController {
    @Autowired
    private UserFacade userFacade;


    @Secured("ROLE_ARTIST_SEARCH")
    @RequestMapping(value = "search")
    public
    String search() throws Exception {
        return "artist/search";
    }


}
