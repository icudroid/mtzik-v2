package fr.k2i.adbeback.webapp.bean;

import fr.k2i.adbeback.core.business.media.Person;
import fr.k2i.adbeback.webapp.command.UserType;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class PersonBean extends IdentityBean implements Serializable{

	public PersonBean(Long id, String firstName, String lastName, String photo) {
	    super(id, firstName, lastName, photo);
        type = UserType.MTZIK;
    }

    public PersonBean(Person person) {
        this(person.getId(),person.getFirstName(),person.getLastName(),person.getPhoto());
    }
}
