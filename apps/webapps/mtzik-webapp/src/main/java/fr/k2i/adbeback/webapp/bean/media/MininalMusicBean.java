package fr.k2i.adbeback.webapp.bean.media;

import fr.k2i.adbeback.core.business.media.Category;
import fr.k2i.adbeback.webapp.bean.PersonBean;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

@Data
public class MininalMusicBean implements Serializable{

    private Long id;
    private String title;
    private String jacket;
    private Integer nbAdsNeeded;

}
