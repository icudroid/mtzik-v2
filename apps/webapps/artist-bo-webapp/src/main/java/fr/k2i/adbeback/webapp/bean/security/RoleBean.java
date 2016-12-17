package fr.k2i.adbeback.webapp.bean.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Builder;

import java.util.ArrayList;
import java.util.List;

/**
 * User: dimitri
 * Date: 07/01/15
 * Time: 11:03
 * Goal:
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class RoleBean{
    private Integer id;
    private String name;
    private String description;
    private List<PermissionBean> permissions;

    public RoleBean(){
        permissions = new ArrayList<>();
    }
}
