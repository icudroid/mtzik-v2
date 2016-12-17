package fr.k2i.adbeback.webapp.controller.ajax.music;

import fr.k2i.adbeback.webapp.bean.ArtistUploadBean;
import fr.k2i.adbeback.webapp.bean.MusicBean;
import fr.k2i.adbeback.webapp.bean.datatable.query.DataTableQuery;
import fr.k2i.adbeback.webapp.bean.datatable.response.DataTableResponse;
import fr.k2i.adbeback.webapp.dto.MusicDTO;
import fr.k2i.adbeback.webapp.bean.ResponseBean;
import fr.k2i.adbeback.webapp.facade.MusicBoFacade;
import fr.k2i.adbeback.webapp.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * User: dimitri
 * Date: 30/12/14
 * Time: 14:48
 * Goal:
 */
@Controller
@RequestMapping("/rest/music/")
public class MusicRestController {


    @Autowired
    private UserFacade userFacade;

    @Autowired
    private MusicBoFacade musicBoFacade;



    @Secured("ROLE_MUSIC_L")
    @RequestMapping(value = "/searhQuery",method = RequestMethod.POST)
    public @ResponseBody
    DataTableResponse<MusicBean> test(@RequestBody DataTableQuery query){
        List<String> config = Arrays.asList("id","title","artists","categories","releaseDate","downloaded","jacket");
        return musicBoFacade.findMusicByQuery(query, config);
    }


    @Secured("ROLE_MUSIC_N")
    @RequestMapping(value = "upload")
    public
    @ResponseBody
    ResponseBean upload(@RequestParam ArtistUploadBean artistUploadBean) throws IOException, ServletException, UnsupportedAudioFileException, InterruptedException {
    //ResponseBean upload(@RequestParam MultipartFile music, HttpServletRequest request) throws IOException, ServletException, UnsupportedAudioFileException, InterruptedException {

        //MultipartFile music = (MultipartFile) request.getPart("music");

    /*    ArtistUploadBean artistUploadBean = new ArtistUploadBean();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> stringEntry : parameterMap.entrySet()) {
            Field field = ReflectionUtils.findField(ArtistUploadBean.class, stringEntry.getKey());
            ReflectionUtils.makeAccessible(field);


            try {

                Constructor<?> constructor = field.getType().getConstructor(String.class);
                Object o = constructor.newInstance(stringEntry.getValue()[0]);
                ReflectionUtils.setField(field, artistUploadBean, o);

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


        }
*/

        musicBoFacade.uploadMusic(artistUploadBean);


        return ResponseBean.OK();
    }


    @Secured("ROLE_MUSIC_L")
    @RequestMapping(value = "search/{query}")
    public
    @ResponseBody
    List<MusicBean> search(@PathVariable String query) throws Exception {
        return musicBoFacade.getListArtist(query);
    }

    @Secured("ROLE_MUSIC_R")
    @RequestMapping(value = "get/{id}")
    public
    @ResponseBody
    MusicBean get(@PathVariable Long id) throws Exception {
        return musicBoFacade.findMusicById(id);
    }

    @Secured("ROLE_MUSIC_S")
    @RequestMapping(value = "list")
    public
    @ResponseBody
    List<MusicBean> list() {
        return musicBoFacade.list();
    }


    @Secured("ROLE_MUSIC_D")
    @RequestMapping(value = "delete/{id}")
    public
    @ResponseBody
    ResponseBean delete(@PathVariable Long id) {
        try {
            musicBoFacade.deleteMusic(id);
            return ResponseBean.OK();
        } catch (Exception e) {
            return ResponseBean.FAILED(e.getMessage());
        }
    }


    @Secured("ROLE_MUSIC_W")
    @RequestMapping(value = "update/{id}")
    public
    @ResponseBody
    ResponseBean update(@PathVariable Long id,@RequestParam MusicDTO musicDTO) {
        try {
            musicBoFacade.updateMusic(id,musicDTO);
            return ResponseBean.OK();
        } catch (Exception e) {
            return ResponseBean.FAILED(e.getMessage());
        }
    }


}
