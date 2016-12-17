package fr.k2i.adbeback.webapp.bean.search;

import com.google.common.collect.Lists;
import fr.k2i.adbeback.core.business.media.Artist;
import fr.k2i.adbeback.core.business.media.Category;
import fr.k2i.adbeback.core.business.media.Music;
import fr.k2i.adbeback.core.business.media.Productor;
import fr.k2i.adbeback.date.DateUtils;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 10/01/14
 * Time: 17:47
 * To change this template use File | Settings | File Templates.
 */
@Data
public class MusicBean implements Serializable{

    private Long id;
    private String title;
    private List<ProductorBean> productors;
    private List<ArtistBean> artists;
    private List<Category> categories;
    private String jacket;
    private Date releaseDate;
    private Integer nbAdsNeeded;


    public String getTitleAndProductors(){
        StringBuilder sb = new StringBuilder();
        sb.append(title);
        sb.append(" (");

        for (Iterator<ProductorBean> iterator = productors.iterator(); iterator.hasNext(); ) {
            ProductorBean next = iterator.next();
            sb.append(next.getFullName());
            if(iterator.hasNext()){
                sb.append(", ");
            }
        }

        sb.append(")");

        return sb.toString();
    }

    public MusicBean(Music music){
        id =music.getId();

        title=music.getTitle();

        productors = new ArrayList<ProductorBean>();
        for (Productor productor : music.getProductors()) {
            productors.add(new ProductorBean(productor.getId(),productor.getFirstName(),productor.getLastName(),productor.getPhoto()));
        }

        artists = new ArrayList<ArtistBean>();
        for (Artist artist : music.getArtists()) {
            artists.add(new ArtistBean(artist.getId(),artist.getFirstName(),artist.getLastName(),artist.getPhoto()));
        }

        categories = Lists.newArrayList(music.getCategories());


        if(StringUtils.isEmpty(music.getJacket())){
            jacket = "no.png";
        }else{
            jacket = music.getJacket();
        }




        releaseDate = DateUtils.asDate(music.getReleaseDate());

        nbAdsNeeded = music.getNbAdsNeeded();

    }

}
