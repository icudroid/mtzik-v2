package fr.k2i.adbeback.webapp.controller.ajax.artist;

import fr.k2i.adbeback.webapp.bean.ArtistBean;
import fr.k2i.adbeback.webapp.bean.MusicBean;
import fr.k2i.adbeback.webapp.bean.ResponseBean;
import fr.k2i.adbeback.webapp.bean.datatable.query.DataTableQuery;
import fr.k2i.adbeback.webapp.bean.datatable.response.DataTableResponse;
import fr.k2i.adbeback.webapp.dto.ArtistDTO;
import fr.k2i.adbeback.webapp.facade.ArtistFacade;
import fr.k2i.adbeback.webapp.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * User: dimitri
 * Date: 30/12/14
 * Time: 14:48
 * Goal:
 */
@RestController
@RequestMapping(value = "/rest/artist")
public class ArtistRestController {


    @Autowired
    private UserFacade userFacade;


    @Autowired
    private ArtistFacade artistFacade;


    @Secured("ROLE_ARTIST_L")
    @RequestMapping(value = "/searhQuery",method = RequestMethod.POST)
    public @ResponseBody
    DataTableResponse<MusicBean> test(@RequestBody DataTableQuery query){
        List<String> config = Arrays.asList("id", "artistName", "biography", "photo");
        return artistFacade.findArtistByQuery(query,config);
    }


    @Secured("ROLE_ARTIST_L")
    @RequestMapping(value = "search/{query}")
    public
    @ResponseBody
    List<ArtistBean> search(@PathVariable String query) throws Exception {
        return userFacade.getListArtist(query);
    }


    @Secured("ROLE_ARTIST_R")
    @RequestMapping(value = "get/{id}")
    public
    @ResponseBody
    ArtistBean get(@PathVariable Long id) throws Exception {
        return userFacade.findArtistById(id);
    }


    @Secured("ROLE_ARTIST_L")
    @RequestMapping(value = "list")
    public
    @ResponseBody
    List<ArtistBean> list() throws Exception {
        return userFacade.getListArtist();
    }


    @Secured("ROLE_ARTIST_D")
    @RequestMapping(value = "delete/{id}")
    public
    @ResponseBody
    ResponseBean delete(@PathVariable Long id) {
        try {
            userFacade.deleteArtist(id);
            return ResponseBean.OK();
        } catch (Exception e) {
            return ResponseBean.FAILED(e.getMessage());
        }
    }

    @Secured("ROLE_ARTIST_N")
    @RequestMapping(value = "add")
    public
    @ResponseBody
    ResponseBean add(@RequestParam ArtistDTO artistDTO) {
        try {
            userFacade.addArtist(artistDTO);
            return ResponseBean.OK();
        } catch (Exception e) {
            return ResponseBean.FAILED(e.getMessage());
        }
    }

    @Secured("ROLE_ARTIST_W")
    @RequestMapping(value = "update/{id}")
    public
    @ResponseBody
    ResponseBean update(@PathVariable Long id,@RequestParam ArtistDTO artistDTO) {
        try {
            userFacade.modArtist(id,artistDTO);
            return ResponseBean.OK();
        } catch (Exception e) {
            return ResponseBean.FAILED(e.getMessage());
        }
    }


}
