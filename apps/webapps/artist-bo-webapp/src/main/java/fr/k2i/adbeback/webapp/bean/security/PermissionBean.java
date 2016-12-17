package fr.k2i.adbeback.webapp.bean.security;

import lombok.*;
import lombok.experimental.Builder;

/**
 * User: dimitri
 * Date: 07/01/15
 * Time: 11:02
 * Goal:
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class PermissionBean {
    private Integer id;
    private String name;
    private String right;
    private String description;

    public PermissionBean(Integer id){
        this.id = id;
    }


}