package fr.k2i.adbeback.webapp.bean;

import fr.k2i.adbeback.core.business.media.Person;
import fr.k2i.adbeback.core.business.user.AdminIdentity;
import fr.k2i.adbeback.webapp.command.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class AdminIdentityBean extends IdentityBean implements Serializable{

	public AdminIdentityBean(Long id, String firstName, String lastName, String photo) {
	    super(id, firstName, lastName, photo);
        type = UserType.ADMIN;
    }

    public AdminIdentityBean(AdminIdentity adminIdentity) {
        this(adminIdentity.getId(), adminIdentity.getFirstName(), adminIdentity.getLastName(), adminIdentity.getPhoto());
    }
}
