package fr.k2i.adbeback.webapp.bean.media;

import fr.k2i.adbeback.core.business.media.Album;
import fr.k2i.adbeback.core.business.media.Category;
import fr.k2i.adbeback.core.business.media.Category;
import fr.k2i.adbeback.webapp.bean.PersonBean;
import fr.k2i.adbeback.webapp.bean.search.ProductorBean;
import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Data
public class MediaBean {
	private Long id;
	private String title;
	private List<PersonBean> productors;
	private List<PersonBean> artists;
	private List<Category> categories;
    private List<AlbumBean> albums;
	private String description;
	private Long duration;
	private String jacket;
	private Date releaseDate;
	private String thumbJacket;
    private Integer nbAdsNeeded;

    public String getTitleAndProductors(){
        StringBuilder sb = new StringBuilder();
        sb.append(title);
        sb.append(" (");

        for (Iterator<PersonBean> iterator = productors.iterator(); iterator.hasNext(); ) {
            PersonBean next = iterator.next();
            sb.append(next.getFullName());
            if(iterator.hasNext()){
                sb.append(", ");
            }
        }

        sb.append(")");

        return sb.toString();
    }


    public PersonBean getMainArtist(){
        return artists.get(0);
    }
}
