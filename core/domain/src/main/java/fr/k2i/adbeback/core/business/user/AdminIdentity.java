package fr.k2i.adbeback.core.business.user;

import fr.k2i.adbeback.core.business.media.Identity;
import org.springframework.util.StringUtils;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Admin")
public class AdminIdentity extends Identity {


	public AdminIdentity(){

	}
	public AdminIdentity(String firstName, String lastName) {
		super(firstName, lastName);
	}

    @Override
    public String getFullName() {
        return firstName+((!StringUtils.isEmpty(firstName))?" ":"")+lastName;
    }

    private static final long serialVersionUID = 4957439657901611247L;

	@Override
	public String toString() {
		return "Actor [id=" + id + ", firstName=" + firstName + ", lastName="
				+ lastName + ", website=" + website + ", version=" + version
				+ "]";
	}

}
