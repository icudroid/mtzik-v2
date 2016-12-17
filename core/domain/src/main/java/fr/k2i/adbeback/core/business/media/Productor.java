package fr.k2i.adbeback.core.business.media;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Productor")
@Data
public class Productor extends Identity {

	private String labelName;


	public Productor(){
		
	}
	
	public Productor(String firstName, String lastName) {
		super(firstName, lastName);
	}

    @Override
    public String getFullName() {
        return labelName;
    }


    private static final long serialVersionUID = -7459989016180487826L;


	@Override
	public String toString() {
		return "Productor [id=" + id + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", website=" + website
				+ ", version=" + version + "]";
	}

    public void addArtist(Artist artist) {
        if(!artist.getProductors().contains(artist)){
            artist.getProductors().add(this);
        }
    }
}
