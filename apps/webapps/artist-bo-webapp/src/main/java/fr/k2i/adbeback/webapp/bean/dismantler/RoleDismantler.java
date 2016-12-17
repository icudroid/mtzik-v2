package fr.k2i.adbeback.webapp.bean.dismantler;


import fr.k2i.adbeback.core.business.user.security.Role;
import fr.k2i.adbeback.webapp.bean.security.RoleBean;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User: dimitri
 * Date: 20/01/15
 * Time: 11:06
 * Goal:
 */
public class RoleDismantler {

    public static Role buildRole(RoleBean role) {
        return Role.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .permissions(PermissionDismantler.buildPermissions(role.getPermissions()))
                .build();
    }



    public static List<Role> buildRoles(List<RoleBean> roles) {
        return roles
                .stream()
                .map(RoleDismantler::buildRole)
                .collect(Collectors.toList());
    }
}
