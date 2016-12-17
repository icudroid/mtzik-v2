package fr.k2i.adbeback.webapp.controller;

import fr.k2i.adbeback.core.business.media.Media;
import fr.k2i.adbeback.core.business.media.Music;
import fr.k2i.adbeback.dao.IMediaDao;
import fr.k2i.adbeback.webapp.util.HttpStreaming;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * User: dimitri
 * Date: 03/10/14
 * Time: 11:30
 * Goal:
 */
@Controller
public class SampleController {


    @Value("${addonf.music.sample.location}")
    private String samplePath;

    @Autowired
    private IMediaDao mediaDao;

    @RequestMapping(value = "/sample/{id}")
    public ResponseEntity<byte[]> sample(@PathVariable Long id,HttpServletRequest request) throws Exception {
        Media media = mediaDao.get(id);
        if (media instanceof Music) {
            Music music = (Music) media;
            File file = new File(samplePath+music.getMp3Sample());
            return  HttpStreaming.streaming(request, file);
        }else{
            throw new Exception("Bad Request");
        }
    }
}
