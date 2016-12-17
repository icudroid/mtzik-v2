package fr.k2i.adbeback.webapp.controller;

import fr.k2i.adbeback.webapp.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by dev on 28/01/15.
 */
@Controller
@RequestMapping(value = "/music")
public class MusicController {

    @Autowired
    private UserFacade userFacade;

    @Secured("ROLE_MUSIC_SEARCH")
    @RequestMapping(value = "search")
    public
    String search() throws Exception {
        return "music/search";
    }
}
