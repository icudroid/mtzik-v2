package fr.k2i.adbeback.webapp.bean;

import fr.k2i.adbeback.core.business.media.Category;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * User: dimitri
 * Date: 02/10/14
 * Time: 13:46
 * Goal:
 */
@Data
public class ArtistUploadBean implements Serializable{
    private Long id;

    private String title;
    private Long duration;
    private List<Long> artists;
    private List<Category> categories;
    private Date releaseDate;

    private MultipartFile mp3;
    private MultipartFile jacket;

    private String label;
}
