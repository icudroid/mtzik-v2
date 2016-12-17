package fr.k2i.adbeback.core.business.user.security;

import fr.k2i.adbeback.core.business.BaseObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import javax.persistence.*;

/**
 * User: dimitri
 * Date: 07/01/15
 * Time: 11:02
 * Goal:
 */
@Data
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name","status"})
})
@EqualsAndHashCode(of = {"name","right"},callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission extends BaseObject{

    @Id
    /*@SequenceGenerator(name = "permission",sequenceName = "permission_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "permission")*/
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 32)
    private String name;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private Right right;

    @Column(length = 50)
    private String description;

}
