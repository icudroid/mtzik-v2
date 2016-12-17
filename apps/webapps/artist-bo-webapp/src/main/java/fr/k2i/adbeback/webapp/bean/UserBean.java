package fr.k2i.adbeback.webapp.bean;

import fr.k2i.adbeback.core.business.BaseObject;
import fr.k2i.adbeback.core.business.media.Identity;
import fr.k2i.adbeback.core.business.media.Person;
import fr.k2i.adbeback.core.business.user.security.Profile;
import lombok.*;
import lombok.experimental.Builder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * User: dimitri
 * Date: 05/12/14
 * Time: 09:39
 * Goal:
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBean  {

    private Long id;
    private String username;
    private String email;
    private IdentityBean identity;
    private Set<ProfileBean> profiles = new HashSet<>();
    private boolean enabled;
    private Boolean newsletter;

}
