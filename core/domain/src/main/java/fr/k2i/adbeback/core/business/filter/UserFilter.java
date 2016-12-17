package fr.k2i.adbeback.core.business.filter;

import fr.k2i.adbeback.core.business.media.Identity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;

/**
 * Created by dev on 08/02/15.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFilter {
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private Class<? extends Identity> identity;

}
