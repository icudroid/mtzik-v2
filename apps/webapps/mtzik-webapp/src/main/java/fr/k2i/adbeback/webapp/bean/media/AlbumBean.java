package fr.k2i.adbeback.webapp.bean.media;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * User: dimitri
 * Date: 06/01/14
 * Time: 13:22
 * Goal:
 */
@Data
public class AlbumBean implements Serializable{
    private String name;
    private Long id;

    public AlbumBean(Long id, String title) {
        name = title;
        this.id = id;
    }
}
