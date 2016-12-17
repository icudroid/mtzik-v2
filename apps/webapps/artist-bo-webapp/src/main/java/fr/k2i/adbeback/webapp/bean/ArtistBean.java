package fr.k2i.adbeback.webapp.bean;

import fr.k2i.adbeback.core.business.media.Artist;
import fr.k2i.adbeback.date.DateUtils;
import fr.k2i.adbeback.webapp.command.UserType;
import lombok.*;
import lombok.experimental.Builder;

import java.time.LocalDate;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 10/01/14
 * Time: 17:47
 * To change this template use File | Settings | File Templates.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistBean extends IdentityBean {
    private Date lastRelease;

    private String biography;
    private String facebook;
    private String twitter;
    private String googlePlus;
    private String artistName;


    public ArtistBean(Long id, String firstName, String lastName, String artistName, String photo, LocalDate lastRelease) {
        super(id, firstName, lastName,photo);
        this.artistName = artistName;
        this.lastRelease = DateUtils.asDate(lastRelease);
        type = UserType.ADMIN;
    }

    public ArtistBean(Long id, String firstName, String lastName,String artistName,String photo) {
        super(id, firstName, lastName,photo);
        this.artistName = artistName;
        type = UserType.ARTIST;
    }

    public ArtistBean(Artist a) {
        super(a.getId(),a.getFirstName(),a.getLastName(),a.getPhoto());
        biography = a.getBiography();
        facebook = a.getFacebook();
        twitter = a.getTwitter();
        googlePlus = a.getGooglePlus();
        artistName = a.getArtistName();
        type = UserType.ADMIN;
    }
}
