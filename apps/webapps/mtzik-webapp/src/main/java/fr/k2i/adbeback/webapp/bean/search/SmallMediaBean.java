package fr.k2i.adbeback.webapp.bean.search;

import fr.k2i.adbeback.core.business.media.Album;
import fr.k2i.adbeback.core.business.media.Media;
import fr.k2i.adbeback.core.business.media.Music;
import lombok.Data;

import java.io.Serializable;

/**
 * User: dimitri
 * Date: 06/03/14
 * Time: 10:31
 * Goal:
 */
@Data
public class SmallMediaBean implements Serializable {
    private Long id;
    private String title;
    private String jacket;
    private boolean album;

    public SmallMediaBean(Media music) {
        id = music.getId();
        title = music.getTitle();
        jacket = music.getJacket();
        album = (music instanceof Album);
    }
}
