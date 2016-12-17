package fr.k2i.adbeback.core.business.media;

import lombok.Data;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Data
@Entity
@DiscriminatorValue("Music")
public class Music extends Album {
	private static final long serialVersionUID = 2176672861946794968L;
    @ManyToMany(targetEntity = Album.class, cascade = { CascadeType.PERSIST,
            CascadeType.MERGE })
    @JoinTable(name = "album_artist", joinColumns = @JoinColumn(name = "MUSIC_ID"), inverseJoinColumns = @JoinColumn(name = "ALBUM_ID"))
	private List<Album> albums;

	private String mp3Sample;

	private String label;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((albums == null) ? 0 : albums.hashCode());
		result = prime * result + ((artists == null) ? 0 : artists.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Music other = (Music) obj;
		if (albums == null) {
			if (other.albums != null)
				return false;
		} else if (!albums.equals(other.albums))
			return false;
		if (artists == null) {
			if (other.artists != null)
				return false;
		} else if (!artists.equals(other.artists))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Music [albums=" + albums + ", artists=" + artists + ", id="
				+ id + ", title=" + title + ", productors=" + productors
				+ ", genres=" + categories + ", description=" + description
				+ ", duration=" + duration + ", jacket=" + jacket
				+ ", releaseDate=" + releaseDate + "]";
	}

    public void addArtists(Artist artist) {
        if(getArtists()==null){
            this.artists = new ArrayList<Artist>();
        }
        if(!getArtists().contains(artist)){
            getArtists().add(artist);
        }

    }

    public void addCategory(Category category) {
        if(getCategories()==null){
            this.categories = new ArrayList<Category>();
        }

        if(!getCategories().contains(category)){
            getCategories().add(category);
        }
    }

    public void addProductor(Productor productor) {
        if(getProductors()==null){
            this.productors = new ArrayList<Productor>();
        }

        if(!getProductors().contains(productor)){
            getProductors().add(productor);
        }
    }


    public String getJacket(){
        if(StringUtils.isEmpty(this.jacket)){
            return "no.png";
        }else {
            return jacket;
        }
    }
}
