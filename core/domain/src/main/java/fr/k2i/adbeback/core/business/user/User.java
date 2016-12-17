package fr.k2i.adbeback.core.business.user;

import fr.k2i.adbeback.core.business.BaseObject;
import fr.k2i.adbeback.core.business.media.Identity;
import fr.k2i.adbeback.core.business.media.Person;
import fr.k2i.adbeback.core.business.user.security.Profile;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * User: dimitri
 * Date: 05/12/14
 * Time: 09:39
 * Goal:
 */
@Entity
@Data
@Table(name="mtzik_user")
public class User extends BaseObject {

    @Id
    @SequenceGenerator(name = "User_Gen", sequenceName = "User_Sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "User_Gen")
    private Long id;

    @Column(nullable = false)
    private String password;                    // required

    @Column(nullable = false, length = 50, unique = true)
    private String username;                    // required

    @Column(nullable = false, unique = true)
    private String email;                       // required; unique

    @OneToOne(cascade = CascadeType.ALL)
    private Identity identity;

    @ManyToMany
    private Set<Profile> profiles = new HashSet<>();

    @Column(name = "account_enabled")
    private boolean enabled;

    @Column(name = "account_expired", nullable = true)
    //private boolean accountExpired;
    private LocalDateTime expirationDate;

    @Column(name = "account_locked", nullable = false)
    private boolean accountLocked;

    @Column(name = "credentials_expired", nullable = true)
    //private boolean credentialsExpired;
    private LocalDateTime credentialsExpirationDate;

    private Boolean newsletter;


    @Version
    private Integer version;

    /**
     * Adds a role for the user
     *
     * @param profile the fully instantiated role
     */
    public void addProfile(Profile profile) {
        getProfiles().add(profile);
    }

    @Transient
    public boolean isAccountNonLocked() {
        return !isAccountLocked();
    }


    public static User Mtzik_User(){
        User user = new User();
        user.setIdentity(new Person());
        return user;
    }


}
