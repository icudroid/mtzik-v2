package fr.k2i.adbeback.webapp.dto;

import com.google.common.collect.Lists;
import fr.k2i.adbeback.core.business.media.Artist;
import fr.k2i.adbeback.core.business.media.Category;
import fr.k2i.adbeback.core.business.media.Music;
import lombok.Data;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 10/01/14
 * Time: 17:47
 * To change this template use File | Settings | File Templates.
 */
@Data
public class MusicDTO implements Serializable{

    private Long id;
    private String title;
    private List<Long> artists;
    private List<Category> categories;
    private MultipartFile jacket;
    private MultipartFile mp3;
    private LocalDate releaseDate;
    private Long duration;

}
