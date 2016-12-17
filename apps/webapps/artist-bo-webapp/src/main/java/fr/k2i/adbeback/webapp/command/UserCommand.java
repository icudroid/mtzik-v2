package fr.k2i.adbeback.webapp.command;

import fr.k2i.adbeback.core.business.filter.UserFilter;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * Created by dev on 08/02/15.
 */

@Data
public class UserCommand {
    private String identifiant;
    private String email;
    private String firstname;
    private String lastname;
    private String userType;

    public UserFilter toFilter() {
        return UserFilter.builder()
                .username(identifiant)
                .email(email)
                .firstname(firstname)
                .identity((StringUtils.isEmpty(userType))?null: UserType.valueOf(userType).getType())
                .lastname(lastname)
                .build();
    }
}
