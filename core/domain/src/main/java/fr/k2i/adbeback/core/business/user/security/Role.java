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
public class Role extends BaseObject{

    @Id
    /*@SequenceGenerator(name = "role",sequenceName = "role_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "role")*/
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 32, unique = true, nullable = false)
    private String name;

    @Column(length = 50)
    private String description;

    @ManyToMany
    @JoinTable( name = "rolepermission",
            joinColumns = {
                    @JoinColumn(name = "id", nullable = false, updatable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "per_id",nullable = false, updatable = false)
            })
    private List<Permission> permissions;

}
