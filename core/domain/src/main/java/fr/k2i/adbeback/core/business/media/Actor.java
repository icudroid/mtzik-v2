package fr.k2i.adbeback.core.business.media;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Actor")
public class Actor extends Identity {
    private String actorName;



	public Actor(){
		
	}
	public Actor(String firstName, String lastName) {
		super(firstName, lastName);
	}

    @Override
    public String getFullName() {
        return actorName;
    }

    private static final long serialVersionUID = 4957439657901611247L;

	@Override
	public String toString() {
		return "Actor [id=" + id + ", firstName=" + firstName + ", lastName="
				+ lastName + ", website=" + website + ", version=" + version
				+ "]";
	}

}
