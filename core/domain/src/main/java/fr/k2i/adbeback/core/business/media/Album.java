package fr.k2i.adbeback.core.business.media;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@DiscriminatorValue("album")
@Data
public class Album extends Media {
	private static final long serialVersionUID = -8195169031786628618L;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "album_id")
    private List<TrackNumberMusic> tracks;


    @ManyToMany(targetEntity = Artist.class, cascade = { CascadeType.PERSIST,
            CascadeType.MERGE })
    @JoinTable(name = "music_artist", joinColumns = @JoinColumn(name = "MUSIC_ID"), inverseJoinColumns = @JoinColumn(name = "ARTIST_ID"))
    protected List<Artist> artists;

/*
    @OneToMany(cascade=CascadeType.ALL)
    @MapKey(name = "trackNumber")
    private Map<Integer,Music> musics;
*/

/*	private List<Music> musics;

    @ManyToMany(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        mappedBy = "albums",
        targetEntity = Music.class
    )
	public List<Music> getMusics() {
		return musics;
	}

	public void setMusics(List<Music> musics) {
		this.musics = musics;
	}*/


	@Override
	public String toString() {
		return "Album [ id=" + id + ", title=" + title
				+ ", productors=" + productors + ", genres=" + categories
				+ ", description=" + description + ", duration=" + duration
				+ ", jacket=" + jacket + ", releaseDate=" + releaseDate
				+ ", nbAdsNeeded=" + nbAdsNeeded + ", file=" + file + "]";
	}


    public void addMusic(Music music, int trackNumber) {
        if(!music.getAlbums().contains(this)){
            music.getAlbums().add(this);
        }
        getTracks().add(new TrackNumberMusic(trackNumber,music));
    }

    public void addArtists(Artist artist) {
        if(!getArtists().contains(artist)){
            getArtists().add(artist);
        }
    }
}
