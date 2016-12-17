package fr.k2i.adbeback.core.business.user.security;

import fr.k2i.adbeback.core.business.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import javax.persistence.*;
import java.util.List;

/**
 * User: dimitri
 * Date: 07/01/15
 * Time: 11:03
 * Goal:
 */
@Data
@Entity
@Table
@EqualsAndHashCode(of = "name",callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile extends BaseObject implements IProfile{

    @Id
    /*@SequenceGenerator(name = "profile",sequenceName = "profile_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "profile")*/
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true,nullable = false,length = 32)
    private String name;

    @Column(length = 50)
    private String description;

    @ManyToMany
    @JoinTable( name = "profilerole",
            joinColumns = {
                    @JoinColumn(name = "profileid", nullable = false, updatable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "roleid",nullable = false, updatable = false)
            })
    private List<Role> roles;

}
