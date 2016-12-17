package fr.k2i.adbeback.webapp.bean.builder.security;


import fr.k2i.adbeback.core.business.user.security.Role;
import fr.k2i.adbeback.webapp.bean.security.RoleBean;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User: dimitri
 * Date: 20/01/15
 * Time: 11:03
 * Goal:
 */
public class RoleBeanBuilder {

    public static List<RoleBean> build(List<Role> roles) {
        return  roles
                    .stream()
                    .map(RoleBeanBuilder::build)
                    .collect(Collectors.toList());
    }

    public static RoleBean build(Role role) {
        return
                RoleBean.builder()
                        .id(role.getId())
                        .name(role.getName())
                        .description(role.getDescription())
                        .permissions(PermissionBeanBuilder.build(role.getPermissions()))
                        .build();
    }
}
