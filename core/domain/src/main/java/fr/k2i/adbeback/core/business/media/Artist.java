package fr.k2i.adbeback.core.business.media;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@DiscriminatorValue("Artist")
public class Artist extends Identity {
	public Artist() {

	}

	private String artistName;

	public Artist(String firstName, String lastName) {
		super(firstName, lastName);
	}

    @Override
    public String getFullName() {
        return artistName;
    }


    public Artist(String artistName,String firstName, String lastName) {
		super(firstName, lastName);
		this.artistName = artistName;
	}

	public Artist(String artistName) {
		this.artistName = artistName;
	}


	private static final long serialVersionUID = -8371684556048514484L;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "productor_artist",
            joinColumns = { @JoinColumn(name = "artist_id") },
            inverseJoinColumns = @JoinColumn(name = "productor_id")
    )
    private Set<Productor> productors = new HashSet<>();


	@ManyToOne
	@JoinColumn(name = "productor_id")
	private Productor mainProductor;

	//private List<Music> musics;


/*    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "artists",
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
		return "Artist [id=" + id + ", artistName=" + artistName + "]";
	}


	public void addProductors(Productor productor) {
		if(productors == null){
			productors = new HashSet<>();
		}
		productors.add(productor);
	}

	public void setMainProductor(Productor productor){
		setMainProductor(productor);
		addProductors(productor);
	}
}
