package fr.k2i.adbeback.webapp.dto;

import fr.k2i.adbeback.core.business.media.Category;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 10/01/14
 * Time: 17:47
 * To change this template use File | Settings | File Templates.
 */
@Data
public class ArtistDTO implements Serializable{

    private Long id;
    private String artistName;
    private String firstName;
    private String lastName;
    private MultipartFile photo;

    private String biography;
    private String facebook;
    private String twitter;
    private String googlePlus;

    private String website;



}
