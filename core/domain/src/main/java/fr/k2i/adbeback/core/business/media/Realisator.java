package fr.k2i.adbeback.core.business.media;

import org.springframework.util.StringUtils;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Realisator")
public class Realisator extends Identity {

	public Realisator(){
		
	}


	
	public Realisator(String firstName, String lastName) {
		super(firstName, lastName);
	}

    @Override
    public String getFullName() {
        return firstName+((!StringUtils.isEmpty(firstName))?" ":"")+lastName;
    }

    @Override
	public String toString() {
		return "Realisator [id=" + id + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", website=" + website
				+ ", version=" + version + "]";
	}

	private static final long serialVersionUID = 4531383173936609179L;

}
