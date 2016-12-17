package fr.k2i.adbeback.webapp.bean.search;

import fr.k2i.adbeback.core.business.media.Identity;
import lombok.Data;

import java.io.Serializable;

@Data
public class SmallPersonBean implements Serializable {
    private Long id;
    private String fullName;
    private String photo;

    public SmallPersonBean(Identity artist) {
        id = artist.getId();
        fullName = artist.getFullName();
        photo = artist.getPhoto();
    }
}