package fr.k2i.adbeback.webapp.bean.search;

import fr.k2i.adbeback.core.business.media.Artist;
import fr.k2i.adbeback.date.DateUtils;
import fr.k2i.adbeback.webapp.bean.PersonBean;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 10/01/14
 * Time: 17:47
 * To change this template use File | Settings | File Templates.
 */
@Data
public class ArtistBean extends PersonBean {
    private Date lastRelease;

    private String biography;
    private String facebook;
    private String twitter;
    private String googlePlus;


    public ArtistBean(Long id, String firstName, String lastName, String photo, LocalDate lastRelease) {
        super(id, firstName, lastName,photo);
        this.lastRelease = DateUtils.asDate(lastRelease);
    }

    public ArtistBean(Long id, String firstName, String lastName,String photo) {
        super(id, firstName, lastName,photo);
    }

    public ArtistBean(Artist a) {
        super(a.getId(),a.getFirstName(),a.getLastName(),a.getPhoto());
        biography = a.getBiography();
        facebook = a.getFacebook();
        twitter = a.getTwitter();
        googlePlus = a.getGooglePlus();
    }
}
